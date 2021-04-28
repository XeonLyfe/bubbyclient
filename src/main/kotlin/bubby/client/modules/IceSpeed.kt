package bubby.client.modules

import bubby.api.event.Event
import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import net.minecraft.init.Blocks


class IceSpeed : Module("IceSpeed", "changes speed on ice", -1, Category.Movement) {

  private var speed = Setting<Float>("IceSpeed Speed", this).withValue(0.7f).withMin(0.3f).withMax(0.7f).add()

  @Suppress("DEPRECATION")
  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      Blocks.FROSTED_ICE.slipperiness = speed.value
      Blocks.PACKED_ICE.slipperiness = Blocks.FROSTED_ICE.slipperiness
      Blocks.ICE.slipperiness = Blocks.PACKED_ICE.slipperiness
    }
  }

  @Suppress("DEPRECATION")
  override fun onDisable() {
    super.onDisable()
    Blocks.FROSTED_ICE.slipperiness = 0.97f
    Blocks.PACKED_ICE.slipperiness = Blocks.FROSTED_ICE.slipperiness
    Blocks.ICE.slipperiness = Blocks.PACKED_ICE.slipperiness
  }
}
