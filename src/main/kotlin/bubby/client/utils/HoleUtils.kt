package bubby.client.utils

import bubby.client.BubbyClient.MC
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import java.util.*
import java.util.stream.Collectors

class Hole(var safeBlockAmount: Int, var blockPos: BlockPos)

class City(val player: EntityPlayer, val cityBlocks: MutableList<BlockPos>)

object HoleUtils {
  // Comment graph because shitty laptop can't open graph stuff and js is bloat
  // PP = Player
  // AA = Air
  // || = Bedrock/Obby
  //
  // Need to check for the following conditions left/right/forward/back
  //    PP  AA // Both need to be air because crystals need 2 high gap.
  //  ||PP||AA
  //    ||  || // Only need to check for this obby and the air. It will only check the entities in holes.
  //
  //  AA  PP
  //  AA||PP||
  //  ||  ||

  fun getCityEntities(range: Float): List<City> {
    return MC.world.playerEntities.parallelStream()
    .filter{MC.player.getDistance(it) <= range}
    .map{City(it, mutableListOf())}
    .filter{isEntityInHole(it.player)}
    .filter {
      val pos = EntityUtils.getBetterBlockPos(it.player)
      var isCity = false

      // This is kinda cringe ngl, but idk how else to do it that's readable enough
      // I do not think this works anyways lmao

      if(checkCity(pos.add(0, -1, 2), pos.add(0, 0, 2), pos.add(0, 1, 2))) {
        it.cityBlocks.add(pos.add(0, 0, 1))
        isCity = true
      }

      if(checkCity(pos.add(0, -1, -2), pos.add(0, 0, -2), pos.add(0, 1, -2))) {
        it.cityBlocks.add(pos.add(0, 0, -1))
        isCity = true
      }

      if(checkCity(pos.add(2, -1, 0), pos.add(2, 0, 0), pos.add(2, 1, 0))) {
        it.cityBlocks.add(pos.add(1, 0, 0))
        isCity = true
      }

      if(checkCity(pos.add(-2, -1, 0), pos.add(-2, 0, 0), pos.add(-2, 1, 0))) {
        it.cityBlocks.add(pos.add(-1, 0, 0))
        isCity = true
      }
      isCity
    }
    .collect(Collectors.toList())
  }

  fun checkCity(bottom: BlockPos, air1: BlockPos, air2: BlockPos): Boolean {
    return isObbyOrBedrock(bottom) > 0
    && MC.world.getBlockState(air1).block == Blocks.AIR
    && MC.world.getBlockState(air2).block == Blocks.AIR
  }

  fun getHolesWithinRange(range: Int): List<Hole> {
    return EntityUtils
    .getSphere(EntityUtils.getBetterBlockPos(MC.player), range).parallelStream()
    .map{Hole(0, it)}
    .filter{MC.world.getBlockState(it.blockPos).block == Blocks.AIR}
    .filter{MC.world.getBlockState(it.blockPos.add(0, 1, 0)).block == Blocks.AIR}
    .filter{MC.world.getBlockState(it.blockPos.add(0, 2, 0)).block == Blocks.AIR}
    .filter{checkBlock(it, it.blockPos.add(1, 0, 0)) > 0}
    .filter{checkBlock(it, it.blockPos.add(0, 0, 1)) > 0}
    .filter{checkBlock(it, it.blockPos.add(0, -1, 0)) > 0}
    .filter{checkBlock(it, it.blockPos.add(-1, 0, 0)) > 0}
    .filter{checkBlock(it, it.blockPos.add(0, 0, -1)) > 0}
    .collect(Collectors.toList())
  }

  fun checkBlock(hole: Hole, pos: BlockPos): Int {
    val blockType = isObbyOrBedrock(pos)
    hole.safeBlockAmount += blockType
    return blockType
  }

  fun isObbyOrBedrock(pos: BlockPos): Int {
    val block = MC.world.getBlockState(pos).block
    if(block == Blocks.BEDROCK) {
      return 2
    } else if(block == Blocks.OBSIDIAN) {
      return 1
    }

    return 0
  }

  fun isEntityInHole(entity: Entity): Boolean {
    val north = EntityUtils.getBetterBlockPos(entity, BlockPos(0, 0, -1))
    val south = EntityUtils.getBetterBlockPos(entity, BlockPos(0, 0, 1))
    val east = EntityUtils.getBetterBlockPos(entity, BlockPos(-1, 0, 0))
    val west = EntityUtils.getBetterBlockPos(entity, BlockPos(1, 0, 0))
    return (isObbyOrBedrock(north) > 0
    && isObbyOrBedrock(south) > 0
    && isObbyOrBedrock(east) > 0
    && isObbyOrBedrock(west) > 0)
  }
}
