package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent

class AirJump: Module("AirJump", "jum pair", -1, Category.Movement) {
  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(mc.gameSettings.keyBindJump.isPressed)
        mc.player.jump()
    }
  }
}
