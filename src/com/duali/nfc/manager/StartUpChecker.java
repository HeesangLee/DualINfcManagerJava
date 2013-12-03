package com.duali.nfc.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


public final class StartUpChecker {
    
    /** 
     * <p>This method checks for APP PID, inorder make
     * sure that the application is not runing currently.</p>
     * @return true if the application is not running currently,
     * false other wise.
     */
	public static boolean canProceed() {
		File file = new File("lock.txt");
		if (!file.exists()){
			System.out.println("not exists");
			try {
				file.createNewFile();
				file.deleteOnExit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//LOGGER.error(e.getMessage(),e); //Logging error.
			}
			return true;
		}
		else
			System.out.println("Exists");
		return (file.exists() && file.delete());
	}

	
    /**
     * this method writes gets the PID process ID of the running
     * application an d write it in kill.bat file 
     * which is having dos command taskkill process ID
     *
     */
    public static void writeProcessIdToBatchFile() {  
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();  
        int processPid = Integer.parseInt(rt.getName().substring(0,rt.getName().indexOf("@"))); 

        try {
            File batchFile = new File("kill.bat");
            batchFile.createNewFile();
            batchFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(batchFile);

            fos.write(("taskkill /t /f /PID "+processPid).getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
