package org.mmo.bill.server.tcp;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.mmo.engine.io.message.IDMessage;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解析游戏服连接的消息
 */
public class BillTcpByteToMessageCodec extends ByteToMessageCodec<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillTcpByteToMessageCodec.class);
    private static final int HEADER_EXCLUDE_LENGTH = 14;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        // 需要组装玩家id，消息id等额外信息
        if (msg instanceof IDMessage) {
            IDMessage idMessage = (IDMessage) msg;
            if (idMessage.getMsg() instanceof byte[]) {
                byte[] bytes = (byte[]) idMessage.getMsg();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.type.intValue());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else if (idMessage.getMsg() instanceof Message) {
                Message message = (Message) idMessage.getMsg();
                byte[] bytes = message.toByteArray();
                out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                out.writeShort(MsgType.IDMESSAGE.type.intValue());
                out.writeLong(idMessage.getId());
                out.writeInt(idMessage.getMsgId());
                out.writeBytes(bytes);
            } else {
                LOGGER.warn("IDMessage加密类型{}未实现", idMessage.getMsg().getClass().getSimpleName());
            }
        } else {
            LOGGER.warn("加密类型{}未实现", msg.getClass().getSimpleName());
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        MsgUtil.decode(ctx, in, out);
    }
}
