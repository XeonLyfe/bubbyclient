package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.mixin.interfaces.IKeyBinding
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent

class AutoWalk: Module("AutoWalk", "Makes you walk automatically", -1, Category.Movement) {
  override fun onDisable() {
    super.onDisable()
    (mc.gameSettings.keyBindForward as IKeyBinding).setPressed(false)
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      (mc.gameSettings.keyBindForward as IKeyBinding).setPressed(true)
    }
  }
}
