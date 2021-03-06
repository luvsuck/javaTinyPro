package com.shjk.dtuDataGetter.runner;

import com.shjk.dtuDataGetter.config.ServerConfig;
import com.shjk.dtuDataGetter.config.SocketProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TestRunner implements CommandLineRunner {
    @Autowired
    private SocketProperties properties;

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    @Override
    public void run(String... args) throws Exception {
        ServerSocket server = null;
        Socket socket = null;
        server = new ServerSocket(properties.getPort());
        logger.warn("--------------服务开启, 监听端口:" + properties.getPort()+"--------------");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                properties.getPoolCore(),
                properties.getPoolMax(),
                properties.getPoolKeep(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(properties.getPoolQueueInit()),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        while (true) {
            socket = server.accept();
            pool.execute(new ServerConfig(socket));
        }
    }
}