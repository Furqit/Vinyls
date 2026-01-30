package dev.furq.vinyls

import de.tomalbrc.filament.api.FilamentLoader
import de.tomalbrc.filament.api.registry.BehaviourRegistry
import dev.furq.vinyls.behaviour.MusicDiscBehaviour
import dev.furq.vinyls.registry.DiscsRegistry
import dev.furq.vinyls.sound.UserSoundManager
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

class Vinyls : ModInitializer {
    companion object {
        val LOGGER = LoggerFactory.getLogger("Vinyls")!!
    }

    override fun onInitialize() {
        BehaviourRegistry.registerBehaviour(
            Identifier.of("vinyls", "music_disc"),
            MusicDiscBehaviour::class.java
        )
        UserSoundManager.init()
        FilamentLoader.loadItems("vinyls")
        DiscsRegistry.init()
    }
}