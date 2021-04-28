package bubby.client.modules

import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import org.lwjgl.input.Keyboard

class ClickGUI: Module("HUD Editor", "oink oink", Keyboard.KEY_GRAVE, Category.Gui, false) {
  private var stringRed = Setting<Int>("String Red", this).withValue(255).withMin(0).withMax(255).add();
  private var stringGreen = Setting<Int>("String Green", this).withValue(255).withMin(0).withMax(255).add();
  private var stringBlue = Setting<Int>("String Blue", this).withValue(255).withMin(0).withMax(255).add();

  override fun onEnable() {
    mc.displayGuiScreen(BubbyClient.componentClickGUI)
    this.setToggled(false)
  }
}
