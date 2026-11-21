package com.github.davishi25.statcheck.event.impl

import net.minecraftforge.fml.common.eventhandler.Event

/**
 * This event is fired when the player fully loads into the world.
 * mc.thePlayer and mc.theWorld are non-null.
 */
class UserJoinEvent(): Event() {}