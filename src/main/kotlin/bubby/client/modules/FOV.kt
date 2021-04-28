package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent

class FOV: Module("FOV", "fov", -1, Category.Render) {
  private var oldfov = mc.gameSettings.fovSetting
  private var fov = Setting<Int>("FOV fov", this).withValue(130).withMin(50).withMax(180).add()

  override fun onDisable() {
    super.onDisable()
    mc.gameSettings.fovSetting = oldfov
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      mc.gameSettings.fovSetting = fov.value.toFloat()
    }
  }
}


