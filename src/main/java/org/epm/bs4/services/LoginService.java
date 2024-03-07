package org.epm.bs4.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.epm.bs4.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    private LoadingCache<String, Integer> loginsCache;
    public static final int UNSUCCESSFUL_LOGINS = 3;
    public static final int BLOCK_TIME = 5;

    public LoginService() {
        super();
        loginsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_TIME, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(final String key) {
                        return 0;
                    }
                });
    }

    public void loginFailed(final String key) {
        int loginsCounter;
        try {
            loginsCounter = loginsCache.get(key);
        } catch (final ExecutionException e) {
            loginsCounter = 0;
        }
        loginsCounter++;
        loginsCache.put(key, loginsCounter);
    }

    public boolean isBlocked(User user) {
        try {
            return loginsCache.get(user.getEmail()) >= UNSUCCESSFUL_LOGINS;
        } catch (final ExecutionException e) {
            return false;
        }
    }
}