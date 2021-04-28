package bubby.client.gui.hud

import bubby.client.BubbyClient
import me.rina.gui.api.component.Component
import me.rina.gui.api.component.impl.ComponentSetting

class Modules:
Component("Modules", "Modules", "Displays all toggled modules", StringType.Use) {
  enum class Mode {
    Ascending,
    Descending,
    Alphabetical,
  }

  val mode = ComponentSetting<Mode>("Mode", "mode", "Mode", Mode.Descending)
  override fun onRender(partialTicks: Float) {
    var modules = BubbyClient.modules.getToggledModules().map{it.name}

    modules = when(mode.value) {
      Mode.Ascending -> modules.sortedBy{getStringWidth(it)}
      Mode.Descending -> modules.sortedByDescending{getStringWidth(it)}
      Mode.Alphabetical -> modules.sorted()
    }

    this.renderList(modules, true)
  }
}
