package com.duali.nfc.manager.handler.p2p.reader;

public interface P2PReaderListerner {

	public void ndefReadingProcessStarted();		
	public void updateNdef(byte[] data);
	public void deviceRemoved(String cardUid);
	public void updateStatusNoDevice();
	
	void readSuccess();
	void reading();
	void readFail();
	
	boolean isAlive();
}
