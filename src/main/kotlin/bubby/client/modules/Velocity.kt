package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.ReadPacketEvent
import bubby.client.events.TickEvent
import bubby.client.events.MoveUpdateEvent
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion

class Velocity: Module("Velocity", "stop.. now pls ;w;", -1, Category.Player) {
  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      mc.player.entityCollisionReduction = 1f
    }
  }

  @PogEvent
  fun onMoveUpdate(event: MoveUpdateEvent) {
    event.run {
      if(mc.player.isHandActive && !mc.player.isRiding) {
        mc.player.movementInput.moveForward /= 0.2f
        mc.player.movementInput.moveStrafe /= 0.2f
      }
    }
  }

  @PogEvent
  fun onPacketRead(event: ReadPacketEvent) {
    event.run {
      if(packet is SPacketEntityVelocity && packet.entityID == mc.player.entityId)
        isCancelled = true
      else if(packet is SPacketExplosion)
        isCancelled = true
    }
  }
}
