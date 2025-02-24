package com.mmstechnology.dmw.wallet_service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmstechnology.dmw.wallet_service.model.Alias;
import com.mmstechnology.dmw.wallet_service.service.IAliasGeneratorService;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.List;

public class AliasGenerator {

    private final SecureRandom secureRandom = new SecureRandom();
    private final IAliasGeneratorService aliasGeneratorService;
    private final List<String> words;

    public AliasGenerator(IAliasGeneratorService aliasGeneratorService) {
        this.aliasGeneratorService = aliasGeneratorService;
        this.words = loadWordsFromFile();
    }

    /**
     * Loads the word list from the JSON file located in resources/json/wordList.json.
     *
     * @return List of words to use for alias generation.
     */
    private List<String> loadWordsFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/json/wordList.json")) {
            if (is == null) {
                throw new RuntimeException("wordList.json not found in /json folder on the classpath.");
            }
            return mapper.readValue(is, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to load wordList.json", e);
        }
    }

    /**
     * Generates a unique alias for the given walletId. The alias is composed of three randomly
     * chosen words separated by a dot.
     *
     * @param walletId the associated wallet identifier.
     * @return the generated unique alias.
     * @throws RuntimeException if unable to generate a unique alias after the maximum number of attempts.
     */
    public String generateAlias(String walletId) {
        final int MAX_ATTEMPTS = 100;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            String alias = randomAlias();
            if (!exists(alias)) {
                Alias newAlias = Alias.builder().alias(alias).walletId(walletId).build();
                return aliasGeneratorService.saveAlias(newAlias);
            }
            attempts++;
        }
        throw new RuntimeException("Unable to generate unique alias after " + MAX_ATTEMPTS + " attempts");
    }

    /**
     * Picks three random words from the list and joins them with dots.
     *
     * @return A string in the format word1.word2.word3.
     */
    private String randomAlias() {
        int index1 = secureRandom.nextInt(words.size());

        int index2;
        do {
            index2 = secureRandom.nextInt(words.size());
        } while (index2 == index1);

        int index3;
        do {
            index3 = secureRandom.nextInt(words.size());
        } while (index3 == index1 || index3 == index2);

        String word1 = words.get(index1);
        String word2 = words.get(index2);
        String word3 = words.get(index3);

        return word1 + "." + word2 + "." + word3;
    }


    /**
     * Checks if the provided alias already exists.
     *
     * @param alias the alias to check.
     * @return true if it exists, false otherwise.
     */
    public boolean exists(String alias) {
        return aliasGeneratorService.exists(alias);
    }
}
