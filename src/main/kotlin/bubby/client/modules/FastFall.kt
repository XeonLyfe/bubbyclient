package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent
import net.minecraft.network.play.client.CPacketPlayer

// TODO: Stop making this fuck up with pearls
class FastFall: Module("FastFall", "Makes you fall off of ledges faster", -1, Category.Movement) {
  @PogEvent
  fun onTick(event: TickEvent) {
    if(mc.player.isInLava || mc.player.isInWater)
      return

    event.run {
      if(mc.player.onGround) {
        mc.player.setVelocity(mc.player.motionX, -1.0, mc.player.motionZ)
        mc.player.connection.sendPacket(CPacketPlayer(true))
      }
    }
  }
}
