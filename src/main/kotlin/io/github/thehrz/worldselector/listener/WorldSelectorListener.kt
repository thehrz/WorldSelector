package io.github.thehrz.worldselector.listener

import io.github.thehrz.worldselector.nms.NMS
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.messaging.PluginMessageListener
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.BukkitPlugin

object WorldSelectorListener {
    @Awake(LifeCycle.ENABLE)
    fun register() {
        Bukkit.getMessenger().registerIncomingPluginChannel(BukkitPlugin.getInstance(), if (MinecraftVersion.major >= 5) "worldinfo:world_id" else "world_id", VoxelMap)
    }

    @SubscribeEvent
    fun onPlayerChangedWorld(e: PlayerChangedWorldEvent) {
        submit(async = true) {
            NMS().sendXaeroWorldMapData(e.player, e.player.world)
        }
    }

    @SubscribeEvent
    fun onPlayerJoin(e: PlayerJoinEvent) {
        submit(async = true) {
            NMS().sendXaeroWorldMapData(e.player, e.player.world)
        }
    }

    object VoxelMap : PluginMessageListener {
        override fun onPluginMessageReceived(channel: String, player: Player, data: ByteArray) {
            NMS().sendVoxelMapData(
                player,
                player.world
            )
        }
    }
}