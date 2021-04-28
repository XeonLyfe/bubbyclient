package bubby.api.chat

import bubby.client.BubbyClient
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting

object Chat {
  fun message(message: String) {
    BubbyClient.MC.player.sendMessage(TextComponentString("${TextFormatting.WHITE}[${TextFormatting.BLUE}BubbyClient${TextFormatting.WHITE}] $message"))
  }

  private fun message(message: String, level: String) {
    message("${TextFormatting.WHITE}[$level${TextFormatting.WHITE}] $message")
  }

  fun info(message: String) {
    message(message, "INFO")
  }

  fun error(message: String) {
    message(message, "ERROR")
  }
}
