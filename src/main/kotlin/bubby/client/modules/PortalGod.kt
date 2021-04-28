package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.SendPacketEvent
import net.minecraft.network.play.client.CPacketConfirmTeleport

class PortalGod: Module("PortalGod", "Godmode through portals", -1, Category.Player) {
  @PogEvent
  fun onSendPacket(event: SendPacketEvent) {
    event.run {
      if(packet is CPacketConfirmTeleport) isCancelled = true
    }
  }
}
