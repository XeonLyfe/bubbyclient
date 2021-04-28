package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.BubbyClient
import bubby.client.events.Render3DEvent
import bubby.client.events.TickEvent
import bubby.client.utils.RenderUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer

class ESP: Module("ESP", "see stuff through walls pog", -1, Category.Render) {
  private var rPlayers = Setting<Boolean>("ESP Players", this).withValue(true).add()
  private var rMobs = Setting<Boolean>("ESP Mobs", this).withValue(true).add()
  private var rAnimals = Setting<Boolean>("ESP Animals", this).withValue(true).add()
  private var rItems = Setting<Boolean>("ESP Items", this).withValue(true).add()
  private var rCrystals = Setting<Boolean>("ESP Crystals", this).withValue(true).add()
  private var fill = Setting<Boolean>("ESP Fill", this).withValue(false).add()
  private var modes = arrayOf("Glow, Box")


  // TODO: Nicer looking ESP
  private var mode = Setting<String>("ESP Mode", this)
    .withValue("Glow")
    .withValues(modes)
    //.add()
  private var players = mutableListOf<Entity>()
  private var friends = mutableListOf<Entity>()
  private var mobs = mutableListOf<Entity>()
  private var animals = mutableListOf<Entity>()
  private var items = mutableListOf<Entity>()
  private var crystals = mutableListOf<Entity>()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      players.clear()
      friends.clear()
      mobs.clear()
      animals.clear()
      items.clear()
      crystals.clear()

      mc.world.getLoadedEntityList().forEach {
          if(it is EntityPlayer && rPlayers.value && it != mc.player)
            if(BubbyClient.friends.isFriend(it.getName()))
              friends.add(it)
          else players.add(it)
          else if(it is EntityMob && rMobs.value) mobs.add(it)
          else if(it is EntityAnimal && rAnimals.value) animals.add(it)
          else if(it is EntityItem && rItems.value) items.add(it)
          else if(it is EntityEnderCrystal && rCrystals.value) crystals.add(it)
        }
    }
  }

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      players.forEach { RenderUtils.drawBox(it.entityBoundingBox, 0.8f, 0f, 0.1f, 1f, fill.value) }
      friends.forEach { RenderUtils.drawBox(it.entityBoundingBox, 0.4f, 1f, 0f, 1f, fill.value) }
      mobs.forEach { RenderUtils.drawBox(it.entityBoundingBox, 0.5f, 0.5f, 1f, 1f, fill.value) }
      animals.forEach { RenderUtils.drawBox(it.entityBoundingBox, 0.9f, 1f, 0.3f, 1f, fill.value) }
      items.forEach { RenderUtils.drawBox(it.entityBoundingBox, 0.9f, 0.5f, 0.1f, 1f, fill.value) }
      crystals.forEach { RenderUtils.drawBox(it.entityBoundingBox, 1f, 0f, 0.8f, 1f, fill.value) }
    }
  }
}
