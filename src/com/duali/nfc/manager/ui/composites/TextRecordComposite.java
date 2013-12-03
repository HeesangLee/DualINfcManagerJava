package com.duali.nfc.manager.ui.composites;

import java.nio.charset.Charset;
import java.util.Locale;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.TextRecord;

public class TextRecordComposite extends AbstractRecordComposite {
	private Text txtText;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TextRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		createText();
		
		setDefaultData();
	}

	private void createText() {
		Label lblText = new Label(this, SWT.NONE);
		lblText.setAlignment(SWT.RIGHT);
		lblText.setBounds(10, 78, 75, 15);
		lblText.setText("Text");
		lblText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtText = new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		txtText.setBounds(100, 75, 250, 100);
		txtText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtText.addTraverseListener(new TraverseListener() { // adding tab for focus traversal
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void addRecord(TableViewer tbvRecord) { 
		if(txtText.getText() == null 
				|| txtText.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TEXT); //All fields are mandatory. Please enter valid title.
			return;
		}
		
		// list¿¡ Ãß°¡				
		TextRecord textRecord = new TextRecord(txtText.getText(), Charset.forName("UTF-8"), new Locale("en"));
		addRecord(tbvRecord, textRecord);
	}

	@Override
	public void clear() {
		txtText.setText("");		
	}

	@Override
	public void setDefaultData() {
		txtText.setText("NFC Manager is a fast and easy to use PC application for issuing NFC compliant tags efficiently.");		
	}	
}
