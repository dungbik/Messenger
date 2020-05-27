/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import client.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import main.Main;
import main.Setting;
import network.Crypto;
import network.data.LittleEndianAccessor;

/**
 *
 * @author ghks0
 */
public class NettyHandler  extends SimpleChannelInboundHandler<LittleEndianAccessor> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final String address = ctx.channel().remoteAddress().toString().split(":")[0];
        final Client client = new Client(ctx.channel(), new Crypto(), new Crypto());
        ctx.channel().attr(Client.CLIENTKEY).set(client);
//        Main.jTextArea1.append(address + " Connected\n");
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        Client client = ctx.channel().attr(Client.CLIENTKEY).get();

        if (client != null) {
            try {
            } finally {
                ctx.channel().attr(Client.CLIENTKEY).set(null);
                client.getSession().close();
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
        final Client client = (Client) ctx.channel().attr(Client.CLIENTKEY).get();
        byte header_num = slea.readByte();
        
        System.out.println(header_num + " " + slea.toString());

        try {
            handlePacket(header_num, slea, client);
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
            
    }
    
    public static final void handlePacket(final byte header, final LittleEndianAccessor slea, final Client client) throws Exception {
        switch (header) {
            case 0: {
                boolean suc = slea.readByte() == 1;
                if (suc) 
                    JOptionPane.showMessageDialog(Setting.setting, "회원가입 성공.", "성공", JOptionPane.OK_OPTION);
                else
                    JOptionPane.showMessageDialog(Setting.setting, "이미 존재하는 아이디입니다.", "실패", JOptionPane.OK_OPTION);
                break;
            }
            case 1: {
                byte type = slea.readByte();
                String id = "", pw = "", nickname = "";
                if (type == 1) {
                    id = slea.readLenAsciiString();
                    pw = slea.readLenAsciiString();
                    nickname = slea.readLenAsciiString();
                    client.setId(id);
                    client.setPW(pw);
                    client.setNickname(nickname);
                    Main main = new Main(client);
                    main.setVisible(true);
                    Setting.setting.setVisible(false);
                } else if (type == 2) {
                    JOptionPane.showMessageDialog(Setting.setting, "이미 접속중인 아이디입니다.", "실패", JOptionPane.OK_OPTION);
                }
                break;
            }
            case 2: {
                String msg = slea.readLenAsciiString();
                if (Main.jTextArea1 != null)
                    Main.jTextArea1.append(msg + "\n");
                break;
            }
            case 3: {
                String filename = slea.readLenAsciiString();
                int filesize = slea.readInt();
                int val = JOptionPane.showConfirmDialog(Main.main, "파일을 받으시겠습니까?\n\n파일이름 : " + filename + "\n 파일 크기 : " + filesize / 1000.0 + "kb", "파일 받기", JOptionPane.YES_OPTION);
                if (val == JOptionPane.YES_OPTION) {
                    FileDialog fd = new FileDialog(Main.main, "저장", FileDialog.SAVE);
                    fd.setName(filename);
                    fd.setLocation(300, 200);
                    fd.setVisible(true);
                    File file = new File(fd.getDirectory() + "\\" + fd.getFile());           
                    file.createNewFile(); 
                    FileOutputStream out = new FileOutputStream(file);

                    for (int i = 0; i < filesize; i++) { 
                        out.write(slea.readByte()); 
                    }
                    out.close();
                }
                break;
            }
        }
    }
}
