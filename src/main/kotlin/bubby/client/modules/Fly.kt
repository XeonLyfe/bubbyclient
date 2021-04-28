package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent

class Fly: Module("Fly", "Allows creative flight in survival", -1, Category.Movement) {
  private var speed = Setting<Double>("Fly Speed", this).withValue(0.05).withMin(0.0).withMax(0.1).add()

  override fun onDisable() {
    super.onDisable()
    mc.player.capabilities.isFlying = false
    mc.player.capabilities.allowFlying = false
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      mc.player.capabilities.allowFlying = true
      mc.player.capabilities.flySpeed = speed.value.toFloat()
      mc.player.capabilities.isFlying = true
    }
  }
}
