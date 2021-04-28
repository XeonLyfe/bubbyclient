package bubby.client.gui.components

import bubby.api.module.Module
import bubby.client.BubbyClient
import bubby.client.BubbyClient.MC
import bubby.client.utils.RenderUtils2D.drawBorderedRect
import bubby.client.utils.Theme.borderColour
import bubby.client.utils.Theme.innerColour
import org.lwjgl.input.Keyboard

class Bind(private var mod: Module, private var parent: Button, offset: Int): Component {
  private var offset: Int
  private var x: Int
  private var y: Int
  private var over = false
  private var listening = false

  override fun renderComponent() {
    drawBorderedRect(x - 2, parent.parent.y + offset - 2, x + 90, parent.parent.y + offset + 14, innerColour, borderColour, 1)
    MC.fontRenderer.drawStringWithShadow(if(listening) "Listening..." else "Bind: " + if(mod.bind == -1) "none" else Keyboard.getKeyName(mod.bind), x + 2.toFloat(), parent.parent.y + offset + 2.toFloat(), -1)
  }

  override fun setOff(newOff: Int) {
    offset = newOff
  }

  override val height: Int
    get() = TODO("Not yet implemented")

  override fun handleMouseInput() {
  }

  override fun mouseClicked(mx: Int, my: Int, mb: Int) {
    if(over && parent.open) listening = true
  }

  override fun mouseReleased(x: Int, y: Int, b: Int) {
  }

  override val parentHeight: Int
    get() = TODO("Not yet implemented")

  override fun updateComponent(mx: Int, my: Int) {
    over = isMouseOnButton(mx, my)
    x = parent.parent.x + parent.parent.width + 5
    y = parent.parent.y + offset
  }

  override fun keyTyped(c: Char, k: Int) {
    if(parent.open && listening && c.toInt() != 256) {
      BubbyClient.modules.getModuleByName(mod.name).bind = c.toInt()
      mod.bind = k
      listening = false
      if(k == Keyboard.KEY_DELETE) {
        BubbyClient.modules.getModuleByName(mod.name).bind = -1
        mod.bind = -1
      }
      BubbyClient.modules.saveModule(mod)
    }
  }

  private fun isMouseOnButton(mx: Int, my: Int): Boolean {
    return mx > x && mx < x + 88 && my > y && my < y + 13
  }

  init {
    x = parent.parent.x + parent.parent.width + 5
    y = parent.parent.y + offset
    this.offset = offset
  }
}
