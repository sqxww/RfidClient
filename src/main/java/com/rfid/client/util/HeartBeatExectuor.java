package com.rfid.client.util;

public class HeartBeatExectuor {
	private static Thread hbThread = null;
	
	public static void execute(Runnable task){
		synchronized (HeartBeatExectuor.class) {
			if(hbThread != null && !hbThread.isInterrupted()){
				hbThread.interrupt();
			}
			hbThread = new Thread(task);
			hbThread.start();
		}
	}
	
	public static void shutdown(){
		hbThread.interrupt();
	}
	
}
