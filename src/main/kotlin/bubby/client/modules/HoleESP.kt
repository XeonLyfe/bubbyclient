package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.api.setting.Setting
import bubby.client.events.Render3DEvent
import bubby.client.events.TickEvent
import bubby.client.utils.*

class HoleESP: Module("HoleESP", "Render boxes in good holes ;)", -1, Category.Render) {
  private val red = Setting<Float>("Obby Red", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val green = Setting<Float>("Obby Green", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val blue = Setting<Float>("Obby Blue", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val alpha = Setting<Float>("Obby Alpha", this).withValue(255f).withMin(0f).withMax(255f).add()

  private val bred = Setting<Float>("Bedrk Red", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val bgreen = Setting<Float>("Bedrk Green", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val bblue = Setting<Float>("Bedrk Blue", this).withValue(255f).withMin(0f).withMax(255f).add()
  private val balpha = Setting<Float>("Bedrk Alpha", this).withValue(255f).withMin(0f).withMax(255f).add()

  private var fill = Setting<Boolean>("HoleESP Fill", this).withValue(false).add()

  private var range = Setting<Int>("HoleESP Range", this).withValue(12).withMin(0).withMax(24).add()

  private val modes = arrayOf("Full", "Bottom")
  private val mode = Setting<String>("HoleESP Mode", this).withValue("Bottom").withValues(modes).add()


  private var holes = mutableListOf<Hole>()

  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      holes.clear()
      holes.addAll(HoleUtils.getHolesWithinRange(range.value))
    }
  }

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      holes.forEach {
        when(mode.value) {
          "Full" -> {
            when(it.safeBlockAmount) {
              5 -> RenderUtils.drawBox(it.blockPos, red.value / 255f, green.value / 255f, blue.value / 255f, alpha.value / 255f, fill.value)
              //@bubbyroosh what does this do, dm me if you see this
              6, 7, 8, 9 -> RenderUtils.drawBox(it.blockPos, red.value / 255f, green.value / 255f, blue.value / 255f, alpha.value / 255f, fill.value)
              10 -> RenderUtils.drawBox(it.blockPos, bred.value / 255f, bgreen.value / 255f, bblue.value / 255f, balpha.value / 255f, fill.value)
              else -> {
              }
            }
          }
          "Bottom" -> {
            when(it.safeBlockAmount) {
              5 -> RenderUtils.drawBoxBottom(it.blockPos, red.value / 255f, green.value / 255f, blue.value / 255f, alpha.value / 255f)
              //@bubbyroosh what does this do, dm me if you see this
              6, 7, 8, 9 -> RenderUtils.drawBoxBottom(it.blockPos, red.value / 255f, green.value / 255f, blue.value / 255f, alpha.value / 255f)
              10 -> RenderUtils.drawBoxBottom(it.blockPos, bred.value / 255f, bgreen.value / 255f, bblue.value / 255f, balpha.value / 255f)
              else -> {
              }
            }
          }
        }
      }
    }
  }
}
