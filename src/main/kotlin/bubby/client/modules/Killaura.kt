package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.events.*
import bubby.client.utils.EntityUtils
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumHand

class Killaura: Module("Killaura", "Bonk", -1, Category.Combat) {
  private var players = Setting<Boolean>("Killaura Players", this)
  .withValue(true)
  .add()

  private var mobs = Setting<Boolean>("Killaura Mobs", this)
  .withValue(true)
  .add()

  private var animals = Setting<Boolean>("Killaura Animals", this)
  .withValue(true)
  .add()

  private var delay = Setting<Boolean>("Killaura Delay", this)
  .withValue(true)
  .add()

  private val hands = arrayOf("Main", "Off");

  private var hand = Setting<String>("Killaura Hand", this)
  .withValue("Main")
  .withValues(hands)
  .add();

  private var range = Setting<Double>("Killaura Range", this)
  .withValue(6.0)
  .withMin(0.0)
  .withMax(6.0)
  .add()

  private var leftClickToggle = Setting<Boolean>("Killaura Click Toggle", this)
  .withValue(false)
  .add()

  private var clickToggle = true

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(leftClickToggle.value && clickToggle)
        return

      if(delay.value && mc.player.getCooledAttackStrength(0f) < 1f)
        return

      mc.world.loadedEntityList
      .filter{!it.isDead && it != mc.player}
      .filter{(it is EntityPlayer && players.value)
      || (it is EntityMob && mobs.value)
      || (it is EntityAnimal && animals.value)}
      .filter{!BubbyClient.friends.isFriend(it.name)}
      .filter{mc.player.getDistance(it) <= range.value}
      .sortedBy{it.getDistance(mc.player)}
      .forEach{
        when(hand.value) {
          "Main" -> EntityUtils.attackEntity(it, EnumHand.MAIN_HAND)
          "Off" -> EntityUtils.attackEntity(it, EnumHand.OFF_HAND)
        }
        return
      }
    }
  }

  // The event is fired when you press and release. We only want to keep track of the press.
  private var pressCheck = false
  @PogEvent
  fun onMouseClick(event: MouseClickEvent) {
    event.run {
      if(mc.player == null || mc.world == null)
        return

      pressCheck = !pressCheck
      if(pressCheck)
        if(button == 0)
          clickToggle = !clickToggle
    }
  }
}
