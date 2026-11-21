package com.github.davishi25.statcheck.event.impl

import net.minecraftforge.fml.common.eventhandler.Event

/**
 * This event is fired when a Hypixel minigame is detected to start
 * may fire a couple ticks after, as we need the tab list to finish updating
 */
class GameStartEvent(val gametype: String?, val mode: String?): Event() {}