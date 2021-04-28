package bubby.api.setting

import bubby.api.module.Module

class SettingManager {
  var settings = LinkedHashMap<String, Setting<*>>()

  fun addSetting(s: Setting<*>) {
    settings[s.name] = s
  }

  fun getSettingByName(name: String): Setting<*> = settings[name]!!

  fun getSettingsByMod(module: Module): List<Setting<*>> = settings.values.filter { it.parent == module }

  fun getSettingsByType(pog: Any): List<Setting<*>> = settings.values.filter { it.value::class.isInstance(pog) }
}
