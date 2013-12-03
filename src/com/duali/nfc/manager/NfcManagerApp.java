package com.duali.nfc.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.duali.nfc.manager.ui.shells.PrimaryShell;
import com.duali.nfc.manager.ui.utils.AppImages;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;

public class NfcManagerApp {
	private static final Logger LOGGER = Logger.getLogger(NfcManagerApp.class);

	public static void main(String[] args) {		
		if(!StartUpChecker.canProceed()){

			/** 
			 * shows message box to indicate the application is already running.
			 */
			DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
				public void run() {
					Shell boxParent = new Shell(DisplayUtils.CURRENT_DISPLAY);
					MessageBox mb = new MessageBox(boxParent, SWT.ICON_INFORMATION | SWT.OK);
					mb.setText(AppLocale.getText("INFO"));
					mb.setMessage(AppLocale.getText("MSG.TAG.MAN.RUNNING"));	//Tag manager already running!
					mb.open();
					boxParent.dispose();
					System.exit(0);
				}
			});
		}
		createApplicationKillBatFile();
		DOMConfigurator.configure(NfcManagerApp.class.getResource("log4j-config.xml"));
		Logger.getRootLogger().setLevel(Level.DEBUG);
		String fileName = null;
		if(args != null && args.length > 0){
			fileName = args[0];
		}
		startNDEFCreator(fileName);
	}
		
	private static void startNDEFCreator(String fileName){
		LOGGER.debug("Startup check ok");//Logging for debug purpose.

		PrimaryShell primaryShell = new PrimaryShell();

		Rectangle bounds = DisplayUtils.CURRENT_DISPLAY.getPrimaryMonitor().getBounds();
		Rectangle rect =  primaryShell.getShell().getBounds ();
		LOGGER.debug("Monitor Size Width: " + bounds.width + ", Heigth: " + bounds.height);
		
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		primaryShell.getShell().setLocation (x, y);     
		
		primaryShell.getShell().open();	//프로그램 Open
		while(!primaryShell.getShell().isDisposed()){
			if (!DisplayUtils.CURRENT_DISPLAY.readAndDispatch())
				DisplayUtils.CURRENT_DISPLAY.sleep();
		}

		try {
			AppImages.disposeResources();
			DisplayUtils.disposeResources();
			FontUtils.disposeResources();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e); //Logging error.
		}	

		primaryShell.dispose();
		DisplayUtils.CURRENT_DISPLAY.dispose();
		System.exit(0);
	}

	private static void createApplicationKillBatFile() {
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		int processPid = Integer.parseInt(rt.getName().substring(0,
				rt.getName().indexOf("@")));		//실행되고 있는 JAVA 가상머신 관련 정보

		try {
			File batchFile = new File("kill.bat");
			batchFile.createNewFile();
			batchFile.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(batchFile);
			fos.write(("taskkill /t /f /PID " + processPid).getBytes());
			fos.close();
			System.out.println("Kill created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
