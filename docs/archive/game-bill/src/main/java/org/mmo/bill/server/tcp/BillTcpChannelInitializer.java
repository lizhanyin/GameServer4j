package org.mmo.bill.server.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.mmo.bill.service.BillExecutorService;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * channel初始化
 */
@Component
@Scope("prototype")
public class BillTcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyProperties nettyProperties;

    @Autowired
    private BillTcpService tcpService;

    @Autowired
    private BillExecutorService billExecutorService;

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("Codec", new BillTcpByteToMessageCodec());
        ch.pipeline().addLast("MessageHandler", new BillTcpServerHandler(billExecutorService, tcpService));

        NettyProperties.NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(0);
        int bothIdleTime = Math.min(nettyServerConfig.getReaderIdleTime(), nettyServerConfig.getWriterIdleTime());
        ch.pipeline().addLast("IdleStateHandler", new IdleStateHandler(
                nettyServerConfig.getReaderIdleTime(),
                nettyServerConfig.getWriterIdleTime(),
                bothIdleTime));
    }
}
