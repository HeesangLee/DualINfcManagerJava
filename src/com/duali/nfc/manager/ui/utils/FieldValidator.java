package com.duali.nfc.manager.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * <p> </p>
 * 
 * <dt><b>Date</b>
 * 		  <dd>Sep 23, 2011, 7:09:26 PM
 * <dt><b>Module</b>
 * 		  <dd>ndef-creator 
 */
public class FieldValidator {
    /* Implementation details of FieldValidator :    
     */
    
    /**
     * <pre>
     * Format: email addres as per RFC 2822.
     * </pre>
     *
     * <tt>
     * We get a more practical implementation of RFC 2822
     * if we omit the syntax using double quotes and square
     * brackets. It will still match 99.99% of all email addresses
     * in actual use today.
     * 
     * [a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?
     * 
     * A further change you could make is to allow any two-letter
     * country code top level domain, and only specific generic top
     * level domains. This regex filters dummy email addresses like
     * asdf@adsf.adsf. You will need to update it as new top-level
     * domains are added.
     * 
     * [a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+(?:[A-Z]{2}|com|org|net|edu|gov|mil|biz|info|mobi|name|aero|asia|jobs|museum)\b
     * </tt>
     *
     * @param input
     *            <tt>email string to be varified.</tt>
     * @return true if varification OK
     */
    public static boolean isEmailString(final String input) {
        Pattern p = Pattern.compile(PATTERN_START1);
        Matcher m = p.matcher(input);
        if (m.find()){
          /* System.err.println("Email addresses don't start" +
                              " with dots or @ signs.");*/
           return false;
        }
        p = Pattern.compile(PATTERN_START2);
        m = p.matcher(input);
        if (m.find()) {
         /* System.out.println("Email addresses don't start" +
                  " with \"www.\", only web pages do.");*/
          return false;
        }
        p = Pattern.compile(PATTERN_START3);
        m = p.matcher(input);
        if (m.find()) {
            /* System.out.println("Email addresses don't start" +
                     " with \"www.\", only web pages do.");*/
             return false;
        }
        return input.matches(PATTERN_START5);
    }
    
    
    public static boolean isURLString(final String input){
  /*      String[] schemes = {
            "http", 
            "https", 
            "ftp",
            "file",
            "btspp",
            "btl2cap",
            "btgoep",
            "tcpobex",
            "irdaobex",
            "rtsp",
            "telnet",
            "dav",
            "nfs",
            "smb",
            "sftp",
            "ftps"
        };
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(input)) {//"ftp://foo.bar.com/"
            System.out.println("url is valid");
            return true;
        } else {
            System.out.println("url is invalid");
            return false;
        }*/
        
      //turn input.matches("(http|https|ftp|mailto)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
        //return input.matches("^((https?|ftp)://|(www|ftp)\\.)[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$");
        return input.matches("\\A(https?|ftp|file)://.+\\Z");
        
    }
    

    
    
    //Checks for email addresses starting with
    //inappropriate symbols like dots or @ signs.
    private static final String PATTERN_START1 = "^\\.|^\\@";
    //Checks for email addresses that start with
    //www. and prints a message if it does.
    private static final String PATTERN_START2 = "^www\\.";
    //Checks for email addresses that contains illegal characters
    //such as spaces or commas.
    private static final String PATTERN_START3 = "[^A-Za-z0-9\\.\\@_\\-~#]+";
   
//    private static final String PATTERN_START4 = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    
    private static final String PATTERN_START5 = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+(?:[A-Z]{2}|com|org|net|edu|gov|mil|biz|info|mobi|name|aero|asia|jobs|museum)\\b";
    
  
    
}
