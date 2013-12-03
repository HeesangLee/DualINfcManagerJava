package com.duali.nfc.manager.ui.dialog.util;

public class TopazUtil {
	private static final byte BIT0 = 0x00;
	private static final byte BIT1 = 0x01;
	private static final byte BIT2 = 0x02;
	private static final byte BIT3 = 0x04;
	private static final byte BIT4 = 0x08;
	private static final byte BIT5 = 0x10;
	private static final byte BIT6 = 0x20;
	private static final byte BIT7 = 0x30;
	
	public static int getCountLockBlocks(byte lock) {
		int count = 0;
		
		if ((lock & BIT0) > 0) 
			count++;
		if ((lock & BIT1) > 0) 
			count++;
		if ((lock & BIT2) > 0) 
			count++;
		if ((lock & BIT3) > 0) 
			count++;
		if ((lock & BIT4) > 0) 
			count++;
		if ((lock & BIT5) > 0) 
			count++;
		if ((lock & BIT6) > 0) 
			count++;
		if ((lock & BIT7) > 0) 
			count++;
			
		return count;
	}
}
