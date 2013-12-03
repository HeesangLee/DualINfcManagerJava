package com.duali.nfc.manager.handler.p2p.writer;

import java.util.ArrayList;

import com.duali.nfc.ndef.NdefMessage;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.reader.handler.NFCReaderHandler;

public interface P2PWriter {
	/*
	 * TagWriter
	 */
	public void startNDEFWriteProcess(ArrayList<Record> ndefTag,			
			P2PWriterListerner callbackListerner, NFCReaderHandler nfcReaderHandler, String p2pType);
	
	public void pauseNDEFCreationProcess();
	
	public void resumeNDEFCreationProcess();

	public void finishNDEFCreationProcess();
}
