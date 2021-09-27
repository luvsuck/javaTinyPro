package com.shjk.dtuDataGetter.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ServerConfig extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    private final Socket socket;

    public ServerConfig(Socket socket) {
        this.socket = socket;
    }

    // 获取spring容器管理的类，可以获取到sevrice的类
    //private EnvironmentService service = SpringUtil.getBean(EnvironmentServiceImpl.class);

    private String handle(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        int len = inputStream.read(bytes);
        StringBuilder request = new StringBuilder();
        request.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
        return request.toString();
    }

    @Override
    public void run() {
        BufferedWriter writer = null;
        try {
            // 设置连接超时9秒
            socket.setSoTimeout(9000);
            logger.warn("-------------------------------↓[ " + socket.getRemoteSocketAddress() + " ]连入↓-------------------------------\r\n");
            InputStream inputStream = socket.getInputStream();
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String result = null;
            try {
                result = handle(inputStream);
                logger.warn("-------------------------------收到数据-------------------------------\r\n" + result);
                logger.warn("-------------------------------↑ " + Thread.currentThread().getName() + " ↑-------------------------------");
//                writer.write(result);
//                writer.newLine();
//                writer.flush();
            } catch (IOException | IllegalArgumentException e) {
//                writer.write("error");
//                writer.newLine();
//                writer.flush();
//                logger.error("发生异常\r\n" + e.getMessage());
//                try {
//                    logger.warn("再次尝试...");
//                    result = handle(inputStream);
//                    writer.write(result);
//                    writer.newLine();
//                    writer.flush();
//                } catch (SocketTimeoutException ex) {
//                    logger.error("再次尝试，发生异常,连接关闭");
//                }
            }
        } catch (SocketException socketException) {
            socketException.printStackTrace();
            try {
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
