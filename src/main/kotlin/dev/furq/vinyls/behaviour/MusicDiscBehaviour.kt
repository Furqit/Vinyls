package dev.furq.vinyls.behaviour

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import de.tomalbrc.filament.api.behaviour.ItemBehaviour
import dev.furq.vinyls.registry.MusicDiscManager
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.JukeboxPlayableComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
//? if >1.21.1 {
import net.minecraft.registry.entry.LazyRegistryEntryReference
import net.minecraft.util.ActionResult
//?} else {
/*import net.minecraft.registry.RegistryPair
import net.minecraft.util.TypedActionResult*/
//?}
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.world.World

class MusicDiscBehaviour(private val config: Config) : ItemBehaviour<MusicDiscBehaviour.Config> {

    init {
        parseSoundId(config.soundEvent)?.let { soundId ->
            MusicDiscManager.registerDisc(
                MusicDiscManager.DiscInfo(
                    soundId = soundId,
                    lengthInSeconds = config.lengthInSeconds,
                    comparatorOutput = config.comparatorOutput,
                    range = config.range,
                    description = config.description?.let {
                        if (it.isJsonPrimitive) it.asString else it.toString()
                    }
                )
            )
        }
    }

    override fun getConfig() = config

    //? if >1.21.1 {
    override fun use(item: Item, world: World, user: PlayerEntity, hand: Hand): ActionResult {
    //?} else {
    /*override fun use(item: Item, world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {*/
    //?}
        val itemStack = user.getStackInHand(hand)

        if (!itemStack.contains(DataComponentTypes.JUKEBOX_PLAYABLE)) {
            injectComponent(itemStack)
        }

        //? if >1.21.1 {
        return ActionResult.PASS
        //?} else {
        /*return TypedActionResult.pass(itemStack)*/
        //?}
    }

    private fun injectComponent(itemStack: ItemStack) {
        val soundId = parseSoundId(config.soundEvent) ?: return
        val songKey = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, MusicDiscManager.soundIdToSongId(soundId))
        val component = JukeboxPlayableComponent(/*? if >1.21.1 {*/ LazyRegistryEntryReference(songKey) /*?} else {*/ /*RegistryPair(songKey), true*/ /*?}*/)
        itemStack.set(DataComponentTypes.JUKEBOX_PLAYABLE, component)
    }

    private fun parseSoundId(element: JsonElement?): Identifier? = when {
        element == null -> null
        element.isJsonPrimitive && element.asJsonPrimitive.isString -> Identifier.of(element.asString)
        element.isJsonObject -> element.asJsonObject.get("sound_id")?.asString?.let { Identifier.of(it) }
        else -> null
    }

    class Config {
        @SerializedName("sound_event")
        var soundEvent: JsonElement? = null
        var description: JsonElement? = null

        @SerializedName("length_in_seconds")
        var lengthInSeconds: Float = 0f

        @SerializedName("comparator_output")
        var comparatorOutput: Int = 0
        var range: Float? = null
    }
}
