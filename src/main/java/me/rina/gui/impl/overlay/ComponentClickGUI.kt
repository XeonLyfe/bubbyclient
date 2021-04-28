package me.rina.gui.impl.overlay

import bubby.client.BubbyClient.components
import net.minecraft.client.gui.GuiScreen
import me.rina.turok.util.TurokDisplay
import me.rina.turok.hardware.mouse.TurokMouse
import me.rina.gui.GUIColor
import me.rina.turok.render.font.TurokFont
import me.rina.gui.impl.overlay.component.frame.ComponentListFrame
import bubby.client.BubbyClient
import me.rina.gui.api.flag.Flag
import me.rina.gui.api.frame.Frame
import me.rina.gui.impl.overlay.component.frame.ComponentFrame
import me.rina.gui.impl.module.category.CategoryFrame
import me.rina.turok.render.opengl.TurokRenderGL
import me.rina.turok.util.TurokMath
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Font
import java.util.ArrayList

/**
 * @author SrRina
 * @since 1/12/20 at 03:21pm
 */
class ComponentClickGUI : GuiScreen() {
  var display: TurokDisplay? = null
    private set
  var mouse: TurokMouse? = null
    private set
  @JvmField
  var guiColor: GUIColor
  private var loadedFrameList: ArrayList<Frame?>? = null
  private var focusedFrame: Frame? = null
  private var closedWidth = 0
  private var isClosingGUI = false
  @JvmField
  var fontComponentListFrame = TurokFont(Font("Whitney", 0, 24), true, true)
  @JvmField
  var fontWidgetComponent = TurokFont(Font("Whitney", 0, 16), true, true)
  fun init() {
    loadedFrameList = ArrayList()
    val componentListFrame = ComponentListFrame(this, "Component HUD")
    loadedFrameList!!.add(componentListFrame)
    for (components in components.componentList) {
      val componentFrame = ComponentFrame(this, components)
      loadedFrameList!!.add(componentFrame)
    }
  }

  fun moveFocusedFrameToTopMatrix() {
    if (focusedFrame != null) {
      loadedFrameList!!.remove(focusedFrame)
      loadedFrameList!!.add(focusedFrame)
    }
  }

  override fun doesGuiPauseGame(): Boolean {
    return false
  }

  override fun onGuiClosed() {
    components.onSaveList()
    for (frames in loadedFrameList!!) {
      frames!!.onScreenClosed()
      if (frames is ComponentListFrame) {
        frames.onSave()
      }
    }
    if (focusedFrame != null) {
      focusedFrame!!.onCustomScreenClosed()
    }
  }

  override fun initGui() {
    if (isClosingGUI) {
      isClosingGUI = false
    }
    for (frames in loadedFrameList!!) {
      frames!!.onScreenOpened()
    }
    if (focusedFrame != null) {
      focusedFrame!!.onCustomScreenOpened()
    }
  }

  public override fun keyTyped(charCode: Char, keyCode: Int) {
    if (keyCode == Keyboard.KEY_ESCAPE) {
      isClosingGUI = true
    }
  }

  public override fun mouseReleased(mx: Int, my: Int, button: Int) {
    for (frames in loadedFrameList!!) {
      frames!!.onMouseReleased(button)
      if (frames is CategoryFrame) {
        val categoryFrame = frames
        if (categoryFrame.verify(mouse)) {
          focusedFrame = categoryFrame
        }
      }
    }
    if (focusedFrame != null) {
      focusedFrame!!.onCustomMouseReleased(button)
    }
  }

  public override fun mouseClicked(mx: Int, my: Int, button: Int) {
    for (frames in loadedFrameList!!) {
      frames!!.onMouseClicked(button)
      if (frames is CategoryFrame) {
        val categoryFrame = frames
        if (categoryFrame.verify(mouse)) {
          focusedFrame = categoryFrame
        }
      }
    }
    if (focusedFrame != null) {
      focusedFrame!!.onCustomMouseClicked(button)
    }
  }

  override fun drawScreen(mx: Int, my: Int, partialTicks: Float) {
    guiColor.onUpdate()
    display = TurokDisplay(mc)
    display!!.partialTicks = partialTicks
    TurokRenderGL.init(display)
    mouse = TurokMouse(mx, my)
    TurokRenderGL.init(mouse)
    TurokRenderGL.autoScale()
    TurokRenderGL.disable(GL11.GL_TEXTURE_2D)
    for (frames in loadedFrameList!!) {
      TurokRenderGL.enable(GL11.GL_SCISSOR_TEST)
      TurokRenderGL.drawScissor(0, 0, closedWidth, display!!.scaledHeight)
      frames!!.onRender()
      TurokRenderGL.disable(GL11.GL_SCISSOR_TEST)
      if (frames.verifyFocus(mx, my)) {
        focusedFrame = frames
      }
      if (frames is ComponentListFrame) {
        frames.flagMouse = Flag.MouseNotOver
        frames.flagOffsetMouse = Flag.MouseNotOver
      }
      if (frames is ComponentFrame) {
        frames.flagMouse = Flag.MouseNotOver
      }
    }
    if (focusedFrame != null) {
      focusedFrame!!.onCustomRender()
    }
    TurokRenderGL.disable(GL11.GL_TEXTURE_2D)
    TurokRenderGL.disable(GL11.GL_BLEND)
    TurokRenderGL.enable(GL11.GL_TEXTURE_2D)
    TurokRenderGL.color(255, 255, 255)
    val closingValueCalculated = 0
    if (isClosingGUI) {
      closedWidth = TurokMath.lerp(closedWidth, 0, display!!.partialTicks).toInt()
      if (closedWidth <= closingValueCalculated) {
        onGuiClosed()
        mc.displayGuiScreen(null)
      }
    } else {
      closedWidth = TurokMath.lerp(closedWidth, display!!.scaledWidth, display!!.partialTicks)
        .toInt()
    }
    components.onCornerDetectorComponentList(partialTicks)
  }

  init {
    TurokRenderGL.init()
    guiColor = GUIColor()
    init()
  }
}