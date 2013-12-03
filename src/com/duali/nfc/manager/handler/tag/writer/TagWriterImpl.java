package com.duali.nfc.manager.handler.tag.writer;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.duali.nfc.connection.T1TConnection;
import com.duali.nfc.connection.T2TConnection;
import com.duali.nfc.connection.T3TConnection;
import com.duali.nfc.connection.T4TConnection;
import com.duali.nfc.connection.ITagConnection;
import com.duali.nfc.connection.UnknownConnection;
import com.duali.nfc.connection.exception.T1TConnectionException;
import com.duali.nfc.connection.mifare.MifareTagConnection;
import com.duali.nfc.core.connection.exception.ConnectionException;
import com.duali.nfc.manager.ui.dialog.util.TopazUtil;
import com.duali.nfc.ndef.Ndef;
import com.duali.nfc.ndef.NdefMessageEncoder;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.reader.handler.exception.NFCReaderException;
import com.duali.nfc.tag.MifareClassic;
import com.duali.nfc.tag.Tag;
import com.duali.nfc.tag.Type1Tag;
import com.duali.nfc.tag.Type2Tag;
import com.duali.nfc.tag.Type3Tag;
import com.duali.nfc.tag.Type4Tag;
import com.duali.nfc.tag.attribute.AttributeInformation;
import com.duali.nfc.tag.attribute.CapabilityContainer;
import com.duali.nfc.tag.attribute.CapabilityContainerType4;

public class TagWriterImpl implements TagWriter {


	private static Logger LOGGER = Logger
	.getLogger(TagWriterImpl.class);

	private static TagWriterImpl instance = null;
	private boolean RF_POLLING = true;
	/**
	 * <p>
	 * This method can be used for getting the instance.
	 * </p>
	 * 
	 * @return The instance.
	 */
	public static synchronized TagWriterImpl getInstance() {
		if (instance == null) {
			instance = new TagWriterImpl();
		}
		return instance; // returning instance.
	}
	
	private TagWriterImpl() {
	}

	private TagWriterThread tagWriterMF1kThread = null;


