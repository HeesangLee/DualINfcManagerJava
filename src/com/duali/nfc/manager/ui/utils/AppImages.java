/*
 * @(#)AppImages.java}
 *
 * Copyright (c) 2011 Aditux
 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Aditux. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into
 * with Aditux.
 */
package com.duali.nfc.manager.ui.utils;

import org.eclipse.swt.graphics.Image;

import com.duali.nfc.manager.ui.shells.PrimaryShell;



/**
 * <p>  Images used by the application. </p>
 * 
 * <dt><b>Date</b>
 * 		  <dd>Oct 5, 2011, 3:16:43 PM
 * <dt><b>Module</b>
 * 		  <dd>tag-manager 
 */
public final class AppImages {
    /* Implementation details of AppImages :    
     */
	
    /*********************************** Login Background **************************************/
   // public static final Image IMAGE_BG = new Image(DisplayUtils.CURRENT_DISPLAY, MainMenuShell.class.getResourceAsStream("/bg/bg.png"));

    public static final Image IMAGE_DUALI_LOGO = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/logo/duali-logo.png"));
    public static final Image IMAGE_TAGMANGER_ICON = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/logo/icon.png"));
    public static final Image IMAGE_TAGMANGER_ICON_LARGE = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/logo/icon-large.png"));    
    public static final Image IMAGE_EMPTY_PHOTO = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/empty_photo.png"));
    
    /********* Left Main option Button Images*************/
   /* public static final Image IMAGE_BTN_BC_NORMAL = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/bc_normal.png"));
    public static final Image IMAGE_BTN_BC_PRESSED = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/bc_selected.png"));
    public static final Image IMAGE_BTN_BMARK_NORMAL = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/bmark_normal.png"));
    public static final Image IMAGE_BTN_BMARK_PRESSED = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/bmark_selected.png"));
    public static final Image IMAGE_BTN_SMS_NORMAL = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/sms_normal.png"));
    public static final Image IMAGE_BTN_SMS_PRESSED = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/sms_selected.png"));
    public static final Image IMAGE_BTN_CALLREQ_NORMAL = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/callreq_normal.png"));
    public static final Image IMAGE_BTN_CALLREQ_PRESSED = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/button/callreq_selected.png"));*/


    /********* Top right window controls button images*************/
   /* public static final Image IMAGE_BTN_CLOSE_NORMAL = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/btn/close-n.png"));

    public static final Image IMAGE_BTN_CLOSE_PRESSED = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/btn/close-p.png"));

    public static final Image IMAGE_BTN_CLOSE_OVER = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/btn/close-o.png"));

    public static final Image IMAGE_BTN_MIN_NORMAL = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/btn/minimize-n.png"));

    public static final Image IMAGE_BTN_MIN_PRESSED = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/btn/minimize-p.png"));

    public static final Image IMAGE_BTN_MIN_OVER = new Image(DisplayUtils.CURRENT_DISPLAY, AppImages.class.getResourceAsStream("/btn/minimize-o.png"));*/

    public static final void disposeResources(){
//        IMAGE_AUDITUX_LOGO.dispose();
    	IMAGE_DUALI_LOGO.dispose();
        IMAGE_TAGMANGER_ICON.dispose();
        IMAGE_TAGMANGER_ICON_LARGE.dispose();
        IMAGE_EMPTY_PHOTO.dispose();
        /*IMAGE_BTN_CLOSE_NORMAL.dispose();
        IMAGE_BTN_CLOSE_PRESSED.dispose();
        IMAGE_BTN_CLOSE_OVER.dispose();
        IMAGE_BTN_MIN_NORMAL.dispose();
        IMAGE_BTN_MIN_PRESSED.dispose();
        IMAGE_BTN_MIN_OVER.dispose();
        IMAGE_BG.dispose();
        IMAGE_BTN_BC_NORMAL.dispose();
        IMAGE_BTN_BC_PRESSED.dispose();
        IMAGE_BTN_BMARK_NORMAL.dispose();
        IMAGE_BTN_BMARK_PRESSED.dispose();
        IMAGE_BTN_SMS_NORMAL.dispose();
        IMAGE_BTN_SMS_PRESSED.dispose();
        IMAGE_BTN_CALLREQ_NORMAL.dispose();
        IMAGE_BTN_CALLREQ_PRESSED.dispose();
        IMAGE_BTN_CLOSE_NORMAL.dispose();
        IMAGE_BTN_CLOSE_PRESSED.dispose();
        IMAGE_BTN_CLOSE_OVER.dispose();
        IMAGE_BTN_MIN_NORMAL.dispose();
        IMAGE_BTN_MIN_PRESSED.dispose();
        IMAGE_BTN_MIN_OVER.dispose();*/
    }
}
