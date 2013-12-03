package com.duali.nfc.manager.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;

import org.apache.log4j.Logger;

/**
 *  <p> Searches for PC SC Readers connected to the PC, 
 * and notifies listeners if any change occurred.</p>
 * 
 * <dt><b>Date</b>
 * 		  <dd>Sep 28, 2011, 12:21:03 PM
 * <dt><b>Module</b>
 * 		  <dd>ndef-creator 
 */
public final class PCSCReaderUpdator extends Thread {

	/** is reader search is going on*/
	private boolean isSearchingForReader=false;

	/** Apache log4j logger for ReaderStatusThread.*/
	private static final Logger LOGGER =
		Logger.getLogger(PCSCReaderUpdator.class);

	private CardTerminal[] currentCardTerminals = null;

	public static final String[][] NFCMANAGER_SUPPORTED_READER_STRINGS = new String[][]{new String[]{"DUALi", "Duali"/*, "DE-ABCM","COMBI"*/}}; //new String[]{"ACR122","OMNIKEY","DUALI"};  //"OMNIKEY CardMan 5x21-CL 0",
	//    public static final String[][] TAGMANAGER_SUPPORTED_READER_STRINGS = new String[][]{new String[]{"DUALi", "Duali","DE-620","Contactless"}}; //new String[]{"ACR122","OMNIKEY","DUALI"};  //"OMNIKEY CardMan 5x21-CL 0",
	public static final String[]   NFCMANAGER_SUPPORTED_READERS = new String[]{"Duali Dragon Contactless", "Duali DE-620 Contactless", "DE-ABCM6 Contactless"/*, "DUALi DE-ABCM COMBI", "DUALi DRAGON NFC"*/};

	/** Terminal factory*/
	private static TerminalFactory terminalFactory = TerminalFactory.getDefault();

	private static PCSCReaderUpdator instance;

	private static List<PCSCReaderListener> readerListeners = new ArrayList<PCSCReaderListener>();

	public void addPCSCReaderHirarchyListener(PCSCReaderListener listener){
		synchronized (readerListeners) {
			readerListeners.add(listener);
		}
	}

	public void removePCSCReaderHirarchyListener(PCSCReaderListener listener){
		synchronized (readerListeners) {
			readerListeners.remove(listener);
		}
	}


	private PCSCReaderUpdator() {
		super();
	}

	/**
	 * Retrieves the single instance of PCSCReaderHirarchyUpdator.
	 * @return The instance of PCSCReaderHirarchyUpdator.
	 */
	public static synchronized PCSCReaderUpdator getInstance() {
		if (instance == null) {
			instance = new PCSCReaderUpdator();
		}
		return instance;
	}


	public void startReaderUpdator(){
		if(isSearchingForReader) return;
		this.isSearchingForReader = true;
		this.start();
	}
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		LOGGER.debug("Starting Reader UPDator");//Logging for debug purpose.
		while (isSearchingForReader) {          
			CardTerminals cardTerminals = terminalFactory.terminals();        
			try { 
				List<CardTerminal> cardTerminalList = cardTerminals.list();   
				//System.out.println("cardTerminalList.size: "+cardTerminalList.size());
				if((cardTerminalList == null && currentCardTerminals != null)
						|| (cardTerminalList != null && currentCardTerminals == null) ){

					currentCardTerminals = new CardTerminal[cardTerminalList.size()];
					for(int i=0;i<cardTerminalList.size();i++){
						currentCardTerminals[i]=cardTerminalList.get(i);
					}
					fireReaderStatusEvent();
				} else if (cardTerminalList != null && !Arrays.equals(currentCardTerminals, cardTerminalList.toArray())) {
					currentCardTerminals = new CardTerminal[cardTerminalList.size()];
					for(int i=0;i<cardTerminalList.size();i++){
						currentCardTerminals[i]=cardTerminalList.get(i);
					}
					fireReaderStatusEvent();
				}
			} catch (CardException c) {    
				if(currentCardTerminals != null){
					currentCardTerminals = null;
					fireReaderStatusEvent();
				}
			}
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * STOPs search for the reader
	 */
	public void stopSearchingForReader(){
		isSearchingForReader=false;
		try {
			this.interrupt();
		} catch (Exception e) {
		}
	}   

	/** 
	 * <p>This is the method for firing 
	 * event to all registred handlers.</p>
	 */
	private void fireReaderStatusEvent(){
		// Logging for debug purpose.---------------------------------------------------------------------------------
		if (currentCardTerminals != null) {
			for ( int i = 0; i < currentCardTerminals.length; i++) {
				LOGGER.debug("In fireReaderStatusEvent currentCardTerminal: " + currentCardTerminals[i].getName());
			}
		}
		//-----------------------------------------------------------------------------------------------------------
		
		if(currentCardTerminals == null){
			LOGGER.debug("PCSC Reader changed. Number of available readers : " + 0);//Logging for debug purpose.
			for (PCSCReaderListener listener : readerListeners) {
				listener.readerChanged(null, currentCardTerminals);
			}
		}else{
			LOGGER.debug("PCSC Reader hirarchy changed. Number of available readers : " + currentCardTerminals.length);//Logging for debug purpose.
			List<String> terminalNameList = new ArrayList<String>();
			for (CardTerminal cardTerminal : currentCardTerminals) {
				if(isSupportedReader(cardTerminal.getName())){
					LOGGER.debug("Card Terminal : " + cardTerminal.getName());
					terminalNameList.add(cardTerminal.getName());
				}
			}
			String[] readerArray = (String[]) terminalNameList.toArray(new String[0]);
			LOGGER.debug("Number of supported readers : " + readerArray.length );
			for (PCSCReaderListener listener : readerListeners) {
				listener.readerChanged(readerArray, currentCardTerminals);
			}
		}

	}

	private boolean isSupportedReader(String readerName){
//		LOGGER.debug("In isSupportedReader readerName: "+readerName);
		for(String[] supported : NFCMANAGER_SUPPORTED_READER_STRINGS){
			boolean found = true;
			for (String sub : supported) {
				if (readerName.toUpperCase().indexOf(sub.toUpperCase()) == -1) {
					found = false;
				}
			}
			if(found)
				return true;
		}
		return false;
	}

}
