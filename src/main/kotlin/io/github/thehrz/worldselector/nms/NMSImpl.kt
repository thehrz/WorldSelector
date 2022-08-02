package io.github.thehrz.worldselector.nms

import io.github.thehrz.worldselector.id
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketDataSerializer
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload
import net.minecraft.resources.MinecraftKey
import org.bukkit.World
import org.bukkit.entity.Player
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.sendPacket

class NMSImpl : NMS {
    override fun sendXaeroWorldMapData(player: Player, world: World) {
        val id = world.id().toByteArray()

        val buf = Unpooled
            .buffer(id.size + 1)
            .writeByte(0)
            .writeBytes(id)

        sendData(
            player,
            buf,
            Type.LOW to "xaeroworldmap:main",
            Type.NMS to "xaeroworldmap:main",
            Type.UNIVERSAL to "xaeroworldmap:main"
        )
    }

    override fun sendVoxelMapData(player: Player, world: World) {
        val data = world.id().toByteArray()
        val buf = Unpooled
            .buffer(data.size + 2)
            .writeByte(0)
            .writeByte(data.size)
            .writeBytes(data)

        sendData(
            player,
            buf,
            Type.LOW to "world_id",
            Type.NMS to "worldinfo:world_id",
            Type.UNIVERSAL to "worldinfo:world_id"
        )
    }

    override fun sendJourneyMapData(player: Player, world: World) {
        val data = world.id().toByteArray()
        val buf = Unpooled
            .buffer(data.size + 2)
            .writeByte(0)
            .writeByte(data.size)
            .writeBytes(data)

        sendData(player, buf, Type.LOW to "world_info")
    }

    private fun sendData(player: Player, buf: ByteBuf, vararg channels: Pair<Type, String>) {
        val channel = channels.find { it.first.equalsCurrentVersion() }?.second ?: return

        when (MinecraftVersion.major) {
            in 5..8 -> player.sendPacket(
                net.minecraft.server.v1_13_R2.PacketPlayOutCustomPayload(
                    net.minecraft.server.v1_13_R2.MinecraftKey(channel),
                    net.minecraft.server.v1_13_R2.PacketDataSerializer(buf)
                )
            )
            in 9..11 -> player.sendPacket(
                PacketPlayOutCustomPayload(
                    MinecraftKey(channel),
                    PacketDataSerializer(buf)
                )
            )
            else -> player.sendPacket(
                net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload(
                    channel,
                    net.minecraft.server.v1_12_R1.PacketDataSerializer(buf)
                )
            )
        }
    }

    private enum class Type {
        UNIVERSAL, NMS, LOW;

        fun equalsCurrentVersion() = this == when (MinecraftVersion.major) {
            in 5..8 -> NMS
            in 9..11 -> UNIVERSAL
            else -> LOW
        }
    }
}