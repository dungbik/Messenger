/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.packet;

import network.data.PacketLittleEndianWriter;

/**
 *
 * @author À±Á¤È¯
 */
public class Packet {
    
    public static final byte[] resigsterResult(boolean suc) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(0);
        p.write(suc);

        return p.getPacket();
    }   
    
    public static final byte[] loginInfo(byte type, String id, String pw, String nickname) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(1);
        p.write(type);
        if (type == 1) {
            p.writeLenAsciiString(id);
            p.writeLenAsciiString(pw);
            p.writeLenAsciiString(nickname);
        }
        return p.getPacket();
    }   
    
    public static final byte[] sendMsg(String msg) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(2);
        p.writeLenAsciiString(msg);
        
        return p.getPacket();
    }   
    
    public static final byte[] sendFile(String filename, int filesize, byte[] bytes) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(3);
        p.writeLenAsciiString(filename);
        p.writeInt(filesize);
        p.write(bytes);
        
        return p.getPacket();
    }   
}
