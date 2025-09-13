package com.springboot.template.util.external.tcp.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.FailoverClientConnectionFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ClientGatewayConfig {

//    private final FailoverClientConnectionFactory failoverConnectionFactory;
    private final ClientChannel clientChannel;

    /**
     * tcpRequestChannel 감시 , 해당 팩토리를 사용해서 전송 및 응답가져옴
     * @author 이봉용
     * @date 25. 9. 10.
     */
//    @Bean
//    public TcpOutboundGateway tcpOutboundGateway() {
//        log.info("tcpRequestChannel");
//        // TCP 아웃바운드 게이트웨이: 외부 TCP 서버로 메시지를 보내고 응답을 받음
//        TcpOutboundGateway gateway = new TcpOutboundGateway();
//        gateway.setConnectionFactory(failoverConnectionFactory);
//        return gateway;
//    }
}
