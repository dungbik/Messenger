package handler;


import client.User;
import client.UserResister;
import main.Main;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.File;
import java.io.FileOutputStream;
import network.Crypto;
import network.packet.Packet;
import network.data.LittleEndianAccessor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 윤정환
 */
public class NettyHandler extends SimpleChannelInboundHandler<LittleEndianAccessor> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final String address = ctx.channel().remoteAddress().toString().split(":")[0];
        final User user = new User(ctx.channel(), new Crypto(), new Crypto());
        ctx.channel().attr(User.CLIENTKEY).set(user);
        Main.jTextArea1.append(address + " Connected\n");
        Main.users.add(user);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        User user = ctx.channel().attr(User.CLIENTKEY).get();

        if (user != null) {
            try {
                Main.jTextArea1.append(user.getSession().remoteAddress().toString() + " Connected\n");
                Main.users.remove(user);
                Main.jTextArea1.append("남은 수 : " + Main.users.size() + "\n");
            } finally {
                ctx.channel().attr(User.CLIENTKEY).set(null);
                user.getSession().close();
            }
        }
    }

    @Override 
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
    
     @Override
     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
         if (evt instanceof IdleStateEvent) {
             IdleStateEvent e = (IdleStateEvent) evt;
         }
     }

    @Override 
    protected void channelRead0(ChannelHandlerContext ctx, LittleEndianAccessor slea) throws Exception {
        final User user = (User) ctx.channel().attr(User.CLIENTKEY).get();
        byte header_num = slea.readByte();
        
        System.out.println(header_num + " " + slea.toString());

        try {
            handlePacket(header_num, slea, user);
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
            
    }
    
    public static final void handlePacket(final byte header, final LittleEndianAccessor slea, final User user) throws Exception {
        switch (header) {
            case 0: {
                String id = slea.readLenAsciiString();
                String pw = slea.readLenAsciiString();
                if (UserResister.getAccountExists(id)) {
                    user.getSession().writeAndFlush(Packet.resigsterResult(false));
                } else {
                    UserResister.createAccount(id, pw);
                    user.getSession().writeAndFlush(Packet.resigsterResult(true));
                }
                break;
            }
            case 1: {
                String id = slea.readLenAsciiString();
                String pw = slea.readLenAsciiString();
                boolean dup = false;
                for (Object user_ : Main.users) { // 중복 방지
//                    User user__ = ((User) user_);
//                    if (user__.getId() != null && user__.getId().equals(id) && user__.getPW().equals(pw)) {
//                        dup = true;
//                        break;
//                    }
                }
                int type =  dup ? 2 : User.login(id, pw) ? 1 : 0;
                user.getSession().writeAndFlush(Packet.loginInfo((byte) type, id, pw, User.getNickname()));
                break;
            }
            case 2: {
                String msg = slea.readLenAsciiString();
                for (Object user_ : Main.users) {
                    ((User) user_).getSession().writeAndFlush(Packet.sendMsg(((User) user_).getNickname() + " : " + msg));
                }
                break;
            }
            case 3: {
                String filename = slea.readLenAsciiString();
                int filesize = slea.readInt();

                for (Object user_ : Main.users) {
//                    if (!((User) user_).equals(user))
                        ((User) user_).getSession().writeAndFlush(Packet.sendFile(filename, filesize, slea.read(filesize)));
                }
            }
        }
    }
    
}
