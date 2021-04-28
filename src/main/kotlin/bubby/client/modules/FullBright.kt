package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent

class FullBright: Module("FullBright", "I see you", -1, Category.Render) {
  private var old = 0f
  override fun onEnable() {
    old = mc.gameSettings.gammaSetting
    super.onEnable()
  }

  override fun onDisable() {
    super.onDisable()
    mc.gameSettings.gammaSetting = old
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      mc.gameSettings.gammaSetting = 69420f
    }
  }
}
