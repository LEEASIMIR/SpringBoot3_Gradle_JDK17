package com.springboot.template.util.external.tcp.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ServerInputChannel {
    @Bean
    public MessageChannel serverInChannel() {
        return new DirectChannel();
    }
}
