package com.mmstechnology.dmw.api_keycloak_server.util;

import java.security.SecureRandom;

public class CvuGenerator {

    private static final int CVU_LENGTH = 22;
    private static final SecureRandom random = new SecureRandom();

    public static String generateCvu() {
        StringBuilder cvu = new StringBuilder(CVU_LENGTH);
        for (int i = 0; i < CVU_LENGTH; i++) {
            cvu.append(random.nextInt(10));
        }
        return cvu.toString();
    }
}
