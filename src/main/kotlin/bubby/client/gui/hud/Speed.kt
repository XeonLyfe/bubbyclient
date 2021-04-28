package bubby.client.gui.hud

import net.minecraft.util.math.MathHelper
import java.text.DecimalFormat
import me.rina.gui.api.component.Component as Component


class Speed:
  Component("Speed", "speed", "Displays the time", StringType.Use) {

  private val dec = DecimalFormat("#.#")


  override fun onRender(partialTicks: Float) {

    val deltaX = mc.player.posX - mc.player.prevPosX
    val deltaZ = mc.player.posZ - mc.player.prevPosZ
    val tickRate = mc.tickLength / 1000f


    val kmh = "KM/H: " + dec.format(((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate) * 3.6f).toDouble())

    this.render(kmh, 0, 0)

    this.rect.setWidth(getStringWidth(kmh))
    this.rect.setHeight(getStringHeight(kmh))
  }
}