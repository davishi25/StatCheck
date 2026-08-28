package com.github.davishi25.statcheck;

import com.github.davishi25.statcheck.parser.ApiParser;
import com.github.davishi25.statcheck.parser.BedwarsParser;
import com.github.davishi25.statcheck.parser.BridgeDuelsParser;
import com.github.davishi25.statcheck.parser.DuelsParser;

import java.util.HashMap;
import java.util.Map;

public final class ApiParserRegistry {
    public static final Map<String, ApiParser> parsers = new HashMap();
    static {
        register(new BedwarsParser(),"bw","bedwars");
        register(new DuelsParser(),"d","duels");
        register(new BridgeDuelsParser(),"b","bridge");
    }
    private static void register(ApiParser parser, String... aliases) {
        for(String alias : aliases)
            parsers.put(alias,parser);
    }
}
