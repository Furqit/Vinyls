package dev.furq.vinyls.registry

import dev.furq.vinyls.Vinyls
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback
import net.minecraft.block.jukebox.JukeboxSong
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent
import net.minecraft.text.Text

object DiscsRegistry {
    fun init() {
        DynamicRegistrySetupCallback.EVENT.register { registryManager ->
            registryManager.getOptional(RegistryKeys.JUKEBOX_SONG).orElse(null)?.let { registry ->
                MusicDiscManager.getDiscs().forEach { disc ->
                    val songId = MusicDiscManager.soundIdToSongId(disc.soundId)

                    val soundEvent = if (disc.range != null) {
                        SoundEvent.of(disc.soundId, disc.range)
                    } else {
                        SoundEvent.of(disc.soundId)
                    }

                    val song = JukeboxSong(
                        RegistryEntry.of(soundEvent),
                        Text.literal(disc.description ?: "Music Disc"),
                        disc.lengthInSeconds,
                        disc.comparatorOutput
                    )
                    Registry.register(registry, songId, song)
                    Vinyls.LOGGER.info("Registered song: {}", songId)
                }
            }
        }
    }
}
