package com.duali.nfc.manager.handler.tag.writer;

import java.util.ArrayList;

import com.duali.nfc.ndef.NdefMessage;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.reader.handler.NFCReaderHandler;

public interface TagWriter {
	/*
	 * TagWriter
	 */
	public void startNDEFWriteProcess(ArrayList<Record> ndefTag,			
			TagWriterListerner callbackListerner, NFCReaderHandler nfcReaderHandler);
	
	public void pauseNDEFCreationProcess();
	
	public void resumeNDEFCreationProcess();

	public void finishNDEFCreationProcess();
}
