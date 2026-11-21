package com.github.davishi25.statcheck.event.impl

import net.minecraftforge.fml.common.eventhandler.Event

/**
 * This event fires when LocationUtils is done updating its stored location.
 * useful when wanting to guarentee accurate location data.
 * fired AFTER [UserJoinEvent], so mc.thePlayer and mc.theWorld are non-null
 */
class LocrawUpdateEvent(): Event() {}