package com.duali.nfc.manager.ui.dialog.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.UnknownRecord;

public class NonRecordComposite extends RecordComposite {
	
	private Label lblNotification;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NonRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		lblNotification = new Label(this, SWT.NONE);
		lblNotification.setFont(FontUtils.FONT_TAHOMA_BOLD);
		lblNotification.setForeground(DisplayUtils.COLOR_LIGHT_RED);
		lblNotification.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNotification.setText("Ready to..");
//		updateNotify("Reading Data..", 
//					 SWTResourceManager.getFont("맑�? 고딕", 16, SWT.BOLD), 
//					 SWTResourceManager.getColor(SWT.COLOR_RED));	
	}
	
	private void updateNotify(String strData, Font font, Color fontColor) {
		
		lblNotification.setForeground(fontColor);
		lblNotification.setFont(font);
		lblNotification.setText(strData);
	}

	@Override
	public void initData(Record record) {
		if ( record instanceof UnknownRecord) {
			lblNotification.setText("Unknown Record Type");		
		}
		
	}

	@Override
	public void updateData(Record record) {
		// TODO Auto-generated method stub
		
	}

	public void initData(final String data) {
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				lblNotification.setText(data);		
			}
		});
	}
}
