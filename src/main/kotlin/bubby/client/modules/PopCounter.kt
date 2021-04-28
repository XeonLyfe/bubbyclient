package bubby.client.modules

import bubby.api.chat.Chat
import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.*
import net.minecraft.entity.Entity
import net.minecraft.network.play.server.SPacketEntityStatus

class PopCounter: Module("PopCounter", "Sends notifications for totem pops", -1, Category.Combat) {
  private var pops = HashMap<Entity, Int>()

  override fun onDisable() {
    super.onDisable()
    pops.clear()
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      pops.keys.filter{it.isDead}.forEach{
        Chat.info("${it.name} died after popping ${pops.get(it)} totems")
        pops.remove(it)
      }
    }
  }

  @PogEvent
  fun onReadPacket(event: ReadPacketEvent) {
    event.run {
      if(packet is SPacketEntityStatus
      && packet.opCode.toInt() == 35) {
        handlePop(packet.getEntity(mc.world))
      }
    }
  }

  private fun handlePop(entity: Entity) {
    if(entity == mc.player) return

    if(pops[entity] == null) {
      pops[entity] = 1
      Chat.info("${entity.name} popped 1 totem")
    }
    else {
      var popc: Int = pops[entity]!!
      popc++
      pops[entity] = popc
      Chat.info("${entity.name} popped $popc totems")
    }
  }
}
