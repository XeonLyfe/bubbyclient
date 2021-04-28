package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.SendPacketEvent
import net.minecraft.network.play.client.CPacketChatMessage

class GreenText: Module("GreenText", ">", -1, Category.Misc) {
  @PogEvent
  fun onSendPacket(event: SendPacketEvent) {
    event.run {
      if(packet is CPacketChatMessage) {
        val pack = packet as CPacketChatMessage
        if(pack.message.startsWith(">")
        || pack.message.startsWith(".")
        || pack.message.startsWith("/"))
          return

        mc.player.sendChatMessage(">" + pack.message)
        isCancelled = true
      }
    }
  }
}
