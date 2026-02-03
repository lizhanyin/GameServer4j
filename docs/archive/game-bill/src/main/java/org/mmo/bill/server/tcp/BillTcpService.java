package org.mmo.bill.server.tcp;

import org.mmo.engine.io.message.TcpMessageBean;
import org.mmo.engine.io.service.TcpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * tcp通信
 */
@Service
public class BillTcpService extends TcpService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillTcpService.class);

    // 存TCP消息处理类
    private Map<Integer, TcpMessageBean<?, ?>> messageBeans = new ConcurrentHashMap<>();

    @Autowired
    private TcpService nettyServer;

    @PostConstruct
    public void initHandler() {
        //TODO 注册消息实体
    }

    /**
     * 获取tcp消息结构
     */
    public TcpMessageBean<?, ?> getMessageBean(int id) {
        return messageBeans.get(id);
    }
}
