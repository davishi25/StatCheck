package com.github.davishi25.statcheck.api.parser

import com.github.davishi25.statcheck.api.playerdata.StatsData

interface ApiParser {
    fun getStatLine(statsData: StatsData?): String
    fun getVitalStatLine(statsData: StatsData?): String
}
