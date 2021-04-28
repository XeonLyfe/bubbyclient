package bubby.client.gui.hud
import me.rina.gui.api.component.Component

class Players:
Component("Players", "Players", "Shows all nearby players and their distance", StringType.Use) {

  override fun onRender(partialTicks: Float) {

    val players = mc.world.playerEntities.filter{it != mc.player}.sortedBy{mc.player.getDistance(it)}.map{"${it.name} (${mc.player.getDistance(it).toInt()}) (${mc.player.health.toInt()})"}

    this.renderList(players.toList(), true)
  }
}
