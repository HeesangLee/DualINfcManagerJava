package com.duali.nfc.manager.handler.tag.formatter;

import com.duali.nfc.reader.handler.NFCReaderHandler;

/**
 * <p>
 * </p>
 * 
 *  <dt><b>Date</b>
 *         <dd>Sep 21, 2011, 1:05:11 PM
 *         <dt><b>Module</b>
 *         <dd>ndef-creator
 *         <dt>TODO
 *         <dd>
 */
public interface TagFormatter {
	/*
	 * TagWriter
	 */
	public void startTagFormatProcess(TagFormatterListerner callbackListerner, NFCReaderHandler nfcReaderHandler);
	
	public void pauseTagFormatProcess();
	
	public void resumeTagFormatProcess();

	public void finishTagFormatProcess();
}
