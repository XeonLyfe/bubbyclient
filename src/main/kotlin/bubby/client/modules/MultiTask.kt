package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent
import net.minecraft.init.Items
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemFood
import net.minecraft.network.Packet
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.util.EnumHand

// TODO: make this work
class MultiTask: Module("MultiTask", " pvp pog", -1, Category.Misc) {
  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(mc.gameSettings.keyBindUseItem.isKeyDown && mc.player.activeHand == EnumHand.MAIN_HAND) {
        if(mc.player.heldItemOffhand.item !is ItemBlock && mc.player.heldItemOffhand.item !== Items.END_CRYSTAL) {
          if(mc.player.heldItemOffhand.item is ItemFood && mc.gameSettings.keyBindUseItem.isKeyDown && mc.gameSettings.keyBindAttack.isKeyDown) {
            mc.player.activeHand = EnumHand.OFF_HAND
            mc.player.swingArm(EnumHand.MAIN_HAND)
          }
        }
      }
      else {
        val jake = mc.player.rayTrace(6.0, mc.renderPartialTicks)
        mc.player.connection.sendPacket(CPacketPlayerTryUseItemOnBlock(jake!!.blockPos, jake.sideHit, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f) as Packet<*>)
      }
    }
  }
}
