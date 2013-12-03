package com.duali.nfc.manager.handler.tag.formatter;

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
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.reader.handler.exception.NFCReaderException;
import com.duali.nfc.tag.MifareClassic;
import com.duali.nfc.tag.Tag;
import com.duali.nfc.tag.Type1Tag;
import com.duali.nfc.tag.Type2Tag;
import com.duali.nfc.tag.Type3Tag;
import com.duali.nfc.tag.Type4Tag;

public class TagFomatterImpl implements TagFormatter {


	private static Logger LOGGER = Logger
	.getLogger(TagFomatterImpl.class);

	private static TagFomatterImpl instance = null;
	private boolean RF_POLLING = true;
	/**
	 * <p>
	 * This method can be used for getting the instance.
	 * </p>
	 * 
	 * @return The instance.
	 */
	public static synchronized TagFomatterImpl getInstance() {
		if (instance == null) {
			instance = new TagFomatterImpl();
		}
		return instance; // returning instance.
	}

	/**
	 * Constructor for NDEFTagWriterMF1K.
	 */
	private TagFomatterImpl() {
	}

	/*
	 * Implementation details of NDEFTagWriterMF1K :
	 */

	private TagFormatterThread tagWriterMF1kThread = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aditux.ems.card.NDEFTagWriter#startNDEFWriteProcess()
	 */
	@Override
	public synchronized void startTagFormatProcess(TagFormatterListerner callbackListerner, NFCReaderHandler nfcReaderHandler) {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.stopRunning();
				tagWriterMF1kThread.interrupt();
			} catch (Exception e) {
			}
		}
		tagWriterMF1kThread = new TagFormatterThread(callbackListerner, nfcReaderHandler);
		tagWriterMF1kThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aditux.ems.card.NDEFTagWriter#pauseNDEFCreationProcess()
	 */
	@Override
	public void pauseTagFormatProcess() {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.setPaused(true);
			} catch (Exception e) {
			}
		}		
	}


	/* (non-Javadoc)
	 * @see com.aditux.ndefcreate.nfc.card.NDEFTagWriter#resumeNDEFCreationProcess()
	 */
	@Override
	public void resumeTagFormatProcess() {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.setPaused(false);
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
	public void finishTagFormatProcess() {
		if (tagWriterMF1kThread != null) {
			try {
				tagWriterMF1kThread.stopRunning();
				tagWriterMF1kThread.interrupt();
			} catch (Exception e) {
			}
		}
	}

	class TagFormatterThread extends Thread {

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

		private TagFormatterListerner callbackListerner = null;

		private NFCReaderHandler nfcReaderHandler = null;
		private String actualUid = null;

		/**
		 * Constructor for NDEFTagWriterMF1kImpl.TagWriterMF1K.
		 */
		public TagFormatterThread(
				TagFormatterListerner callbackListerner, NFCReaderHandler nfcReaderHandler) {
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			isRunning = true;
			if (callbackListerner != null &&  isRunning) {
				callbackListerner.tagFormatProcessStarted();
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
							formatType1Tag ( (T1TConnection) connection );
						} else if ( connection instanceof T2TConnection ) {
							formatType2Tag ( (T2TConnection) connection );
						} else if ( connection instanceof T3TConnection ) {
							formatType3Tag ( (T3TConnection) connection );
						} else if ( connection instanceof T4TConnection ) {
							formatType4Tag ( (T4TConnection) connection );	
						} else if ( connection instanceof MifareTagConnection ) {
							formatMifareTag ( (MifareTagConnection) connection );	
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

		private void formatMifareTag(MifareTagConnection connection) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					MifareClassic mifare1k = (MifareClassic) tag;
					mifare1k.Format();

					callbackListerner.tagFormatSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagFormatFail();
			} finally {				
			}			
		}

		private void formatType4Tag(T4TConnection connection) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type4Tag type4Tag = (Type4Tag) tag;
					
					connection.RFPollingStop();
					connection.FindCard();
					type4Tag.Format();

					callbackListerner.tagFormatSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagFormatFail();
			} finally {		
				try {
					connection.RFPollingStart();
					connection.RFOff();
				} catch (ConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}

		private void formatType3Tag(T3TConnection connection) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type3Tag type3Tag = (Type3Tag) tag;
					type3Tag.Format();

					callbackListerner.tagFormatSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagFormatFail();
			} finally {				
			}			
		}

		private void formatType2Tag(T2TConnection connection) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type2Tag type2Tag = (Type2Tag) tag;
					type2Tag.Format();

					callbackListerner.tagFormatSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagFormatFail();
			} finally {				
			}			
		}

		private void formatType1Tag(T1TConnection connection) {
			try{
				Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO);

				if (callbackListerner != null  &&  isRunning) {
					callbackListerner.tagDetected(tag);

					Type1Tag topazTag = (Type1Tag) tag;
					topazTag.Format();

					callbackListerner.tagFormatSuccess(tag);
				}
			} catch (Exception e) {
				callbackListerner.tagFormatFail();
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
