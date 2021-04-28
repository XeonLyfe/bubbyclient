package bubby.client.utils

import bubby.api.mixin.interfaces.ICPacketPlayer
import bubby.client.BubbyClient
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.inventory.ClickType
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.network.play.client.*
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.MathHelper

object EntityUtils {
  var mc = BubbyClient.MC
  private val rightClickable = arrayOf(Blocks.ENDER_CHEST, Blocks.CHEST)

  fun getBetterBlockPos(entity: Entity, offset: BlockPos = BlockPos(0, 0, 0)): BlockPos {
    val vec = entity.getPositionVector()
    return BlockPos(vec.x, vec.y, vec.z).add(offset)
  }

  fun attackEntity(e: Entity, hand: EnumHand, packet: Boolean = false) {
    if(packet) {
      BubbyClient.MC.connection?.sendPacket(CPacketUseEntity(e))
      BubbyClient.MC.connection?.sendPacket(CPacketAnimation(hand))
    }
    BubbyClient.MC.playerController.attackEntity(mc.player, e)
    BubbyClient.MC.player.swingArm(hand)
    BubbyClient.MC.playerController.updateController()
  }

  fun getSphere(pos: BlockPos, radius: Int): List<BlockPos> {
    val cBlocks = mutableListOf<BlockPos>()
    val cx = pos.x
    val cy = pos.y
    val cz = pos.z
    for(x in cx - radius..cx + radius) {
      for(z in cz - radius..cz + radius) {
        for(y in cy - radius..cy + radius) {
          val dist = (cx - x) * (cx - x) + (cy - y) * (cy - y) + (cz - z) * (cz - z)
          if(dist < radius * radius) cBlocks.add(BlockPos(x, y, z))
        }
      }
    }
    return cBlocks
  }

  fun placeOnBlock(pos: BlockPos, hand: EnumHand) {
    val result = BubbyClient.MC.world.rayTraceBlocks(Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), Vec3d(pos.x + 0.5, pos.y - 0.5, pos.z + 0.5))
    val facing =
    if(result == null || result.sideHit == null)
      EnumFacing.UP
    else
      result.sideHit
    var flag = false
    if(rightClickable.contains(mc.world.getBlockState(pos).block)) {
      flag = true
      mc.player.connection.sendPacket(CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING))
    }

    mc.player.connection.sendPacket(CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0f, 0f, 0f))
    BubbyClient.MC.player.swingArm(hand)

    if(flag)
      mc.player.connection.sendPacket(CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING))
  }

  fun changeHotbarSlotToItem(item: Item): Int {
    var oldSlot = -1
    var itemSlot = -1
    for(i in 0..8) if(mc.player.inventory.getStackInSlot(i).item == item) {
      itemSlot = i
      oldSlot = mc.player.inventory.currentItem
      break
    }
    if(itemSlot != -1)
      mc.player.inventory.currentItem = itemSlot
    mc.playerController.updateController()
    return oldSlot
  }

  fun changeHotbarSlotToBlock(block: Block): Int {
    var oldSlot = -1
    var itemSlot = -1
    for(i in 0..8) {
      if(mc.player.inventory.getStackInSlot(i).item is ItemBlock
      && (mc.player.inventory.getStackInSlot(i).item
      as ItemBlock).block == block) {
      itemSlot = i
      oldSlot = mc.player.inventory.currentItem
      break
      }
    }
    if(itemSlot != -1)
      mc.player.inventory.currentItem = itemSlot
    mc.playerController.updateController()
    return oldSlot
  }

  fun changeOffhandSlotToItem(item: Item): Boolean {
    if(BubbyClient.MC.player.heldItemOffhand.item == item) {
      return true
    }

    var slott = -1

    for(i in 0..44) {
      if(BubbyClient.MC.player.inventory.getStackInSlot(i).item == item) {
        slott = i
        break
      }
    }
    if(slott != -1) {
      val slot = if(slott < 9) slott + 36 else slott
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player)
      mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player)
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player)
      return true
    }
    return false
  }

  fun lookAt(pos: BlockPos, clientSide: Boolean) {
    lookAt(pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, clientSide)
  }


  fun lookAt(x: Double, y: Double, z: Double, clientSide: Boolean) {
    val diffX = x - mc.player.posX
    val diffY = (y - mc.player.posY) * -1f // saw the player looking down when it should be up with clientside rotations :monocle: idk how servers didn't cuck place/break
    val diffZ = z - mc.player.posZ

    val len = MathHelper.sqrt(diffX * diffX + diffZ * diffZ)

    val yaw = MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90f)
    val pitch = MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffY, len.toDouble())))

    if(clientSide) {
      mc.player.rotationPitch = pitch.toFloat()
      mc.player.rotationYaw = yaw.toFloat()
    }
    var packet = CPacketPlayer(mc.player.onGround) as ICPacketPlayer
    packet.setPitch(pitch.toFloat())
    packet.setYaw(yaw.toFloat())
    mc.player.connection.sendPacket(packet as CPacketPlayer)
  }
}
