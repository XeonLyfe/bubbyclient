package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType

class AutoTotem: Module("AutoTotem", "Puts a totem in your offhand", -1, Category.Combat) {
  private var atMode = arrayOf("Force", "Health")
  private var mode = Setting<String>("AutoTotem Mode", this).withValue("Force").withValues(atMode).add()
  private var health = Setting<Double>("AutoTotem Health", this).withValue(10.0).withMin(0.0).withMax(20.0).add()


  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(mc.player.heldItemOffhand.item == Items.TOTEM_OF_UNDYING)
        return
      else if(mode.value == "Health"
      && mc.player.health + mc.player.absorptionAmount > health.value
      && mc.player.heldItemOffhand.item != Items.AIR)
        return
      else
        swapToTotem()
    }
  }

  private fun swapToTotem() {
    var slott = -1

    for(i in 0 until 44) {
      if(mc.player.inventory.getStackInSlot(i).item == Items.TOTEM_OF_UNDYING) {
        slott = i
        break
      }
    }
    if(slott != -1) {
      val slot = if(slott < 9) slott + 36 else slott
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player)
      mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player)
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player)
    }
  }
}
