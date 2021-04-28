package me.rina.gui.api.component.impl

/**
 * @author SrRina
 * @since 30/11/20 at 8:32pm
 *
 * @param <T> The value. :o
</T> */
class ComponentSetting<T> {
  var name: String
  var tag: String
  var description: String
  var value: T
  var minimum: T? = null
    private set
  var maximum: T? = null
    private set

  constructor(name: String, tag: String, description: String, value: T) {
    this.name = name
    this.tag = tag
    this.description = description
    this.value = value
  }

  constructor(name: String, tag: String, description: String, value: T, minimum: T, maximum: T) {
    this.name = name
    this.tag = tag
    this.description = description
    this.value = value
    this.minimum = minimum
    this.maximum = maximum
  }

  fun setMinimum(minimum: T) {
    this.minimum = minimum
  }

  fun setMaximum(maximum: T) {
    this.maximum = maximum
  }
}