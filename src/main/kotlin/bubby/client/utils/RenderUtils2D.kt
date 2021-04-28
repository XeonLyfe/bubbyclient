package bubby.client.utils

import net.minecraft.client.gui.Gui

object RenderUtils2D {
  fun drawRectOutline(x1: Int, y1: Int, x2: Int, y2: Int, colour: Int, thiccness: Int) {
    Gui.drawRect(x1 + thiccness, y1, x2 - thiccness, y1 + thiccness, colour)
    Gui.drawRect(x1, y1, x1 + thiccness, y2, colour)
    Gui.drawRect(x2 - thiccness, y1, x2, y2, colour)
    Gui.drawRect(x1 + thiccness, y2 - thiccness, x2 - thiccness, y2, colour)
  }

  fun drawBorderedRect(x1: Int, y1: Int, x2: Int, y2: Int, colour1: Int, colour2: Int, thiccness: Int) {
    Gui.drawRect(x1 + thiccness, y1 + thiccness, x2 - thiccness, y2 - thiccness, colour1)
    drawRectOutline(x1, y1, x2, y2, colour2, thiccness)
  }
}