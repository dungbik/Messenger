/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.netty;

import client.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.concurrent.locks.Lock;
import network.Crypto;
import tools.HexTool;

/**
 *
 * @author À±Á¤È¯
 */
public class NettyEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf buffer) throws Exception {
        final User user = ctx.channel().attr(User.CLIENTKEY).get();
        final Crypto send_crypto = user.getSendCrypto();

        buffer.writeInt(msg.length);
        buffer.writeBytes(send_crypto.crypt(msg, false));
        ctx.flush();
        System.out.println(HexTool.toString(msg));
    }
    
}
