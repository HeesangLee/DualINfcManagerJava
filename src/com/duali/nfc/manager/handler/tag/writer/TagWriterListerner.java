package com.duali.nfc.manager.handler.tag.writer;

import com.duali.nfc.tag.Tag;

public interface TagWriterListerner {

	public static final int REASON_FAILED_TO_AUTHENTICATE_CARD = 0;
	public static final int REASON_WRITE_FAILED = 1;
	public static final int REASON_FAILED_TO_LOCK_CARD = 2;
	
	public static final int REASON_FAILED_NO_READER_DETECTED = 3;

	void ndefCreationProcessStarted();
	
	void unsupportedTagDetected();
	void noFormattedCC();
	void invalidNdefMagicNumber();
	
	void tagDetected(Tag tag);
	void tagIssueFail();
	void tagSizeOver(int length);
	void tagIssueSuccess(Tag tag);

	void updateStatusNoCard();

	

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
