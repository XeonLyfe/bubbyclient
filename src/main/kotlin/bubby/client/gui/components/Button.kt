package bubby.client.gui.components

import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.BubbyClient.MC
import bubby.client.utils.Theme.extraColour
import bubby.client.utils.Theme.toggledColour
import bubby.client.utils.Theme.unToggledColour
import java.util.*
import java.util.function.Consumer

//import net.minecraft.util.Formatting;
class Button(var mod: Module, var parent: Frame, var offset: Int): Component {
  private val subcomponents: ArrayList<Component> = ArrayList()

  @JvmField
  var open: Boolean
  private var isHovered = false
  override fun setOff(newOff: Int) {
    var newOff = newOff
    for(c in subcomponents) {
      c.setOff(newOff)
      newOff += 16
    }
  }

  override var height: Int = 0
    get() = TODO("Not yet implemented")

  override fun handleMouseInput() {
  }

  val x: Int
    get() = parent.x

  override fun renderComponent() {
    MC.fontRenderer.drawStringWithShadow(mod.name, parent.x + 4f, parent.y + offset + 2.toFloat(), if(mod.isToggled()) toggledColour else unToggledColour)
    if(isHovered) MC.fontRenderer.drawStringWithShadow(mod.getDescription(), BubbyClient.MC.displayWidth - BubbyClient.MC.fontRenderer.getStringWidth(mod.getDescription()) - 1.toFloat(), 1f, -1)
    if(open) {
      MC.fontRenderer.drawStringWithShadow("-", parent.x + parent.width - 12.toFloat(), parent.y + offset + 2.toFloat(), extraColour)
      subcomponents.forEach(Consumer { c: Component -> c.renderComponent() })
    }
    else MC.fontRenderer.drawStringWithShadow("+", parent.x + parent.width - 12.toFloat(), parent.y + offset + 2.toFloat(), extraColour)
  }

  override fun updateComponent(x: Int, y: Int) {
    isHovered = isMouseOnButton(x, y)
    subcomponents.forEach(Consumer { c: Component -> c.updateComponent(x, y) })
  }

  override fun mouseClicked(mx: Int, my: Int, mb: Int) {
    if(isMouseOnButton(mx, my) && mb == 0) {
      mod.toggle()
    }
    else if(isMouseOnButton(mx, my) && mb == 1) {
      open = !open
      parent.onButtonOpen(this)
      parent.refresh()
    }
    subcomponents.forEach(Consumer { c: Component -> c.mouseClicked(mx, my, mb) })
  }

  override fun mouseReleased(mx: Int, my: Int, mb: Int) {
    subcomponents.forEach(Consumer { c: Component -> c.mouseReleased(mx, my, mb) })
  }

  override val parentHeight: Int
    get() = TODO("Not yet implemented")

  override fun keyTyped(typedChar: Char, key: Int) {
    subcomponents.forEach(Consumer { c: Component -> c.keyTyped(typedChar, key) })
  }

  private fun isMouseOnButton(x: Int, y: Int): Boolean {
    return x > parent.x && x < parent.x + parent.width && y > parent.y + offset && y < parent.y + 12 + offset
  }

  init {
    open = false
    height = 12
    var opY = offset
    for(s in BubbyClient.settings.getSettingsByMod(mod)) {
      if(s.value is String) {
        subcomponents.add(ModeButton(s as Setting<String>, this, mod, opY))
        opY += 12
      }
      else if(s.value is Double || s.value is Float) {
        subcomponents.add(DoubleSlider(s as Setting<Double>, this, opY))
        opY += 12
      }
      else if(s.value is Int || s.value is Long) {
        subcomponents.add(IntSlider(s as Setting<Int>, this, opY))
        opY += 12
      }
      else if(s.value is Boolean) {
        subcomponents.add(CheckBox(s as Setting<Boolean>, this, opY))
        opY += 12
      }
    }
    subcomponents.add(Bind(mod, this, opY))
  }
}