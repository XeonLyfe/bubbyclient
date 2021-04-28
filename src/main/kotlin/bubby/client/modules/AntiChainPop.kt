package bubby.client.modules
import bubby.api.event.PogEvent
import bubby.api.module.*
import bubby.client.BubbyClient
import bubby.client.events.ReadPacketEvent
import net.minecraft.network.play.server.SPacketEntityStatus

class AntiChainPop: Module("AntiChainPop", "Automatically toggles surround when you pop a totem", -1, Category.Combat) {
  @PogEvent
  fun  onReadPacket(event: ReadPacketEvent) {
    event.run {
      if(packet is SPacketEntityStatus
      && packet.opCode.toInt() == 35
      && packet.getEntity(mc.world) == mc.player) {
        BubbyClient.modules.getModuleByName("Surround").setToggled(true)
      }
    }
  }
}
