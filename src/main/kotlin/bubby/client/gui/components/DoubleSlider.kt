package bubby.client.gui.components

import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.BubbyClient.MC
import bubby.client.utils.RenderUtils2D.drawBorderedRect
import bubby.client.utils.Theme.borderColour
import bubby.client.utils.Theme.extraColour
import bubby.client.utils.Theme.innerColour
import net.minecraft.client.gui.Gui
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class DoubleSlider(private val setting: Setting<Double>, private val parent: Button, offset: Int): Component {
  private var over = false
  private var offset: Int
  private var x: Int
  private var y: Int
  private var dragging = false
  private var renderWidth = 0.0
  override fun renderComponent() {
    drawBorderedRect(x - 2, parent.parent.y + offset - 2, x + 90, parent.parent.y + offset + 14, innerColour, borderColour, 1)
    Gui.drawRect(x, parent.parent.y + offset, x + renderWidth.toInt(), parent.parent.y + offset + 12, extraColour)
    MC.fontRenderer.drawStringWithShadow(setting.name.replace(parent.mod.name + " ", "") + ": " + (setting.value * 100.0).roundToInt() / 100.0, x + 2.toFloat(), (parent.parent.y + offset + 2).toFloat(), -1)
  }

  override fun setOff(newOff: Int) {
    offset = newOff
  }

  override val height: Int
    get() = TODO("Not yet implemented")

  override fun handleMouseInput() {
  }

  override fun updateComponent(mx: Int, my: Int) {
    over = isMouseOnButtonD(mx, my) || isMouseOnButtonI(mx, my)
    x = parent.parent.x + parent.parent.width + 5
    y = parent.parent.y + offset
    val diff = 88.coerceAtMost(0.coerceAtLeast(mx - x)).toDouble()
    val setVal = setting.value
    val min = setting.min
    val max = setting.max
    renderWidth = 88 * (setVal - min) / (max - min)
    if(dragging) {
      if(diff == 0.0) {
        setting.value = setting.min
      }
      else {
        val newValue = roundToPlace(diff / 88 * (max - min) + min, 2)
        setting.value = newValue
      }

      BubbyClient.modules.saveModule(setting.parent)
    }
  }

  override fun mouseClicked(mx: Int, my: Int, mb: Int) {
    if(isMouseOnButtonD(mx, my) && mb == 0 && parent.open) {
      dragging = true
    }
    if(isMouseOnButtonI(mx, my) && mb == 0 && parent.open) {
      dragging = true
    }
  }

  override fun mouseReleased(mx: Int, my: Int, mb: Int) {
    dragging = false
  }

  override val parentHeight: Int
    get() = TODO("Not yet implemented")

  override fun keyTyped(c: Char, k: Int) {
  }

  private fun isMouseOnButtonD(mx: Int, my: Int): Boolean {
    return mx > x - 2 && mx < x + (parent.parent.width / 2 + 1) && my > y && my < y + 12 + 1
  }

  private fun isMouseOnButtonI(mx: Int, my: Int): Boolean {
    return mx > x + parent.parent.width / 2 && mx < x + parent.parent.width && my > y && my < y + 12 + 1
  }

  companion object {
    private fun roundToPlace(value: Double, places: Int): Double {
      require(places >= 0)
      var bd = BigDecimal(value)
      bd = bd.setScale(places, RoundingMode.HALF_UP)
      return bd.toDouble()
    }
  }

  init {
    x = parent.parent.x + parent.parent.width
    y = parent.parent.y + parent.offset
    this.offset = offset
  }
}
