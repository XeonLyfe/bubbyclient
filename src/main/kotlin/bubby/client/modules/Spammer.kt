package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.TickEvent

class Spammer: Module("Spammer", "Spams stuff from Spam.txt in chat. Toggle to refresh entries", -1, Category.Misc) {
  private var spams = listOf<String>()
  private var index = 0
  private var counter = 0
  private var delay = Setting<Int>("Spammer Delay", this).withValue(5).withMin(0).withMax(20).add()

  override fun onEnable() {
    index = 0
    super.onEnable()
  }

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      counter++
      if(counter >= delay.value * 20) {
        mc.player.sendChatMessage(spams[index])
        index++
        if(index > spams.size - 1) index = 0

        counter = 0
      }
    }
  }
}
