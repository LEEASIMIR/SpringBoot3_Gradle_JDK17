package com.springboot.template.util.external.tcp.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.FailoverClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
//@RequiredArgsConstructor
//@Configuration
public class TcpClientConfig {

    /**
     * 단일 TCP 요청 팩토리 생성
     * @author 이봉용
     * @date 25. 9. 10.
     */
//    @Bean
//    public AbstractClientConnectionFactory clientLocalhostFactory() {
//        return this.getConnectionFactory("localhost", 1234);
//    }

    /**
     * 주 서버, 예비 서버 TCP 요청 팩토리 생성
     * @author 이봉용
     * @date 25. 9. 10.
     */
//    @Bean(name = "failoverClientConnectionFactory")
//    public FailoverClientConnectionFactory failoverClientConnectionFactory() {
//        List<AbstractClientConnectionFactory> connectionFactories = new ArrayList<>();
////        connectionFactories.add(this.getConnectionFactory("localhost", 1234));
//        connectionFactories.add(this.getConnectionFactory("tcpbin.com", 4242));
//        log.info("connection count {}",  connectionFactories.size());
//        for(AbstractClientConnectionFactory factory : connectionFactories) {
//            log.info("connection info {}:{}", factory.getHost(), factory.getPort());
//        }
//        FailoverClientConnectionFactory failoverClientConnectionFactory = new FailoverClientConnectionFactory(connectionFactories);
//        failoverClientConnectionFactory.setRefreshSharedInterval(1000);
//
//        return failoverClientConnectionFactory;
//    }

    /**
     * TCP 팩토리 생성
     * @author 이봉용
     * @date 25. 9. 10.
     */
//    private TcpNetClientConnectionFactory getConnectionFactory(String host, int port) {
//        // TCP 클라이언트를 위한 연결 팩토리 생성 (서버 주소, 포트)
//        TcpNetClientConnectionFactory factory = new TcpNetClientConnectionFactory(host, port);
//        factory.setSingleUse(false); // 연결 재사용, true 일 경우 계속 연결을 끊고 보낼때 재연결
//        factory.setConnectTimeout(5000);//TCP 서버와 연결 타임아웃
//        factory.setSoTimeout(10000);//TCP 서버와 연결 후 통신 타임아웃
//        return factory;
//    }
}
