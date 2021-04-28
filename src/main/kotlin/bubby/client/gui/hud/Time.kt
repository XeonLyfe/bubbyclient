package bubby.client.gui.hud

import me.rina.gui.api.component.Component
import java.text.SimpleDateFormat
import java.util.*

class Time:
  Component("Time", "Time", "Displays the time", StringType.Use) {

  override fun onRender(partialTicks: Float) {
    val time = SimpleDateFormat("h:mm a").format(Date())
    this.render(time, 0, 0)
    this.rect.setWidth(getStringWidth(time))
    this.rect.setHeight(getStringHeight(time))
  }
}
