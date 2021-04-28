package bubby.client
import bubby.api.event.EventManager
import bubby.api.module.ModuleManager
import bubby.api.setting.SettingManager
import bubby.client.gui.ClickGuiManager
import bubby.client.utils.*
import me.rina.gui.api.component.management.ComponentManager
import me.rina.gui.impl.module.ModuleClickGUI
import me.rina.gui.impl.overlay.ComponentClickGUI
import net.minecraft.client.Minecraft
import org.apache.logging.log4j.LogManager;

object BubbyClient {
  @kotlin.jvm.JvmField
  var name = "BubbyClient"
  val logger = LogManager.getLogger("BubbyClient")
  @JvmField
  val version = "git-${GIT_SHA.take(7)}"
  val buildate = "${GIT_DATE.take(10)}"
  private var discordRPC: RPCUtils? = null
  lateinit var clickGuiManager: ClickGuiManager
  val MC by lazy { Minecraft.getMinecraft() }
  val modules by lazy { ModuleManager() }
  val events by lazy { EventManager() }
  val settings by lazy { SettingManager() }
  val friends by lazy { FriendManager() }

  val capeUtils by lazy { CapeUtils() }

  val components by lazy { ComponentManager() }

  lateinit var moduleClickGUI: ModuleClickGUI
  lateinit var componentClickGUI: ComponentClickGUI;

  fun onInit() {

    logger.info("Loading $name $version...")
    moduleClickGUI = ModuleClickGUI()
    moduleClickGUI.onLoadList()

    componentClickGUI = ComponentClickGUI()

    Thread {
      clickGuiManager = ClickGuiManager()
      discordRPC = RPCUtils()
      discordRPC!!.run()

      modules.loadModules()
      components.onLoadList()
      FileManager.loadFriends()
      logger.info("Finished loading $name $version")

    }.start()
  }
}
