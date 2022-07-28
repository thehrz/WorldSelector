package io.github.thehrz.worldselector

import org.bukkit.Bukkit
import org.bukkit.World
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.getMap

object WorldSelector : Plugin() {
    @Config(value = "settings.yml", autoReload = true)
    lateinit var settings: Configuration
        private set

    override fun onActive() {
        val worldNamesMap = settings.getMap<String, String>("WorldNames").toMutableMap()

        Bukkit.getWorlds().forEach { world ->
            if (!worldNamesMap.containsKey(world.name)) {
                worldNamesMap[world.name] = world.name
            }
        }

        settings["WorldNames"] = worldNamesMap

        settings.saveToFile()
    }
}

fun World.id() = WorldSelector.settings["WorldNames.${this.name}", this.name].toString()