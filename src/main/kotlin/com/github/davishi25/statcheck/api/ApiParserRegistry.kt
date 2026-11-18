package com.github.davishi25.statcheck.api

import com.github.davishi25.statcheck.api.parser.*

object ApiParserRegistry {
    val parsers = mutableMapOf<String,ApiParser>()
    init {
        register(BedwarsParser,"bw","bedwars")
        register(DuelsParser,"d","duels")
        register(BridgeDuelsParser,"b","bridge")
    }
    fun register(parser: ApiParser, vararg aliases: String) {
        aliases.forEach { alias ->
            parsers[alias] = parser
        }
    }
}
