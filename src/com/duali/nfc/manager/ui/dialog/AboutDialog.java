/*
 * @(#)AboutDialog.java}
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
package com.duali.nfc.manager.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.duali.nfc.manager.ui.custom.LinkButton;
import com.duali.nfc.manager.ui.utils.AppImages;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;


/**
 * <p> About dialog</p>
 * 
 * <dt><b>Date</b>
 * 		  <dd>Sep 20, 2011, 8:39:16 PM
 * <dt><b>Module</b>
 * 		  <dd>ndef-creator 
 * <dt>TODO
 * 		  <dd>
 */
public class AboutDialog extends Dialog{
    /* Implementation details of AboutDialog :    
     */



    private Shell subShell = null;

    private Label aboutTagManagerLabel = null;
    
    private Label aboutTagManagerLabel2 = null;

    private Label aboutTagManagerVisitLabel = null;
    
    private Label aboutTagManagerContactLabel = null;
    
    private LinkButton visitLinkBtn = null;    
    private LinkButton contactLinkBtn = null;
    
    //private Link aboutTagManagerLinkLabel = null;

    private Canvas logoCanvas = null;

    /**
     * Constructor for CardCreationDialog.
     * @param parent
     */
    public AboutDialog(Shell parent) {
        super(parent);
    }

