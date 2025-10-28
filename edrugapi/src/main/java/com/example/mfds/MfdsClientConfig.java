package com.example.mfds;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class MfdsClientConfig {

    @Bean
    public WebClient mfdsWebClient(MfdsProperties props) {
        int connectMs = props.connectTimeoutMs() != null ? props.connectTimeoutMs() : 5000;
        int readMs = props.readTimeoutMs() != null ? props.readTimeoutMs() : 5000;

        HttpClient http = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectMs)
                .responseTimeout(Duration.ofMillis(readMs))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(readMs)));

        return WebClient.builder()
                .baseUrl(props.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(http))
                .filter(logUrl())
                .build();
    }

    private ExchangeFilterFunction logUrl() {
        return (request, next) -> {
            System.out.println(">> " + request.method() + " " + request.url());
            return next.exchange(request);
        };
    }
}
