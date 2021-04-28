package bubby.client.utils

import bubby.client.BubbyClient

object Theme {
  inline val innerColour: Int
    get() {
      when(BubbyClient.settings.getSettingByName("ClickGui Theme").value) {
        "Nodus" -> return -0x56000000
        "nhack" -> return 0x502d2d2d
        "WeepCraft" -> return -0x5d000000
      }
      return -0x56000000
    }
  inline val borderColour: Int
    get() {
      when(BubbyClient.settings.getSettingByName("ClickGui Theme").value) {
        "Nodus" -> return -0x55c2c2c3
        "nhack" -> return -0x929293
        "WeepCraft" -> return -0x55c2c2c3
      }
      return -0x56000000
    }
  inline val extraColour: Int
    get() {
      when(BubbyClient.settings.getSettingByName("ClickGui Theme").value) {
        "Nodus" -> return -0x55e50100
        "nhack" -> return -0x55ff412e
        "WeepCraft" -> return -0x550008a7
      }
      return -0x56000000
    }
  inline val buttonColour: Int
    get() {
      when(BubbyClient.settings.getSettingByName("ClickGui Theme").value) {
        "Nodus" -> return -0x55e50100
        "nhack" -> return -0x55ff412e
        "WeepCraft" -> return -0x55c2c2c3
      }
      return -0x56000000
    }
  inline val titleColour: Int
    get() {
      when(BubbyClient.settings.getSettingByName("ClickGui Theme").value) {
        "Nodus" -> return -0xe50100
        "nhack" -> return -0x1
        "WeepCraft" -> return -0x5009c
      }
      return -0x56000000
    }
  inline val toggledColour: Int
    get() {
      when(BubbyClient.settings.getSettingByName("ClickGui Theme").value) {
        "Nodus" -> return -0xe50100
        "nhack" -> return -0xff412e
        "WeepCraft" -> return -0xe50100
      }
      return -0x56000000
    }
  inline val unToggledColour: Int
    get() {
      when(BubbyClient.settings.getSettingByName("ClickGui Theme").value) {
        "Nodus" -> return -0x1
        "nhack" -> return -0x1
        "WeepCraft" -> return -0x5f0000
      }
      return -0x56000000
    }
}
