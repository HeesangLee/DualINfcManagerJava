package com.duali.nfc.manager.ui.res;

import java.io.IOException;

import com.duali.nfc.manager.ui.utils.OrderedProperties;

public class NFCForumRTDResource {
 

    /** The URI_IDENTIFIER_CODES is declared for keeping URI identifier codes
     * defined in URI record type definition document.*/
    public static final OrderedProperties URI_IDENTIFIER_CODES = new OrderedProperties(); 
    
    /** The SP_ACTION_RECORD_VALUES is declared for keeping nfc forum smart
     * poster rtd's action record values.*/
    public static final OrderedProperties SP_ACTION_RECORD_VALUES = new OrderedProperties();

    /** Static loading of URI_IDENTIFIER_CODES.*/
    static{
        try {
            URI_IDENTIFIER_CODES.load(NFCForumRTDResource.class.getResourceAsStream("/nfcforum/recordtypeuri.properties"));
        } catch (IOException e) {
        }
        
        try {
            SP_ACTION_RECORD_VALUES.load(NFCForumRTDResource.class.getResourceAsStream("/nfcforum/spactiontypes.properties"));
        } catch (IOException e) {
        }
    }

    /** 
     * <p>This method returns the URI identifier codes loaded from the resource.</p>
     * @return URI_IDENTIFIER_CODES
     */
    public static final OrderedProperties getURIIdentifierCodes(){
        return URI_IDENTIFIER_CODES;
    }
    
    /** 
     * <p>This method returns the Smart poster action record types loaded from the resource.</p>
     * @return SP_ACTION_RECORD_VALUES
     */
    public static final OrderedProperties getSPActionRecordTypes(){
        return SP_ACTION_RECORD_VALUES;
    }
}
