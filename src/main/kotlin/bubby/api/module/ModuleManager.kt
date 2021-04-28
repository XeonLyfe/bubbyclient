package bubby.api.module
import bubby.api.event.PogEvent
import bubby.client.BubbyClient
import bubby.client.events.KeyEvent
import bubby.client.modules.*
import bubby.client.utils.FileManager
import com.google.gson.*
import java.io.*
import java.nio.file.*

class ModuleManager {
  private var modules = LinkedHashMap<String, Module>()

  init {
    addModule(AirJump())
    addModule(AirWalk())
    addModule(Ambiance())
    addModule(AutoArmour())
    addModule(AutoCrystal())
    addModule(AutoWalk())
    addModule(BlockOutline())
    addModule(BoatFly())
    addModule(AntiChainPop())
    addModule(AntiHunger())
    addModule(Capes())
    addModule(ChestESP())
    //addModule(CityESP())
    addModule(ClickGUI())
    addModule(ClickGUIRina())
    addModule(ChatStamps())
    addModule(Criticals())
    addModule(ElytraFly())
    addModule(ESP())
    addModule(FakePlayer())
    addModule(FastFall())
    addModule(FastUse())
    addModule(Fly())
    addModule(FOV())
    addModule(Freecam())
    addModule(FullBright())
    //addModule(GameMode())
    addModule(GreenText())
    addModule(HoleESP())
    //addModule(IRC())
    addModule(IceSpeed())
    addModule(NoFall())
    addModule(NoForceLook())
    addModule(Killaura())
    addModule(MiddleClickPearl())
    //addModule(MultiTask())
    addModule(Music())
    addModule(NoClip())
    addModule(NoRender())
    addModule(NoSlow())
    addModule(NoSwing())
    addModule(Offhand())
    //addModule(PlaySound())
    addModule(PopCounter())
    addModule(PortalESP())
    addModule(PortalGod())
    addModule(Portals())
    addModule(Spammer())
    addModule(MobOwners())
    addModule(Surround())
    addModule(Sprint())
    addModule(Timer())
    addModule(Tracers())
    addModule(Velocity())
    addModule(XCarry())

    BubbyClient.events.register(this)
  }

  fun addModule(module: Module) {
    modules[module.name] = module
  }

  fun getMods(): List<Module> {
    return modules.values.toList()
  }

  fun getToggledModules(): List<Module> {
    return modules.values.filter { m -> m.isToggled() && m.isVisible() }
  }

  fun getModuleByName(name: String): Module {
    return modules[name]!!
  }

  fun saveModule(module: Module) {
    val gson = GsonBuilder().setPrettyPrinting().create()

    val main = JsonObject();
    main.add("Enabled", JsonPrimitive(module.isToggled()))
    main.add("Bind", JsonPrimitive(module.bind))
    BubbyClient.settings.getSettingsByMod(module).forEach {
      when(it.value) {
        is Boolean -> main.add(it.name, JsonPrimitive(it.value as Boolean))
        is String -> main.add(it.name, JsonPrimitive(it.value as String))
        is Double -> main.add(it.name, JsonPrimitive(it.value as Double))
        is Float -> main.add(it.name, JsonPrimitive(it.value as Float))
        is Long -> main.add(it.name, JsonPrimitive(it.value as Long))
        is Int -> main.add(it.name, JsonPrimitive(it.value as Int))
      }
    }

    val json = gson.toJson(JsonParser().parse(main.toString()))
    FileManager.writeToFile(
      FileManager.BCM,
      arrayListOf(json),
      "${module.name}.json"
    )
  }

  private fun loadModule(module: Module) {

    if(File(FileManager.BCM, "${module.name}.json").exists()) {
      val main = JsonParser()
        .parse(
          FileManager.readFileAsString(FileManager.BCM, "${module.name}.json")
        )
        .getAsJsonObject()

      // like everything gets screwed up with this and idk why
      try {
        if(main["Enabled"].getAsBoolean()) {
          module.toggle()
        }
        //module.setToggled(main["Enabled"].getAsBoolean())
        module.bind = main["Bind"].getAsInt()
        BubbyClient.settings.getSettingsByMod(module).forEach{
          when(it.value) {
            is Boolean -> it.apply(main[it.name].getAsBoolean())
            is String -> it.apply(main[it.name].getAsString())
            is Double -> it.apply(main[it.name].getAsDouble())
            is Float -> it.apply(main[it.name].getAsFloat())
            is Long -> it.apply(main[it.name].getAsLong())
            is Int -> it.apply(main[it.name].getAsInt())
          }
        }
      } catch(ignored: Exception) {}
    }
  }

  fun loadModules() {
    modules.values.forEach { loadModule(it) }
  }

  @PogEvent
  fun onKeyPress(event: KeyEvent) {
    event.run {
      modules.values.filter { key == it.bind }.forEach { it.toggle() }
    }
  }
}
