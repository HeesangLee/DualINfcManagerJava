package com.duali.nfc.manager.handler.tag.reader;

import com.duali.nfc.reader.handler.NFCReaderHandler;

public interface TagReader {
	/*
	 * NDEFTagReader
	 */
	public void startNDEFReadProcess(TagReaderListerner callbackListerner, NFCReaderHandler nfcReaderHandler);
	
	public void pauseNDEFReadProcess();
	
	public void resumeNDEFReadProcess();

	public void finishNDEFReadProcess();

}
