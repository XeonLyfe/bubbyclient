package bubby.client.gui.hud
import me.rina.gui.api.component.Component
import me.rina.gui.api.component.impl.ComponentSetting
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper

class Position:
Component("Position", "Position", "Displays your current position", StringType.Use) {

  enum class StackMode {
    Stacked,
    One
  }

  val stackMode = ComponentSetting<StackMode>("Stack Mode", "stack", "How the coords should be stacked", StackMode.Stacked)
  val showNether = ComponentSetting<Boolean>("Show Nether", "nether", "Shows nether coords", true)
  val showHeadRots = ComponentSetting<Boolean>("Rotations", "Rotations", "Show pitch/yaw", false)
  override fun onRender(partialTicks: Float) {
    val position = mutableListOf<String>()

    val nether = mc.player.dimension == -1
    val pos = mc.player.position
    val pos2: BlockPos
    val blocc = mc.player.position
    pos2 = if(nether) BlockPos(blocc.x * 8, blocc.y, blocc.z * 8) else BlockPos(blocc.x / 8, blocc.y, blocc.z / 8)

    if(showHeadRots.value) {
      position.add("Pitch: ${MathHelper.wrapDegrees(mc.player.rotationPitch).toInt()}")
      position.add("Yaw: ${MathHelper.wrapDegrees(mc.player.rotationYaw).toInt()}")
    }

    when(stackMode.value) {
      StackMode.Stacked -> {
        if(showNether.value) {
          position.add("X: ${pos.x} (${pos2.x})")
          position.add("Y: ${pos.y} (${pos2.y})")
          position.add("Z: ${pos.z} (${pos2.z})")
        } else {
          position.add("X: ${pos.x}")
          position.add("Y: ${pos.y}")
          position.add("Z: ${pos.z}")
        }
      }
      StackMode.One -> {
        if(showNether.value) {
          position.add("X: ${pos.x} (${pos2.x}) Y: ${pos.y} (${pos2.y}) Z: ${pos.z} (${pos2.z})")
        } else {
          position.add("X: ${pos.x} Y: ${pos.y} Z: ${pos.z}")
        }
      }
    }

    this.renderList(position.toList(), true)
  }
}
