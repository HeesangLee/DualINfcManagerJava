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
import com.duali.nfc.manager.ui.utils.FormatterUtil;
import com.duali.nfc.ndef.records.BluetoothOOBDataRecord;
import com.duali.nfc.ndef.records.BluetoothOOBDataRecord.EIR;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.handover.OOBData;
import com.duali.utils.Hex;

public class HandOverRecordComposite extends RecordComposite {
	private Text txtDeviceName;
	private Text txtMacAddress;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public HandOverRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		Group grpSmartposterRecord = new Group(this, SWT.NONE);
		grpSmartposterRecord.setLayout(null);
		grpSmartposterRecord.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSmartposterRecord.setText("Handover Record");
		grpSmartposterRecord.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		
		Label lblDeviceName = new Label(grpSmartposterRecord, SWT.NONE);
		lblDeviceName.setAlignment(SWT.RIGHT);
		lblDeviceName.setBounds(20, 57, 106, 15);
		lblDeviceName.setText("Device Address");
		lblDeviceName.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblDeviceName.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtDeviceName = new Text(grpSmartposterRecord, SWT.BORDER);
		txtDeviceName.setEditable(false);
		txtDeviceName.setBounds(140, 54, 144, 21);
		txtDeviceName.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtDeviceName.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		Label lblDeviceAddress = new Label(grpSmartposterRecord, SWT.NONE);
		lblDeviceAddress.setAlignment(SWT.RIGHT);
		lblDeviceAddress.setBounds(31, 86, 95, 15);
		lblDeviceAddress.setText("MacAddress");
		lblDeviceAddress.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblDeviceAddress.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtMacAddress = new Text(grpSmartposterRecord, SWT.BORDER);
		txtMacAddress.setEditable(false);
		txtMacAddress.setBounds(140, 81, 144, 21);
		txtMacAddress.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMacAddress.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

	}

	@Override
	public void initData(Record record) {
		if (record == null) {
			txtDeviceName.setText("");
			txtMacAddress.setText("");
		} else {
			BluetoothOOBDataRecord btRecord = (BluetoothOOBDataRecord) record;
			byte[] macAddress = btRecord.getMacAddress();
			
			if (macAddress != null)
				txtMacAddress.setText(FormatterUtil.formatMacAddress(Hex.bytesToHexString(macAddress)));
			
			OOBData[] options = btRecord.getOptions();
			
			if (options != null && options.length > 0) {
				for (OOBData option : options) {
					if (EIR.COMPLETE_LOCAL_NAME == option.getType()) {
						byte[] name = option.getData();
						
						if (name != null && name.length > 0)
							txtDeviceName.setText(new String(name));
						else
							txtDeviceName.setText("");
						break;
					}
				}
			}
		}
	}

	@Override
	public void updateData(Record record) {
	}
}