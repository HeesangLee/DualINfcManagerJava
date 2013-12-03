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
import com.duali.nfc.ndef.records.ActionRecord;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.SmartPosterRecord;
import com.duali.nfc.ndef.records.TextRecord;
import com.duali.nfc.ndef.records.UriRecord;

public class SmartPosterRecordComposite extends RecordComposite {
	private Text txtText;
	private Text txtUri;
	private Text txtAction;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SmartPosterRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		Group grpSmartposterRecord = new Group(this, SWT.NONE);
		grpSmartposterRecord.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSmartposterRecord.setText("SmartPoster Record");
		grpSmartposterRecord.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		grpSmartposterRecord.setLayout(new GridLayout(2, false));

		Label lblText = new Label(grpSmartposterRecord, SWT.NONE);
		lblText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblText.setText("Text");

		txtText = new Text(grpSmartposterRecord, SWT.BORDER);
		txtText.setEditable(false);
		txtText.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtText.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtText.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblUri = new Label(grpSmartposterRecord, SWT.NONE);
		lblUri.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUri.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblUri.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblUri.setText("URI");

		txtUri = new Text(grpSmartposterRecord, SWT.BORDER);
		txtUri.setEditable(false);
		txtUri.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtUri.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtUri.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAction = new Label(grpSmartposterRecord, SWT.NONE);
		lblAction.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAction.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblAction.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblAction.setText("Action");

		txtAction = new Text(grpSmartposterRecord, SWT.BORDER);
		txtAction.setEditable(false);
		txtAction.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtAction.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtAction.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtAction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	public void initData(Record record) {
		if (record == null) {
			txtText.setText("");
			txtUri.setText("");
			txtAction.setText("");
		} else {
			
			TextRecord title = ((SmartPosterRecord) record).getTitle();
			if(title != null)
				txtText.setText( title.getText() );
			
			UriRecord uri = ((SmartPosterRecord) record).getUri();
			if(uri != null)
				txtUri.setText( uri.getUri() );
			
			ActionRecord action = ((SmartPosterRecord) record).getAction();
			if(action != null)
				txtAction.setText( action.toString() );
		}
	}

	@Override
	public void updateData(Record record) {
		
		
	}
}