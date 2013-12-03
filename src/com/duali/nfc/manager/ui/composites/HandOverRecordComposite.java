package com.duali.nfc.manager.ui.composites;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.duali.nfc.manager.ui.composites.handover.BluetoothOOBComposite;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import org.eclipse.swt.widgets.Label;

public class HandOverRecordComposite extends AbstractRecordComposite {
	public static final int ACTION_BLUETOOTH_OOB = 1;	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public HandOverRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		Button rbtnBookmark = new Button(this, SWT.RADIO);
		rbtnBookmark.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateUI(ACTION_BLUETOOTH_OOB);
			}

		});
		rbtnBookmark.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		rbtnBookmark.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		rbtnBookmark.setSelection(true);
		rbtnBookmark.setText("Bluetooth OOB");	
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(null);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));		

		createBluetoothOOBComposite(composite);
	}
	
	private BluetoothOOBComposite bluetoothOOBComposite;
	
	private void createBluetoothOOBComposite(Composite composite) {
		bluetoothOOBComposite = new BluetoothOOBComposite(composite, SWT.NONE);
		bluetoothOOBComposite.setBounds(0, 0, 401, 250);
	}
	
	private void populateUI(int action) {
		switch (action) {		
		case ACTION_BLUETOOTH_OOB:

			bluetoothOOBComposite.clear();
			bluetoothOOBComposite.setDefaultData();
			
			bluetoothOOBComposite.setVisible(true);
			break;		
		default:
			break;
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void addRecord(TableViewer tbvRecord) {
		// 활성호된 컴포지트 찾기
		
		if (bluetoothOOBComposite.isVisible()) {
			bluetoothOOBComposite.addRecord(tbvRecord);
		} 
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultData() {
		// TODO Auto-generated method stub
		
	}
}