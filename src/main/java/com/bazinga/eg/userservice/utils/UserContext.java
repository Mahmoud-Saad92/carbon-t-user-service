package com.bazinga.eg.userservice.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserContext {
    private String correlationId;
    private String authToken;
    private String userId;
    private String contactId;
}
