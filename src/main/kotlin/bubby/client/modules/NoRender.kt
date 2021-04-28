package bubby.client.modules

import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting

class NoRender: Module("NoRender", "Prevents some things from rendering", -1, Category.Render) {
  init {
    addSetting(Setting<Boolean>("NoRender HurtCam", this).withValue(true))
    addSetting(Setting<Boolean>("NoRender Fire", this).withValue(true))
    addSetting(Setting<Boolean>("NoRender Armour", this).withValue(false))
    addSetting(Setting<Boolean>("NoRender BossBar", this).withValue(false))
  }
}
