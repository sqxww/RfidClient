package com.rfid.client.util.cprg;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.rfid.client.pojo.cprg.CMessage;


public class ReqMessageQueue {
	private static final ReqMessageQueue REQ_MESSAGE_QUEUE = new ReqMessageQueue(100);
	private final List<CMessage> msgQueue = new LinkedList<CMessage>();
	private final int queueSize;
	private final Lock lock;
	private final Condition isEmpty;
	private final Condition isFull;
	private ReqMessageQueue(int queueSize) {
		this.queueSize = queueSize;
		this.lock = new ReentrantLock(false);
		this.isEmpty = lock.newCondition();
		this.isFull = lock.newCondition();
	}
	
	public static ReqMessageQueue getInstance(){
		return REQ_MESSAGE_QUEUE;
	}
	
	public boolean add(CMessage e) throws InterruptedException{
		lock.lock();
		boolean result = false;
		try{
			//判断队列是否已满
			while(queueSize <= msgQueue.size()){
				//进行等待
				//一秒内，若依然处于等待状态，抛出中断异常
				isFull.await(1, TimeUnit.SECONDS);
				if(queueSize <= msgQueue.size())
					throw new InterruptedException("addReqMsgTimeout");
			}
			result = msgQueue.add(e);
			isEmpty.signal();
			
		}finally{
			lock.unlock();
		}
		return result;
		
	}
	
	public CMessage remove() throws InterruptedException{
		lock.lock();
		CMessage reqMsg = null;
		try{
			//判断队列是否为空
			while(msgQueue.size() <= 0){
				//等待
				isEmpty.await();
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
