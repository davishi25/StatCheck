package com.github.davishi25.statcheck.event

import net.minecraftforge.fml.common.eventhandler.Event

class GameStartEvent(val gametype: String?, val mode: String?): Event() {}