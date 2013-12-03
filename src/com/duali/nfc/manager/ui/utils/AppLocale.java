package com.duali.nfc.manager.ui.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * <p> Contains the localized messages, are used to display.</p>
 * <dt><b>Date</b>
 * 		  <dd>Sep 23, 2011, 6:19:31 PM
 * <dt><b>Module</b>
 * 		  <dd>ndef-creator 
 */
public final class AppLocale {
    /** Apache log4j logger for AppLocale.*/
    private static final Logger LOGGER = Logger.getLogger(AppLocale.class);
    /** properties */
    private static Properties properties =null;
    /** url to resources */
    private final static String PROPERTIES_NAME = "i18n/locale_en.properties";

    private AppLocale() {
    }
    
    public static final String APP_NAME = getText("APP.NAME");
    public static final String VERSION = getText("VERSION");
    
    public static final String MSG_WAIT_WRITING_P2P = getText("MSG.WAIT.WRITING.P2P");
    public static final String MSG_SUCCESS_P2P_FINISH = getText("MSG.SUCCESS.P2P.FINISH");
    public static final String MSG_P2P_SENT_REMOVE = getText("MSG.P2P.SENT.REMOVE");
    public static final String MSG_PLACE_P2P_DEVICE = getText("MSG.PLACE.P2P.DEVICE");
//    public static final String MSG_SUCCS_SENT_FINISH = getText("MSG.SUCCS.SENT.FINISH");
    
    public static final String CANCEL_CARD_CREATION = getText("CANCEL.CARD.CREATION");
    public static final String CANCEL_CARD_FORMAT = getText("CANCEL.CARD.FORMAT");
    public static final String MSG_PLACE_MF_CARD = getText("MSG.PLACE.MF.CARD");
    public static final String MSG_UNSUPPORTED_CARD = getText("MSG.UNSUPPORTED.CARD");
    public static final String MSG_SIZEOVER_CARD = getText("MSG.SIZEOVER.CARD");
    public static final String MSG_CC_CARD = getText("MSG.CC.CARD");
    public static final String MSG_WAIT_WRITING_TO_CARD = getText("MSG.WAIT.WRITING.CARD");
    public static final String MSG_WAIT_FORMATING_CARD = getText("MSG.WAIT.FRMTING.CARD");
    public static final String MSG_AUTH_FAILED = getText("MSG.AUTH.FAILED");
    public static final String MSG_ERR_WRITE_CARD = getText("MSG.ERROR.WRITE");
    public static final String MSG_ERR_WRITE_CARD_SIZE_OVER = getText("MSG.ERROR.WRITE.SIZE.OVER");
    
    public static final String MSG_ERR_FORMAT_CARD = getText("MSG.ERROR.FORMAT");
    public static final String MSG_ERR_LOCK_CARD = getText("MSG.ERROR.LOCK");
    
    public static final String GROUP_CARD_DETAILS = getText("GROUP.CARD.DETAILS");
    public static final String CARD_UID = getText("CARD.UID");
    public static final String CARD_TYPE = getText("CARD.TYPE");
    public static final String TODO_PEND = getText("GROUP.TO.DO");
    public static final String CARD_PENDING = getText("CARD.PENDING");
    public static final String CARD_COMPLTED = getText("CARD.COMPLTED");
    public static final String MSG_READER_ABSENT = getText("MSG.READER.NOTFOUND");
    public static final String MSG_ERR_WHILE_CREATE_CARD = getText("MSG.ERROR.CREAT.TAG");
    public static final String MSG_CARD_CREATED_REMOVE = getText("MSG.SUCCESS.CREAT.TAG.REM");
    public static final String MSG_CARD_FRMTED_REMOVE = getText("MSG.SUCCESS.FRMT.TAG.REM");
    public static final String MSG_SUCCS_CREATED_FINISH = getText("MSG.SUCCESS.ALL.FINISH");
    public static final String MSG_SUCCS_FORMATED_FINISH = getText("MSG.SUCCESS.FRMT.ALL.FINISH");
    
