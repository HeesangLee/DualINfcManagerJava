package com.duali.nfc.manager.ui.composites;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.AndroidApplicationRecord;
import org.eclipse.swt.custom.StyledText;

public class AndroidApplicationRecordComposite extends AbstractRecordComposite {
	private Text txtPackageName;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AndroidApplicationRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		create();
		
		setDefaultData();
	}

	private void create() {
		Label lblText = new Label(this, SWT.NONE);
		lblText.setAlignment(SWT.RIGHT);
		lblText.setBounds(10, 41, 117, 15);
		lblText.setText("Package Name");
		lblText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtPackageName = new Text(this, SWT.BORDER);
		txtPackageName.setBounds(142, 38, 210, 21);
		txtPackageName.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtPackageName.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		StyledText styledText = new StyledText(this, SWT.READ_ONLY | SWT.WRAP);
		styledText.setText("* Introduced in Android 4.0 (API level 14), an Android Application Record (AAR) provides a stronger certainty that your application is started when an NFC tag is scanned. An AAR has the package name of an application embedded inside an NDEF record.");
		styledText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		styledText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		styledText.setBounds(27, 116, 383, 70);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void addRecord(TableViewer tbvRecord) { 
		if(txtPackageName.getText() == null 
				|| txtPackageName.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TEXT); //All fields are mandatory. Please enter valid title.
			return;
		}
		
		// list¿¡ Ãß°¡		
		AndroidApplicationRecord textRecord = new AndroidApplicationRecord(txtPackageName.getText());
		addRecord(tbvRecord, textRecord);
	}

	@Override
	public void clear() {
		txtPackageName.setText("");		
	}

	@Override
	public void setDefaultData() {
		txtPackageName.setText("com.duali.nfc");
	}	
}