    /**
     * Makes the dialog visible.
     * @return
     */
    public String open() { 
        subShell =
            new Shell(getParent(), SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE); // , SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL
        subShell.setText("About NFC Manager");
        final FormLayout layout = new FormLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        subShell.setLayout(layout);
        subShell.addListener(SWT.Resize, aboutDialogEventListener);
        subShell.addListener(SWT.Close, aboutDialogEventListener);

        createLogoCanvas();
        createAboutContentLabel();
        createAboutTagManagerVisitLabel();
        createVisitLinkBtn();
        createAboutTagManagerContactLabel();
        createContactLinkBtn();
        createAboutContentLabel2();
      //  createAboutContentLink();

        subShell.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ));
        subShell.setBackgroundMode(SWT.INHERIT_DEFAULT);

        subShell.addListener(SWT.Traverse, new Listener() {
            public void handleEvent(Event event) {
                if(event.detail == SWT.TRAVERSE_ESCAPE)
                    event.doit = false;
                dispose();
            }
        });

        // subShell.pack();
        subShell.setSize(480, 240);

        Rectangle bounds = getParent().getBounds ();
        Rectangle rect = subShell.getBounds ();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        subShell.setLocation (x, y);  
        subShell.open();

        Display display = getParent().getDisplay();
        while (!subShell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return null;
    }

    /** 
     * <p>This is the method for .</p>
     */
     private void createAboutContentLabel() {
        aboutTagManagerLabel = new Label(subShell, SWT.WRAP);
        aboutTagManagerLabel.setFont(FontUtils.FONT_TAHOMA_NORMAL);
        aboutTagManagerLabel.setForeground(DisplayUtils.COLOR_DARK_GRAY );
        aboutTagManagerLabel.setText(AppLocale.getText("ABOUT.TXT1") + 
        		AppLocale.getText("ABOUT.TXT.VERSION") + AppLocale.getText("VERSION"));

        final FormData ndefDataLayout = new FormData();
        ndefDataLayout.left = new FormAttachment(40, 100, 0); //new FormAttachment(categoryCCombo, 20);
        ndefDataLayout.top = new FormAttachment(5);
        ndefDataLayout.right = new FormAttachment(95, 100, 0);
        aboutTagManagerLabel.setLayoutData(ndefDataLayout);
     }
     
     private void createAboutTagManagerVisitLabel() {
         aboutTagManagerVisitLabel = new Label(subShell, SWT.WRAP);
         aboutTagManagerVisitLabel.setFont(FontUtils.FONT_TAHOMA_NORMAL);
         aboutTagManagerVisitLabel.setForeground(DisplayUtils.COLOR_DARK_GRAY );
         aboutTagManagerVisitLabel.setText(AppLocale.getText("ABOUT.TXT.VISIT"));

         final FormData ndefDataLayout = new FormData();
         ndefDataLayout.left = new FormAttachment(40, 100, 0); //new FormAttachment(categoryCCombo, 20);
         ndefDataLayout.top = new FormAttachment(aboutTagManagerLabel, 3);
         //ndefDataLayout.right = new FormAttachment(95, 100, 0);
         aboutTagManagerVisitLabel.setLayoutData(ndefDataLayout);
     }
     
     private void createVisitLinkBtn() {
         visitLinkBtn = new LinkButton(subShell, SWT.NONE);
         visitLinkBtn.setText(AppLocale.getText("ABOUT.TXT.VISIT.URL"));
         visitLinkBtn.setFont(FontUtils.FONT_HELVETICAS_NORMAL_SIZE8);
         visitLinkBtn.setColorNormal(DisplayUtils.COLOR_DARK_GRAY);
         visitLinkBtn.setColorPressed(DisplayUtils.COLOR_GREEN);
         visitLinkBtn.setColorOver(DisplayUtils.COLOR_BG_BLUE);
         //visitLinkBtn.setColorBackGround(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
         visitLinkBtn.addListener(SWT.Selection, new Listener() {
             public void handleEvent(Event e) {
                 startLinkAction("http://" + visitLinkBtn.getText());
             }
         });
         
         final FormData ndefDataLayout = new FormData();
         ndefDataLayout.left = new FormAttachment(aboutTagManagerVisitLabel, 0); //new FormAttachment(categoryCCombo, 20);
         ndefDataLayout.top = new FormAttachment(aboutTagManagerLabel, 2);
         ndefDataLayout.height = 14;
         ndefDataLayout.width = 100;
         //ndefDataLayout.right = new FormAttachment(95, 100, 0);
         visitLinkBtn.setLayoutData(ndefDataLayout);
     }
     
     private void createAboutTagManagerContactLabel() {
         aboutTagManagerContactLabel = new Label(subShell, SWT.WRAP);
         aboutTagManagerContactLabel.setFont(FontUtils.FONT_TAHOMA_NORMAL);
         aboutTagManagerContactLabel.setForeground(DisplayUtils.COLOR_DARK_GRAY );
         aboutTagManagerContactLabel.setText(AppLocale.getText("ABOUT.TXT.CONTACT"));

         final FormData ndefDataLayout = new FormData();
         ndefDataLayout.left = new FormAttachment(40, 100, 0); //new FormAttachment(categoryCCombo, 20);
         ndefDataLayout.top = new FormAttachment(aboutTagManagerVisitLabel, 3);
        // ndefDataLayout.right = new FormAttachment(95, 100, 0);
         aboutTagManagerContactLabel.setLayoutData(ndefDataLayout);
     }
     
     private void createContactLinkBtn() {
         contactLinkBtn = new LinkButton(subShell, SWT.NONE);
         contactLinkBtn.setText(AppLocale.getText("ABOUT.TXT.CONTACT.EMAIL"));
         contactLinkBtn.setFont(FontUtils.FONT_HELVETICAS_NORMAL_SIZE8);
         contactLinkBtn.setColorNormal(DisplayUtils.COLOR_DARK_GRAY);
         contactLinkBtn.setColorPressed(DisplayUtils.COLOR_GREEN);
         contactLinkBtn.setColorOver(DisplayUtils.COLOR_BG_BLUE);
         //contactLinkBtn.setColorBackGround(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
         contactLinkBtn.addListener(SWT.Selection, new Listener() {
             public void handleEvent(Event e) {
                 startLinkAction("mailto://" + contactLinkBtn.getText());
             }
         });
         
         final FormData ndefDataLayout = new FormData();
         ndefDataLayout.left = new FormAttachment(aboutTagManagerContactLabel, 0); //new FormAttachment(categoryCCombo, 20);
         ndefDataLayout.top = new FormAttachment(aboutTagManagerVisitLabel, 2);
         ndefDataLayout.height = 14;
         ndefDataLayout.width = 100;
         contactLinkBtn.setLayoutData(ndefDataLayout);
     }
     
     private void createAboutContentLabel2() {
         aboutTagManagerLabel2 = new Label(subShell, SWT.WRAP);
         aboutTagManagerLabel2.setFont(FontUtils.FONT_TAHOMA_NORMAL);
         aboutTagManagerLabel2.setForeground(DisplayUtils.COLOR_DARK_GRAY );
         aboutTagManagerLabel2.setText(AppLocale.getText("ABOUT.TXT2"));

         final FormData ndefDataLayout = new FormData();
         ndefDataLayout.left = new FormAttachment(40, 100, 0); //new FormAttachment(categoryCCombo, 20);
         ndefDataLayout.top = new FormAttachment(aboutTagManagerContactLabel, 2);
         ndefDataLayout.right = new FormAttachment(95, 100, 0);
         aboutTagManagerLabel2.setLayoutData(ndefDataLayout);
     }
      

    private Listener aboutDialogEventListener = new Listener() {
        public void handleEvent(Event e)  {
            if (e.type == SWT.Resize) {
                drawGradientBackgorundImage();
            }
            if (e.type == SWT.Close) {
                if (imageGradient != null) {
                    imageGradient.dispose();
                }
            }
        }
    };


    /** 
     * <p>This is the method for .</p>
     */
    private void createLogoCanvas() {
        // TODO Auto-generated method stub
        logoCanvas = new Canvas(subShell,SWT.NO_REDRAW_RESIZE);
        logoCanvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.drawImage(AppImages.IMAGE_TAGMANGER_ICON_LARGE,0,0);
            }
        });

        final FormData layoutData = new FormData();
        layoutData.left  = new FormAttachment(12, 100, 0);
        layoutData.top = new FormAttachment(38, 100, 0);
        layoutData.height = AppImages.IMAGE_TAGMANGER_ICON_LARGE.getImageData().height;
        layoutData.width = AppImages.IMAGE_TAGMANGER_ICON_LARGE.getImageData().width;
        logoCanvas.setLayoutData(layoutData);

    }


    private Image imageGradient = null;

    private void drawGradientBackgorundImage() {
        Image oldImage = imageGradient;
        Rectangle rect = subShell.getClientArea();
        imageGradient = new Image(DisplayUtils.CURRENT_DISPLAY, rect.width, rect.height);
        GC gc = new GC(imageGradient);
        try {
            gc.setForeground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
            gc.setBackground(DisplayUtils.COLOR_LIGHT_GRAY);
            gc.fillGradientRectangle(rect.x, rect.y, rect.width,
                rect.height, true);
        } catch (Exception e) {
        } finally {
            gc.dispose();
        }
        subShell.setBackgroundImage(imageGradient);
        if (oldImage != null) {
            oldImage.dispose();
        }
    }

    public void dispose() {
        if (imageGradient != null) {
            imageGradient.dispose();
        }
        subShell.dispose();
    }

    private void startLinkAction(String linkContent){
        //String url = "http://www.google.com";
      
       /* try {
            //URI mailURI = new URI("mailto://user@example.com");
            URI mailURI = new URI("http://www.google.com");
            Desktop.getDesktop().browse(mailURI);
        } catch (URISyntaxException e1) {
            // TODO Auto-generated catch block
           System.out.println(e1.getMessage()); //Logging error.
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            System.out.println(e1.getMessage()); //Logging error.
        }*/
        System.out.println(linkContent);
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.indexOf("win") >= 0) {

                // this doesn't support showing urls in the form of
                // "page.html#nameLink"
                rt.exec("rundll32 url.dll,FileProtocolHandler " + linkContent);

            } else if (os.indexOf("mac") >= 0) {

                rt.exec("open " + linkContent);

            } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

                // Do a best guess on unix until we get a platform independent
                // way
                // Build a list of browsers to try, in this order.
                String[] browsers = { "epiphany", "firefox", "mozilla",
                    "konqueror", "netscape", "opera", "links", "lynx" };

                // Build a command string which looks like
                // "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \""
                        + linkContent + "\" ");

                rt.exec(new String[] { "sh", "-c", cmd.toString() });

            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
    }
}
