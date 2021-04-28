package bubby.client.modules

import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.utils.CrystalUtils.mc
import bubby.client.utils.RenderUtils
import org.lwjgl.input.Keyboard
import java.awt.Font

class ClickGUIRina: Module("ClickGUI", "oink oink", Keyboard.KEY_RSHIFT, Category.Gui, false) {
  private var backgroundRed = Setting<Int>("BG Red", this)
    .withValue(0)
    .withMin(0)
    .withMax(255)
    .add();
  private var backgroundGreen = Setting<Int>("BG Green", this)
    .withValue(0)
    .withMin(0)
    .withMax(255)
    .add();
  private var backgroundBlue = Setting<Int>("BG Blue", this)
    .withValue(0)
    .withMin(0)
    .withMax(255)
    .add();

  private var baseRed = Setting<Int>("Red", this)
    .withValue(125)
    .withMin(0)
    .withMax(255)
    .add();
  private var baseGreen = Setting<Int>("Green", this)
    .withValue(0)
    .withMin(0)
    .withMax(255)
    .add();
  private var baseBlue = Setting<Int>("Blue", this)
    .withValue(160)
    .withMin(0)
    .withMax(255)
    .add();
  private var themee = arrayOf("Nodus", "nhack", "WeepCraft")
  private var theme = Setting<String>("ClickGui Theme", this).withValue("Nodus").withValues(themee).add()
  private val modes = arrayOf("Rina Gui", "Bubby Gui")
  private val mode = Setting<String>("Gui modes", this).withValue("Rina Gui").withValues(modes).add()
/*
  fun fontStyle() {
    when (mode.value) {
      "Normal" -> {
        Font.PLAIN
      }
    }
    when (mode.value) {
      "Bold" -> {
        Font.BOLD
      }
    }
    when (mode.value) {
      "Italic" -> {
        Font.ITALIC
      }
    }
  }


   */


  override fun onEnable() {
    when (mode.value) {
      "Rina Gui" -> {
        mc.displayGuiScreen(BubbyClient.moduleClickGUI)
        this.setToggled(false)
      }
    }
    when (mode.value) {
      "Bubby Gui" -> {
        mc.displayGuiScreen(BubbyClient.clickGuiManager)
        this.setToggled(false)
      }
    }
  }
}