package com.duali.nfc.manager.handler.p2p.writer;


public interface P2PWriterListerner {

	public static final int REASON_FAILED_TO_AUTHENTICATE_CARD = 0;
	public static final int REASON_WRITE_FAILED = 1;
	public static final int REASON_FAILED_TO_LOCK_CARD = 2;
	
	public static final int REASON_FAILED_NO_READER_DETECTED = 3;

	void ndefCreationProcessStarted();
	
	void sending();
	void sendFail(String message);	
	void sendSuccess();

	void updateStatusNoDevice();

}
