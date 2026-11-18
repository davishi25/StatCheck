package com.github.davishi25.statcheck.api.playerdata

data class PlayerData(
    val displayname: String,
    val newPackageRank: String?,
    val monthlyPackageRank: String?,
    val monthlyRankColor: String?,
    val rankPlusColor: String?,
    val stats: StatsData?
)
