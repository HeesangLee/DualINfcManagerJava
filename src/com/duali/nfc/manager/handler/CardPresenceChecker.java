package com.duali.nfc.manager.handler;

import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import org.apache.log4j.Logger;

import com.duali.utils.Hex;

public class CardPresenceChecker extends Thread{
	private static final Logger LOGGER =
		Logger.getLogger(CardPresenceChecker.class);
	/* Implementation details of CardPresenceChecker :    
	 */

	private boolean isSearchingForCard = false;

	private CardTerminal cardTerminal = null;

	private Card card = null;

	private CardChannel cardChannel = null;

	private CardListener cardListener = null;

	private boolean cardConnected = false;

	/**
	 * Constructor for CardPresenceChecker.
	 * @throws CardException 
	 */
	public CardPresenceChecker(String readerName, CardListener cardListener) throws CardException {
		this.cardListener = cardListener;
		LOGGER.debug("CardPresenceChecker readerName: "+readerName);
		CardTerminals cardTerminals= TerminalFactory.getDefault().terminals();             
		List<CardTerminal> cardTerminalList = cardTerminals.list();
		if(cardTerminalList != null && !cardTerminalList.isEmpty()){
			for (int i = 0; i < cardTerminalList.size(); i++) {
				if(cardTerminalList.get(i).getName().toUpperCase().indexOf(readerName.toUpperCase()) != -1){
					cardTerminal = cardTerminalList.get(i);
					break;
				}
			}
		}
		if(cardTerminal == null){
			throw new CardException("Error opening selected reader.");
		} else {
//			if(cardTerminal.isCardPresent()){                                        
//				card = cardTerminal.connect("*");
//				cardChannel = card.getBasicChannel();
//				cardChannel.transmit(new CommandAPDU(getStartRF()));
//			}
		}
	}
	private byte[] getStartRF() {
		byte[] readCom = new byte[]{	// Polling
				(byte)0xFE,     
				(byte)0x81,     
				(byte)0xFE,     
				(byte)0xFE,  
				(byte)0x01,	//Length
				(byte)0x00, //Polling command
		};  
		LOGGER.debug("Start RF Checking Command: "+Hex.toHexString(readCom));
		return readCom;
	}

	private byte[] getVersion() {
		byte[] readCom = new byte[]{	// Polling
				(byte)0xFE,     
				(byte)0x16,     
				(byte)0xFE,     
				(byte)0xFE,  
				(byte)0x00 
		};  
		LOGGER.debug("getVersion Command: "+Hex.toHexString(readCom));
		return readCom;
	}
	
	/** 
	 * <p>This is the method for .</p>
	 */
	public void run() {
		isSearchingForCard=true;
		while (isSearchingForCard) {
			try {
				if(!isCardConnected()){
					//					LOGGER.debug("CardPresenceChecker Run: " + cardTerminal.getName());
					cardTerminal.waitForCardPresent(1000);  
					if(cardTerminal.isCardPresent()){                                        
						card = cardTerminal.connect("*");
						cardChannel = card.getBasicChannel();
						setCardConnected(true);

						if(cardTerminal.getName().toLowerCase().startsWith("duali")){

							try {	
								
//								ResponseAPDU res = cardChannel.transmit(new CommandAPDU(getVersion()));
//								LOGGER.debug("res: "+Hex.toHexString(res.getBytes()));
															
								
								cardListener.cardConnected(card);
							} catch (Exception e) {
								System.out.println("Error getting UID.");
							}
						}
						/*else{

							try {
								//								String uid = getUid();
								cardListener.cardConnected(getUid());
							} catch (Exception e) {
								System.out.println("Error getting UID.");
							}
							//System.out.println("******************CARD CONNECTED***************");
						}*/
					}
				}
				if(isCardConnected()){
					cardTerminal.waitForCardAbsent(0);
					if(!cardTerminal.isCardPresent()){   
						cardChannel=null;
						card=null;
						setCardConnected(false);
						cardListener.cardRemoved();                           
						// System.out.println("******************CARD DISCONNECTED***************");
					}
				}                      
			} catch (CardException e) {  
				cardChannel=null;
				card=null;
				setCardConnected(false);    
//				isSearchingForCard=false;
//				break;
			} catch (Exception e) {
				cardChannel=null;
				card=null;
				setCardConnected(false);    
//				isSearchingForCard=false;
//				break;
			} 
		}
		LOGGER.debug("CardPresenceChecker Theread Exit");
		super.run();
	}


	/**
	 * <p>This method can be used for getting the cardConnected.</p>
	 * @return The cardConnected.
	 */
	private boolean isCardConnected() {
		return cardConnected; // returning cardConnected.
	}

	/**
	 * <p>This method can be used for setting the cardConnected.</p>
	 * @param cardConnected The cardConnected to set.
	 */
	private void setCardConnected(boolean cardConnected) {
		this.cardConnected = cardConnected; // Assigning to this.cardConnected.
	}

	
	public static void main(String[] args) {
		String readerName = "ACR122";
		try {
			CardPresenceChecker cardPC = new CardPresenceChecker(readerName, new CardListener() {

				@Override
				public void cardRemoved() {
					// TODO Auto-generated method stub
					System.out.println("Card removed ");
				}

				@Override
				public void cardConnected(Card card) {
					System.out.println("Card connected " + Hex.bytesToHexString(card.getATR().getHistoricalBytes()));
				}
			});

			cardPC.start();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}


	/** 
	 * <p>This is the method for .</p>
	 */
	public void stopCheck() {
		isSearchingForCard = false;
	}
}
