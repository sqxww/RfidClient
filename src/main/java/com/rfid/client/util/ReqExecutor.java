package com.rfid.client.util;

public class ReqExecutor {
	private static  Thread reqThread = null;
	
	public static void exectue(Runnable reqTask){
		synchronized (ReqExecutor.class) {
			//判断之前线程是否已中断
			if(reqThread != null && !reqThread.isInterrupted()){
				reqThread.interrupt();
			}
			reqThread = new Thread(reqTask);
			reqThread.start();
		}
	}
	
	public static void shutdown(){
		//中断线程
		reqThread.interrupt();
	}
}
