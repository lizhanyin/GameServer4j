package org.mmo.bill.server.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.engine.io.message.MsgType;
import org.mmo.engine.io.message.MsgUtil;
import org.mmo.engine.thread.IExecutorService;
import org.mmo.message.MIDMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录服tcp消息处理
 *
 * @author JiangZhiYong
 * @date 2018/12/11
 */
public class BillTcpServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillTcpServerHandler.class);
    private final IExecutorService executorService;
    private final BillTcpService tcpService;

    public BillTcpServerHandler(IExecutorService executorService, BillTcpService tcpService) {
        this.executorService = executorService;
        this.tcpService = tcpService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.warn("已打开连接{}", ctx.channel().toString());
        if (tcpService != null) {
            tcpService.onChannelConnect(ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionClosed(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("连接{} {}【异常关闭】", ctx.channel().remoteAddress(), cause.getMessage());
        sessionClosed(ctx.channel());
    }

    private void sessionClosed(Channel channel) {
        LOGGER.warn("连接{}已关闭", channel);
        if (tcpService != null) {
            tcpService.onChannelClosed(channel);
        }
        LOGGER.warn(MsgUtil.getLocalIpPort(channel) + "断开链接");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            int msgType = byteBuf.readShort();
            long id = byteBuf.readLong();
            if (msgType == MsgType.IDMESSAGE.type.intValue()) { // 数据结构:msgId:pfbytes
                int msgId = byteBuf.readInt();
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                // 在本地注册，必须预处理
                TcpMessageBean<?, ?> messageBean = tcpService.getMessageBean(msgId);
                if (messageBean != null) {
                    Object message = messageBean.buildMessage(bytes);
                    TcpHandler handler = (TcpHandler) messageBean.newHandler();
                    if (handler != null) {
                        handler.setPid(id);
                        handler.setMsgBytes(bytes);
                        handler.setMessage(message);
                        handler.setChannel(ctx.channel());
                        if (executorService != null) {
                            executorService.getExecutor(messageBean.getExecuteThread()).execute(handler);
                        }
                    }
                } else {
                    LOGGER.warn("消息[{}]代码未实现逻辑", MIDMessage.MID.forNumber(msgId));
                }
            } else {
                LOGGER.warn("消息类型{}未实现,玩家{}消息发送失败", msgType, id);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("channelRead"), e);
        } finally {
            byteBuf.release();
        }
    }
}
