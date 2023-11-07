package com.backend.configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class Environment {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static String getEnv(String key) {
        return dotenv.get(key);
    }
}
