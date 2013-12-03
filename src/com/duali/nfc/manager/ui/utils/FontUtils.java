package com.duali.nfc.manager.ui.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Shell;

import com.duali.nfc.manager.ui.shells.PrimaryShell;


/**
 * <p> UI text Font utility classes. </p>
 * 
 * <dt><b>Date</b>
 * 		  <dd>Apr 29, 2011, 3:48:22 PM
 * <dt><b>Module</b>
 * 		  <dd>ftss-commodity-tagger-new 
 */
public class FontUtils {
    /* Implementation details of FontUtils :    
     */
    
    private static boolean isFontLoaded = false;
    
    
    
//    public static final Font FONT_ARIAL_BOLD_ITALIC = new Font(CURRENT_DISPLAY, "Arial", 12, SWT.NORMAL); // SWT.BOLD | SWT.ITALIC
    
    //public static final Font FONT_ARIAL_BOLD_ITALIC = new Font(DisplayUtils.CURRENT_DISPLAY, "Times New Roman", 12, SWT.NORMAL); // SWT.BOLD | SWT.ITALIC

    //public static final Font FONT_COURIER_NEW_NORMAL = new Font(DisplayUtils.CURRENT_DISPLAY, "Courier New", 10, SWT.NORMAL); // SWT.BOLD | SWT.ITALIC
    
    public static final Font FONT_TAHOMA_BOLD = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 14, SWT.NORMAL | SWT.BOLD);
    
    public static final Font FONT_TAHOMA_BOLD_SMALL = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 10, SWT.NORMAL | SWT.BOLD);
    
    public static final Font FONT_TAHOMA_BOLD_ITALICS_SMALL = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 8, SWT.NORMAL | SWT.BOLD);
    
    public static final Font FONT_TAHOMA_NORMAL = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 8, SWT.NORMAL);
    
    public static final Font FONT_TAHOMA_BOLD_HEADER = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 10, SWT.NORMAL | SWT.BOLD);
    
    public static final Font FONT_HELVETICAS_BOLD_SIZE8 = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 8, SWT.NORMAL | SWT.BOLD);
    
    //public static final Font FONT_HELVETICAS_BOLD_SIZE7 = loadFont(PrimaryShell.getInstance().getsShell(), "helr45w.ttf", 7, SWT.NORMAL | SWT.BOLD);
    
    public static final Font FONT_HELVETICAS_NORMAL_SIZE10 = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 10, SWT.NORMAL);
    
    public static final Font FONT_HELVETICAS_NORMAL_SIZE8 = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 8, SWT.NORMAL);
    
    public static final Font FONT_HELVETICAS_NORMAL_SIZE18 = loadFont(PrimaryShell.getInstance().getShell(), "ARIAL.TTF", 18, SWT.NORMAL);
    
    private static Font loadFont(Shell shell, String fontFileName, int fontSize, int style) {
        if(!isFontLoaded) {
            isFontLoaded = shell.getDisplay().loadFont(fontFileName); 
        }
        if( isFontLoaded ) {
            //System.out.println("font loaded");
            // font name found by inspecting the font list..
            return new Font(shell.getDisplay(), "tahoma", fontSize, style);
        }else{
           // System.out.println("Font load failed");
        }
        return new Font(shell.getDisplay(), "Arial", fontSize, SWT.NORMAL);
    }

    
    public static void disposeResources(){
        FONT_TAHOMA_BOLD.dispose();
        FONT_TAHOMA_NORMAL.dispose();
        FONT_TAHOMA_BOLD_HEADER.dispose();
        FONT_TAHOMA_BOLD_SMALL.dispose();
        FONT_TAHOMA_BOLD_ITALICS_SMALL.dispose();
    }
    
   /* private static void displayAllLoadedFonts(Shell shell) {
        // display all scalable fonts in the system
        FontData[] fd = shell.getDisplay().getFontList(null, true);
        for( int i = 0; i < fd.length; i++ ) {
            System.out.println(fd[i].getName());
        }

        System.out.println("...................NON SCALABLE....................");
        // and the non-scalable ones
        fd = shell.getDisplay().getFontList(null, false);
        for( int i = 0; i < fd.length; i++ ) {
            System.out.println(fd[i].getName());
        }
    }*/
}
