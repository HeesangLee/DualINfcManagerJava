package com.duali.nfc.manager.handler.tag.reader;

import org.apache.log4j.Logger;

import com.duali.nfc.connection.ITagConnection;
import com.duali.nfc.connection.UnknownConnection;
import com.duali.nfc.connection.exception.T1TConnectionException;
import com.duali.nfc.core.connection.exception.ConnectionException;
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.reader.handler.exception.NFCReaderException;
import com.duali.nfc.tag.Tag;

public class TagReaderImpl implements TagReader {

	private static Logger LOGGER = Logger
	.getLogger(TagReaderImpl.class);

	private static TagReaderImpl instance = null;

	private boolean RF_POLLING = true;

	/**
	 * <p>
	 * This method can be used for getting the instance.
	 * </p>
	 * 
	 * @return The instance.
	 */
	public static synchronized TagReaderImpl getInstance() {
		if (instance == null) {
			instance = new TagReaderImpl();
		}
		return instance; // returning instance.
	}

	/**
	 * Constructor for NDEFTagReader.
	 */
	private TagReaderImpl() {
	}

	private TagReader TagReaderThread = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aditux.ems.card.NDEFTagWriter#startNDEFWriteProcess()
	 */
	@Override
	public synchronized void startNDEFReadProcess(TagReaderListerner callbackListerner, NFCReaderHandler nfcReaderHandler) {
		if (TagReaderThread != null) {
			try {
				TagReaderThread.stopRunning();
				TagReaderThread.interrupt();
			} catch (Exception e) {
			}
		}
		TagReaderThread = new TagReader(callbackListerner, nfcReaderHandler);
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

	class TagReader extends Thread {

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
		private TagReaderListerner callbackListerner = null;

		private NFCReaderHandler nfcReaderHandler = null;
//		private String actualUid = null;

		/**
		 * Constructor for NDEFTagReaderImpl.TagReader.
		 */
		public TagReader(TagReaderListerner callbackListerner, NFCReaderHandler nfcReaderHandler) {
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
				callbackListerner.ndefReadingProcessStarted();
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
					if (!nfcReaderHandler.waitForCardPresent(1000) &&  isRunning) {
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
						Tag tag = connection.GetTag(Tag.GET_TAG_LEVEL_INFO | Tag.GET_TAG_LEVEL_NDEF_DATA);
						callbackListerner.updateTag(tag);
						
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
					e.printStackTrace();
				} catch (T1TConnectionException e) {
					e.printStackTrace();
				} catch (ConnectionException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}  finally {
					try {
					} catch (Exception e) {
					}
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
				//				nfcReaderHandler.isCardPresent()
				if (ret) {
					LOGGER.debug("Card removed");
					if (callbackListerner != null && cardUid != null  &&  isRunning) {
						callbackListerner.tagRemoved(cardUid);
					}
					break;
				}
				Thread.sleep(500);
			}
		}
	}
}