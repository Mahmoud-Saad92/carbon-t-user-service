package com.bazinga.eg.userservice.service.client;

import com.bazinga.eg.userservice.dto.ContactDto;

import java.util.Map;

@FunctionalInterface
public interface FunctionalClient<T, R, U> {

    ContactDto apply(T t, R r, Map<R, U> map);
}
