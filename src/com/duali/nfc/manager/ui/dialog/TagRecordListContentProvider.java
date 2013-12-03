package com.duali.nfc.manager.ui.dialog;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.duali.nfc.ndef.records.Record;

public class TagRecordListContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement == null)
			return new Object[]{};
		ArrayList<Record> v = (ArrayList<Record>)inputElement;
        return v.toArray();
	}
}
