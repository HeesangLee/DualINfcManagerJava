package com.duali.nfc.manager.handler;

import javax.smartcardio.CardTerminal;

public interface PCSCReaderListener {
    
    /** 
     * <p>This Method will be fire if a reader is attached or
     * detached from the PC.</p>
     * @param updatedReaderList the currently connected reader list.
     * @param cardTerminals TODO
     */
    void readerChanged(String[] updatedReaderList, CardTerminal[] cardTerminals);
}