	@Override
	public synchronized void startNDEFWriteProcess(ArrayList<Record> outputData,			
			TagWriterListerner callbackListerner, NFCReaderHandler nfcReaderHandler) {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.stopRunning();
				tagWriterMF1kThread.interrupt();
			} catch (Exception e) {
			}
		}
		tagWriterMF1kThread = new TagWriterThread(outputData, callbackListerner, nfcReaderHandler);
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
		private TagWriterListerner callbackListerner = null;

		private NFCReaderHandler nfcReaderHandler = null;
		private String actualUid = null;

		/**
		 * Constructor for NDEFTagWriterMF1kImpl.TagWriterMF1K.
		 */
		public TagWriterThread(ArrayList<Record> outputData,
				TagWriterListerner callbackListerner, NFCReaderHandler nfcReaderHandler) {
			this.outputData = outputData;
			this.callbackListerner = callbackListerner;
			this.nfcReaderHandler = nfcReaderHandler;
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

				try {					
					if(paused){
						System.out.println("Paused");
						sleep(1000);
						continue;
					} 
					LOGGER.debug("Waiting for card");
					if (!nfcReaderHandler.waitForCardPresent(3000) &&  isRunning) {
						callbackListerner.updateStatusNoCard();
						continue;
					}
					if(!isRunning) return;


					// 타입에 따라 앞에 헤더 붙혀줘야 함.
					NdefMessageEncoder ndefMessageEncoder = Ndef.getNdefMessageEncoder();			
					byte[] ndef = ndefMessageEncoder.encode(outputData);

					ITagConnection connection = null;
					try {
						connection = nfcReaderHandler.getTagConnection();
					} catch (Exception e) {
						connection = null;
					} 

					if ( connection == null || connection instanceof UnknownConnection) {
						callbackListerner.unsupportedTagDetected();
					} else {
						if ( connection instanceof T1TConnection ) {
							writeType1Tag ( (T1TConnection) connection, ndef );
						} else if ( connection instanceof T2TConnection ) {
							writeType2Tag ( (T2TConnection) connection, ndef );
						} else if ( connection instanceof T3TConnection ) {
							writeType3Tag ( (T3TConnection) connection, ndef );
						} else if ( connection instanceof T4TConnection ) {
							writeType4Tag ( (T4TConnection) connection, ndef );	
						} else if ( connection instanceof MifareTagConnection ) {
							writeMifareTag ( (MifareTagConnection) connection, ndef );	
						} 
					}

					if(!RF_POLLING){
						nfcReaderHandler.waitForCardAbsent(1000);
						connection.RFPollingStart();
						//						Thread.sleep(50);
						RF_POLLING = true;
					}
					LOGGER.debug("Waiting for card to be removed");
					waitUntilCardRemoved(null);

				} catch (InterruptedException e) {
					isRunning = false;
					return;
				} catch (NFCReaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						// pn53xReader.disconnectNfcDevice();
						// pn53xReader.close();
					} catch (Exception e) {
					}
				}
			}
		}

		private void writeMifareTag(MifareTagConnection connection, byte[] ndef) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					MifareClassic mifareClassic = (MifareClassic) tag;				

					switch(	mifareClassic.GetType()) {
					case MifareClassic.MIFARE_CLASSIC_1k:
						if ( ndef.length >  716) {
							callbackListerner.tagSizeOver(ndef.length);
							return;
						}
						break;
					case MifareClassic.MIFARE_CLASSIC_4k:
						if ( ndef.length >  3356) {
							callbackListerner.tagSizeOver(ndef.length);
							return;
						}
						break;
					}
					
					mifareClassic.SetNdef(ndef);
					mifareClassic.Update(Tag.UPDATE_TAG_LEVEL_NDEF);

					callbackListerner.tagIssueSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagIssueFail();
			} finally {				
			}			
		}

		private void writeType4Tag(T4TConnection connection, byte[] ndef) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type4Tag type4Tag = (Type4Tag) tag;

					CapabilityContainerType4 cc = type4Tag.GetCapabilityContainerType4();

					int maxSize = cc.getNdefMaxSizeToInt();

					if ( ndef.length >  maxSize) {
						callbackListerner.tagSizeOver(ndef.length);
						return;
					}
					type4Tag.SetNdef(ndef);
					type4Tag.Update(Tag.UPDATE_TAG_LEVEL_NDEF);

					callbackListerner.tagIssueSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagIssueFail();
			} finally {				
			}			
		}

		private void writeType3Tag(T3TConnection connection, byte[] ndef) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type3Tag type3Tag = (Type3Tag) tag;

					AttributeInformation aib = type3Tag.GetAttributeInformation();

					int maxSize = aib.getNdefMaxSize();


					if ( ndef.length >  maxSize) {
						callbackListerner.tagSizeOver(ndef.length);
						return;
					}
					type3Tag.SetNdef(ndef);
					type3Tag.Update(Tag.UPDATE_TAG_LEVEL_NDEF);

					callbackListerner.tagIssueSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagIssueFail();
			} finally {				
			}			
		}

		private void writeType2Tag(T2TConnection connection, byte[] ndef) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type2Tag type2Tag = (Type2Tag) tag;

					CapabilityContainer cc = type2Tag.GetCapabilityContainer();

					if(cc.isNoFormatted()) {
						callbackListerner.noFormattedCC();
						return;
					}

					if(!cc.isValidNdefMagicNumber()) {
						callbackListerner.invalidNdefMagicNumber();
						return;
					}

					
					int memorySize = cc.getPhysicalTagMemorySize(Tag.NFC_FORUM_TYPE_2);
					
					switch(type2Tag.GetType()) {
					case Type2Tag.NXP_TYPE_MIFARE_ULTRALIGHT:
					case Type2Tag.NXP_TYPE_MIFARE_ULTRALIGHT_C:							
						break;
					case Type2Tag.INFINEON_TYPE_SLE_66R01P:	
//						memorySize = memorySize - 24;
						break;
					case Type2Tag.INFINEON_TYPE_SLE_66R32P:						
						break;
					case Type2Tag.KOVIO_2Kb:	
//						memorySize = memorySize - 24; // 4block * 6
						break;
					}
					
					
					if (memorySize > 255) {
						memorySize -=4;							
					} else {
						if (memorySize > 0)
							memorySize -=2;
					}

					if ( ndef.length >  memorySize) {
						callbackListerner.tagSizeOver(ndef.length);
						return;
					}
					type2Tag.SetNdef(ndef);
					type2Tag.Update(Tag.UPDATE_TAG_LEVEL_NDEF);

					callbackListerner.tagIssueSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagIssueFail();
			} finally {				
			}			
		}

		private void writeType1Tag(T1TConnection connection, byte[] ndef) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type1Tag topazTag = (Type1Tag) tag;

					CapabilityContainer cc = topazTag.GetCapabilityContainer();

					if(cc.isNoFormatted()) {
						callbackListerner.noFormattedCC();
						return;
					}

					if(!cc.isValidNdefMagicNumber()) {
						callbackListerner.invalidNdefMagicNumber();
						return;
					}

					int memorySize = cc.getPhysicalTagMemorySize(Tag.NFC_FORUM_TYPE_1);

					byte lock0 = topazTag.GetLock0();
					byte lock1 = topazTag.GetLock1();
					int cntLock0 = TopazUtil.getCountLockBlocks(lock0);
					int cntLock1 = TopazUtil.getCountLockBlocks(lock1);

					int nMax = 0;
					if (memorySize != 0)
						nMax = memorySize - (cntLock0 + cntLock1) * 8 - 6;

					if ( ndef.length >  nMax) {
						callbackListerner.tagSizeOver(ndef.length);
						return;
					}
					topazTag.SetNdef(ndef);
					topazTag.Update(Tag.UPDATE_TAG_LEVEL_NDEF);

					callbackListerner.tagIssueSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagIssueFail();
			} finally {
				try {
					connection.RFPollingStart();
				} catch (T1TConnectionException e1) {						
					e1.printStackTrace();
				} catch (ConnectionException e2) {
					e2.printStackTrace();
				}
			}			
		}

		/**
		 * <p>
		 * This is the method for .
		 * </p>
		 */
		private void waitUntilCardRemoved(String cardUid) throws Exception {
			while (true &&  isRunning) {
				boolean ret = nfcReaderHandler.waitForCardAbsent(3000);
				if (ret) {
					LOGGER.debug("Card removed");
					if (callbackListerner != null && cardUid != null  &&  isRunning) {
						//						callbackListerner.tagRemoved(cardUid);
					}
					break;
				}
				Thread.sleep(500);
			}
		}
	}
}
