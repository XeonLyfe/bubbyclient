package bubby.client.modules
import bubby.api.event.PogEvent
import bubby.api.mixin.interfaces.ICPacketPlayer
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent
import bubby.client.utils.EntityUtils
import bubby.client.utils.HoleUtils
import net.minecraft.init.Blocks
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper

class Surround: Module("Surround", "mm feet", -1, Category.Combat) {
  private val autoToggle = Setting<Boolean>("Surround Auto toggle", this)
  .withValue(true)
  .add()

  private val center = Setting<Boolean>("Surround Center", this)
  .withValue(true)
  .add()

  private val bpt = Setting<Int>("Surround Blocks/Tick", this)
  .withValue(8)
  .withMin(1)
  .withMax(8)
  .add()

  private val client = Setting<Boolean>("Surround ClientSide Rotations", this)
  .withValue(false)
  .add()

  private val positions = arrayOf(
    BlockPos(1, -1, 0),
    BlockPos(1, 0, 0),
    BlockPos(-1, -1, 0),
    BlockPos(-1, 0, 0),
    BlockPos(0, -1, 1),
    BlockPos(0, 0, 1),
    BlockPos(0, -1, -1),
    BlockPos(0, 0, -1)
  )

  override fun onEnable() {
    super.onEnable()
    if(center.value) {
      val x = MathHelper.floor(mc.player.posX) + 0.5
      val z = MathHelper.floor(mc.player.posZ) + 0.5

      mc.player.setPosition(x, mc.player.posY, z)
      val pack = CPacketPlayer(true) as ICPacketPlayer
      pack.setX(x)
      pack.setZ(z)
      mc.player.connection.sendPacket(pack as CPacketPlayer)
    }
    oldSlot = EntityUtils.changeHotbarSlotToBlock(Blocks.OBSIDIAN)
  }

  override fun onDisable() {
    super.onDisable()
    if(oldSlot != -1)
      mc.player.inventory.currentItem = oldSlot

    oldSlot = -1
  }

  private var oldSlot = -1

  @PogEvent
  fun onTick(event: TickEvent) {
    if(oldSlot == -1)
      return

    if(HoleUtils.isEntityInHole(mc.player)) {
      if(autoToggle.value) {
        this.setToggled(false)
      }
      return
    }

    event.run {
      var counter = 1
      val positionsToPlace = positions
      .map { EntityUtils.getBetterBlockPos(mc.player, it) }
      .filter { mc.world.getBlockState(it).block == Blocks.AIR 
      || mc.world.getBlockState(it).material.isReplaceable }

      positionsToPlace.forEach {
        if(counter > bpt.value) return
        EntityUtils.lookAt(it, client.value)
        EntityUtils.placeOnBlock(it.add(0, -1, 0), EnumHand.MAIN_HAND)
        counter++
      }
    }
  }
}
