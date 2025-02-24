package com.mmstechnology.dmw.wallet_service.util;

import com.mmstechnology.dmw.wallet_service.model.Cvu;
import com.mmstechnology.dmw.wallet_service.service.ICvuGeneratorService;
import com.mmstechnology.dmw.wallet_service.service.impl.CvuGeneratorServiceImpl;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class CvuGenerator {

    private  final SecureRandom SECURE_RANDOM = new SecureRandom();

    private  final ICvuGeneratorService cvuGeneratorService;

    public CvuGenerator(ICvuGeneratorService cvuGeneratorService) {
        this.cvuGeneratorService = cvuGeneratorService;
    }


    /**
     * Generates a unique 22-digit CVU number.
     * Uses SecureRandom for better randomization quality.
     *
     * @return A unique 22-digit CVU number
     * @throws RuntimeException if unable to generate a unique CVU after maximum attempts
     */
    public  String generateCvu(String walletId) {
        final int MAX_ATTEMPTS = 100;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            int CVU_LENGTH = 22;
            StringBuilder cvuBuilder = new StringBuilder(CVU_LENGTH);

            for (int i = 0; i < CVU_LENGTH; i++) {
                cvuBuilder.append(SECURE_RANDOM.nextInt(10));
            }

            String cvu = cvuBuilder.toString();
            if (!exists(cvu)) {
                Cvu newCvu = Cvu.builder().cvu(cvu).walletId(walletId).build();
                return cvuGeneratorService.saveCvu(newCvu);

            }
            attempts++;
        }

        throw new RuntimeException("Unable to generate unique CVU after " + MAX_ATTEMPTS + " attempts");
    }

    /**
     * Checks if a CVU number already exists.
     *
     * @param cvu The CVU number to check
     * @return true if the CVU exists, false otherwise
     */
    public  boolean exists(String cvu) {
        return cvuGeneratorService.exists(cvu);

    }
}