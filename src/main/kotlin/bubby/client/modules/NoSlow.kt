package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.mixin.interfaces.IEntity
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent

class NoSlow: Module("NoSlow", "Prevents some things from slowing you down", -1, Category.Movement) {
  private var webs = Setting<Boolean>("NoSlow Webs", this).withValue(true).add()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      if(webs.value) if((mc.player as IEntity).getInWeb()) (mc.player as IEntity).forceWeb(false)
    }
  }
}
