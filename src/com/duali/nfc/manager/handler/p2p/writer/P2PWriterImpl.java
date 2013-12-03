package com.duali.nfc.manager.handler.p2p.writer;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.duali.nfc.connection.UnknownConnection;
import com.duali.nfc.core.connection.exception.ConnectionException;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.ndef.Ndef;
import com.duali.nfc.ndef.NdefMessageEncoder;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.p2p.connection.P2PConnection;
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.reader.handler.exception.NFCReaderException;

public class P2PWriterImpl implements P2PWriter {


	private static Logger LOGGER = Logger
	.getLogger(P2PWriterImpl.class);

	private static P2PWriterImpl instance = null;
	private boolean RF_POLLING = true;
	/**
	 * <p>
	 * This method can be used for getting the instance.
	 * </p>
	 * 
	 * @return The instance.
	 */
	public static synchronized P2PWriterImpl getInstance() {
		if (instance == null) {
			instance = new P2PWriterImpl();
		}
		return instance; // returning instance.
	}
	
	private P2PWriterImpl() {
	}

	private TagWriterThread tagWriterMF1kThread = null;


	@Override
	public synchronized void startNDEFWriteProcess(ArrayList<Record> outputData,			
			P2PWriterListerner callbackListerner, NFCReaderHandler nfcReaderHandler, String p2pType) {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.stopRunning();
				tagWriterMF1kThread.interrupt();
			} catch (Exception e) {
			}
		}
		tagWriterMF1kThread = new TagWriterThread(outputData, callbackListerner, nfcReaderHandler, p2pType);
		tagWriterMF1kThread.start();
	}

	@Override
	public void pauseNDEFCreationProcess() {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.setPaused(true);
			} catch (Exception e) {
			}
		}		
	}


	@Override
	public void resumeNDEFCreationProcess() {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.setPaused(false);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void finishNDEFCreationProcess() {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.stopRunning();
				tagWriterMF1kThread.interrupt();
			} catch (Exception e) {
			}
		}
	}

	class TagWriterThread extends Thread {

		private boolean isRunning = false;

		/**
		 * <p>
		 * This method can be used for setting the isRunning.
		 * </p>
		 * 
		 * @param isRunning
		 *            The isRunning to set.
		 */
		public void stopRunning() {
			this.isRunning = false; // Assigning to this.isRunning.
		}

		/**
		 * <p>
		 * This method can be used for getting the isRunning.
		 * </p>
		 * 
		 * @return The isRunning.
		 */
		public boolean isRunning() {
			return isRunning; // returning isRunning.
		}

		private ArrayList<Record> outputData = null;
		private P2PWriterListerner callbackListerner = null;

		private NFCReaderHandler nfcReaderHandler = null;
		private String actualUid = null;
		private String p2pType;
		/**
		 * Constructor for NDEFTagWriterMF1kImpl.TagWriterMF1K.
		 * @param p2pType 
		 */
		public TagWriterThread(ArrayList<Record> outputData,
				P2PWriterListerner callbackListerner, NFCReaderHandler nfcReaderHandler, String p2pType) {
			this.outputData = outputData;
			this.callbackListerner = callbackListerner;
			this.nfcReaderHandler = nfcReaderHandler;
			this.p2pType = p2pType;
		}

		private boolean paused = false;

		/**
		 * <p>This method can be used for setting the paused.</p>
		 * @param paused The paused to set.
		 */
		public void setPaused(boolean paused) {
			this.paused = paused; // Assigning to this.paused.
		}

		@Override
		public void run() {

			isRunning = true;
			if (callbackListerner != null &&  isRunning) {
				callbackListerner.ndefCreationProcessStarted();
			}

			while (isRunning) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					isRunning = false;
					return;
				}
				P2PConnection connection = null;
				try {		
					if(paused){
						System.out.println("Paused");
						sleep(1000);
						continue;
					} 
					LOGGER.debug("Waiting for card");
					if (!nfcReaderHandler.waitForCardPresent(1000) &&  isRunning) {
						callbackListerner.updateStatusNoDevice();
						continue;
					}
					if(!isRunning) return;
					
					try {
						connection = nfcReaderHandler.getP2PConnection();
					} catch (Exception e) {
						connection = null;
					} 
					connection.RFPollingStop();
					
					// 타입에 따라 앞에 헤더 붙혀줘야 함.
					NdefMessageEncoder ndefMessageEncoder = Ndef.getNdefMessageEncoder();			
					byte[] ndef = ndefMessageEncoder.encode(outputData);
									
					if ( connection == null || connection instanceof UnknownConnection) {
//						callbackListerner.unsupportedTagDetected();
					} else {
						try {
							callbackListerner.sending();
							int ret = connection.SendNdef(p2pType, ndef);
							if (ret == 0)
								callbackListerner.sendSuccess();
							else if (ret == P2PConnection.ERROR_SIZE_OVER)
								callbackListerner.sendFail(AppLocale.MSG_ERR_WRITE_CARD_SIZE_OVER);
							else
								callbackListerner.sendFail(AppLocale.MSG_ERR_WRITE_CARD);		
						} catch (Exception e) {
							callbackListerner.sendFail(AppLocale.MSG_ERR_WRITE_CARD);							
						}
					}

					if(!RF_POLLING){
//						nfcReaderHandler.waitForCardAbsent(1000);
						//						Thread.sleep(50);
						RF_POLLING = true;
					}
					LOGGER.debug("Waiting for card to be removed");
					waitUntilCardRemoved();

				} catch (InterruptedException e) {
					isRunning = false;
					return;
				} catch (NFCReaderException e) {
					e.printStackTrace();
					try {
						connection.RFOff();
					} catch (ConnectionException e1) {
						// 
						e1.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						connection.RFPollingStart();
					} catch (Exception e) {
					}
				}
			}
		}

		private void waitForCardPresent() {
			
		}
		/**
		 * <p>
		 * This is the method for .
		 * </p>
		 */
		private void waitUntilCardRemoved() throws Exception {
			while (true &&  isRunning) {
//				boolean ret = nfcReaderHandler.waitForCardAbsent(3000);
				
				P2PConnection p2pConnection = nfcReaderHandler.getP2PConnection();

				int cnt = 0;
				for (int i = 0; i < 10; i++) {
					byte[] res = p2pConnection.DEA_IDLE_REQ();

//					LOGGER.debug("DEA_IDLE_REQ: " + Hex.bytesToHexString(res));
					if (res[0] == 0x00) {
//						cnt = 0;
					} else {
						cnt += 1;
					}
				}
				
				if (cnt == 10) {
					LOGGER.debug("Card removed");
					break;
				}
//				if (!ret) {
//					LOGGER.debug("Card removed");
//					if (callbackListerner != null && cardUid != null  &&  isRunning) {
//						//						callbackListerner.tagRemoved(cardUid);
//					}
//					break;
//				}
				Thread.sleep(500);
			}
		}
	}	
}