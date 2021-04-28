package bubby.client.gui.hud

import me.rina.gui.api.component.Component
import net.minecraft.init.Items


class TotemCount:
  Component("Totem Count", "Totem Count", "Displays the client and its version", StringType.Use) {

  override fun onRender(partialTicks: Float) {
    var totemCount = "Totems: " + this.getTotemCount()
    this.render(totemCount, 0, 0)

    this.rect.setWidth(getStringWidth(totemCount))
    this.rect.setHeight(getStringHeight(totemCount))

  }
  private fun getTotemCount() {
    var totems = 0
    for (i in 0..44) {
      val stack = mc.player.inventory.getStackInSlot(i)
      if (stack.item === Items.TOTEM_OF_UNDYING) {
        totems += stack.count
      }
    }
  }
}
