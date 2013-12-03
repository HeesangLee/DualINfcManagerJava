package com.duali.nfc.manager.ui.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public final class DisplayUtils {    
    public static final Display CURRENT_DISPLAY = new Display();

    public static final int DEFAULT_MIN_HEIGHT = 600;
    public static final int DEFAULT_MIN_WIDTH = 800;
    
    public static final Color COLOR_TAGMAN_HEAD_DARK_GRAY = new Color(CURRENT_DISPLAY,  0x4b, 0x4b, 0x4b);    
    public static final Color COLOR_TAGMAN_BG_LIGHT_GRAY = new Color(CURRENT_DISPLAY,  0x67, 0x67, 0x67);    
    public static final Color COLOR_TAGMAN_BG_ULTRA_LIGHT_GRAY = new Color(CURRENT_DISPLAY,  0xc2, 0xc2, 0xc2);
    
    public static final Color COLOR_BG_DARK_BLUE = new Color(CURRENT_DISPLAY,  20, 46, 79);
    public static final Color COLOR_BG_LIGHT_BLUE = new Color(CURRENT_DISPLAY,  187, 202, 230);
    
    public static final Color COLOR_BG_DARK_GREEN = new Color(CURRENT_DISPLAY,  47, 145, 33);
    public static final Color COLOR_BG_LIGHT_GREEN = new Color(CURRENT_DISPLAY,  153, 183, 108);
    
    public static final Color COLOR_LIGHT_RED = new Color(CURRENT_DISPLAY,  0xFF, 0x00, 0x00);    
    public static final Color COLOR_GREEN = new Color(CURRENT_DISPLAY,  0x4C, 0xC4, 0x17);    
    public static final Color COLOR_LIGHT_GREEN = new Color(CURRENT_DISPLAY,  0x8A, 0xFB, 0x17);    
    public static final Color COLOR_LIGHT_GRAY = new Color(CURRENT_DISPLAY,  0xb7, 0xb7, 0xb7);    
    public static final Color COLOR_LEMON_CHIFFON = new Color(CURRENT_DISPLAY,  0xec, 0xe5, 0xb6);     
    public static final Color COLOR_DARK_GRAY = new Color(CURRENT_DISPLAY, 0x61, 0x63, 0x62);    
    public static final Color COLOR_BG_BLUE = new Color(Display.getCurrent(),0x2B, 0x60, 0xDE);
    
    public static final Color COLOR_BLUE = new Color(Display.getCurrent(), 11, 27, 145);
    
    public static void disposeResources() {
        COLOR_TAGMAN_HEAD_DARK_GRAY.dispose();    
        COLOR_TAGMAN_BG_LIGHT_GRAY.dispose();    
        COLOR_TAGMAN_BG_ULTRA_LIGHT_GRAY.dispose();
        
        COLOR_BG_DARK_BLUE.dispose();
        COLOR_BG_LIGHT_BLUE.dispose();
        
        COLOR_BG_DARK_GREEN.dispose();
        COLOR_BG_LIGHT_GREEN.dispose();
        
        COLOR_LIGHT_RED.dispose();    
        COLOR_GREEN.dispose();    
        COLOR_LIGHT_GREEN.dispose();    
        COLOR_LIGHT_GRAY.dispose();    
        COLOR_LEMON_CHIFFON.dispose();     
        COLOR_DARK_GRAY.dispose();    
        COLOR_BG_BLUE.dispose();
    }
}
