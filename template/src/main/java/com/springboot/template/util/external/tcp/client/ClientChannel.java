package com.springboot.template.util.external.tcp.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ClientChannel {
    @Bean
    public MessageChannel tcpRequestChannel() {
        return new DirectChannel();
    }
}
