package com.duali.nfc.manager.handler.tag.reader;

import com.duali.nfc.tag.Tag;

public interface TagReaderListerner {

	public static final int REASON_FAILED_TO_AUTHENTICATE_CARD = 0;
	public static final int REASON_WRITE_FAILED = 1;
	public static final int REASON_FAILED_TO_LOCK_CARD = 2;
	
	public static final int REASON_FAILED_NO_READER_DETECTED = 3;
	public static final int REASON_WRONG_CC_FORMAT = 4;
	public static final int REASON_NOT_CC_FORMAT = 5;

	public void ndefReadingProcessStarted();
	
	public void unsupportedTagDetected();
	
	public void tagDetected(String tagIdHex, String tagType);

	public void tagRemoved(String tagIdHex);

	public void ndeReadingFailed(String tagIdHex,int reason);

	public void ndefReadSucceeded(String tagIdHex);
	
	public void ndefReadingProcessFinished();
	
	public void ndefReadingProcessError(int reason);
	
	public void setContent(String content);
	public void updateStatusNoCard();
	
	public void updateTag(Tag tag);
}
