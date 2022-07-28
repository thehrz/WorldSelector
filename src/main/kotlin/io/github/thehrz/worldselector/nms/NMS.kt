package io.github.thehrz.worldselector.nms

import io.netty.buffer.ByteBuf
import org.bukkit.World
import org.bukkit.entity.Player
import taboolib.module.nms.nmsProxy

interface NMS {
    fun sendXaeroWorldMapData(player: Player, world: World)

    fun sendVoxelMapData(player: Player, world: World)

    fun sendData(channel: String, minecraftKey: String, player: Player, buf: ByteBuf)

    companion object {
        operator fun invoke(): NMS {
            return nmsProxy()
        }
    }
}