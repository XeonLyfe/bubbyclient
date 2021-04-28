package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.events.Render3DEvent
import bubby.client.utils.RenderUtils
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.*
import net.minecraft.util.math.Vec3d

class Tracers: Module("Tracers", "line entity", -1, Category.Render) {
  var x = 0.0
  var y = 0.0
  var z = 0.0
  private var players = Setting<Boolean>("Tracers Players", this).withValue(true).add()
  private var animals = Setting<Boolean>("Tracers Animals", this).withValue(true).add()
  private var mobs = Setting<Boolean>("Tracers Mobs", this).withValue(true).add()
  private var crystals = Setting<Boolean>("Tracers Crystals", this).withValue(true).add()
  private var items = Setting<Boolean>("Tracers Items", this).withValue(true).add()
  private var chests = Setting<Boolean>("Tracers Chests", this).withValue(true).add()
  private var endChests = Setting<Boolean>("Tracers EndChests", this).withValue(true).add()
  private var shulkers = Setting<Boolean>("Tracers Shulkers", this).withValue(true).add()
  private var thiccness = Setting<Float>("Tracers Thickness", this).withValue(2f).withMin(0f).withMax(5f).add()

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      val thicc = thiccness.value
      val eyes = Vec3d(0.0, 0.0, 75.0)
      .rotatePitch(-Math.toRadians(mc.player.rotationPitch.toDouble()).toFloat())
      .rotateYaw(-Math.toRadians(mc.player.rotationYaw.toDouble()).toFloat())
      .add(mc.player.positionVector.add(0.0, mc.player.getEyeHeight().toDouble(), 0.0))

      mc.world.getLoadedEntityList().forEach {
        x = it.posX
        y = it.posY + it.height / 2.0
        z = it.posZ

        if(it is EntityPlayer && it != mc.player && players.value) if(BubbyClient.friends.isFriend(it.getName())) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 0.5f, 1f, 0.5f, 1f, thicc)
        else RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 0.8f, 0f, 0.1f, 1f, thicc)
        else if(it is EntityMob && mobs.value) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 0.5f, 0.5f, 1f, 1f, thicc)
        else if(it is EntityAnimal && animals.value) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 0.9f, 1f, 0.3f, 1f, thicc)
        else if(it is EntityItem && items.value) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 1f, 0.7f, 0f, 1f, thicc)
        else if(it is EntityEnderCrystal && crystals.value) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 1f, 0f, 1f, 1f, thicc)
      }

      mc.world.loadedTileEntityList.forEach {
        x = it.pos.x + 0.5
        y = it.pos.y + 0.5
        z = it.pos.z + 0.5

        if(it is TileEntityChest && chests.value) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 1.9f, 1.5f, 0.3f, 1f, thicc)
        else if(it is TileEntityEnderChest && endChests.value) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 1f, 0.05f, 1f, 1f, thicc)
        else if(it is TileEntityShulkerBox && shulkers.value) RenderUtils.drawLine(x, y, z, eyes.x, eyes.y, eyes.z, 0.5f, 0.2f, 1f, 1f, thicc)
      }
    }
  }
}
