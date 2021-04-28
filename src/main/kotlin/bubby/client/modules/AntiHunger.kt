package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.mixin.interfaces.ICPacketPlayer
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.SendPacketEvent
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraft.network.play.client.CPacketPlayer

class AntiHunger: Module("AntiHunger", "Prevents hunger", -1, Category.Player) {
  @PogEvent
  fun onSendPacket(event: SendPacketEvent) {
    event.run {
      if(packet is CPacketPlayer) {
        val pack = packet as ICPacketPlayer

        if(mc.player.fallDistance > 0 || mc.playerController.isHittingBlock) pack.setOnGround(true)
        else pack.setOnGround(false)
      }
      else if(packet is CPacketEntityAction) {
        val pack = packet as CPacketEntityAction
        if(pack.action == CPacketEntityAction.Action.START_SPRINTING
        || pack.action == CPacketEntityAction.Action.STOP_SPRINTING)
          isCancelled = true
      }
    }
  }
}
