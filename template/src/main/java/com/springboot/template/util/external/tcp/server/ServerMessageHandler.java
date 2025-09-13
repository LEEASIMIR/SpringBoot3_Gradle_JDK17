package com.springboot.template.util.external.tcp.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerMessageHandler {
    @ServiceActivator(inputChannel = "serverInChannel")
    public String messageHandler(String message) {
        log.info("서버에서 수신된 메시지: {}", message);
        // 받은 메시지를 그대로 반환 (Echo)
        return "저는서버에요 보낸 메세지 다시 보내드려요 : " + message;
    }
}
