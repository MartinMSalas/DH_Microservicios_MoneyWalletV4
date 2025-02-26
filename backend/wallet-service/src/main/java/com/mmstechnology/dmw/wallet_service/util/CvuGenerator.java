package com.mmstechnology.dmw.wallet_service.util;

import com.mmstechnology.dmw.wallet_service.model.Cvu;
import com.mmstechnology.dmw.wallet_service.service.ICvuGeneratorService;
import lombok.Setter;

import java.security.SecureRandom;


public final class CvuGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Static field to hold the service instance
    private static ICvuGeneratorService cvuGeneratorService;

    // Private constructor to prevent instantiation
    private CvuGenerator() { }

    /**
     * Set the CVU generator service.
     * This method must be called before using generateCvu().
     *
     * @param service the service instance
     */
    public static void setCvuGeneratorService(ICvuGeneratorService service) {
        cvuGeneratorService = service;
    }

    /**
     * Generates a unique 22-digit CVU number for the given wallet.
     * Uses SecureRandom for better randomization quality.
     *
     * @param walletId the wallet identifier associated with the CVU
     * @return A unique 22-digit CVU number after saving it via the service
     * @throws RuntimeException if unable to generate a unique CVU after maximum attempts
     */
    public static String generateCvu(String walletId) {
        final int MAX_ATTEMPTS = 100;
        int attempts = 0;
        final int CVU_LENGTH = 22;

        while (attempts < MAX_ATTEMPTS) {
            StringBuilder cvuBuilder = new StringBuilder(CVU_LENGTH);
            for (int i = 0; i < CVU_LENGTH; i++) {
                cvuBuilder.append(SECURE_RANDOM.nextInt(10));
            }
            String cvu = cvuBuilder.toString();
            if (!exists(cvu)) {
                Cvu newCvu = Cvu.builder()
                        .cvu(cvu)
                        .walletId(walletId)
                        .build();
                return cvuGeneratorService.saveCvu(newCvu);
            }
            attempts++;
        }
        throw new RuntimeException("Unable to generate unique CVU after " + MAX_ATTEMPTS + " attempts");
    }

    /**
     * Checks if a given CVU already exists.
     *
     * @param cvu the CVU number to check
     * @return true if the CVU exists, false otherwise
     */
    public static boolean exists(String cvu) {
        return cvuGeneratorService.exists(cvu);
    }
}
