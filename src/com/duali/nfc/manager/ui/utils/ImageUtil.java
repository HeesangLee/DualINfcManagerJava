package com.duali.nfc.manager.ui.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class ImageUtil {
	public static Image resize(Image image, int width, int height) {

		int orinHeight = image.getBounds().height;
		int orinWidth = image.getBounds().width;

		int scaledWidth = (height * orinWidth) / orinHeight;

		Image scaled = new Image(Display.getDefault(), scaledWidth, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, 
				image.getBounds().width, image.getBounds().height, 
				0, 0, scaledWidth, height);
		gc.dispose();
		image.dispose(); // don't forget about me!
		return scaled;
	}

	public static Image convertByteArraToImage(Display display, byte[] data) {
		Image byteImage = null;
		try {
			BufferedInputStream inputStreamReader = new BufferedInputStream(new ByteArrayInputStream(data));
			ImageData imageData = new ImageData(inputStreamReader);
			byteImage = new Image(display, imageData );
		} catch(Exception e) {			
			return null;
		}

		return byteImage;
	}
}
