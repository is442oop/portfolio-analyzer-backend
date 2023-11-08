package com.backend.configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class Environment {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();

    public static String getEnv(String key) {
        String k = System.getenv(key);
        if (dotenv != null) {
            k = dotenv.get(key);
        }
        if (k == null) {
            throw new RuntimeException("Environment variable '" + key + "' not set!");
        }
        return k;
    }
}
