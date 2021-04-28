package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.mixin.interfaces.ITextComponentString
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.BubbyClient.MC
import bubby.client.events.ReadPacketEvent
import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.network.play.server.SPacketChat
import net.minecraft.util.text.TextComponentString
import java.text.SimpleDateFormat
import java.util.*

class ChatStamps: Module("ChatStamps", "show timestamps", -1, Category.Misc, true) {
  @PogEvent
  fun onPacket(event: ReadPacketEvent) {
    if(event.packet is SPacketChat) {
      val sPacketChat = event.packet
      if(sPacketChat.chatComponent is TextComponentString) {
        val textComponentString = sPacketChat.chatComponent as ITextComponentString
        textComponentString.setText(ChatFormatting.BLUE.toString() + "<" + ChatFormatting.GRAY + SimpleDateFormat("h:mm a").format(Date()) + ChatFormatting.BLUE + "> " + (sPacketChat.chatComponent as TextComponentString).text)
      }
    }
  }
}
