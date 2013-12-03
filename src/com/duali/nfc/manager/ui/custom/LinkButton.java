package com.duali.nfc.manager.ui.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.duali.nfc.manager.ui.utils.DisplayUtils;

/**
 * <p>Custom Link Button implementatino.</p>
 * 
 * <dt><b>Date</b>
 * 		  <dd>Sep 30, 2011, 1:03:07 PM
 * <dt><b>Module</b>
 * 		  <dd>tag-manager 
 */
public class LinkButton extends Canvas {

    private int mouse = 0;

    private boolean hit = false;

    private Color colorBackGround = null;

    
    
    /**
     * <p>This method can be used for setting the colorBackGround.</p>
     * @param colorBackGround The colorBackGround to set.
     */
    public void setColorBackGround(Color colorBackGround) {
        this.colorBackGround = colorBackGround; // Assigning to this.colorBackGround.
    }


    /**
     * <p>This method can be used for getting the colorBackGround.</p>
     * @return The colorBackGround.
     */
    public Color getColorBackGround() {
        return colorBackGround; // returning colorBackGround.
    }


    private Color colorPressed = null;
    
    private Color colorNormal = null;
    
    private Color colorOver = null;

    private boolean selected = false;

    private String text = null;
    
    /**
     * <p>This method can be used for getting the text.</p>
     * @return The text.
     */
    public String getText() {
        return text; // returning text.
    }

    
    /**
     * <p>This method can be used for setting the text.</p>
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text; // Assigning to this.text.
    }

    /**
     * <p>This method can be used for getting the colorOver.</p>
     * @return The colorOver.
     */
    public Color getColorOver() {
        return colorOver; // returning colorOver.
    }


    
    /**
     * <p>This method can be used for setting the colorOver.</p>
     * @param colorOver The colorOver to set.
     */
    public void setColorOver(Color colorOver) {
        this.colorOver = colorOver; // Assigning to this.colorOver.
    }


    /**
     * <p>This method can be used for getting the colorPressed.</p>
     * @return The colorPressed.
     */
    public Color getColorPressed() {
        return colorPressed; // returning colorPressed.
    }


    /**
     * <p>This method can be used for setting the colorPressed.</p>
     * @param colorPressed The colorPressed to set.
     */
    public void setColorPressed(Color colorPressed) {
        this.colorPressed = colorPressed; // Assigning to this.colorPressed.
    }


    /**
     * <p>This method can be used for getting the colorNormal.</p>
     * @return The colorNormal.
     */
    public Color getColorNormal() {
        return colorNormal; // returning colorNormal.
    }


    /**
     * <p>This method can be used for setting the colorNormal.</p>
     * @param colorNormal The colorNormal to set.
     */
    public void setColorNormal(Color colorNormal) {
        this.colorNormal = colorNormal; // Assigning to this.colorNormal.
    }


    private static final Font fontDefault = new Font(DisplayUtils.CURRENT_DISPLAY, "Arial", 10, SWT.BOLD); // SWT.BOLD | SWT.ITALIC


    
    /**
     * <p>This method can be used for getting the fontDefault.</p>
     * @return The fontdefault.
     */
    private static Font getFontdefault() {
        return fontDefault; // returning fontDefault.
    }


    public boolean isSelected() {
        return selected;
    }



