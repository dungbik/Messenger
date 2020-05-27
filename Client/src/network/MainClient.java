/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import handler.NettyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import network.netty.NettyDecoder;
import network.netty.NettyEncoder;

/**
 *
 * @author À±Á¤È¯
 */
public class MainClient {
    private static final EventLoopGroup group = new NioEventLoopGroup();  
    private static Channel channel;
    
    public static final Channel connectServer(String ip, int port) {

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new NettyDecoder());
                        ch.pipeline().addLast("encoder", new NettyEncoder());
//                        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 30, 0));
                        ch.pipeline().addLast("handler", new NettyHandler());
                    }
                });
            channel = bootstrap.connect(ip, port).sync().channel();

//            jTextArea1.append("Socket Open\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return channel;
    }
    
    public static final void closeServer() {
        if (channel != null)
            channel.close();
        group.shutdownGracefully();
    }
}
