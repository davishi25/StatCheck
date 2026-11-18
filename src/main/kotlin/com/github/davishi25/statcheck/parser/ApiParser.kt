package com.github.davishi25.statcheck.parser

import com.github.davishi25.statcheck.playerdata.StatsData

interface ApiParser {
    fun getStatLine(statsData: StatsData?): String
    fun getVitalStatLine(statsData: StatsData?): String
}
