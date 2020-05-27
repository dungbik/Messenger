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
    
    public static final byte[] newUserInfo(String id, String pw) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(0);
        p.writeLenAsciiString(id);
        p.writeLenAsciiString(pw);
        
        return p.getPacket();
    }   
    
    public static final byte[] oldUserInfo(String id, String pw) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(1);
        p.writeLenAsciiString(id);
        p.writeLenAsciiString(pw);
        
        return p.getPacket();
    }   
    
    public static final byte[] sendMsg(String msg) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(2);
        p.writeLenAsciiString(msg);
        
        return p.getPacket();
    }   
    
    public static final byte[] sendFile(String filename, int size, byte[] bytes) {
        final PacketLittleEndianWriter p = new PacketLittleEndianWriter();

        p.write(3);
        p.writeLenAsciiString(filename);
        p.writeInt(size);
        p.write(bytes);
        
        return p.getPacket();
    }  
}
