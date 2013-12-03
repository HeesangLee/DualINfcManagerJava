package com.duali.nfc.manager.ui.composites.mime;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.composites.AbstractRecordComposite;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.MimeRecord;

public class ApplicationXChOneTouchComposite extends AbstractRecordComposite {
	public static final String TYPE = "application/x-ch-onetouch";
	private Text txtMac1;
	private Text txtMac2;
	private Text txtMac3;
	private Text txtMac4;
	private Text txtMac5;
	private Text txtMac6;
	
	public static void main(String[] args) {
		char a = 'F';
		
		System.out.println((int) a);
	}
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ApplicationXChOneTouchComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		create();
				
		setDefaultData();
	}

	private void create() {
		Label lblTitle = new Label(this, SWT.NONE);
		lblTitle.setAlignment(SWT.RIGHT);
		lblTitle.setBounds(10, 72, 103, 15);
		lblTitle.setText("MAC Address");
		lblTitle.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblTitle.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtMac1 = new Text(this, SWT.BORDER | SWT.CENTER);
		txtMac1.setBounds(125, 69, 25, 20);
		txtMac1.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMac1.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtMac1.setTextLimit(2);
		txtMac1.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.keyCode == SWT.HOME) {
					txtMac1.forceFocus();
					int charCnt = txtMac1.getCharCount();
					txtMac1.setSelection(0, charCnt);
				}

				if(e.keyCode == SWT.END) {
					txtMac6.forceFocus();
					int charCnt = txtMac6.getCharCount();
					txtMac6.setSelection(0,charCnt);				
				}

				if(e.keyCode == SWT.DEL) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition(); 
					if (charCnt == 0) {
						txtMac2.forceFocus();
						txtMac2.setSelection(0,0);
					} else {				
						if (caretPosition == 2 ) {
							txtMac2.forceFocus();
							txtMac2.setSelection(0,0);
						}
					}
				}
				
				if(e.keyCode == SWT.ARROW_RIGHT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac2.forceFocus();
					else
						if(caretPosition == 2)
							txtMac2.forceFocus();
					return;
				}
				if( (48 <= e.keyCode && e.keyCode <= 57) || (97 <= e.keyCode && e.keyCode <= 102) ||
						(65 <= e.keyCode && e.keyCode <= 70)) {
					int charCnt = ((Text) e.widget).getCharCount();
					if (charCnt == 1)
						txtMac2.forceFocus();
				}					
			}
		});
		txtMac1.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				String string = arg0.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					
