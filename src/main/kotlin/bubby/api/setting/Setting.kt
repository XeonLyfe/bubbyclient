package bubby.api.setting

import bubby.api.module.Module
import bubby.client.BubbyClient

class Setting<T: Any>(val name: String, val parent: Module) {
  lateinit var value: T
  lateinit var min: T
  lateinit var max: T
  private lateinit var values: Array<T>

  fun withValue(value: T): Setting<T> {
    this.value = value
    return this
  }

  fun withValues(values: Array<T>): Setting<T> {
    this.values = values
    return this
  }

  fun value(): T {
    return value
  }

  fun set(newValue: T) {
    this.value = newValue
    BubbyClient.modules.saveModule(this.parent)
  }

  @Suppress("UNCHECKED_CAST")
  fun apply(value: Any?) {
    this.value = value as T
  }

  fun withMin(min: T): Setting<T> {
    this.min = min
    return this
  }

  fun withMax(max: T): Setting<T> {
    this.max = max
    return this
  }

  fun add(): Setting<T> {
    BubbyClient.settings.addSetting(this)
    return this
  }

  fun getNext(): T {
    var n = values.indexOf(value)
    n++
    if(n >= values.size) n = 0

    return values[n]
  }
}
