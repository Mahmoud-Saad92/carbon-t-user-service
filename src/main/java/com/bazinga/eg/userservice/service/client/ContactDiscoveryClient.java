package com.bazinga.eg.userservice.service.client;

import com.bazinga.eg.userservice.dto.ContactDto;
import com.bazinga.eg.userservice.exception.model.ClientRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Slf4j
@Component("discovery")
public class ContactDiscoveryClient implements Client {

    private final DiscoveryClient discoveryClient;

    @Autowired
    public ContactDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public ContactDto createContact(ContactDto dto) {
        try {
            log.info("Hello from discovery client");

            RestTemplate restTemplate = new RestTemplate();

            List<ServiceInstance> instances = discoveryClient.getInstances("contact-service");

            if (instances.isEmpty()) throw new RuntimeException("could not find any contact service instances");

            String serviceUri = String.format("%s/api/v1/contact", instances.get(0).getUri().toString());

            URI uri = new URI(serviceUri);

            return restTemplate.postForEntity(uri, dto, ContactDto.class).getBody();

        } catch (Exception e) {
            throw new ClientRequestException("remote calling to contact service failed", e);
        }
    }
}
