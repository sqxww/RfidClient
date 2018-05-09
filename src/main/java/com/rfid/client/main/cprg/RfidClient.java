package com.rfid.client.main.cprg;

import java.util.concurrent.TimeUnit;

import com.rfid.client.codec.NettyMessageDecoder;
import com.rfid.client.codec.NettyMessageEncoder;
import com.rfid.client.handler.HeartBeatReqHandler;
import com.rfid.client.handler.LoginAuthReqHandler;
import com.rfid.client.handler.ReqHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class RfidClient {
	EventLoopGroup group = new NioEventLoopGroup();
	public void connect(int port, String host) throws Exception{
		
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8));
					p.addLast(new NettyMessageEncoder());
					p.addLast(new ReadTimeoutHandler(50,TimeUnit.SECONDS));
					p.addLast(new LoginAuthReqHandler());
					p.addLast(new HeartBeatReqHandler());
					p.addLast(new ReqHandler());
					
				}
				
			});
			ChannelFuture f = b.connect(host, port).sync();
			System.out.println("------------------------>in");
			f.channel().closeFuture().sync();
			System.out.println("---------------->out");
		} finally {
			//优雅退出
			group.shutdownGracefully();
		}
	}
}
