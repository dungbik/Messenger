/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

/**
 *
 * @author À±Á¤È¯
 */
public class Crypto {
    
    private static final byte[] cryptBytes = new byte[]{
        (byte) 0xEC, (byte) 0x3F, (byte) 0x77, (byte) 0xA4, (byte) 0x45, 
        (byte) 0xD0, (byte) 0x71, (byte) 0xBF, (byte) 0xB7, (byte) 0x98, 
        (byte) 0x20, (byte) 0xFC, (byte) 0x4B, (byte) 0xE9, (byte) 0xB3
    };
    
    public byte[] getPacketHeader(int length) {
        byte[] ret = new byte[4];
        ret[0] = (byte) (length & 0xFF);
        ret[1] = (byte) ((length >>> 8) & 0xFF);
        ret[2] = (byte) ((length >>> 16) & 0xFF);
        ret[3] = (byte) ((length >>> 24) & 0xFF);
        return ret;
    }
    
    public byte[] crypt(byte[] data, boolean decrypt) {
        int len = data.length;
        byte[] datac = new byte[len];
        System.arraycopy(data, 0, datac, 0, data.length);
        
        for (int i = 0; i < len; i++) {
            byte key = (byte) cryptBytes[i % cryptBytes.length];
            if (decrypt)
                datac[i] -= key;
            else
                datac[i] += key;
        }

        return datac;
    }
}
