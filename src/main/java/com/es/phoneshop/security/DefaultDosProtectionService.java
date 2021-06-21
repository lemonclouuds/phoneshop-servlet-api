package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultDosProtectionService implements DosProtectionService {
    public static final long MAX_REQUESTS_PER_TIME = 20L;
    private Map<String, Long> countMap = new ConcurrentHashMap();

    {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> countMap.clear(), 0, 1, TimeUnit.MINUTES);
    }

    private  static class SingletonHelper {
        public static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.SingletonHelper.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > MAX_REQUESTS_PER_TIME) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