//					System.out.println("");					
					
					if (!('0' <= chars[i] && chars[i] <= '9') && !('a' <= chars[i] && chars[i] <= 'f') &&
							!('A' <= chars[i] && chars[i] <= 'F')) {
						arg0.doit = false;
						return;
					}
				}
			}
		});

		Label lblDash1 = new Label(this, SWT.CENTER);
		lblDash1.setText("-");
		lblDash1.setAlignment(SWT.CENTER);
		lblDash1.setBounds(151, 72, 10, 15);
		lblDash1.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblDash1.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtMac2 = new Text(this, SWT.BORDER | SWT.CENTER);
		txtMac2.setBounds(162, 69, 25, 20);
		txtMac2.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMac2.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtMac2.setTextLimit(2);
		txtMac2.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.HOME) {
					txtMac1.forceFocus();
					int charCnt = txtMac1.getCharCount();
					txtMac1.setSelection(0, charCnt);
				}

				if(e.keyCode == SWT.END) {
					txtMac6.forceFocus();
					int charCnt = txtMac6.getCharCount();
					txtMac6.setSelection(0,charCnt);				
				}

				if(e.keyCode == SWT.DEL) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						txtMac3.forceFocus();
						txtMac3.setSelection(0,0);
					} else {
						if (caretPosition == 2 ) {
							txtMac3.forceFocus();
							txtMac3.setSelection(0,0);
						}
					}		
				}
				
				if(e.keyCode == SWT.BS) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						charCnt = txtMac1.getCharCount();
						txtMac1.forceFocus();
						txtMac1.setSelection(charCnt,charCnt);
					} else {
						if (caretPosition == 0 ) {
							charCnt = txtMac1.getCharCount();
							txtMac1.forceFocus();
							txtMac1.setSelection(charCnt,charCnt);
						}
					}							
				}

				if(e.keyCode == SWT.ARROW_LEFT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac1.forceFocus();
					else
						if(caretPosition == 0)
							txtMac1.forceFocus();
					return;
				}
				
				if(e.keyCode == SWT.ARROW_RIGHT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac3.forceFocus();
					else
						if(caretPosition == 2)
							txtMac3.forceFocus();
					return;
				}
				
				if( (48 <= e.keyCode && e.keyCode <= 57) || (97 <= e.keyCode && e.keyCode <= 102) ||
						(65 <= e.keyCode && e.keyCode <= 70)) {
					int charCnt = ((Text) e.widget).getCharCount();
					if (charCnt == 1)
						txtMac3.forceFocus();
				}	
			}
		});
		txtMac2.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				String string = arg0.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9') && !('a' <= chars[i] && chars[i] <= 'f') &&
							!('A' <= chars[i] && chars[i] <= 'F')) {
						arg0.doit = false;
						return;
					}
				}
			}
		});
		
		txtMac3 = new Text(this, SWT.BORDER | SWT.CENTER);
		txtMac3.setBounds(199, 69, 25, 20);
		txtMac3.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMac3.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtMac3.setTextLimit(2);
		txtMac3.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.HOME) {
					txtMac1.forceFocus();
					int charCnt = txtMac1.getCharCount();
					txtMac1.setSelection(0, charCnt);
				}

				if(e.keyCode == SWT.END) {
					txtMac6.forceFocus();
					int charCnt = txtMac6.getCharCount();
					txtMac6.setSelection(0,charCnt);				
				}

				if(e.keyCode == SWT.DEL) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						txtMac4.forceFocus();
						txtMac4.setSelection(0,0);
					} else {
						if (caretPosition == 2 ) {
							txtMac4.forceFocus();
							txtMac4.setSelection(0,0);
						}
					}				
				}
				
				if(e.keyCode == SWT.BS) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						charCnt = txtMac2.getCharCount();
						txtMac2.forceFocus();
						txtMac2.setSelection(charCnt,charCnt);
					} else {
						if (caretPosition == 0 ) {
							charCnt = txtMac2.getCharCount();
							txtMac2.forceFocus();
							txtMac2.setSelection(charCnt,charCnt);
						}
					}							
				}

				if(e.keyCode == SWT.ARROW_LEFT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac2.forceFocus();
					else
						if(caretPosition == 0)
							txtMac2.forceFocus();
					return;
				}
				
				if(e.keyCode == SWT.ARROW_RIGHT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac4.forceFocus();
					else
						if(caretPosition == 2)
							txtMac4.forceFocus();
					return;
				}				
				
				if( (48 <= e.keyCode && e.keyCode <= 57) || (97 <= e.keyCode && e.keyCode <= 102) ||
						(65 <= e.keyCode && e.keyCode <= 70)) {
					int charCnt = ((Text) e.widget).getCharCount();
					if (charCnt == 1)
						txtMac4.forceFocus();
				}	
			}
		});
		txtMac3.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				String string = arg0.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9') && !('a' <= chars[i] && chars[i] <= 'f') &&
							!('A' <= chars[i] && chars[i] <= 'F')) {
						arg0.doit = false;
						return;
					}
				}
			}
		});
		
		txtMac4 = new Text(this, SWT.BORDER | SWT.CENTER);
		txtMac4.setBounds(236, 69, 25, 20);
		txtMac4.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMac4.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtMac4.setTextLimit(2);
		txtMac4.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.HOME) {
					txtMac1.forceFocus();
					int charCnt = txtMac1.getCharCount();
					txtMac1.setSelection(0, charCnt);
				}

				if(e.keyCode == SWT.END) {
					txtMac6.forceFocus();
					int charCnt = txtMac6.getCharCount();
					txtMac6.setSelection(0,charCnt);				
				}

				if(e.keyCode == SWT.DEL) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						txtMac5.forceFocus();
						txtMac5.setSelection(0,0);
					} else {
						if (caretPosition == 2 ) {
							txtMac5.forceFocus();
							txtMac5.setSelection(0,0);
						}
					}			
				}
				
				if(e.keyCode == SWT.BS) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						charCnt = txtMac3.getCharCount();
						txtMac3.forceFocus();
						txtMac3.setSelection(charCnt,charCnt);
					} else {
						if (caretPosition == 0 ) {
							charCnt = txtMac3.getCharCount();
							txtMac3.forceFocus();
							txtMac3.setSelection(charCnt,charCnt);
						}
					}							
				}

				if(e.keyCode == SWT.ARROW_LEFT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac3.forceFocus();
					else
						if(caretPosition == 0)
							txtMac3.forceFocus();
					return;
				}
				
				if(e.keyCode == SWT.ARROW_RIGHT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac5.forceFocus();
					else
						if(caretPosition == 2)
							txtMac5.forceFocus();
					return;
				}	
				if( (48 <= e.keyCode && e.keyCode <= 57) || (97 <= e.keyCode && e.keyCode <= 102) ||
						(65 <= e.keyCode && e.keyCode <= 70)) {
					int charCnt = ((Text) e.widget).getCharCount();
					if (charCnt == 1)
						txtMac5.forceFocus();
				}	
			}
		});
		txtMac4.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				String string = arg0.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9') && !('a' <= chars[i] && chars[i] <= 'f') &&
							!('A' <= chars[i] && chars[i] <= 'F')) {
						arg0.doit = false;
						return;
					}
				}
			}
		});
		
		txtMac5 = new Text(this, SWT.BORDER | SWT.CENTER);
		txtMac5.setBounds(273, 69, 25, 20);
		txtMac5.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMac5.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtMac5.setTextLimit(2);
		txtMac5.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.HOME) {
					txtMac1.forceFocus();
					int charCnt = txtMac1.getCharCount();
					txtMac1.setSelection(0, charCnt);
				}

				if(e.keyCode == SWT.END) {
					txtMac6.forceFocus();
					int charCnt = txtMac6.getCharCount();
					txtMac6.setSelection(0,charCnt);				
				}

				if(e.keyCode == SWT.DEL) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						txtMac6.forceFocus();
						txtMac6.setSelection(0,0);
					} else {
						if (caretPosition == 2 ) {
							txtMac6.forceFocus();
							txtMac6.setSelection(0,0);
						}
					}			
				}
				
				if(e.keyCode == SWT.BS) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						charCnt = txtMac4.getCharCount();
						txtMac4.forceFocus();
						txtMac4.setSelection(charCnt,charCnt);
					} else {
						if (caretPosition == 0 ) {
							charCnt = txtMac4.getCharCount();
							txtMac4.forceFocus();
							txtMac4.setSelection(charCnt,charCnt);
						}
					}							
				}

				if(e.keyCode == SWT.ARROW_LEFT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac4.forceFocus();
					else
						if(caretPosition == 0)
							txtMac4.forceFocus();
					return;
				}
				
				if(e.keyCode == SWT.ARROW_RIGHT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac6.forceFocus();
					else
						if(caretPosition == 2)
							txtMac6.forceFocus();
					return;
				}	
				if( (48 <= e.keyCode && e.keyCode <= 57) || (97 <= e.keyCode && e.keyCode <= 102) ||
						(65 <= e.keyCode && e.keyCode <= 70)) {
					int charCnt = ((Text) e.widget).getCharCount();
					if (charCnt == 1)
						txtMac6.forceFocus();
				}	
			}
		});
		txtMac5.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				String string = arg0.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9') && !('a' <= chars[i] && chars[i] <= 'f') &&
							!('A' <= chars[i] && chars[i] <= 'F')) {
						arg0.doit = false;
						return;
					}
				}
			}
		});
		
		txtMac6 = new Text(this, SWT.BORDER | SWT.CENTER);
		txtMac6.setBounds(310, 69, 25, 20);
		txtMac6.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMac6.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtMac6.setTextLimit(2);
		txtMac6.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.HOME) {
					txtMac1.forceFocus();
					int charCnt = txtMac1.getCharCount();
					txtMac1.setSelection(0, charCnt);
				}

				if(e.keyCode == SWT.END) {
					txtMac6.forceFocus();
					int charCnt = txtMac6.getCharCount();
					txtMac6.setSelection(0,charCnt);				
				}

				if(e.keyCode == SWT.BS) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if (charCnt == 0) {
						charCnt = txtMac5.getCharCount();
						txtMac5.forceFocus();
						txtMac5.setSelection(charCnt,charCnt);
					} else {
						if (caretPosition == 0 ) {
							charCnt = txtMac5.getCharCount();
							txtMac5.forceFocus();
							txtMac5.setSelection(charCnt,charCnt);
						}
					}							
				}
				
				if(e.keyCode == SWT.ARROW_LEFT) {
					int charCnt = ((Text) e.widget).getCharCount();
					int caretPosition = ((Text) e.widget).getCaretPosition();
					if(charCnt == 0)
						txtMac5.forceFocus();
					else
						if(caretPosition == 0)
							txtMac5.forceFocus();
					return;
				}
			}
		});
		txtMac6.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
				String string = arg0.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9') && !('a' <= chars[i] && chars[i] <= 'f') &&
							!('A' <= chars[i] && chars[i] <= 'F')) {
						arg0.doit = false;
						return;
					}
				}
			}
		});
		
		Label lblDash2 = new Label(this, SWT.CENTER);
		lblDash2.setAlignment(SWT.CENTER);
		lblDash2.setBounds(188, 72, 10, 15);
		lblDash2.setText("-");
		
		Label lblDash3 = new Label(this, SWT.CENTER);
		lblDash3.setBounds(225, 72, 10, 15);
		lblDash3.setText("-");
		
		Label lblDash4 = new Label(this, SWT.CENTER);
		lblDash4.setBounds(262, 72, 10, 15);
		lblDash4.setText("-");
		
		Label lblDash5 = new Label(this, SWT.CENTER);
		lblDash5.setBounds(299, 72, 10, 15);
		lblDash5.setText("-");
	
	}	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void clear() {
		txtMac1.setText("");
		txtMac2.setText("");
		txtMac3.setText("");
		txtMac4.setText("");
		txtMac5.setText("");		
		txtMac6.setText("");
	}

	public void setDefaultData() {
		txtMac1.setText("00");
		txtMac2.setText("00");
		txtMac3.setText("00");
		txtMac4.setText("00");
		txtMac5.setText("00");
		txtMac6.setText("00");
	}

	@Override
	public void addRecord(TableViewer tbvRecord) {
		// validate
		// list¿¡ Ãß°¡
		if(txtMac1.getText() == null 
				|| txtMac1.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		
		if(txtMac2.getText() == null 
				|| txtMac2.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		if(txtMac3.getText() == null 
				|| txtMac3.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		if(txtMac4.getText() == null 
				|| txtMac4.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		if(txtMac5.getText() == null 
				|| txtMac5.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		if(txtMac6.getText() == null 
				|| txtMac6.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		
		MimeRecord mimeRecord = new MimeRecord();
		mimeRecord.setType("application/x-ch-onetouch");
		mimeRecord.setContent(getContent());
		
		addRecord(tbvRecord, mimeRecord);
	}
	
	private byte[] getContent() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(txtMac1.getText());
		sb.append("-");
		sb.append(txtMac2.getText());
		sb.append("-");
		sb.append(txtMac3.getText());
		sb.append("-");
		sb.append(txtMac4.getText());
		sb.append("-");
		sb.append(txtMac5.getText());
		sb.append("-");
		sb.append(txtMac6.getText());
		return sb.toString().getBytes();
	}
}
