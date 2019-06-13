package com.nautilus.configuration.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
public class RedisSessionConfig implements ApplicationListener {

    @Value("${nautilus.discovery-service.security.time-life-redis-session}")
    private Integer timeSession;

    @Autowired
    private RedisOperationsSessionRepository redisOperation;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            redisOperation.setDefaultMaxInactiveInterval(timeSession);
        }
    }
}
