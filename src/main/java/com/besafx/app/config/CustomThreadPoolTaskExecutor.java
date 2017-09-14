package com.besafx.app.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long serialVersionUID = 1L;

    @Override
    public void execute(final Runnable r) {
        final Authentication a = SecurityContextHolder.getContext().getAuthentication();
        super.execute(() -> {
            try {
                SecurityContext ctx = SecurityContextHolder.createEmptyContext();
                ctx.setAuthentication(a);
                SecurityContextHolder.setContext(ctx);
                r.run();
            } finally {
                SecurityContextHolder.clearContext();
            }
        });
    }
}
