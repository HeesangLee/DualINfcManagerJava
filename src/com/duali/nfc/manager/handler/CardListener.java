package com.duali.nfc.manager.handler;

import javax.smartcardio.Card;


public interface CardListener {
     
   void cardConnected(Card card);
   
   void cardRemoved();
}
