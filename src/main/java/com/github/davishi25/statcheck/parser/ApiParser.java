package com.github.davishi25.statcheck.parser;

import com.google.gson.JsonObject;

public interface ApiParser {
    String getStatLine(JsonObject playerObj);
}
