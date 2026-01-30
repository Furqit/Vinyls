package dev.furq.vinyls.sound

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.furq.vinyls.Vinyls
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import java.nio.file.Files

object UserSoundManager {
    private val gson = Gson()
    private val soundData = mutableMapOf<Identifier, ByteArray>()

    fun init() {
        val soundsDir = FabricLoader.getInstance().configDir.resolve("vinyls/sounds")

        if (!Files.exists(soundsDir)) {
            Files.createDirectories(soundsDir)
            return
        }

        val soundsJson = JsonObject()

        Files.list(soundsDir)
            .filter { it.toString().endsWith(".ogg") }
            .forEach { path ->
                val name = path.fileName.toString().removeSuffix(".ogg")
                val sanitizedName = name.lowercase().replace(Regex("[^a-z0-9_./-]"), "_")

                runCatching {
                    val bytes = Files.readAllBytes(path)
                    soundData[Identifier.of("vinyls", "sounds/$sanitizedName.ogg")] = bytes

                    soundsJson.add(sanitizedName, JsonObject().apply {
                        add("sounds", JsonArray().apply { add("vinyls:$sanitizedName") })
                    })

                    Vinyls.LOGGER.info("Loaded sound: {}", sanitizedName)
                }.onFailure { e ->
                    Vinyls.LOGGER.error("Failed to load: {}", path.fileName, e)
                }
            }

        if (soundsJson.size() > 0) {
            soundData[Identifier.of("vinyls", "sounds.json")] = gson.toJson(soundsJson).toByteArray()
        }

        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register { builder ->
            soundData.forEach { (id, bytes) ->
                builder.addData("assets/${id.namespace}/${id.path}", bytes)
            }
        }
    }
}
