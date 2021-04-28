package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.Render3DEvent
import bubby.client.events.TickEvent
import bubby.client.utils.*
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos

class PortalESP: Module("PortalESP", "see portals in range", -1, Category.Render) {
  private var portals = mutableListOf<BlockPos>()
  private var counter = 0
  private var range = Setting<Int>("PortalESP Range", this).withValue(32).withMin(0).withMax(128).add()
  private val red = Setting<Float>("Portal Red", this).withValue(150f).withMin(0f).withMax(255f).add()
  private val green = Setting<Float>("Portal Green", this).withValue(0f).withMin(0f).withMax(255f).add()
  private val blue = Setting<Float>("Portal Blue", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val alpha = Setting<Float>("Portal Alpha", this).withValue(100f).withMin(0f).withMax(255f).add()
  private var fill = Setting<Boolean>("PortalESP Fill", this).withValue(false).add()
  private var waitTicks = Setting<Int>("PortalESP WaitTicks", this).withValue(10).withMin(0).withMax(20).add()

  @PogEvent
  fun onTick(event: TickEvent) {
    counter++

    if(counter < waitTicks.value)
      return
    counter = 0

    portals.clear()
    event.run {
      EntityUtils
      .getSphere(EntityUtils.getBetterBlockPos(mc.player), range.value)
      .parallelStream()
      .filter{mc.world.getBlockState(it).block == Blocks.PORTAL}
      .forEach{portals.add(it)}

      /*
      portals.clear()
      for(x in -range.value..range.value)
        for(y in -range.value..range.value)
          for(z in -range.value..range.value) {
        val pos = BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z)
        if(mc.world.getBlockState(pos).block == Blocks.PORTAL) portals.add(pos)
      }
        */
    }
  }

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      portals.forEach {
        RenderUtils.drawBox(AxisAlignedBB(it.x.toDouble(), it.y.toDouble(), it.z.toDouble() + 0.4, it.x.toDouble() + 1.0, it.y.toDouble() + 1.0, it.z.toDouble() + 0.6), red.value / 255f, green.value / 255f, blue.value / 255f, alpha.value / 255f, fill.value)
      }
    }
  }
}
