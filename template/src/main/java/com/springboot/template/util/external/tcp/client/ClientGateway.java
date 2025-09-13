package com.springboot.template.util.external.tcp.client;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.stereotype.Component;

@MessagingGateway
public interface ClientGateway {
    @Gateway(requestChannel = "tcpRequestChannel")
    String send(String message);
}
