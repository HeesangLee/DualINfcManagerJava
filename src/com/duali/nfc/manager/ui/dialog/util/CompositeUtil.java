package com.duali.nfc.manager.ui.dialog.util;

import org.eclipse.swt.widgets.Composite;

import com.duali.nfc.manager.ui.dialog.composites.NonRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.SmartPosterRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.TextRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.UriRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.VCardRecordComposite;

public class CompositeUtil {
	public static String getClassName(Composite compoosite) {
		String strComp = "com.aditux.tagman.ui.dialog.composites.";
		if (compoosite instanceof NonRecordComposite)
			strComp += "NonRecordComposite";
		else if (compoosite instanceof SmartPosterRecordComposite)
			strComp += "SmartPosterRecordComposite";
		else if (compoosite instanceof TextRecordComposite)
			strComp += "TextRecordComposite";
		else if (compoosite instanceof UriRecordComposite)
			strComp += "UriRecordComposite";
		else if (compoosite instanceof VCardRecordComposite)
			strComp += "VCardRecordComposite";
		else
			strComp = "";

		return strComp;
	}
}
