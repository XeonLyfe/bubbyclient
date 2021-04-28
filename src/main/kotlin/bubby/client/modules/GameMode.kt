package bubby.client.modules
import bubby.api.event.PogEvent
import bubby.api.module.*
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import net.minecraft.world.GameType

class GameMode: Module("GameMode", "Forces your gamemode", -1, Category.Misc) {
  val modes = arrayOf("Creative", "Survival", "Adventure", "Spectator")
  val gamemode = Setting<String>("GameMode Mode", this)
  .withValue("Creative")
  .withValues(modes)
  .add()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      var gameType = GameType.SURVIVAL
      when(gamemode.value) {
        "Creative" -> gameType = GameType.CREATIVE
        "Adventure" -> gameType = GameType.ADVENTURE
        "Spectator" -> gameType = GameType.SPECTATOR
      }
      mc.player.setGameType(gameType)
    }
  }
}
