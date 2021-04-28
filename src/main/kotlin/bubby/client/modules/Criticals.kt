package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.SendPacketEvent
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketUseEntity

class Criticals: Module("Criticals", "Forces criticals with every hit", -1, Category.Combat) {
  @PogEvent
  fun onSendPacket(event: SendPacketEvent) {
    event.run {
      if(packet is CPacketUseEntity && (packet as CPacketUseEntity).action == CPacketUseEntity.Action.ATTACK) {
        mc.player.connection.sendPacket(CPacketPlayer.Position(mc.player.posX, mc.player.posY + .1f, mc.player.posZ, false))
        mc.player.connection.sendPacket(CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false))
      }
    }
  }
}
