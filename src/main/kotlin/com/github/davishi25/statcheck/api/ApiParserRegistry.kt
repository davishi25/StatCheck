package com.github.davishi25.statcheck.api

import com.github.davishi25.statcheck.api.parser.*
import com.github.davishi25.statcheck.utils.location.LocationUtils

object ApiParserRegistry {
    private val parsers = mutableMapOf<String,ApiParser>()
    init {
        register(BedwarsParser,"bw","bedwars")
        register(DuelsParser,"d","duels")
        register(BridgeDuelsParser,"b","bridge")
        register(SkyWarsParser, "sw", "skywars")
    }
    fun register(parser: ApiParser, vararg aliases: String) {
        aliases.forEach { alias ->
            parsers[alias] = parser
        }
    }
    fun getParser(key: String): ApiParser? {
        if (parsers[key] is DuelsParser && LocationUtils.getDuelsGamemode() == "bridge") return BridgeDuelsParser
        return parsers[key]
    }
}
