package io.github.thehrz.worldselector.nms

import org.bukkit.World
import org.bukkit.entity.Player
import taboolib.module.nms.nmsProxy

interface NMS {
    fun sendXaeroWorldMapData(player: Player, world: World)

    fun sendVoxelMapData(player: Player, world: World)

    fun sendJourneyMapData(player: Player, world: World)

    companion object {
        operator fun invoke(): NMS {
            return nmsProxy()
        }
    }
}