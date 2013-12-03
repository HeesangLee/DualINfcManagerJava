package com.duali.nfc.manager.ui.dialog.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.TextRecord;

public class TextRecordComposite extends RecordComposite {
	private Text txtText;
	private Text txtLocale;
	private Text txtEncoding;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TextRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Group grpTextRecord = new Group(this, SWT.NONE);
		grpTextRecord.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTextRecord.setText("Text Record");
		grpTextRecord.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		grpTextRecord.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(grpTextRecord, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel.setText("Text");
		
		txtText = new Text(grpTextRecord, SWT.BORDER);
		txtText.setEditable(false);
		txtText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtText.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(grpTextRecord, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_1.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_1.setText("Locale");
		
		txtLocale = new Text(grpTextRecord, SWT.BORDER);
		txtLocale.setEditable(false);
		txtLocale.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtLocale.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtLocale.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtLocale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(grpTextRecord, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_2.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_2.setText("Encoding");
		
		txtEncoding = new Text(grpTextRecord, SWT.BORDER);
		txtEncoding.setEditable(false);
		txtEncoding.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtEncoding.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtEncoding.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtEncoding.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	public void initData(Record record) {
		if ( record == null ) {
			txtText.setText("");
			txtLocale.setText("");
			txtEncoding.setText("");
		} else {
			txtText.setText( ((TextRecord) record).getText() );
			txtLocale.setText( ((TextRecord) record).getLocale().toString() );
			txtEncoding.setText( ((TextRecord) record).getEncoding().displayName() );
		}
	}

	@Override
	public void updateData(Record record) {
		// TODO Auto-generated method stub
		
	}
}
