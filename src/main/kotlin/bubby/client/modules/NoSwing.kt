package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.SendPacketEvent
import net.minecraft.network.play.client.CPacketAnimation

class NoSwing: Module("NoSwing", "Prevents the swinging animation probably", -1, Category.Player) {
  @PogEvent
  fun onSendPacket(event: SendPacketEvent) {
    event.run {
      if(packet is CPacketAnimation)
        isCancelled = true
    }
  }
}
