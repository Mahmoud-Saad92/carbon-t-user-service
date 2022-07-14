package com.bazinga.eg.userservice.service.client;

import com.bazinga.eg.userservice.dto.ContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "contact-service")
public interface ContactFeignClientInterface extends Client {

    @Override
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/contact",
            consumes = "application/json"
    )
    ContactDto createContact(@RequestBody ContactDto dto);
}
