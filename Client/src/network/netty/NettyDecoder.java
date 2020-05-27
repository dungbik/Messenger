/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.netty;

import client.Client;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import network.data.ByteArrayByteStream;
import network.data.LittleEndianAccessor;
import tools.HexTool;

/**
 *
 * @author 윤정환
 */
public class NettyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> list) throws Exception {
        Client client = ctx.channel().attr(Client.CLIENTKEY).get();
        if (client == null || buffer.readableBytes() < 4) {
            return;
        }

        int packetlength = buffer.readInt();

        if (buffer.readableBytes() < packetlength) {
            buffer.resetReaderIndex();
            return;
        }

        buffer.markReaderIndex();

        byte[] decoded = new byte[packetlength];
        buffer.readBytes(decoded);
        buffer.markReaderIndex();
        System.out.println("복호화 전 : " + HexTool.toString(decoded));
        list.add(new LittleEndianAccessor(new ByteArrayByteStream(client.getRecvCrypto().crypt(decoded, true))));
    }
}