package com.github.davishi25.statcheck.utils.api.playerdata

import com.github.davishi25.statcheck.utils.api.playerdata.stats.BedwarsData
import com.github.davishi25.statcheck.utils.api.playerdata.stats.DuelsData
import com.github.davishi25.statcheck.utils.api.playerdata.stats.SkyWarsData
import com.google.gson.annotations.SerializedName

data class StatsData(
    @SerializedName("Bedwars") val bedwars: BedwarsData,
    @SerializedName("Duels") val duels: DuelsData,
    @SerializedName("SkyWars") val skyWars: SkyWarsData
)
