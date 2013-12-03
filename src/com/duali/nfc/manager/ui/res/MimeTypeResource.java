package com.duali.nfc.manager.ui.res;

import java.io.IOException;

import com.duali.nfc.manager.ui.utils.OrderedProperties;

public class MimeTypeResource {
 
    public static final OrderedProperties MIME_TYPE_LIST = new OrderedProperties(); 
    
    static{
        try {
        	MIME_TYPE_LIST.load(MimeTypeResource.class.getResourceAsStream("/rfc/mimetype.properties"));
        } catch (IOException e) {
        }
    }

    public static final OrderedProperties getMimeTypeList(){
        return MIME_TYPE_LIST;
    }
}
