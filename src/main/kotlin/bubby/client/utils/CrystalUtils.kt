package bubby.client.utils
import bubby.client.BubbyClient
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Explosion
import net.minecraft.world.World
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.init.MobEffects
import net.minecraft.util.CombatRules
import net.minecraft.util.DamageSource
import net.minecraft.enchantment.EnchantmentHelper;

object CrystalUtils {
  var mc = BubbyClient.MC
  fun canPlace(pos: BlockPos, multi: Boolean): Boolean {
    val up1 = pos.add(0, 1, 0)
    val up2 = pos.add(0, 2, 0)

    if(multi) {
      return ((mc.world.getBlockState(pos).block === Blocks.OBSIDIAN 
      || mc.world.getBlockState(pos).block === Blocks.BEDROCK)) 
      && mc.world.getBlockState(up1).block === Blocks.AIR 
      && mc.world.getBlockState(up2).block === Blocks.AIR 
      && mc.world.getEntitiesWithinAABB<Entity>(Entity::class.java, AxisAlignedBB(up1)).isEmpty()
      && mc.world.getEntitiesWithinAABB<Entity>(Entity::class.java, AxisAlignedBB(up2)).isEmpty()
    }
    else {
      return ((mc.world.getBlockState(pos).block === Blocks.OBSIDIAN 
      || mc.world.getBlockState(pos).block === Blocks.BEDROCK)) 
      && mc.world.getBlockState(up1).block === Blocks.AIR 
      && mc.world.getBlockState(up2).block === Blocks.AIR
      && checkEntities(up1)
      && checkEntities(up2)
    }
  }

  fun checkEntities(pos: BlockPos): Boolean {
    mc.world.getEntitiesWithinAABB<Entity>(Entity::class.java, AxisAlignedBB(pos)).forEach{
      if(!(it is EntityEnderCrystal)) 
        return false
    }
    return true
  }

  fun calculateDamage(pos: BlockPos, entity: Entity): Float {
    return calculateDamage(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5, entity)
  }

  @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
  fun calculateDamage(posX: Double, posY: Double, posZ: Double, entity: Entity): Float {
    val doubleExplosionSize = 12.0
    val distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize
    val vec3d = Vec3d(posX, posY, posZ)
    var blockDensity = 0.0
    try {
      blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox()).toDouble()
    } 
    catch(ex: Exception) {
    }

    val v = (1.0 - distancedsize) * blockDensity
    val damage = ((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0).toInt().toFloat()
    var finald = 1.0
    if(entity is EntityLivingBase)
      finald = getBlastReduction(entity, getDamageMultiplied(damage), Explosion(BubbyClient.MC.world, null, posX, posY, posZ, 6.0f, false, true)).toDouble()
    return finald.toFloat()
  }

  fun getBlastReduction(entity: EntityLivingBase, damageI: Float, explosion: Explosion?): Float {
    var damage = damageI
    if(entity is EntityPlayer) {
      val ep: EntityPlayer = entity
      val ds: DamageSource = DamageSource.causeExplosionDamage(explosion)
      damage = CombatRules.getDamageAfterAbsorb(damage, ep.totalArmorValue.toFloat(), ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue().toFloat())
      var k = 0
      try {
        k = EnchantmentHelper.getEnchantmentModifierDamage(ep.armorInventoryList, ds)
      } 
      catch(ex: Exception) {
      }
      val f: Float = MathHelper.clamp(k.toFloat(), 0.0f, 20.0f)
      damage *= 1.0f - f / 25.0f
      if(entity.isPotionActive(MobEffects.RESISTANCE))
        damage -= damage / 4.0f
      damage = Math.max(damage, 0.0f)
      return damage
    }
    damage = CombatRules.getDamageAfterAbsorb(damage, entity.totalArmorValue.toFloat(), entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue().toFloat())
    return damage
  }

  fun getDamageMultiplied(damage: Float): Float {
    val diff: Int = BubbyClient.MC.world.difficulty.id
    return damage * if(diff == 0) 0.0f else if(diff == 2) 1.0f else if(diff == 1) 0.5f else 1.5f
  }
}
