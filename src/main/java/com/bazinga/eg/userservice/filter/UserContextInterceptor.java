package com.bazinga.eg.userservice.filter;

import com.bazinga.eg.userservice.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
public class UserContextInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();

        headers.add(Constants.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        headers.add(Constants.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());

        log.debug("UserContextInterceptor, header contains correlationId key: {}", request.getHeaders().containsKey(Constants.CORRELATION_ID));
        log.debug("UserContextInterceptor, header contains authToken key: {}", request.getHeaders().containsKey(Constants.AUTH_TOKEN));

        return execution.execute(request, body);
    }
}
