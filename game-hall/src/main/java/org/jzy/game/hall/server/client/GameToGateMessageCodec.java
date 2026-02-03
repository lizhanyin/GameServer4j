package org.jzy.game.hall.server.client;

import com.google.protobuf.Message;
import com.jzy.javalib.network.io.message.IdMessage;
import com.jzy.javalib.network.io.message.MsgUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解析与Gate服连接的消息
 * 
 * @author JiangZhiYong
 */
public class GameToGateMessageCodec extends ByteToMessageCodec<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameToGateMessageCodec.class);
    /** 消息头长度，除去消息长度  */
    private static final int HEADER_EXCLUDE_LENGTH = 16;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        switch (msg) {
            case ByteBuf byteBuf -> out.writeBytes(byteBuf);
            case IdMessage idMessage -> {
                switch (idMessage.getMsg()) {
                    case byte[] bytes -> {
                        out.writeInt(HEADER_EXCLUDE_LENGTH +bytes.length);
                        out.writeInt(idMessage.getMsgId());
                        out.writeLong(idMessage.getId());
                        out.writeInt(idMessage.getMsgSequence());
                        out.writeBytes(bytes);
                    }
                    case Message message -> {
                        byte[] bytes = message.toByteArray();
                        out.writeInt(HEADER_EXCLUDE_LENGTH + bytes.length);
                        out.writeInt(idMessage.getMsgId());
                        out.writeLong(idMessage.getId());
                        out.writeInt(idMessage.getMsgSequence());
                        out.writeBytes(bytes);
                    }
                    default -> LOGGER.warn("IDMessage加密类型{}未实现", idMessage.getMsg().getClass().getSimpleName());
                }
            }
            default -> LOGGER.warn("未知的数据类型{}", msg.getClass().getName());
        }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MsgUtil.decode(ctx, in, out);
    }

}
