package com.duali.nfc.manager.ui.dialog.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.dialog.composites.mime.ApplicationXChOneTouchComposite;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.MimeRecord;
import com.duali.nfc.ndef.records.Record;

public class MimeRecordComposite extends RecordComposite {
	private Text txtText;

	private ApplicationXChOneTouchComposite applicationXChOneTouchComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MimeRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		Group grpTextRecord = new Group(this, SWT.NONE);
		grpTextRecord.setLayout(null);
		grpTextRecord.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTextRecord.setText("MimeRecord");
		grpTextRecord.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		grpTextRecord.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		//		grpTextRecord.setLayout(new GridLayout(2, false));

		Label lblNewLabel = new Label(grpTextRecord, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(10, 26, 65, 17);
		lblNewLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel.setText("TYPE");

		txtText = new Text(grpTextRecord, SWT.BORDER);
		txtText.setBounds(94, 23, 229, 20);
		txtText.setEditable(false);
		txtText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtText.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		
		applicationXChOneTouchComposite = new ApplicationXChOneTouchComposite(grpTextRecord, SWT.NONE);
		applicationXChOneTouchComposite.setBounds(10, 59, 351, 210);
	}

	@Override
	public void initData(Record record) {
		if ( record == null ) {
			txtText.setText("");
		} else {
			String type =  ((MimeRecord) record).getType();
			txtText.setText(type);
			
			if(ApplicationXChOneTouchComposite.TYPE.equals(type)) {
				applicationXChOneTouchComposite.setVisible(true);
				applicationXChOneTouchComposite.initData(record);
			}
		}
	}

	@Override
	public void updateData(Record record) {
		// TODO Auto-generated method stub

	}
}
