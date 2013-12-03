package com.duali.nfc.manager.ui.shells;

import info.ineighborhood.cardme.vcard.VCard;
import info.ineighborhood.cardme.vcard.features.EmailFeature;
import info.ineighborhood.cardme.vcard.features.FormattedNameFeature;
import info.ineighborhood.cardme.vcard.features.TelephoneFeature;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.duali.nfc.manager.ui.shells.PrimaryShell;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FormatterUtil;
import com.duali.nfc.ndef.records.ActionRecord;
import com.duali.nfc.ndef.records.ActionRecord.Action;
import com.duali.nfc.ndef.records.AndroidApplicationRecord;
import com.duali.nfc.ndef.records.BluetoothOOBDataRecord;
import com.duali.nfc.ndef.records.MimeRecord;
import com.duali.nfc.ndef.records.SmartPosterRecord;
import com.duali.nfc.ndef.records.TextRecord;
import com.duali.nfc.ndef.records.UnknownRecord;
import com.duali.nfc.ndef.records.UriRecord;
import com.duali.nfc.ndef.records.VCardRecord;
import com.duali.utils.Hex;

public class TagRecordListLabelProvider extends LabelProvider {		
	private static final Logger LOGGER =
		Logger.getLogger(TagRecordListLabelProvider.class);

	public Image getImage(Object record) {
		Image image = null;
		if (record instanceof SmartPosterRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/smartposter_16.png"));				
		} else if (record instanceof TextRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/textrecord_16.png")); 
		} else if (record instanceof UriRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/urirecord_16.png")); 
		} else if (record instanceof VCardRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/vcard2_16.png")); 
		} else if (record instanceof MimeRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/mime_16.png")); 
		} else if (record instanceof AndroidApplicationRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/aar_16.png")); 
		} else if (record instanceof UnknownRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/un_16.png")); 
		} else if (record instanceof BluetoothOOBDataRecord) {
			image = new Image(DisplayUtils.CURRENT_DISPLAY, PrimaryShell.class.getResourceAsStream("/drawable/handover_16.png")); 
		}
		return image;
	}

	public String getText(Object record) {
		StringBuffer sb = new StringBuffer();

		if (record instanceof SmartPosterRecord) {

			SmartPosterRecord spr = (SmartPosterRecord) record;
			LOGGER.debug("SmartPosterRecord: " + spr.toString());

			sb.append(spr.getTitle().getText());				
			sb.append(" / ");

			sb.append(spr.getUri().getUri().toString());				
			sb.append(" / ");

			ActionRecord actionRecord = spr.getAction();
			if (actionRecord != null) {
				if (actionRecord.getAction() == Action.DO_THE_ACTION) {
					sb.append("Do the Action");	
				} else if (actionRecord.getAction() == Action.SAVE_FOR_LATER) {
					sb.append("Save for Later");	
				} else if (actionRecord.getAction() == Action.OPEN_FOR_EDITING) {
					sb.append("Open for Editing");	
				}
			}

			return cutString(40, sb.toString());
		} else if (record instanceof BluetoothOOBDataRecord) {

			BluetoothOOBDataRecord btRecord = (BluetoothOOBDataRecord) record;
			LOGGER.debug("BluetoothOOBDataRecord: " + btRecord.toString());

			sb.append(FormatterUtil.formatMacAddress(Hex.bytesToHexString(btRecord.getMacAddress())));
			
			return cutString(40, sb.toString());
		} else if (record instanceof TextRecord) {
			TextRecord tr = (TextRecord) record;
			sb.append(tr.getText());
			sb.append(" / ");
			sb.append(tr.getLocale());
			sb.append(" / ");
			sb.append(tr.getEncoding().displayName());

			return cutString(40, sb.toString());
		} else if (record instanceof UriRecord) {
			UriRecord ur = (UriRecord) record;
			return cutString(40, ur.getUri().toString());
		} else if (record instanceof VCardRecord) {
			VCardRecord vr = (VCardRecord) record;
			VCard vCard = vr.getvCard();

			FormattedNameFeature name = vCard.getFormattedName();
			if (name != null) {
				sb.append(name.getFormattedName());
				sb.append(" / ");
			}

			Iterator<TelephoneFeature> tel = vCard.getTelephoneNumbers();

			if (tel != null) {
				while(tel.hasNext()) {
					TelephoneFeature telepone = tel.next();
					sb.append(telepone.getTelephone());
					sb.append(" / ");
				}
			}

			Iterator<EmailFeature> emails = vCard.getEmails();
			if (emails != null) {
				while(emails.hasNext()) {
					EmailFeature email = emails.next();
					if(email != null && email.getEmail() != null && email.getEmail().trim().length() > 0) {
						sb.append(email.getEmail());
						sb.append(" / ");
					}
				}				
			}

			return cutString(40, sb.toString());
		} else if (record instanceof MimeRecord) {

			sb.append(((MimeRecord) record).getType());				
			sb.append(" / ");
			sb.append(Hex.bytesToHexString(((MimeRecord) record).getContent()));
			return cutString(40, sb.toString());
		} else if (record instanceof AndroidApplicationRecord) {

			sb.append(((AndroidApplicationRecord) record).getPackageName());				
			return cutString(40, sb.toString());
		} else if (record instanceof UnknownRecord) {
			return "Unknown Record Type";
		}
		return "";		
	}

	private String cutString(int lengthLimit, String displayStr) {
		if (displayStr.length() > lengthLimit) {
			displayStr = displayStr.substring(0, lengthLimit);
			displayStr += "...";
		}

		return displayStr;
	}
}
