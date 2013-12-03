package com.duali.nfc.manager.handler.p2p.reader;

import com.duali.nfc.reader.handler.NFCReaderHandler;

public interface P2PReader {
	/*
	 * NDEFTagReader
	 */
	public void startNDEFReadProcess(P2PReaderListerner callbackListerner, NFCReaderHandler nfcReaderHandler, String p2pType);
	
	public void pauseNDEFReadProcess();
	
	public void resumeNDEFReadProcess();

	public void finishNDEFReadProcess();

}
