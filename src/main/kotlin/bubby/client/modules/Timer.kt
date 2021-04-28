package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent

class Timer: Module("Timer", "zooooom", -1, Category.World) {
  private var speed = Setting<Float>("Timer Speed", this).withValue(4f).withMin(0.1f).withMax(50f).add()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      imc.timer.setTickLen((1000f / 20f) / speed.value)
    }
  }

  override fun onDisable() {
    super.onDisable()
    imc.timer.setTickLen(1000f / 20f)
  }
}
