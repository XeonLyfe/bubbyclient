package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.MoveEvent
import bubby.client.events.ReadPacketEvent
import net.minecraft.network.play.server.SPacketTimeUpdate

class Ambiance: Module("Ambiance", "Forces the time/weather", -1, Category.World) {
  private var modes = arrayOf("Clear", "Raining")
  private var weather = Setting<Boolean>("Ambiance Weather", this).withValue(false).add()
  private var mode = Setting<String>("Ambiance Type", this).withValue("Clear").withValues(modes).add()
  private var rain = Setting<Float>("Ambiance RainLevel", this).withValue(0f).withMin(0f).withMax(2f).add()
  private var time = Setting<Long>("Ambiance Time", this).withValue(12500L).withMin(0L).withMax(24000L).add()

  @PogEvent
  fun onMove(event: MoveEvent) {
    event.run {
      mc.world.worldTime = time.value
      if(weather.value)
        if(mode.value == "Raining")
          mc.world.setRainStrength(rain.value)
      else mc.world.setRainStrength(0f)
    }
  }

  @PogEvent
  fun onPacketRead(event: ReadPacketEvent) {
    event.run {
      if(packet is SPacketTimeUpdate)
        isCancelled = true
    }
  }
}
