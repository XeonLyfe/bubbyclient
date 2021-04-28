package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.*
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.network.play.client.CPacketInput
import net.minecraft.network.play.client.CPacketPlayer

class Freecam: Module("Freecam", "Move clientside only", -1, Category.Player) {
  private var flight = Setting<Boolean>("Freecam Flight", this).withValue(true).add()
  private var speed = Setting<Float>("Freecam Speed", this).withValue(0.05f).withMin(0.0f).withMax(0.1f).add()
  private var noclip = Setting<Boolean>("Freecam NoClip", this).withValue(true).add()
  private lateinit var retard: EntityOtherPlayerMP
  private var oldX = 0.0
  private var oldY = 0.0
  private var oldZ = 0.0

  override fun onEnable() {
    retard = EntityOtherPlayerMP(mc.world, mc.session.profile)
    retard.copyLocationAndAnglesFrom(mc.player)
    retard.rotationYawHead = mc.player.rotationYawHead
    mc.world.addEntityToWorld(57498, retard)
    oldX = mc.player.posX
    oldY = mc.player.posY
    oldZ = mc.player.posZ
    super.onEnable()
  }

  override fun onDisable() {
    super.onDisable()
    mc.world.removeEntityFromWorld(57498)
    mc.player.setPositionAndRotation(oldX, oldY, oldZ, mc.player.rotationYaw, mc.player.rotationPitch)

    mc.player.noClip = false
    mc.player.onGround = true
    mc.player.capabilities.allowFlying = false
    mc.player.capabilities.isFlying = false
  }

  @PogEvent
  fun onMove(event: MoveUpdateEvent) {
    if(mc.player == null || mc.world == null)
      return

    event.run {
      if(noclip.value) {
        mc.player.noClip = true
        mc.player.onGround = false
      }
    }
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(flight.value) {
        mc.player.capabilities.allowFlying = true
        mc.player.capabilities.flySpeed = speed.value
        mc.player.capabilities.isFlying = true
      }
      else {
        mc.player.capabilities.allowFlying = false
        mc.player.capabilities.isFlying = false
      }
    }
  }

  @PogEvent
  fun onSendPacket(event: SendPacketEvent) {
    event.run {
      if(packet is CPacketPlayer || packet is CPacketInput) isCancelled = true
    }
  }
}
