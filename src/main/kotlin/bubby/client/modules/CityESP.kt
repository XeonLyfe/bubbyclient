package bubby.client.modules
import bubby.api.event.PogEvent
import bubby.api.module.*
import bubby.api.setting.Setting
import bubby.client.events.Render3DEvent
import bubby.client.events.TickEvent
import bubby.client.utils.City
import bubby.client.utils.HoleUtils
import bubby.client.utils.RenderUtils

class CityESP: Module("CityESP", "Renders blocks that can be citied", -1, Category.Render) {
  private val range = Setting<Float>("CityESP Range", this)
  .withValue(12f)
  .withMin(0f)
  .withMax(20f)
  .add()

  private val waitTicks = Setting<Int>("CityESP WaitTicks", this)
  .withValue(10)
  .withMin(0)
  .withMax(20)
  .add()

  private val red = Setting<Float>("CityESP Red", this)
  .withValue(255f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val green = Setting<Float>("CityESP Green", this)
  .withValue(0f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val blue = Setting<Float>("CityESP Blue", this)
  .withValue(0f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val alpha = Setting<Float>("CityESP Alpha", this)
  .withValue(170f)
  .withMin(0f)
  .withMax(255f)
  .add()

  private val fill = Setting<Boolean>("CityESP Fill", this)
  .withValue(true)
  .add()

  var cities = mutableListOf<City>()
  @PogEvent
  fun onTick(event: TickEvent) {
    event.run {
      cities.clear()
      cities.addAll(HoleUtils.getCityEntities(range.value))
    }
  }

  @PogEvent
  fun onRender(event: Render3DEvent) {
    event.run {
      cities.forEach {
        it.cityBlocks.forEach {
          RenderUtils.drawBox(
            it,
            red.value / 255,
            green.value / 255,
            blue.value / 255,
            alpha.value / 255,
            fill.value
          )
        }
      }
    }
  }
}
