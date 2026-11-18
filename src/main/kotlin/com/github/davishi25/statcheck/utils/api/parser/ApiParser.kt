package com.github.davishi25.statcheck.utils.api.parser

import com.github.davishi25.statcheck.utils.api.playerdata.StatsData

interface ApiParser {
    fun getStatLine(statsData: StatsData?): String
    fun getVitalStatLine(statsData: StatsData?): String
}
