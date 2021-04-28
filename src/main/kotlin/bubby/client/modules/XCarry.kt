package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.SendPacketEvent
import net.minecraft.network.play.client.CPacketCloseWindow

class XCarry: Module("XCarry", "Allows you to carry items in your crafting slots", -1, Category.Misc) {
  @PogEvent
  fun onPacket(event: SendPacketEvent) {
    event.run {
      if(packet is CPacketCloseWindow) isCancelled = true
    }
  }
}
