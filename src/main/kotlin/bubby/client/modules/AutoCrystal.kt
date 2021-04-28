package bubby.client.modules
import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.*
import bubby.client.BubbyClient
import bubby.client.utils.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemPickaxe
import net.minecraft.item.ItemEndCrystal
import net.minecraft.network.play.server.SPacketSoundEffect
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import java.util.stream.Collectors

class AutoCrystal: Module("AutoCrystal", "Places/breaks crystals automatically", -1, Category.Combat) {

  private val hands = arrayOf("Main", "Off")

  private val friends = Setting<Boolean>("AutoCrystal Friends", this)
  .withValue(true)
  .add()

  private val place = Setting<Boolean>("AutoCrystal Place", this)
  .withValue(true)
  .add()

  private val placeRange = Setting<Int>("AutoCrystal Place Range", this)
  .withValue(6)
  .withMin(1) .withMax(8)
  .add()

  private val placeDelay = Setting<Int>("AutoCrystal Place Delay", this)
  .withValue(3)
  .withMin(0)
  .withMax(20)
  .add()

  private val placeHand = Setting<String>("AutoCrystal Place Hand", this)
  .withValue("Main")
  .withValues(hands)
  .add()

  private val breakk = Setting<Boolean>("AutoCrystal Break", this)
  .withValue(true)
  .add()

  private val walls = Setting<Boolean>("AutoCrystal Break Walls", this)
  .withValue(false)
  .add()

  private val breakRange = Setting<Int>("AutoCrystal Break Range", this)
  .withValue(6)
  .withMin(1)
  .withMax(8)
  .add()

  private val breakDelay = Setting<Int>("AutoCrystal Break Delay", this)
  .withValue(3)
  .withMin(0)
  .withMax(20)
  .add()

  private val breakHand = Setting<String>("AutoCrystal Break Hand", this)
  .withValue("Main")
  .withValues(hands)
  .add()

  private val clientSide = Setting<Boolean>("AutoCrystal Break ClientSide", this)
  .withValue(false)
  .add()

  private val breakAttempts = Setting<Int>("AutoCrystal Break Attempts", this)
  .withValue(1)
  .withMin(1)
  .withMax(6)
  .add()

  private val autoSwitch = Setting<Boolean>("AutoCrystal Auto Switch", this)
  .withValue(true)
  .add()

  private val mining = Setting<Boolean>("AutoCrystal Pickaxe", this)
  .withValue(false)
  .add()

  private val multiplace = Setting<Boolean>("AutoCrystal Multi Place", this)
  .withValue(true)
  .add()


  private val maxSelfDMG = Setting<Float>("AutoCrystal MaxSelfDMG", this)
  .withValue(6f)
  .withMin(0f)
  .withMax(20f)
  .add()

  private val minEnemyDMG = Setting<Float>("AutoCrystal MinEnemyDMG", this)
  .withValue(8f)
  .withMin(0f)
  .withMax(36f)
  .add()

  private val faceplace = Setting<Float>("AutoCrystal FacePlace", this)
  .withValue(8f)
  .withMin(0f)
  .withMax(37f)
  .add()

  // Just putting for funy :)
  // So this was just added for funy but it actually showed that the rotations are SHIT
  private val clientSideRotations = Setting<Boolean>("AutoCrystal ClientSide Rotations", this)
  .withValue(false)
  .add()

