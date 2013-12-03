package com.duali.nfc.manager.handler.tag.formatter;

import com.duali.nfc.tag.Tag;

public interface TagFormatterListerner {
	/*
	 * NDEFTagWriterListerner
	 */
	public static final int REASON_FAILED_TO_AUTHENTICATE_CARD = 0;
	public static final int REASON_WRITE_FAILED = 1;
	public static final int REASON_FAILED_TO_LOCK_CARD = 2;	
	public static final int REASON_FAILED_NO_READER_DETECTED = 3;

	void tagDetected(Tag tag);
	void tagFormatProcessStarted();
	void updateStatusNoCard();
	void unsupportedTagDetected();	
	void tagFormatFail();
	void tagFormatSuccess(Tag tag);
	

	/*public void ndefCreationProcessStarted();
	
	public void unsupportedTagDetected();
	
	public void unCCTagDetected();
	
	public void unTagSizeOver();
	public void unTagSizeOver(String tagSize, String dataSize);
	
	public void tagDetected(String tagIdHex, String tagType);

	public void tagRemoved(String tagIdHex);

	public void ndefCreationFailed(String tagIdHex,int reason);

	public boolean ndefCreationSucceeded(String tagIdHex,
			int numberOfTagsWritten);
	
	public void ndefCreationProcessFinished(int numberOfTagsWritten);
	
	public void ndefCreationProcessError(int reason);
	
	public void ndefTagGoingToWrite(NdefMessage ndefTag);
	
	public String GetFormatTag();
	
	public int GetCCVal();*/
	
	


}