    public void setSelected(boolean selected) {
        this.selected = selected;
        redraw();
    }


    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.selected = false;
    }

    public LinkButton(Composite parent, int style) {
        super(parent, style);
        initialize();
        this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if(getColorBackGround() != null) {
                    e.gc.setBackground(getColorBackGround());
                    e.gc.fillRectangle(new Rectangle(0, 0, getBounds().width, getBounds().height));
                }
                switch (mouse) {
                    case 0:
                        // Default state
                        if(getColorNormal() != null && !isSelected()) {
                            e.gc.setForeground(getColorNormal());
                        }
                        break;
                    case 1:
                     // Default state
                        if(getColorOver() != null && !isSelected()) {
                            e.gc.setForeground(getColorOver());
                        }
                        break;
                    case 2:
                        // Mouse down
                        if(getColorPressed() != null && !isSelected()){
                            e.gc.setForeground(getColorPressed());
                        }
                        break;
                }

                if(isSelected()){
                    if(getColorPressed() != null && !isSelected()){
                        e.gc.setForeground(getColorPressed());
                    }
                }
                e.gc.setFont(getFont());
                FontMetrics fontMetrics = e.gc.getFontMetrics();
                int stringHeight = fontMetrics.getHeight();
                char[] chars =getText().toCharArray();
                int stringWidth = 0;
                for (int i = 0; i < chars.length; i++) {
                    stringWidth += e.gc.getCharWidth(chars[i]);
                }
                
                stringWidth +=  chars.length;
                
                int x = ( getBounds().width - stringWidth)/2;
                int y = (getBounds().height - stringHeight)/2;
                
                e.gc.drawString(getText(),x, y, true);
                e.gc.setLineWidth(2);
                e.gc.drawLine(x, y + stringHeight , x + stringWidth, y + stringHeight);
            }
        });

        this.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
                if (!hit)
                    return;
                mouse = 2;
                if (e.x < 0 || e.y < 0 || e.x > getBounds().width
                        || e.y > getBounds().height) {
                    mouse = 0;
                }
                redraw();
            }
        }
        );
        this.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseEnter(MouseEvent e) {
                mouse = 1;
                redraw();
            }

            public void mouseExit(MouseEvent e) {
                mouse = 0;
                redraw();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                hit = true;
                mouse = 2;
                redraw();
            }

            public void mouseUp(MouseEvent e) {
                hit = false;
                mouse = 1;
                if (e.x < 0 || e.y < 0 || e.x > getBounds().width
                        || e.y > getBounds().height) {
                    mouse = 0;
                }
                redraw();
                if (mouse == 1)
                    notifyListeners(SWT.Selection, new Event());
            }
        });

        this.addListener(SWT.Traverse, new Listener () {
            public void handleEvent (Event e) {
                switch (e.detail) {
                    /* Do tab group traversal */
                    case SWT.TRAVERSE_ESCAPE:
                    case SWT.TRAVERSE_RETURN:
                    case SWT.TRAVERSE_TAB_NEXT: 
                    case SWT.TRAVERSE_TAB_PREVIOUS:
                    case SWT.TRAVERSE_PAGE_NEXT:    
                    case SWT.TRAVERSE_PAGE_PREVIOUS:
                        e.doit = true;
                        break;
                }
            }
        });

        
      /*  this.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent arg0) {
                mouse = 0;
                redraw();
            }

            public void focusGained(FocusEvent arg0) {
                mouse = 1;
                redraw();
            }
        });*/

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == '\r' || e.character == ' ') {
                    hit = true;
                    mouse = 2;
                    redraw();
                }
            }
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == '\r' || e.character == ' ') {
                    hit = false;
                    mouse = 1;
                    redraw();
                    notifyListeners(SWT.Selection, new Event());
                }
            }
        });
        this.setSize(28, 21);
    }
    
    public LinkButton(Shell parent, int style) {
        super(parent, style);
        initialize();
        this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if(getColorBackGround() != null) {
                    e.gc.setBackground(getColorBackGround());
                    e.gc.fillRectangle(new Rectangle(0, 0, getBounds().width, getBounds().height));
                }
                switch (mouse) {
                    case 0:
                        // Default state
                        if(getColorNormal() != null && !isSelected()) {
                            e.gc.setForeground(getColorNormal());
                        }
                        break;
                    case 1:
                     // Default state
                        if(getColorOver() != null && !isSelected()) {
                            e.gc.setForeground(getColorOver());
                        }
                        break;
                    case 2:
                        // Mouse down
                        if(getColorPressed() != null && !isSelected()){
                            e.gc.setForeground(getColorPressed());
                        }
                        break;
                }

                if(isSelected()){
                    if(getColorPressed() != null && !isSelected()){
                        e.gc.setForeground(getColorPressed());
                    }
                }
                e.gc.setFont(getFont());
                FontMetrics fontMetrics = e.gc.getFontMetrics();
                int stringHeight = fontMetrics.getHeight();
                char[] chars =getText().toCharArray();
                int stringWidth = 0;
                for (int i = 0; i < chars.length; i++) {
                    stringWidth += e.gc.getCharWidth(chars[i]);
                }
                
                stringWidth +=  (stringWidth/4);
                
                int x = ( getBounds().width - stringWidth)/2;
                int y = (getBounds().height - stringHeight)/2;
                
                e.gc.drawString(getText(),x, y, true);
                e.gc.setLineWidth(1);
                e.gc.drawLine(x, y + stringHeight , x + stringWidth, y + stringHeight);
            }
        });

        this.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
                if (!hit)
                    return;
                mouse = 2;
                if (e.x < 0 || e.y < 0 || e.x > getBounds().width
                        || e.y > getBounds().height) {
                    mouse = 0;
                }
                redraw();
            }
        }
        );
        this.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseEnter(MouseEvent e) {
                mouse = 1;
                redraw();
            }

            public void mouseExit(MouseEvent e) {
                mouse = 0;
                redraw();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                hit = true;
                mouse = 2;
                redraw();
            }

            public void mouseUp(MouseEvent e) {
                hit = false;
                mouse = 1;
                if (e.x < 0 || e.y < 0 || e.x > getBounds().width
                        || e.y > getBounds().height) {
                    mouse = 0;
                }
                redraw();
                if (mouse == 1)
                    notifyListeners(SWT.Selection, new Event());
            }
        });

        this.addListener(SWT.Traverse, new Listener () {
            public void handleEvent (Event e) {
                switch (e.detail) {
                    /* Do tab group traversal */
                    case SWT.TRAVERSE_ESCAPE:
                    case SWT.TRAVERSE_RETURN:
                    case SWT.TRAVERSE_TAB_NEXT: 
                    case SWT.TRAVERSE_TAB_PREVIOUS:
                    case SWT.TRAVERSE_PAGE_NEXT:    
                    case SWT.TRAVERSE_PAGE_PREVIOUS:
                        e.doit = true;
                        break;
                }
            }
        });

        
      /*  this.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent arg0) {
                mouse = 0;
                redraw();
            }

            public void focusGained(FocusEvent arg0) {
                mouse = 1;
                redraw();
            }
        });*/

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == '\r' || e.character == ' ') {
                    hit = true;
                    mouse = 2;
                    redraw();
                }
            }
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == '\r' || e.character == ' ') {
                    hit = false;
                    mouse = 1;
                    redraw();
                    notifyListeners(SWT.Selection, new Event());
                }
            }
        });
        this.setSize(28, 21);
    }



}  

