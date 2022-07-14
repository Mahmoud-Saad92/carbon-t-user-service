package com.bazinga.eg.userservice.service.client;

import com.bazinga.eg.userservice.dto.ContactDto;

public interface Client {

    ContactDto createContact(ContactDto dto);
}
