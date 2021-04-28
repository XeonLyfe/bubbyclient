package bubby.client.modules
import bubby.api.event.PogEvent
import bubby.api.module.*
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import net.minecraft.entity.item.EntityEnderCrystal
import bubby.client.utils.CrystalUtils
import bubby.client.utils.EntityUtils
import net.minecraft.init.Items

class Offhand: Module("Offhand", "Put item hand", -1, Category.Combat) {
  private val items = arrayOf("Totem", "Gap", "Crystal")

  private val primary = Setting<String>("Offhand Primary", this)
  .withValue("Gap")
  .withValues(items)
  .add()

  private val secondary = Setting<String>("Offhand Secondary", this)
  .withValue("Totem")
  .withValues(items)
  .add()

  private val swapHealth = Setting<Double>("Offhand Swap Health", this)
  .withValue(17.0)
  .withMin(0.0)
  .withMax(36.0)
  .add()
  
  // TODO: Fix crystal calcs I think
  private val lethalCheck = Setting<Boolean>("Offhand Lethal Crystal Swap", this)
  .withValue(false)
  //.add()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      val first = when(primary.value) {
        "Gap" -> Items.GOLDEN_APPLE
        "Crystal" -> Items.END_CRYSTAL
        else -> {
          Items.TOTEM_OF_UNDYING
        }
      }

      val second = when(secondary.value) {
        "Gap" -> Items.GOLDEN_APPLE
        "Crystal" -> Items.END_CRYSTAL
        else -> {
          Items.TOTEM_OF_UNDYING
        }
      }

      if(mc.player.health + mc.player.absorptionAmount > swapHealth.value && !checkLethal()) {
        if(!EntityUtils.changeOffhandSlotToItem(first)) {
          EntityUtils.changeOffhandSlotToItem(second)
        } else {}
      } else {
        EntityUtils.changeOffhandSlotToItem(second)
      }
    }
  }

  private fun checkLethal(): Boolean {
    if(lethalCheck.value) {
      mc.world.loadedEntityList
      .filter{it is EntityEnderCrystal}
      .forEach {
        if(CrystalUtils.calculateDamage(EntityUtils.getBetterBlockPos(it), mc.player) >= mc.player.health + mc.player.absorptionAmount) {
          return true
        }
      }
    }
    return false
  }
}
