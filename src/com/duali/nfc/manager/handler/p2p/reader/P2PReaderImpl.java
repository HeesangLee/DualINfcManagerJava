package com.duali.nfc.manager.handler.p2p.reader;

import org.apache.log4j.Logger;

import com.duali.nfc.connection.UnknownConnection;
import com.duali.nfc.core.connection.exception.ConnectionException;
import com.duali.nfc.p2p.connection.P2PConnection;
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.reader.handler.exception.NFCReaderException;

public class P2PReaderImpl implements P2PReader {

	private static Logger LOGGER = Logger
	.getLogger(P2PReaderImpl.class);

	private static P2PReaderImpl instance = null;

	/**
	 * <p>
	 * This method can be used for getting the instance.
	 * </p>
	 * 
	 * @return The instance.
	 */
	public static synchronized P2PReaderImpl getInstance() {
		if (instance == null) {
			instance = new P2PReaderImpl();
		}
		return instance; // returning instance.
	}

	/**
	 * Constructor for NDEFTagReader.
	 */
	private P2PReaderImpl() {
	}

	private P2PReader TagReaderThread = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aditux.ems.card.NDEFTagWriter#startNDEFWriteProcess()
	 */
	@Override
	public synchronized void startNDEFReadProcess(P2PReaderListerner callbackListerner,
			NFCReaderHandler nfcReaderHandler, String p2pType) {
		if (TagReaderThread != null) {
			try {
				TagReaderThread.stopRunning();
				TagReaderThread.interrupt();
			} catch (Exception e) {
			}
		}
		TagReaderThread = new P2PReader(callbackListerner, nfcReaderHandler, p2pType);
		TagReaderThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aditux.ems.card.NDEFTagWriter#pauseNDEFCreationProcess()
	 */
	@Override
	public void pauseNDEFReadProcess() {
		if (TagReaderThread != null) {
			try {
				TagReaderThread.setPaused(true);
			} catch (Exception e) {
			}
		}
	}


	/* (non-Javadoc)
	 * @see com.aditux.ndefcreate.nfc.card.NDEFTagWriter#resumeNDEFCreationProcess()
	 */
	@Override
	public void resumeNDEFReadProcess() {
		if (TagReaderThread != null) {
			try {
				TagReaderThread.setPaused(false);
			} catch (Exception e) {
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aditux.ems.card.NDEFTagWriter#finishNDEFCreationProcess()
	 */
	@Override
	public void finishNDEFReadProcess() {
		if (TagReaderThread != null) {
			try {
				
				TagReaderThread.stopRunning();
				TagReaderThread.interrupt();
			} catch (Exception e) {
			}
		}
	}

	class P2PReader extends Thread {

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

		//private int numberOfTagsWritten = 0;
		//private NDEFTag ndefTag = null;
		//private int numberOfTagsTobeWritten = 0;
		private P2PReaderListerner callbackListerner = null;

		private NFCReaderHandler nfcReaderHandler = null;
		
		private String p2pType = null;
		//		private String actualUid = null;

		/**
		 * Constructor for NDEFTagReaderImpl.TagReader.
		 * @param p2pType 
		 */
		public P2PReader(P2PReaderListerner callbackListerner, NFCReaderHandler nfcReaderHandler, String p2pType) {
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			isRunning = true;
			if (callbackListerner != null &&  isRunning) {
				if(callbackListerner.isAlive())
					callbackListerner.ndefReadingProcessStarted();
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

					LOGGER.debug("Waiting for card!!");
					if (!nfcReaderHandler.waitForCardPresent(1000) &&  isRunning) {
						if(callbackListerner.isAlive())
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

					if ( connection == null || connection instanceof UnknownConnection) {
						//						callbackListerner.unsupportedTagDetected();
					} else {
						if(callbackListerner.isAlive())
							callbackListerner.reading();
						byte[] data = connection.ReceiveNdef(p2pType);

						if (data != null) {
							if(callbackListerner.isAlive()) {
								callbackListerner.updateNdef(data);
								callbackListerner.readSuccess();
							}
						} else {
							if(callbackListerner.isAlive())
								callbackListerner.readFail();
						}
						Thread.sleep(1000);
					}												                 

					LOGGER.debug("Waiting for card to be removed");
					waitUntilCardRemoved(null);
				} catch (ConnectionException e) {
					e.printStackTrace();
					LOGGER.error(e.getMessage());
					
				} catch (NFCReaderException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(connection != null)
							connection.RFPollingStart();
					} catch (Exception e) {						
					}
				}
			}
		}

		private boolean waitForCardPresent() throws InterruptedException, ConnectionException, NFCReaderException {
			Thread.sleep(500);

			P2PConnection p2pConnection = nfcReaderHandler.getP2PConnection();

			int cnt = 0;
			for (int i = 0; i < 10; i++) {
				byte[] res = p2pConnection.DEA_IDLE_REQ();

				if (res[0] != 0x00) {
					cnt += 1;
				}
			}

			if (cnt == 10) {
//				LOGGER.debug("Card removed");
				return false;
			}
			Thread.sleep(500);

			return true;
		}

		/**
		 * <p>
		 * This is the method for .
		 * </p>
		 */
		private void waitUntilCardRemoved(String cardUid) throws Exception {
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