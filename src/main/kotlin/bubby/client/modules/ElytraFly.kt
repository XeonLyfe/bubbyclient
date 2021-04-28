package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import net.minecraft.network.play.client.CPacketEntityAction

class ElytraFly: Module("ElytraFly", "Better elytra", -1, Category.Movement) {
  private var speed = Setting<Double>("ElytraFly Speed", this).withValue(0.05).withMin(0.0).withMax(0.1).add()
  private var takeoff = Setting<Boolean>("ElytraFly Takeoff", this).withValue(false).add()

  override fun onEnable() {
    if(mc.player == null || mc.world == null)
      return
    if(takeoff.value)
      mc.player.connection.sendPacket(CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING))
    super.onEnable()
  }

  override fun onDisable() {
    super.onDisable()
    mc.player.capabilities.isFlying = false
    mc.player.capabilities.allowFlying = false
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(mc.player.isElytraFlying) {
        mc.player.capabilities.allowFlying = true
        mc.player.capabilities.isFlying = true
        mc.player.capabilities.flySpeed = speed.value.toFloat()
      }
    }
  }
}
