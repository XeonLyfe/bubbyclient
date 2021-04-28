package bubby.client.gui.hud

import bubby.client.BubbyClient
import me.rina.gui.api.component.Component
import me.rina.gui.api.component.impl.ComponentSetting

class Watermark:
Component("Watermark", "Watermark", "Displays the client and its version", StringType.Use) {

  val git = ComponentSetting("Git", "Git", "Show git hash", true)

  override fun onRender(partialTicks: Float) {
    var mark = BubbyClient.name
    if(git.value) {
      mark += " ${BubbyClient.version}"
    }

    this.render(mark, 0, 0)

    this.rect.setWidth(getStringWidth(mark))
    this.rect.setHeight(getStringHeight(mark))
  }
}
