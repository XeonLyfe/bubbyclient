package me.rina.gui.api.component.management

import bubby.client.gui.hud.*
import me.rina.turok.util.TurokRect
import me.rina.turok.render.font.TurokFont
import me.rina.gui.api.component.impl.ComponentSetting
import me.rina.turok.util.TurokDisplay
import net.minecraft.client.Minecraft
import me.rina.turok.util.TurokMath
import me.rina.gui.api.component.Component
import java.awt.Font
import java.lang.Exception
import java.util.ArrayList

class ComponentManager {
  var componentList: ArrayList<Component>

  /*
     * The corners, so, we push and drooop;
     */
  var componentListTopLeft: ArrayList<Component>
  var componentListTopRight: ArrayList<Component>
  var componentListBottomLeft: ArrayList<Component>
  var componentListBottomRight: ArrayList<Component>

  /*
     * Variables rect to all corner dock;
     */
  var rectTopLeft = TurokRect("TopLeft", 0, 0, 10, 0)
  var rectTopRight = TurokRect("TopRight", 0, 0, 10, 0)
  var rectBottomLeft = TurokRect("BottomLeft", 0, 0, 10, 0)
  var rectBottomRight = TurokRect("BottomRight", 0, 0, 10, 0)
  var font: TurokFont
  private var offsetChat = 0
  fun registry(component: Component) {
    try {
      for (fields in component.javaClass.declaredFields) {
        if (ComponentSetting::class.java.isAssignableFrom(fields.type)) {
          if (!fields.isAccessible) {
            fields.isAccessible = true
          }
          val setting = fields[component] as ComponentSetting<*>
          component.registry(setting)
        }
      }
    } catch (exc: Exception) {
      exc.printStackTrace()
    }
    componentList.add(component)
  }

  fun onSaveList() {
    for (components in componentList) {
      components.onSave()
    }
  }

  fun onLoadList() {
    for (components in componentList) {
      components.onLoad()
    }
  }

  fun onRenderComponentList() {
    for (components in componentList) {
      if (components.isEnabled) {
        components.onRender()
      }
    }
  }

  fun onCornerDetectorComponentList(partialTicks: Float) {
    val display = TurokDisplay(Minecraft.getMinecraft())
    for (components in componentList) {
      // Detect the corner of rect hit and set the Dock;
      components.cornerDetector()
      if (components.rect.collideWithRect(rectTopLeft)) {
        if (!componentListTopLeft.contains(components)) {
          componentListTopLeft.add(components)
        }
      } else {
        if (componentListTopLeft.contains(components)) {
          componentListTopLeft.remove(components)
        }
      }
      if (components.rect.collideWithRect(rectTopRight)) {
        if (!componentListTopRight.contains(components)) {
          componentListTopRight.add(components)
        }
      } else {
        if (componentListTopRight.contains(components)) {
          componentListTopRight.remove(components)
        }
      }
      if (components.rect.collideWithRect(rectBottomLeft)) {
        if (!componentListBottomLeft.contains(components)) {
          componentListBottomLeft.add(components)
        }
      } else {
        if (componentListBottomLeft.contains(components)) {
          componentListBottomLeft.remove(components)
        }
      }
      if (components.rect.collideWithRect(rectBottomRight)) {
        if (!componentListBottomRight.contains(components)) {
          componentListBottomRight.add(components)
        }
      } else {
        if (componentListBottomRight.contains(components)) {
          componentListBottomRight.remove(components)
        }
      }
    }
    if (Minecraft.getMinecraft().ingameGUI.chatGUI.chatOpen) {
      offsetChat = TurokMath.lerp(offsetChat, 14, partialTicks).toInt()
    } else {
      offsetChat = TurokMath.lerp(offsetChat, 0, partialTicks).toInt()
    }
    var memoryPositionLengthTopLeft = 1
    for (components in componentListTopLeft) {
      components.rect.setX(1)
      components.rect.setY(memoryPositionLengthTopLeft)
      memoryPositionLengthTopLeft = components.rect.getY() + components.rect.getHeight() + 1
      if (rectTopLeft.getWidth() < components.rect.getWidth()) {
        rectTopLeft.setWidth(components.rect.getWidth())
      }
    }
    rectTopLeft.setX(1)
    rectTopLeft.setY(1)
    rectTopLeft.setHeight(memoryPositionLengthTopLeft)
    var memoryPositionLengthTopRight = 1
    for (components in componentListTopRight) {
      components.rect.setX(display.scaledWidth - components.rect.getWidth())
      components.rect.setY(memoryPositionLengthTopRight)
      memoryPositionLengthTopRight = components.rect.getY() + components.rect.getHeight() + 1
      if (rectTopRight.getWidth() < components.rect.getWidth()) {
        rectTopRight.setWidth(components.rect.getWidth())
      }
    }
    rectTopRight.setX(display.scaledWidth - rectTopRight.getWidth() - 1)
    rectTopRight.setY(1)
    rectTopRight.setHeight(memoryPositionLengthTopRight)
    var memoryPositionLengthBottomLeft = display.scaledHeight - offsetChat - 1
    for (components in componentListBottomLeft) {
      components.rect.setX(1)
      components.rect.setY(memoryPositionLengthBottomLeft - components.rect.getHeight())
      memoryPositionLengthBottomLeft = components.rect.getY() - 1
      if (rectBottomLeft.getWidth() < components.rect.getWidth()) {
        rectBottomLeft.setWidth(components.rect.getWidth())
      }
    }
    rectBottomLeft.setX(1)
    rectBottomLeft.setY(display.scaledHeight - rectBottomLeft.getHeight() - 1)
    rectBottomLeft.setHeight(display.scaledHeight - memoryPositionLengthBottomLeft)
    var memoryPositionLengthBottomRight = display.scaledHeight - offsetChat
    for (components in componentListBottomRight) {
      components.rect.setX(display.scaledWidth - components.rect.getWidth())
      components.rect.setY(memoryPositionLengthBottomRight - components.rect.getHeight())
      memoryPositionLengthBottomRight = components.rect.getY() - 1
      if (rectBottomRight.getWidth() < components.rect.getWidth()) {
        rectBottomRight.setWidth(components.rect.getWidth())
      }
    }
    rectBottomRight.setX(display.scaledWidth - rectBottomRight.getWidth() - 1)
    rectBottomRight.setY(display.scaledHeight - rectBottomRight.getHeight() - 1)
    rectBottomRight.setHeight(display.scaledHeight - memoryPositionLengthBottomRight)
  }

  companion object {
    lateinit var INSTANCE: ComponentManager
    operator fun get(clazz: Class<*>): Component? {
      for (components in INSTANCE.componentList) {
        if (components.javaClass == clazz) {
          return components
        }
      }
      return null
    }

    operator fun get(tag: String?): Component? {
      for (components in INSTANCE.componentList) {
        if (components.rect.getTag().equals(tag, ignoreCase = true)) {
          return components
        }
      }
      return null
    }
  }

  init {
    INSTANCE = this
    componentList = ArrayList()
    componentListTopLeft = ArrayList()
    componentListTopRight = ArrayList()
    componentListBottomLeft = ArrayList()
    componentListBottomRight = ArrayList()
    font = TurokFont(Font("Verdana", 0, 18), true, true)
    registry(Entities())
    registry(Info())
    registry(Modules())
    registry(Players())
    registry(Position())
    registry(Watermark())
    //this.registry(new ServerBrand());
    //this.registry(new Speed());
    //this.registry(new TotemCount());
    registry(Time())
  }
}