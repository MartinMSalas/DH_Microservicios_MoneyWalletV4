package com.mmstechnology.dmw.wallet_service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmstechnology.dmw.wallet_service.model.Alias;
import com.mmstechnology.dmw.wallet_service.service.IAliasGeneratorService;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.List;

public final class AliasGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static IAliasGeneratorService aliasGeneratorService;
    private static final List<String> WORDS = loadWordsFromFile();

    // Private constructor prevents instantiation.
    private AliasGenerator() { }

    /**
     * Loads the word list from the JSON file located in resources/json/wordList.json.
     *
     * @return List of words to use for alias generation.
     */
    private static List<String> loadWordsFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = AliasGenerator.class.getResourceAsStream("/json/wordList.json")) {
            if (is == null) {
                throw new RuntimeException("wordList.json not found in /json folder on the classpath.");
            }
            return mapper.readValue(is, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to load wordList.json", e);
        }
    }

    /**
     * Sets the alias generator service dependency.
     * This must be called (for example, in a Spring initializer) before any alias generation is performed.
     *
     * @param service an instance of IAliasGeneratorService.
     */
    public static void setAliasGeneratorService(IAliasGeneratorService service) {
        aliasGeneratorService = service;
    }

    /**
     * Generates a unique alias for the given walletId. The alias is composed of three randomly chosen words
     * (all distinct) separated by a dot.
     *
     * @param walletId the associated wallet identifier.
     * @return the generated unique alias.
     * @throws RuntimeException if unable to generate a unique alias after the maximum number of attempts.
     */
    public static String generateAlias(String walletId) {
        final int MAX_ATTEMPTS = 100;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            String alias = randomAlias();
            if (!exists(alias)) {
                Alias newAlias = Alias.builder()
                        .alias(alias)
                        .walletId(walletId)
                        .build();
                return aliasGeneratorService.saveAlias(newAlias);
            }
            attempts++;
        }
        throw new RuntimeException("Unable to generate unique alias after " + MAX_ATTEMPTS + " attempts");
    }

    /**
     * Picks three distinct random words from the list and joins them with dots.
     *
     * @return A string in the format word1.word2.word3.
     */
    private static String randomAlias() {
        int index1 = SECURE_RANDOM.nextInt(WORDS.size());

        int index2;
        do {
            index2 = SECURE_RANDOM.nextInt(WORDS.size());
        } while (index2 == index1);

        int index3;
        do {
            index3 = SECURE_RANDOM.nextInt(WORDS.size());
        } while (index3 == index1 || index3 == index2);

        String word1 = WORDS.get(index1);
        String word2 = WORDS.get(index2);
        String word3 = WORDS.get(index3);

        return word1 + "." + word2 + "." + word3;
    }

    /**
     * Checks if the provided alias already exists.
     *
     * @param alias the alias to check.
     * @return true if it exists, false otherwise.
     */
    public static boolean exists(String alias) {
        return aliasGeneratorService.exists(alias);
    }
}
