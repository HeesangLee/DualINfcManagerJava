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
import com.duali.nfc.ndef.records.UriRecord;

public class UriRecordComposite extends RecordComposite {
	private Text txtUri;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public UriRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Group grpUriRecord = new Group(this, SWT.NONE);
		grpUriRecord.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpUriRecord.setText("URI Record");
		grpUriRecord.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		grpUriRecord.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(grpUriRecord, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("URI");
		
		txtUri = new Text(grpUriRecord, SWT.BORDER);
		txtUri.setEditable(false);
		txtUri.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtUri.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtUri.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	public void initData(Record record) {
		if ( record == null)
			txtUri.setText("");
		else 
			txtUri.setText( ((UriRecord) record).getUri() );
		
	}

	@Override
	public void updateData(Record record) {
		// TODO Auto-generated method stub
		
	}
}