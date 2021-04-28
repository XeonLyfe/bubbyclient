package bubby.client.modules
import bubby.api.event.PogEvent
import bubby.api.mixin.interfaces.IEntity
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import net.minecraft.network.play.client.CPacketPlayer

class NoFall: Module("NoFall", "Prevents fall damage", -1, Category.Player) {
  private var modee = arrayOf("Simple", "Webs")
  private var mode = Setting<String>("NoFall Mode", this).withValue("Simple").withValues(modee).add()

  var counter = 0
  var web = false
  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {

      if(mc.player.fallDistance >= 2) {
        counter++
        if(counter > 5) {
          web = !web
          counter = 0
        }
        when(mode.value) {
        "Simple" -> mc.player.connection.sendPacket(CPacketPlayer(true))
        "Webs" -> (mc.player as IEntity).forceWeb(web)
        }
      }
    }
  }
}
