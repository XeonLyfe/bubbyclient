package bubby.client.gui.components

import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.BubbyClient.MC
import bubby.client.utils.RenderUtils2D.drawBorderedRect
import bubby.client.utils.Theme.borderColour
import bubby.client.utils.Theme.innerColour

class ModeButton(set: Setting<String>, button: Button, mod: Module, offset: Int): Component {
  private val parent: Button = button
  private val setting: Setting<String> = set
  private val mod: Module
  private var over = false
  private var offset: Int
  private var x: Int
  private var y: Int
  override fun setOff(nOff: Int) {
    offset = nOff
  }

  override val height: Int
    get() = 0

  override fun handleMouseInput() {
  }

  override fun renderComponent() {
    drawBorderedRect(x - 2, parent.parent.y + offset - 2, x + 90, parent.parent.y + offset + 14, innerColour, borderColour, 1)
    MC.fontRenderer.drawStringWithShadow(setting.name.replace(parent.mod.name + " ", "") + ": " + setting.value, x + 2.toFloat(), (parent.parent.y + offset + 2).toFloat(), -1)
  }

  override fun updateComponent(mx: Int, my: Int) {
    x = parent.parent.x + parent.parent.width + 5
    y = parent.parent.y + offset
  }

  override fun mouseClicked(mx: Int, my: Int, mb: Int) {
    if(isMouseOnButton(mx, my) && mb == 0 && parent.open) {
      setting.value = setting.getNext()
      BubbyClient.modules.saveModule(setting.parent)
    }
  }

  override fun mouseReleased(mx: Int, my: Int, mb: Int) {
  }

  override val parentHeight: Int
    get() = 0

  override fun keyTyped(c: Char, k: Int) {
  }

  private fun isMouseOnButton(mx: Int, my: Int): Boolean {
    return mx > x && mx < x + 88 && my > y && my < y + 13
  }

  init {
    this.mod = mod
    x = parent.parent.x + parent.parent.width + 5
    y = parent.parent.y + offset
    this.offset = offset
  }
}
