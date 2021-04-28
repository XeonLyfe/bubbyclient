package bubby.client.utils
import bubby.client.BubbyClient

import java.util.*

class FriendManager {
  val friends: ArrayList<String> = ArrayList()

  init {
    friends.add("BubbyRoosh")
    friends.add("Boiler3_")
    friends.add("KarioMart")
    friends.add("totemspoof")
    friends.add("jaxcc")
    //friends.add(BubbyClient.MC.player.name)
  }

  fun isFriend(name: String?): Boolean {
    return friends.contains(name)
  }

  fun addFriend(name: String) {
    if(friends.contains(name)) return
    friends.add(name)
    FileManager.saveFriends()
  }

  fun addFriendNoSave(name: String) {
    if(friends.contains(name)) return
    friends.add(name)
  }

  fun removeFriend(name: String?) {
    friends.remove(name)
    FileManager.saveFriends()
  }

}
