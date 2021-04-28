package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.MoveEvent

class NoClip: Module("NoClip", "Allows you to go through blocks sometimes", -1, Category.Player) {
  @PogEvent
  fun onMove(event: MoveEvent) {
    if(mc.player == null || mc.world == null) return

    event.run {
      mc.player.noClip = true
      mc.player.onGround = false
    }
  }

  override fun onDisable() {
    super.onDisable()
    mc.player.noClip = false
    mc.player.onGround = true
  }
}
