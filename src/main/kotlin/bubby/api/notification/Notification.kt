package bubby.api.notification

import bubby.client.utils.*

import me.rina.turok.render.opengl.TurokRenderGL
import me.rina.turok.render.font.TurokFont
import me.rina.turok.render.font.management.TurokFontManager
import me.rina.turok.util.TurokRect

import java.awt.Color

class Notification(val title: String, val description: String, var time: Long) {
  constructor(title: String, description: String): this(title, description, 5L)

  val width = 100
  val height = 20

  val timer = Timer()

  fun shouldPop(): Boolean {
    return timer.hasTimePassed(time)
  }

  // TODO: most of this probably lol
  fun render(offX: Int, offY: Int, font: TurokFont) {
    TurokRenderGL.drawOutlineRectFadingMouse(TurokRect(offX, offY, width, height), 3, Color(100, 100, 100))
    TurokFontManager.render(font, title, offX + 2, offY + 2, true, Color(255, 255, 255))
  }
}
