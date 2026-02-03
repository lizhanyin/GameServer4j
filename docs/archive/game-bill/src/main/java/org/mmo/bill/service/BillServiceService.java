package org.mmo.bill.service;

import org.mmo.bill.server.tcp.BillTcpChannelInitializer;
import org.mmo.bill.server.tcp.BillTcpService;
import org.mmo.engine.io.netty.config.NettyProperties;
import org.mmo.engine.io.netty.tcp.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * 服务器管理
 */
@Service
public class BillServiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillServiceService.class);

    @Autowired
    private TcpServer nettyServer;

    @Autowired
    private BillTcpService billTcpService;

    @Autowired
    private NettyProperties nettyProperties;

    @Autowired
    private BillTcpChannelInitializer billTcpChannelInitializer;

    @PostConstruct
    public void start() {
        LOGGER.debug("run bill tcp ...");
        NettyProperties.NettyServerConfig nettyServerConfig = nettyProperties.getServerConfigs().get(0);
        nettyServer.setNettyServerConfig(nettyServerConfig);
        nettyServer.setChannelInitializer(billTcpChannelInitializer);
        nettyServer.start();
    }

    @PreDestroy
    public void stop() {
        LOGGER.debug(" stop ... ");
        nettyServer.stop();
    }
}
