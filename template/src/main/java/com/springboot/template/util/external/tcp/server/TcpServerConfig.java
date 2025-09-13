package com.springboot.template.util.external.tcp.server;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

@Slf4j
//@Configuration
//@RequiredArgsConstructor
public class TcpServerConfig {

//    private final ServerInputChannel serverInChannel;
//
//    @Bean
//    public AbstractServerConnectionFactory serverConnectionFactory() {
//        // TCP 서버를 위한 연결 팩토리 생성 (포트 1234에서 수신 대기)
//        TcpNetServerConnectionFactory factory = new TcpNetServerConnectionFactory(1234);
//        factory.setSingleUse(false); // 연결 재사용 설정
//        return factory;
//    }
//
//    @Bean
//    public TcpInboundGateway tcpInboundGateway(AbstractConnectionFactory serverConnectionFactory) {
//        // TCP 인바운드 게이트웨이: 들어오는 TCP 메시지를 처리하는 컴포넌트
//        TcpInboundGateway gateway = new TcpInboundGateway();
//        gateway.setConnectionFactory(serverConnectionFactory);
//        gateway.setRequestChannel(serverInChannel.serverInChannel()); // 수신된 메시지를 보낼 채널 설정
//        return gateway;
//    }
}
