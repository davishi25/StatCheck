package com.github.davishi25.statcheck.parser

import com.google.gson.JsonObject

interface ApiParser {
    fun getStatLine(playerObj: JsonObject): String
}
