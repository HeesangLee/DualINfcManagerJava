package com.duali.nfc.manager.ui.composites;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.ndef.records.Record;

public abstract class AbstractRecordComposite extends Composite {
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractRecordComposite(Composite parent, int style) {
		super(parent, style);
	}	
	
	public abstract void clear();
	public abstract void setDefaultData();
	public abstract void addRecord(TableViewer tbvRecord);
	protected void addRecord(TableViewer tbvRecord, Record record) {
		ArrayList<Record> recordList = (ArrayList<Record>) tbvRecord.getInput();		
		recordList.add(record);				
		tbvRecord.setInput(recordList);
	}
	
	protected void showInfoMessageBox(final String message){
		DisplayUtils.CURRENT_DISPLAY.asyncExec(new Runnable() {
			public void run() {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
				mb.setText(AppLocale.getText("INFO"));
				mb.setMessage(message);
				mb.open();
			}
		});
	}
	
	protected void showErrorMessageBox(final String message){
		DisplayUtils.CURRENT_DISPLAY.asyncExec(new Runnable() {
			public void run() {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
				mb.setText(AppLocale.getText("ERR"));
				mb.setMessage(message);
				mb.open();
			}
		});
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
