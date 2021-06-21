package com.es.phoneshop.model.product;

import com.es.phoneshop.security.DefaultDosProtectionService;
import com.es.phoneshop.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DosProtectionServiceTest {
    private DosProtectionService dosProtectionService;

    @Before
    public void setup() {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Test
    public void testIsAllowed() {
        final long MAX_REQUESTS_PER_TIME = 20L;
        String ip = "123";

        long count = 0;
        while (count <= MAX_REQUESTS_PER_TIME) {
            assertTrue(dosProtectionService.isAllowed(ip));
            count++;
        }
        assertFalse(dosProtectionService.isAllowed(ip));
    }
}
