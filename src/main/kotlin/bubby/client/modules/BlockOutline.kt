package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.Render3DEvent
import bubby.client.utils.RenderUtils
import net.minecraft.util.math.RayTraceResult

class BlockOutline: Module("BlockOutline", "Outlines the block you're looking at", -1, Category.Render) {
  private val red = Setting<Float>("BlockOutline Red", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val green = Setting<Float>("BlockOutline Green", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val blue = Setting<Float>("BlockOutline Blue", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val alpha = Setting<Float>("BlockOutline Alpha", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val fill = Setting<Boolean>("BlockOutline Fill", this).withValue(false).add()

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      val rtr = mc.objectMouseOver
      if(rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
        val pos = rtr?.blockPos
        RenderUtils.drawBox(pos!!, red.value / 255f, green.value / 255f, blue.value / 255f, alpha.value / 255f, fill.value)
      }
    }
  }
}