    public static final String MSG_ENTR_TEXT = getText("MSG.ENTR.TEXT");
    public static final String MSG_ENTR_TITLE = getText("MSG.ENTR.TITLE");
    
    public static final String MSG_ENTR_IMAGE_PATH = getText("MSG.ENTR.IMAGE.PATH");
    
    public static final String MSG_ENTR_MESSAGE = getText("MSG.ENTR.MESSAGE");
    public static final String MSG_ALL_FIELDS_MANDTRY = getText("MSG.ALL.FIELDS.MANDTRY");
    
    public static final String MSG_ISSUE_NO_RECORD = getText("MSG.ISSUE.NO.RECORD");
    public static final String MSG_ISSUE_ONLY_ONE_RECORD = getText("MSG.ISSUE.ONLY.ONE.RECORD");
    
    public static final String COMBO_DFLT_TEXT = getText("COMBO_DFLT_TEXT");
    
    public static final String BKMARK_NAME = getText("BM.NAME");
    public static final String BSCARD_NAME = getText("BSC.NAME");
    public static final String CALREQ_NAME = getText("CALREQ.NAME");
    public static final String SMS_NAME = getText("SMS.NAME");
    
    public static final String RADIO_T1T = getText("CARD.T1T");
    public static final String RADIO_MIFARE_UL = getText("CARD.MIFARE.UL");
    public static final String RADIO_MIFARE_NTAG203 = getText("CARD.MIFARE.NTAG203");
    public static final String RADIO_MYDMOVESLE01 = getText("CARD.MYDMOVESLE01");
    public static final String RADIO_MYDMOVESLE32 = getText("CARD.MYDMOVESLE32");
    public static final String RADIO_DESFIREEV12K = getText("CARD.DESFIREEV12K");
    
    public static final String SELECT_TAG = getText("CARD.SELECT");
    public static final String CAPABILITY_CONTIANER = getText("CAPABILITY.CONTIANER");
    public static final String CC_CARD_FORMAT = getText("CC.CARD.FORMAT");
    
    public static String getText(String key){
        if(properties == null){
            initLocale();
        }
        return properties.getProperty(key, "No text");
    }
    
    public synchronized static String getText(String key, String[] placeHoldValues) {
        String txt = getText(key);
        //System.out.println(txt);
        if(txt!=null && placeHoldValues!=null){
            for(int i=0;i<placeHoldValues.length;){
                String placer = "{"+i+"}";
                if(txt.indexOf(placer)!=-1){
                    txt = txt.substring(0,txt.indexOf(placer))+placeHoldValues[i]+txt.substring(txt.indexOf(placer)+placer.length(),txt.length());
                }else{
                    i++;
                }
            }
            //System.out.println(txt);
        }
        return txt;
    }

    private static void initLocale(){
        try {
            properties= new Properties();
            properties.load(getPropertiesInputStream(PROPERTIES_NAME));

        } catch (FileNotFoundException e1) {
            LOGGER.error(PROPERTIES_NAME + " does not exist in classpath");
        } catch (IOException e1) {
            LOGGER.error(PROPERTIES_NAME + " cannot be either opened or read");
        }
    }

    /**
     * loads properties file using given name (prefix), full name or path
     * 
     * @author stell
     * @param name properties file name
     * @return properties file input stream
     */
    private static InputStream getPropertiesInputStream(String name) throws FileNotFoundException {
        /* prepare classloader to load properties */
        ClassLoader classloader = ClassLoader.getSystemClassLoader();
        InputStream stream = null;

        /* find, if can get it without 'properties' suffix */
        stream = classloader.getResourceAsStream(name);
        if (stream == null) stream = classloader.getResourceAsStream(name + ".properties");
        if (stream == null) stream = AppLocale.class.getClass().getResourceAsStream(name);
        if (stream == null) stream = AppLocale.class.getClass().getResourceAsStream(name + ".properties");
        if (stream == null) throw new FileNotFoundException("property file " + name + " could not be found");

        /* return stream */
        return stream;
    }
}
