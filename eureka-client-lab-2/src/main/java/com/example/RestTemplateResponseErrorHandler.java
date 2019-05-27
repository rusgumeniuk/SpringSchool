package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    private List acceptableStatus;

    public  RestTemplateResponseErrorHandler(@Value("${good-status}") String goodStatus) {

        acceptableStatus = Arrays.stream(goodStatus.split(","))
                .map(HttpStatus::valueOf)
                .collect(Collectors.toList()) ;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !acceptableStatus.contains(response.getStatusCode());
    }
}

