/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.netty;

import client.Client;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import network.Crypto;
import tools.HexTool;

/**
 *
 * @author ¿îÁ¤È¯
 */
public class NettyEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf buffer) throws Exception {
        final Client client = ctx.channel().attr(Client.CLIENTKEY).get();
        final Crypto send_crypto = client.getSendCrypto();

        buffer.writeInt(msg.length);
        buffer.writeBytes(send_crypto.crypt(msg, false));
        ctx.flush();

    }
    
}
