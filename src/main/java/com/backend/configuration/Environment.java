package com.backend.configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class Environment {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static String getEnv(String key) {
        String k = dotenv.get(key, System.getenv(key));
        if (k == null) {
            throw new RuntimeException("Environment variable '" + key + "' not set!");
        }
        return k;
    }
}
