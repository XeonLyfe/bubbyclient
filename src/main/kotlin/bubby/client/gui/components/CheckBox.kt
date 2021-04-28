package bubby.client.gui.components

import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.BubbyClient.MC
import bubby.client.utils.RenderUtils2D.drawBorderedRect
import bubby.client.utils.RenderUtils2D.drawRectOutline
import bubby.client.utils.Theme.borderColour
import bubby.client.utils.Theme.extraColour
import bubby.client.utils.Theme.innerColour
import net.minecraft.client.gui.Gui

class CheckBox(private val setting: Setting<Boolean>, private val parent: Button, offset: Int): Component {
  private var over = false
  private var offset: Int
  private var x: Int
  private var y: Int
  override fun renderComponent() {
    drawBorderedRect(x - 2, parent.parent.y + offset - 2, x + 90, parent.parent.y + offset + 14, innerColour, borderColour, 1)
    MC.fontRenderer.drawStringWithShadow(setting.name.replace(parent.mod.name + " ", ""), x + 13.toFloat(), parent.parent.y + offset + 2.toFloat(), -1)
    drawRectOutline(x + 2, parent.parent.y + offset + 2, x + 10, parent.parent.y + offset + 10, borderColour, 1)
    if(setting.value) {
      Gui.drawRect(x + 3, parent.parent.y + offset + 3, x + 9, parent.parent.y + offset + 9, extraColour)
    }
  }

  override fun setOff(newOff: Int) {
    offset = newOff
  }

  override val height: Int
    get() = TODO("Not yet implemented")

  override fun handleMouseInput() {
  }

  override fun updateComponent(mx: Int, my: Int) {
    over = isMouseOnButton(mx, my)
    x = parent.parent.x + parent.parent.width + 5
    y = parent.parent.y + offset
  }

  override fun mouseClicked(mx: Int, my: Int, mb: Int) {
    if(isMouseOnButton(mx, my) && mb == 0 && parent.open) {
      setting.value = !setting.value
      BubbyClient.modules.saveModule(setting.parent)
    }
  }

  override fun mouseReleased(x: Int, y: Int, b: Int) {
  }

  override val parentHeight: Int
    get() = TODO("Not yet implemented")

  override fun keyTyped(c: Char, k: Int) {
  }

  private fun isMouseOnButton(mx: Int, my: Int): Boolean {
    return mx > x && mx < x + 88 && my > y && my < y + 12 + 1
  }

  init {
    x = parent.parent.x + parent.parent.width + 5
    y = parent.parent.y + offset
    this.offset = offset
  }
}