  private val red = Setting<Float>("AutoCrystal Red", this)
  .withValue(255f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val green = Setting<Float>("AutoCrystal Green", this)
  .withValue(0f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val blue = Setting<Float>("AutoCrystal Blue", this)
  .withValue(0f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val alpha = Setting<Float>("AutoCrystal Alpha", this)
  .withValue(170f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val fill = Setting<Boolean>("AutoCrystal Fill", this)
  .withValue(true)
  .add()

  private var pDelay = 0
  private var bDelay = 0

  var pogBlock: BlockPos? = null
  var oldSlot = -1

  override fun onEnable() {
    pDelay = 0
    bDelay = 0
    super.onEnable()
  }

  override fun onDisable() {
    if(oldSlot != -1)
      mc.player.inventory.currentItem = oldSlot
    oldSlot = -1
    super.onDisable()
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      pDelay++
      bDelay++

      if(mining.value && mc.player.getHeldItemMainhand().item is ItemPickaxe) {
        return
      }

      if(!autoSwitch.value && mc.player.getHeldItemMainhand().item !is ItemEndCrystal) {
        return
      }

      if(place.value && pDelay >= placeDelay.value) {
        pDelay = 0
        val target = getTarget()
        if(target != null) {
          pogBlock = getBlock(target)
          if(pogBlock != null) {
            if(placeHand.value == "Main") {
              if(oldSlot == -1 && autoSwitch.value) {
                oldSlot = EntityUtils.changeHotbarSlotToItem(Items.END_CRYSTAL)
              }
              EntityUtils.changeHotbarSlotToItem(Items.END_CRYSTAL)
            }
            EntityUtils.lookAt(pogBlock!!, clientSideRotations.value)
            when(placeHand.value) {
              "Main" -> EntityUtils.placeOnBlock(pogBlock!!, EnumHand.MAIN_HAND)
              "Off" -> EntityUtils.placeOnBlock(pogBlock!!, EnumHand.OFF_HAND)
            }
          }
        }
        else {
          pogBlock = null
        }
      }

      if(breakk.value && bDelay >= breakDelay.value) {
        bDelay = 0
        val crystal: Entity? = getCrystal()
        if(crystal != null) {
          EntityUtils.lookAt(EntityUtils.getBetterBlockPos(crystal), clientSideRotations.value)
          for(i in 0..breakAttempts.value - 1) {
            when(breakHand.value) {
              "Main" -> EntityUtils.attackEntity(crystal, EnumHand.MAIN_HAND)
              "Off" -> EntityUtils.attackEntity(crystal, EnumHand.OFF_HAND)
            }
          }
          if(clientSide.value)
            crystal.setDead()
        }
      }
    }
  }

  @PogEvent
  fun onPacketRead(event: ReadPacketEvent) {
    event.run {
      if(packet is SPacketSoundEffect) {
        if (packet.category == SoundCategory.BLOCKS && packet.sound == SoundEvents.ENTITY_GENERIC_EXPLODE) {
          mc.world.loadedEntityList
          .filter{it is EntityEnderCrystal}
          .filter{it.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6f}
          .forEach{it.setDead()}
        }
      }
    }
  }

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      if(pogBlock != null)
        RenderUtils.drawBox(pogBlock!!, red.value / 255, green.value / 255, blue.value / 255, alpha.value / 255, fill.value)
    }
  }

  fun getTarget(): EntityPlayer? {
    return mc.world.playerEntities.parallelStream()
      .filter { it != mc.player }
      .filter { mc.player.getDistance(it) <= 13 }
      .filter { !(BubbyClient.friends.isFriend(it.name) && friends.value) }
      .filter { !(HoleUtils.isEntityInHole(it) && it.health + it.absorptionAmount >= faceplace.value) }
      .collect(Collectors.toList())
      .sortedBy { mc.player.getDistanceSq(it) }
      .firstOrNull()
  }

  fun getBlock(target: EntityPlayer): BlockPos? {
    return EntityUtils.getSphere(EntityUtils.getBetterBlockPos(mc.player), placeRange.value).parallelStream()
      .filter { CrystalUtils.canPlace(it, multiplace.value) }
      .filter { CrystalUtils.calculateDamage(it, mc.player) <= maxSelfDMG.value }
      .filter {
        if(target.health + target.absorptionAmount >= faceplace.value)
          CrystalUtils.calculateDamage(it, target) >= minEnemyDMG.value
        else
          true
      }
      .collect(Collectors.toList())
      .sortedByDescending { CrystalUtils.calculateDamage(it, target) }
      .firstOrNull()
  }

  fun getCrystal(): Entity? {
    return mc.world.loadedEntityList.parallelStream()
      .filter{ it is EntityEnderCrystal }
      .filter{ if(walls.value) true else mc.player.canEntityBeSeen(it) }
      .filter { it.getDistance(mc.player) <= breakRange.value }
      .filter{ CrystalUtils.calculateDamage(it.posX, it.posY, it.posZ, mc.player) <= maxSelfDMG.value }
      .collect(Collectors.toList())
      .sortedBy{ mc.player.getDistance(it) }
      .firstOrNull()
  }
}
