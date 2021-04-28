package bubby.api.module

import bubby.api.mixin.interfaces.IMinecraft
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import net.minecraft.client.Minecraft

open class Module(
        val name: String,
        private val desc: String,
        var bind: Int,
        private val category: Category,
        private val visible: Boolean = true
) {
  open var mc: Minecraft = Minecraft.getMinecraft()
  var imc = Minecraft.getMinecraft() as IMinecraft
  private val visibleSetting = Setting<Boolean>("$name Visible", this).withValue(visible).add()
  private var toggled = false

  open fun onEnable() {
    BubbyClient.events.register(this)
  }

  open fun onDisable() {
    BubbyClient.events.unregister(this)
  }

  fun toggle() {
    setToggled(!toggled)
  }

  fun isVisible(): Boolean {
    return visibleSetting.value
  }

  fun isToggled(): Boolean {
    return toggled
  }

  fun setToggled(toggle: Boolean) {
    this.toggled = toggle
    BubbyClient.modules.saveModule(this)

    if(toggle) {
      try {
        this.onEnable()
      }
      catch(e: Exception) {
        e.printStackTrace()
      }
    }
    else {
      try {
        this.onDisable()
      }
      catch(e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun getDescription(): String {
    return this.desc
  }

  fun getCategory(): Category {
    return this.category
  }

  fun addSetting(setting: Setting<*>) {
    BubbyClient.settings.addSetting(setting)
  }
}
