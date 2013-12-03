package com.duali.nfc.manager.ui.utils;

public class FormatterUtil {

	public static final String formatMacAddress(String macAddress) {
		if (macAddress == null)
			return macAddress;
		
		if (macAddress.length() != 12)
			return macAddress;
		
		StringBuffer sb = new StringBuffer();
		sb.append(macAddress.substring(0, 2));
		sb.append("-");
		sb.append(macAddress.substring(2, 4));
		sb.append("-");
		sb.append(macAddress.substring(4, 6));
		sb.append("-");
		sb.append(macAddress.substring(6, 8));
		sb.append("-");
		sb.append(macAddress.substring(8, 10));
		sb.append("-");
		sb.append(macAddress.substring(10, 12));
		
		return sb.toString();
	}
}
