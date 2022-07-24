package com.bazinga.eg.userservice.filter;

import org.springframework.util.Assert;

import java.util.Objects;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = userContext.get();

        if (Objects.isNull(context)) {
            context = createEmptyContext();

            userContext.set(context);
        }

        return context;
    }

    public static void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

    public static UserContext createEmptyContext() {
        return new UserContext();
    }
}
