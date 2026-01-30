package dev.furq.vinyls.registry

import dev.furq.vinyls.Vinyls
import net.minecraft.util.Identifier

object MusicDiscManager {
    data class DiscInfo(
        val soundId: Identifier,
        val lengthInSeconds: Float,
        val comparatorOutput: Int,
        val range: Float? = null,
        val description: String? = null
    )

    private val discs = mutableMapOf<Identifier, DiscInfo>()

    fun registerDisc(info: DiscInfo) {
        val isNew = !discs.containsKey(info.soundId)
        discs[info.soundId] = info
        if (isNew) Vinyls.LOGGER.info("Registered disc: {}", info.soundId)
    }

    fun getDiscs(): List<DiscInfo> = discs.values.toList()

    fun soundIdToSongId(soundId: Identifier): Identifier {
        val path = "music_disc_" + soundId.path.replace("/", "_").replace(".", "_")
        return Identifier.of("vinyls", path)
    }
}
