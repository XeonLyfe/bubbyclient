package bubby.client.modules


import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent


class Sprint: Module("Sprint", "zoooom", -1, Category.Movement) {

  @PogEvent
  fun onUpdate(event: TickEvent) {
    event.run {
      if (!mc.player.isSprinting) {
        mc.player.isSprinting = true
      }
    }
  }

  override fun onDisable() {
    super.onDisable()
    if (mc.player.isSprinting) {
      mc.player.isSprinting = false
    }
  }
}
