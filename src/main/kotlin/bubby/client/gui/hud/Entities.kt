package bubby.client.gui.hud

import me.rina.gui.api.component.Component
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer

class Entities:
Component("Entities", "Entities", "Shows all entities and how many", StringType.Use) {

  override fun onRender(partialTicks: Float) {

    var elist = mutableListOf<String>()
    val emap: MutableMap<String, Int> = HashMap()
    mc.world.loadedEntityList.filter{!(it is EntityPlayer)}
    .forEach{
      var name = it.name
      var add = 1
      if(it is EntityItem) {
        name = it.item.item.getItemStackDisplayName(it.item)
        add = it.item.count
      }

      val count = (if(emap.containsKey(name)) emap[name] else 0)!!
      emap[name] = count + add
    }
    for((key, value) in emap)
      elist.add("$key ($value)")
    this.renderList(elist.toList(), true)
  }
}
