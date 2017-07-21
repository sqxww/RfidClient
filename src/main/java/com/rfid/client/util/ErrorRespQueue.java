package com.rfid.client.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ErrorRespQueue {
	private static final ErrorRespQueue ERROR_RESP_QUEUE = new ErrorRespQueue(100);
	private final List<Byte> msgQueue = new LinkedList<Byte>();
	private final int queueSize;
	private final Lock lock;
	private final Condition isEmpty;
	private final Condition isFull;
	private ErrorRespQueue(int queueSize) {
		this.queueSize = queueSize;
		this.lock = new ReentrantLock(false);
		this.isEmpty = lock.newCondition();
		this.isFull = lock.newCondition();
	}
	
	public static ErrorRespQueue getInstance(){
		return ERROR_RESP_QUEUE;
	}
	
	public boolean add(byte e) throws InterruptedException{
		lock.lock();
		boolean result = false;
		try{
			//判断队列是否已满
			while(queueSize <= msgQueue.size()){
				//进行等待
				//一秒内，若依然处于等待状态，抛出中断异常
				isFull.await(1, TimeUnit.SECONDS);
				if(queueSize <= msgQueue.size())
					throw new InterruptedException("addErrorMsgTimeout");
			}
			result = msgQueue.add(e);
			isEmpty.signal();
			
		}finally{
			lock.unlock();
		}
		return result;
		
	}
	
	public Byte remove() throws InterruptedException{
		lock.lock();
		Byte reqMsg = null;
		try{
			//判断队列是否为空
			while(msgQueue.size() <= 0){
				//等待
				isEmpty.await(5,TimeUnit.SECONDS);
				if(msgQueue.size() <= 0)
					throw new InterruptedException("getErrorMsgTimeout");
			}
			reqMsg = msgQueue.remove(0);
			isFull.signal();
		}finally{
			lock.unlock();
		}
		return reqMsg;
		
	}
	
	public void clear(){
		msgQueue.clear();
	}
	
}
