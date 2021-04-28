package bubby.client.modules

import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting

class Capes: Module("Capes", "cape cool", -1, Category.Misc, false) {
  private val modes = arrayOf("BlueGrey", "Black", "Trans")
  val mode = Setting<String>("Capes Cape", this).withValue("Trans").withValues(modes).add()
}
