package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.ReadPacketEvent
import net.minecraft.network.play.server.SPacketPlayerPosLook

class NoForceLook: Module("NoForceLook", "Prevents servers from forcing your location", -1, Category.Player) {
  @PogEvent
  fun onPacketRead(event: ReadPacketEvent) {
    event.run {
      if(packet is SPacketPlayerPosLook) isCancelled = true
    }
  }
}
