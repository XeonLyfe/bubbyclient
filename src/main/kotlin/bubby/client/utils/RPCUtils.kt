package bubby.client.utils

import bubby.client.BubbyClient
import club.minnced.discord.rpc.*
import net.minecraft.client.Minecraft

/* TODO: Fix this lmao. idk why but the other one doesn't seem to be working now either? Don't think I changed anything */
class RPCUtils() {
  val mc = Minecraft.getMinecraft()
  val discordRPC = DiscordRPC.INSTANCE

  fun run() {
    var richPresence = DiscordRichPresence()
    val handler = DiscordEventHandlers()
    discordRPC.Discord_Initialize("738423949238599771", handler, true, "")
    richPresence.smallImageKey = "small"
    richPresence.smallImageText = "uwu"
    richPresence.largeImageText = "${BubbyClient.name} ${BubbyClient.version}"
    richPresence.largeImageKey = "logo"

    Thread ({
      var detail: String
      while(!Thread.currentThread().isInterrupted()) {
        if(mc.world == null) {
          detail = "In the menus"
        } else {
          if(mc.isIntegratedServerRunning) {
            detail = "In singleplayer"
          } else {
            detail = "Playing on ${mc.currentServerData?.serverIP}"
          }
        }
        discordRPC.Discord_RunCallbacks()
        richPresence.details = detail
        richPresence.state = BubbyClient.version
        discordRPC.Discord_UpdatePresence(richPresence)
        try {
          Thread.sleep(4000L)
        } catch(ignored: InterruptedException) {}
      }
    }, "RPC-Callback-Handler").start()
  }

  fun stop() {
    discordRPC.Discord_Shutdown()
  }
}
/**/

/* this is def skidded lmao. no skidded code
class RPCUtils() {
  private val discordRPC: DiscordRPC = DiscordRPC.INSTANCE
  private var discordRichPresence: DiscordRichPresence
  val mc = Minecraft.getMinecraft()
  private var detail_option_1: String
  private var detail_option_2: String
  private var detail_option_3: String
  private var detail_option_4: String

  // State.
  private var state_option_1: String
  private var state_option_2: String
  private var state_option_3: String
  private var state_option_4: String
  fun stop() {
    discordRPC.Discord_Shutdown()
  }

  fun run() {
    discordRichPresence = DiscordRichPresence()
    val handler_ = DiscordEventHandlers()
    discordRPC.Discord_Initialize("738423949238599771", handler_, true, "")
    discordRichPresence.smallImageKey = "small"
    discordRichPresence.smallImageText = "uwu"
    discordRichPresence.largeImageText = "BubbyClient " + BubbyClient.version
    discordRichPresence.largeImageKey = "logo"
    Thread({
        while(!Thread.currentThread().isInterrupted) {
            try {
                if(mc.world == null) {
                    detail_option_1 = ""
                    detail_option_2 = "dunking on noobs"
                    state_option_1 = BubbyClient.version
                }
                else {
                    if(mc.player != null) {
                        detail_option_1 = ""
                    }
                    else {
                        detail_option_1 = ""
                    }
                    if(mc.isIntegratedServerRunning) {
                        detail_option_2 = "trashing newf*gs"
                        state_option_1 = BubbyClient.version
                    }
                    else {
                        detail_option_2 = "playing on " + mc.currentServerData!!.serverIP
                        state_option_1 = BubbyClient.version
                    }
                }
                val detail = detail_option_1 + detail_option_2 + detail_option_3 + detail_option_4
                val state = state_option_1 + state_option_2 + state_option_3 + state_option_4
                discordRPC.Discord_RunCallbacks()
                discordRichPresence.details = detail
                discordRichPresence.state = state
                discordRPC.Discord_UpdatePresence(discordRichPresence)
            }
            catch(exc: Exception) {
                exc.printStackTrace()
            }
            try {
                Thread.sleep(4000L)
            }
            catch(exc_: InterruptedException) {
                exc_.printStackTrace()
            }
        }
    }, "RPC-Callback-Handler").start()
  }

  fun set(presume: String): String {
    return " $presume"
  }

  init {
    discordRichPresence = DiscordRichPresence()
    detail_option_1 = ""
    detail_option_2 = ""
    detail_option_3 = ""
    detail_option_4 = ""
    state_option_1 = ""
    state_option_2 = ""
    state_option_3 = ""
    state_option_4 = ""
  }
}
*/
