package bubby.client.modules

import bubby.api.event.PogEvent
import bubby.api.module.Category
import bubby.api.module.Module
import bubby.client.events.TickEvent
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType
import net.minecraft.item.Item

class AutoArmour: Module("AutoArmor", "swaps armour", -1, Category.Combat, true) {
  private val helmets = arrayOf<Item>(
    Items.DIAMOND_HELMET,
    Items.IRON_HELMET,
    Items.GOLDEN_HELMET,
    Items.CHAINMAIL_HELMET,
    Items.LEATHER_HELMET
  )

  private val chestplates = arrayOf<Item>(
    Items.DIAMOND_CHESTPLATE,
    Items.IRON_CHESTPLATE,
    Items.GOLDEN_CHESTPLATE,
    Items.CHAINMAIL_CHESTPLATE,
    Items.LEATHER_CHESTPLATE
  )

  private val leggings = arrayOf<Item>(
    Items.DIAMOND_LEGGINGS,
    Items.IRON_LEGGINGS,
    Items.GOLDEN_LEGGINGS,
    Items.CHAINMAIL_LEGGINGS,
    Items.LEATHER_LEGGINGS
  )

  private val boots = arrayOf<Item>(
    Items.DIAMOND_BOOTS,
    Items.IRON_BOOTS,
    Items.GOLDEN_BOOTS,
    Items.CHAINMAIL_BOOTS,
    Items.LEATHER_BOOTS
  )

  fun getItemSlot(items: Array<Item>): Int {
    items.forEach {
      val slotId = getSlotID(it);
      if(slotId != -1) {
        return slotId
      }
    }
    return -1
  }

  @PogEvent
  fun onUpdate(event: TickEvent?) {
    event.run {
      var selectedSlotId = -1
      if(mc.player.inventory.armorItemInSlot(0).item == Items.AIR) {
        selectedSlotId = getItemSlot(boots)
      }
      else if(mc.player.inventory.armorItemInSlot(1).item == Items.AIR) {
        selectedSlotId = getItemSlot(leggings)
      }
      else if(mc.player.inventory.armorItemInSlot(2).item == Items.AIR) {
        selectedSlotId = getItemSlot(chestplates)
      }
      else if(mc.player.inventory.armorItemInSlot(3).item == Items.AIR) {
        selectedSlotId = getItemSlot(helmets)
      }

      if(selectedSlotId != -1) {
        if(selectedSlotId < 9)
          selectedSlotId += 36
        mc.playerController.windowClick(0, selectedSlotId, 0, ClickType.QUICK_MOVE, mc.player)
      }
    }
  }

  private fun getSlotID(item: Item): Int {
    for(index in 0..36) {
      val stack = mc.player.inventory.getStackInSlot(index)
      if(stack.item === Items.AIR)
        continue
      if(stack.item === item) {
        return index
      }
    }
    return -1
  }
}
