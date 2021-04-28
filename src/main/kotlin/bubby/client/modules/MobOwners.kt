package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent
import bubby.client.utils.APIManager

import net.minecraft.entity.passive.AbstractHorse
import net.minecraft.entity.passive.EntityTameable

class MobOwners: Module("MobOwners", " jaxc module", -1, Category.Misc) {

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      // TODO: Make this uncringe functional because functional > imperative
      for (entity in mc.world.getLoadedEntityList()) {
        if (!entity.customNameTag.contains("Owner: ")) {
          if (entity is EntityTameable) {
            if (entity.isTamed && entity.owner != null) {
              entity.alwaysRenderNameTag = true
              entity.customNameTag = entity.customNameTag + "Owner: " + entity.owner!!.displayName.formattedText
            }
          }
          if (entity is AbstractHorse) {
            if (entity.isTame && entity.ownerUniqueId != null) {
              entity.alwaysRenderNameTag = true
              entity.customNameTag = entity.customNameTag + "Owner: " + APIManager().resolveName(entity.ownerUniqueId.toString())
            }
          }
        }
      }
    }
  }

  @PogEvent
  override fun onDisable() {
    for (entity in mc.world.loadedEntityList) {
      if (entity is EntityTameable || entity is AbstractHorse) {
        try {
          entity.alwaysRenderNameTag = false
        }
        catch (ignored: Exception) {
        }
      }
    }
  }
}
