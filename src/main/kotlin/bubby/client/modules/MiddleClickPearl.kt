package bubby.client.modules
import bubby.api.chat.Chat
import bubby.api.event.PogEvent
import bubby.api.module.*
import bubby.client.events.MouseClickEvent
import bubby.client.utils.EntityUtils
import net.minecraft.init.Items
import net.minecraft.util.EnumHand

class MiddleClickPearl: Module("MiddleClickPearl", "Throws an ender pearl when you middle click", -1, Category.Misc) {

  private var pressCheck = false
  @PogEvent
  fun onMouseClick(event: MouseClickEvent) {
    event.run {
      if(mc.player == null || mc.world == null)
        return
      pressCheck = !pressCheck
      if(pressCheck && button == 2) {
        val oldSlot = EntityUtils.changeHotbarSlotToItem(Items.ENDER_PEARL)
        if(oldSlot == -1) {
          Chat.error("No ender pearl in hotbar")
          return
        }
        mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND)
        mc.player.inventory.currentItem = oldSlot
      }
    }
  }
}
