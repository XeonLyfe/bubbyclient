package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.ReadPacketEvent
import bubby.client.events.TickEvent
import net.minecraft.entity.Entity
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.server.*

class BoatFly: Module("BoatFly", "fitmc 2b2t boatfly exploit", -1, Category.Movement) {
  private val speed = Setting<Double>("BoatFly Speed", this).withValue(0.3).withMin(0.0).withMax(1.0).add()
  private val ground = Setting<Boolean>("BoatFly Ground Packet", this).withValue(false).add()
  private val remount = Setting<Boolean>("BoatFly Remount", this).withValue(true).add()
  private val ignoreServer = Setting<Boolean>("BoatFly Ignore Server", this).withValue(false).add()
  private val gravity = Setting<Boolean>("BoatFly Gravity", this).withValue(false).add()
  var vehicle: Entity? = null
  var y = 0.0
  private var hadVehicle = false

  override fun onDisable() {
    super.onDisable()
    hadVehicle = false
    vehicle = null
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(mc.player == null || mc.world == null) {
        hadVehicle = false
        vehicle = null
        setToggled(false)
        return
      }

      if(mc.player.ridingEntity == null) {
        if(hadVehicle && remount.value && vehicle != null)
          mc.player.startRiding(vehicle!!, true)
        else {
          hadVehicle = false
          vehicle = null
        }
      }

      if(!hadVehicle) {
        y = mc.player.posY
        vehicle = mc.player.ridingEntity
      }

      hadVehicle = true

      vehicle?.motionY = if(mc.gameSettings.keyBindJump.isPressed) speed.value else 0.0
      mc.player?.motionY = if(mc.gameSettings.keyBindJump.isPressed) speed.value else 0.0

      vehicle?.setNoGravity(gravity.value)

      if(ground.value) mc.player.connection.sendPacket(CPacketPlayer(true))
    }
  }

  @PogEvent
  fun onPacketRead(event: ReadPacketEvent) {
    event.run {
      if(mc.player == null || mc.world == null)
        return
      if((packet is SPacketPlayerPosLook 
      || packet is SPacketEntity 
      || packet is SPacketEntityAttach 
      || packet is SPacketEntityVelocity) 
      && ignoreServer.value) 
        isCancelled = true
    }
  }
}
