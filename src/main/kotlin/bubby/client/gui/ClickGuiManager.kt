package bubby.client.gui

import bubby.api.module.Category
import bubby.client.gui.components.Frame
import bubby.client.gui.hud.*
import bubby.client.utils.FileManager
import com.google.gson.GsonBuilder
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Mouse
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class ClickGuiManager: GuiScreen() {
  var frames: ArrayList<Frame> = ArrayList()

  init {
    for(c in Category.values()) frames.add(Frame(c))
    
    resetGui()
  }

  fun moveFrameToFront(frame: Frame) {
    frames.stream().filter { f: Frame -> f.zIndex >= frame.zIndex }.forEach { f: Frame -> f.zIndex = f.zIndex - 1 }
    frame.zIndex = frames.size
  }



  fun resetGui() {
    var frameX = 2
    var frameY = 2
    for (f in frames) {
      f.isOpen = false
      if (frameX > 300) {
        frameX = 2
        frameY += 28
      }
      f.x = frameX
      f.y = frameY
      frameX += f.width + 2
    }
  }
  @Throws(IOException::class)
  override fun mouseClicked(x: Int, y: Int, mb: Int) {
    super.mouseClicked(x, y, mb)
    for(frame in frames) {
      if(frame.isWithinHeaderAndOnTop(x, y) && mb == 0) {
        frame.setDrag(true)
        frame.setDragX(x - frame.x)
        frame.setDragY(y - frame.y)
      }
      if(frame.isWithinExtendRange(x, y) && (mb == 1 || mb == 0)) frame.isOpen = !frame.isOpen
      if(frame.isOpen && frame.components.isNotEmpty()) for(c in frame.components) c.mouseClicked(x, y, mb)
    }
  }

  override fun mouseReleased(x: Int, y: Int, mb: Int) {
    super.mouseReleased(x, y, mb)
    for(frame in frames) {
      frame.setDrag(false)
      if(frame.isOpen && frame.components.isNotEmpty()) for(c in frame.components) c.mouseReleased(x, y, mb)
    }
  }

  @Throws(IOException::class)
  override fun keyTyped(keyCode: Char, scanCode: Int) {
    super.keyTyped(keyCode, scanCode)
    for(frame in frames) {
      for(c in frame.components) {
        c.keyTyped(keyCode, scanCode)
      }
    }
  }

  override fun drawScreen(x: Int, y: Int, d: Float) {
    doScroll()
    super.drawScreen(x, y, d)
    drawDefaultBackground()
    frames.stream().sorted(Comparator.comparingInt(Frame::zIndex)).forEachOrdered { f: Frame ->
      f.render()
      f.updatePosition(x, y)
      for(c in f.components) {
        c.updateComponent(x, y)
      }
    }
  }

  private fun doScroll() {
    val w: Int = Mouse.getDWheel()
    if(w < 0) {
      for(frame in frames) {
        frame.y += 8
      }
    }
    else if(w > 0) {
      for(frame in frames) {
        frame.y -= 8
      }
    }
  }

  override fun onGuiClosed() {
    save()
    super.onGuiClosed()
  }

  override fun doesGuiPauseGame(): Boolean {
    return false
  }

  private fun save() {
    val gson = GsonBuilder().setPrettyPrinting().create()

    frames.forEach {
      val map = HashMap<String, Any>()

      map["Open"] = it.isOpen
      map["X"] = it.x
      map["Y"] = it.y
      FileManager.writeToFile(FileManager.BCM, arrayListOf(gson.toJson(map)), "${it.category.name}.json")
    }
    
  }

  private fun HashMap<String, Any>.getInt(key: String) = (get(key) as Double).toInt()

  fun load() {
    val gson = GsonBuilder().setPrettyPrinting().create()

    frames.forEach {
      if(File(FileManager.BCM, "${it.category.name}.json").exists()) {
        val ssMap = HashMap<String, Any>()::class.java
        val pog = gson.fromJson(FileManager.BCM?.let { it1 -> FileManager.readFileAsString(it1, "${it.category.name}.json") }, ssMap)
        it.isOpen = (pog["Open"] as Boolean?)!!
        it.x = pog.getInt("X")
        it.y = pog.getInt("Y")
      }
    }

  }
}