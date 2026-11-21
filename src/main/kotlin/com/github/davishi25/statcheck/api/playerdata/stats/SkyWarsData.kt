package com.github.davishi25.statcheck.api.playerdata.stats

data class SkyWarsData(
    val wins: Int?,
    val losses: Int?,
    val kills: Int?,
    val deaths: Int?,
    val levelFormattedWithBrackets: String?
)