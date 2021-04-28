package bubby.client.gui.hud
import bubby.api.event.PogEvent
import bubby.client.BubbyClient
import bubby.client.events.ReadPacketEvent
import me.rina.gui.api.component.Component
import me.rina.gui.api.component.impl.ComponentSetting
import net.minecraft.client.Minecraft
import net.minecraft.network.play.server.SPacketTimeUpdate
import net.minecraft.util.math.MathHelper
import kotlin.math.abs
import kotlin.math.roundToInt

class Info:
Component("Info", "Info", "Shows tps, fps, and/or ping", StringType.Use) {

  private var tps = 20.0
  private var lp: Long = 0
  private var pt: Long = 0
  private var fps = 0
  private var ping = 0
  val showTPS = ComponentSetting<Boolean>("tps", "tps", "Show tps", true)
  val showfps = ComponentSetting<Boolean>("fps", "fps", "Show fps", true)
  val showPing = ComponentSetting<Boolean>("ping", "ping", "Show ping", true)

  init {
    BubbyClient.events.register(this)
  }

  override fun onRender(partialTicks: Float) {
    val info = mutableListOf<String>()

    if(showfps.value) {
      fps = Minecraft.getDebugFPS()
      info.add("FPS: $fps")
    }

    if(showPing.value) {
      try {
        ping = mc.connection!!.getPlayerInfo(mc.player.name)!!.responseTime
      }
      catch(ignored: Exception) {
      }

      info.add("Ping $ping")
    }

    if(showTPS.value) {
      var s = "\u00a77"

      when {
        lp + 7600 < System.currentTimeMillis() -> s += "...."
        lp + 5000 < System.currentTimeMillis() -> s += "..."
        lp + 2500 < System.currentTimeMillis() -> s += ".."
        lp + 1200 < System.currentTimeMillis() -> s += "."
      }
      info.add("TPS: $tps$s")
    }

    this.renderList(info.toList(), true)
  }

  @PogEvent
  fun onReadPacket(event: ReadPacketEvent) {
    event.run {
      lp = System.currentTimeMillis()

      if(packet is SPacketTimeUpdate) {
        val t = System.currentTimeMillis()
        if(t < 500)
          return
        val to = abs(1000 - (t - pt)) + 100
        tps = (MathHelper.clamp(20 / (to.toDouble() / 1000), 0.0, 20.0) * 100.0).roundToInt() / 100.0
        pt = t
      }
    }
  }
}
