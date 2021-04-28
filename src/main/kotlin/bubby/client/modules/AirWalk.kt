package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.mixin.interfaces.IKeyBinding
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.MoveEvent
import bubby.client.events.TickEvent
import kotlin.math.cos
import kotlin.math.sin

//TODO: bypass
class AirWalk: Module("AirWalk", "walk air", -1, Category.Movement) {
  private var check = false

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      (mc.gameSettings.keyBindJump as IKeyBinding).setPressed(false)

      mc.player.motionY = 0.0
      mc.player.onGround = true
      check = !check
      if(check) mc.player.setPosition(mc.player.posX, mc.player.posY - 0.0000000001, mc.player.posZ)
      else mc.player.setPosition(mc.player.posX, mc.player.posY + 0.0000000001, mc.player.posZ)

      mc.player.motionY = 0.0
    }
  }

  @PogEvent
  fun onMove(event: MoveEvent) {
    event.run {
      var forward = mc.player.moveForward
      val strafe = mc.player.moveStrafing
      var yaw = mc.player.rotationYaw

      if(forward == 0f && strafe == 0f) {
        x = 0.0
        z = 0.0
      }
      else {
        if(forward != 0f) {
          if(strafe > 0f) yaw += if(forward > 0f) -45 else 45
          else if(strafe < 0f) yaw += if(forward > 0f) 45 else -45
          //strafe = 0f
          if(forward > 0f) forward = 1f
          else if(forward < 0f) forward = -1f

          x = (forward * cos(Math.toRadians(yaw + 90.0)) + strafe * sin(Math.toRadians(yaw + 90.0)))
          z = (forward * cos(Math.toRadians(yaw + 90.0)) - strafe * sin(Math.toRadians(yaw + 90.0)))
        }
      }
    }
  }
}
