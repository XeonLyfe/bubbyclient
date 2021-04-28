package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import net.minecraft.init.Items

class FastUse: Module("FastUse", "use fast", -1, Category.Player) {
  private var everything = Setting<Boolean>("FastUse Everything", this).withValue(false).add()
  private var exp = Setting<Boolean>("FastUse EXP", this).withValue(false).add()
  private var crystals = Setting<Boolean>("FastUse Crystals", this).withValue(false).add()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      val item = mc.player.heldItemMainhand.item
      if((everything.value)
      || (item == Items.EXPERIENCE_BOTTLE && exp.value)
      || (item == Items.END_CRYSTAL && crystals.value)) {
        imc.setRightClickDelayTimer(0)
      }
    }
  }
}
