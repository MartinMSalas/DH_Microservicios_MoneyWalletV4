package com.mmstechnology.dmw.api_keycloak_server.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class AliasGenerator {

    private static final String ALIAS_FILE_PATH = "src/main/resources/alias.txt";
    private static final Random random = new Random();

    public static String generateAlias() {
        try {
            List<String> words = Files.readAllLines(Paths.get(ALIAS_FILE_PATH));
            if (words.size() < 3) {
                throw new IllegalStateException("Alias file must contain at least 3 words.");
            }
            String word1 = words.get(random.nextInt(words.size()));
            String word2 = words.get(random.nextInt(words.size()));
            String word3 = words.get(random.nextInt(words.size()));
            return word1 + "." + word2 + "." + word3;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read alias file.", e);
        }
    }
}
