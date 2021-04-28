package bubby.client.gui.components

import bubby.api.module.Category
import bubby.client.BubbyClient
import bubby.client.BubbyClient.MC
import bubby.client.BubbyClient.clickGuiManager
import bubby.client.utils.RenderUtils2D.drawBorderedRect
import bubby.client.utils.RenderUtils2D.drawRectOutline
import bubby.client.utils.Theme.borderColour
import bubby.client.utils.Theme.buttonColour
import bubby.client.utils.Theme.innerColour
import bubby.client.utils.Theme.titleColour
import net.minecraft.client.gui.Gui
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

class Frame(c: Category) {
  private val barHeight: Int
  var components: ArrayList<Component> = ArrayList()

  @JvmField
  var category: Category = c
  var isOpen: Boolean

  //var visible = false
  var width: Int = 88
  var x: Int
  var y: Int
  private var dragX: Int
  private var dragY = 0
  var zIndex = 0
  private var isDragging: Boolean
  private var isHovered = false

  //private var isVisible: Boolean
  private var isExtendButtonHovered = false
  private val dragged = 0
  private var c1 = 0
  private var c2 = -1

  fun onButtonOpen(c: Component) {
    for(comp in components) {
      if(comp !== c) {
        val cc = comp as Button
        cc.open = false
      }
    }
  }

  fun setDragX(newX: Int) {
    dragX = newX
  }

  fun setDragY(newY: Int) {
    dragY = newY
  }

  fun setDrag(drag: Boolean) {
    isDragging = drag
    if(drag) {
      clickGuiManager.moveFrameToFront(this)
    }
  }

  fun render() {
    c1 = innerColour
    c2 = borderColour
    drawBorderedRect(x, y, x + width, y + barHeight, c1, c2, 1)
    MC.fontRenderer.drawStringWithShadow(this.category.name, x + 3.toFloat(), y + 3.toFloat(), titleColour)
    drawRectOutline(x + width - 3, y + 2, x + width - 10, y + 11, c2, 1)
    if(isOpen) {
      Gui.drawRect(x + width - 3, y + 3, x + width - 10, y + 10, buttonColour)
      if(components.isNotEmpty()) {
        drawBorderedRect(x, y + barHeight + 1, x + width, y + barHeight + 14 * components.size + 2, c1, c2, 1)
        components.forEach(Consumer { obj: Component -> obj.renderComponent() })
      }
    }
  }

  fun refresh() {
    var off = barHeight
    for(c in components) {
      c.setOff(off)
      off += 14
    }
  }

  fun updatePosition(x: Int, y: Int) {
    isHovered = isWithinHeader(x, y)
    isExtendButtonHovered = isWithinExtendRange(x, y)
    if(isDragging) {
      this.x = x - dragX
      this.y = y - dragY
    }
  }

  private fun isWithinHeader(x: Int, y: Int): Boolean {
    return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + barHeight
  }

  fun isWithinHeaderAndOnTop(x: Int, y: Int): Boolean {
    return clickGuiManager.frames.stream() // get all that are within header
      .filter(Predicate { frame: Frame -> frame.isWithinHeader(x, y) }) // get the top most one by z index
      .max(Comparator.comparingInt { obj: Frame -> obj.zIndex }) // check if that top most frame is this one
      .filter(Predicate { frame: Frame -> frame === this }).isPresent
  }

  fun isWithinExtendRange(x: Int, y: Int): Boolean {
    return x <= this.x + width - 2 && x >= this.x + width - 10 && y >= this.y + 2 && y <= this.y + 10
  }
  /*
  fun isVisible(): Boolean
  {
    return isVisible
  }

  fun setVisiable(newVis: Boolean)
  {
    isVisible = newVis
  }
  */

  init {
    x = 5
    y = 5
    barHeight = 13
    dragX = 0
    zIndex = try {
      clickGuiManager.frames.size
    }
    catch(e: Exception) {
      0
    }
    isOpen = false
    isDragging = false
    var tY = barHeight + 3
    BubbyClient.modules.getMods().sortedBy { it.name }
    for(m in BubbyClient.modules.getMods()) {
      if(m.getCategory() != c) continue
      val modButton = Button(m, this, tY)
      components.add(modButton)
      tY += 14
    }
    //isVisible = false
  }
}
