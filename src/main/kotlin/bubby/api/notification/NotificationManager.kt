package bubby.api.notification
// Idk if ANY of this works. didn't even compile to test :)

import java.awt.Font

import java.util.ArrayDeque

import me.rina.turok.render.font.TurokFont

import net.minecraft.client.Minecraft

class NotificationManager {

  val mc = Minecraft.getMinecraft()

  var corner = Corner.BottomRight

  val notifications = ArrayDeque<Notification>()

  var font = TurokFont(Font("Whitney", 0, 16), true, true)

  fun render() {
    notifications.removeIf{ it.shouldPop() }

    var offX = if(corner == Corner.TopLeft || corner == Corner.BottomLeft) {0} else {mc.displayWidth - notifications.element().width}
    var offY = if(corner == Corner.TopLeft || corner == Corner.TopRight) {0} else {mc.displayHeight - notifications.element().height}

    notifications.forEach {
      it.render(offX, offY, font)

      if(corner == Corner.TopLeft || corner == Corner.TopRight) {
        offX += it.width
      } else {
        offX -= it.width
      }
    }
  }

  fun add(title: String, description: String) {
    notifications.push(Notification(title, description))
  }

  fun pop() {
    notifications.pop()
  }
}
