package com.rfid.client.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.rfid.client.pojo.NettyMessage;

public class RespMessageQueue {
	private final List<NettyMessage> msgQueue = new LinkedList<NettyMessage>();
	private final int queueSize;
	private final Lock lock;
	private final Condition isEmpty;
	private final Condition isFull;
	public RespMessageQueue(int queueSize) {
		this.queueSize = queueSize;
		this.lock = new ReentrantLock();
		this.isEmpty = lock.newCondition();
		this.isFull = lock.newCondition();
	}
	
	public boolean add(NettyMessage e) throws InterruptedException{
		//1秒内未能获取锁，抛中断异常
		lock.lock();
		boolean result = false;
		try{
			//判断队列是否已满
			while(queueSize <= msgQueue.size()){
				//进行等待
				//一秒内，若依然处于等待状态，抛出中断异常
				isFull.await(1, TimeUnit.SECONDS);
				if(queueSize <= msgQueue.size())
					throw new InterruptedException("addRespMsgTimeout");
			}
			result = msgQueue.add(e);
			isEmpty.signal();
		}finally{
			lock.unlock();
		}
		return result;
		
	}
	
	public NettyMessage remove() throws InterruptedException{
		//1秒内未能获取锁，抛中断异常
		lock.lock();
		NettyMessage message = null;
		try{
			//判断队列是否为空
			while(msgQueue.size() <= 0){
				//等待
				//10秒内未能获得消息醒来
				isEmpty.await(10, TimeUnit.SECONDS);
				if(msgQueue.size() <= 0)
					throw new InterruptedException("time out");
			}
			message = msgQueue.remove(0);
			isFull.signal();
			
		}finally{
			lock.unlock();
		}
		return message;
		
	}
	
	public void clear(){
		msgQueue.clear();
	}
	
}
