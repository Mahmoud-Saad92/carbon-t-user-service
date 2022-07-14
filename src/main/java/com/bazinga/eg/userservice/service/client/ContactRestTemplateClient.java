package com.bazinga.eg.userservice.service.client;

import com.bazinga.eg.userservice.dto.ContactDto;
import com.bazinga.eg.userservice.exception.model.ClientRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Component("rest")
public class ContactRestTemplateClient implements Client {

    private final RestTemplate restTemplate;

    @Autowired
    public ContactRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ContactDto createContact(ContactDto dto) {
        log.info("Hello from rest client");

        try {
            String serviceUri = String.format("http://%s/api/v1/contact", "contact-service");

            URI uri = new URI(serviceUri);

            return restTemplate.postForEntity(uri, dto, ContactDto.class).getBody();

        } catch (Exception e) {
            throw new ClientRequestException("remote calling to contact service failed", e);
        }
    }
}
