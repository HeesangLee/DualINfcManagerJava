package com.duali.nfc.manager.ui.dialog.composites;

import org.eclipse.swt.widgets.Composite;

import com.duali.nfc.ndef.records.Record;

public abstract class RecordComposite extends Composite {
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RecordComposite(Composite parent, int style) {
		super(parent, style);
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


	public abstract void initData(Record record);
	public abstract void updateData(Record record);
}
