package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.Render3DEvent
import bubby.client.events.TickEvent
import bubby.client.utils.RenderUtils
import net.minecraft.tileentity.*

class ChestESP: Module("ChestESP", "show storage pog", -1, Category.Render) {
  private var chests = mutableListOf<TileEntity>()
  private var endChests = mutableListOf<TileEntity>()
  private var hoppers = mutableListOf<TileEntity>()
  private var shulkers = mutableListOf<TileEntity>()
  private var rChests = Setting<Boolean>("ChestESP Chests", this).withValue(true).add()
  private var rEndChests = Setting<Boolean>("ChestESP EndChests", this).withValue(true).add()
  private var rShulkers = Setting<Boolean>("ChestESP Shulkers", this).withValue(true).add()
  private var rHoppers = Setting<Boolean>("ChestESP Hoppers", this).withValue(true).add()
  private var fill = Setting<Boolean>("ChestESP Fill", this).withValue(true).add()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      chests.clear()
      endChests.clear()
      shulkers.clear()
      hoppers.clear()

      mc.world.loadedTileEntityList.forEach {
        if(it is TileEntityChest && rChests.value) {
          chests.add(it)
        } else if(it is TileEntityEnderChest && rEndChests.value) {
          endChests.add(it)
        } else if(it is TileEntityShulkerBox && rShulkers.value) {
          shulkers.add(it)
        } else if(it is TileEntityHopper && rHoppers.value) {
          hoppers.add(it)
        }
      }
    }
  }

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      chests.forEach { RenderUtils.drawBox(it.pos, 1.9f, 1.5f, 0.3f, 0.5f, fill.value) }
      endChests.forEach { RenderUtils.drawBox(it.pos, 1f, 0.05f, 1f, 0.5f, fill.value) }
      shulkers.forEach { RenderUtils.drawBox(it.pos, 0.5f, 0.2f, 1f, 0.5f, fill.value) }
      hoppers.forEach { RenderUtils.drawBox(it.pos, 0.45f, 0.45f, 0.6f, 0.5f, fill.value) }
    }
  }
}
