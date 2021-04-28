package bubby.client.utils

import bubby.client.BubbyClient
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11

object RenderUtils {
  private inline fun startGL(vararg toToggle: Int, runnable: () -> Unit) {
    toToggle.forEach {
      if (GL11.glIsEnabled(it)) {
        GL11.glDisable(it)
      } else {
        GL11.glEnable(it)
      }
    }

    runnable()

    toToggle.forEach {
      if (GL11.glIsEnabled(it)) {
        GL11.glDisable(it)
      } else {
        GL11.glEnable(it)
      }
    }
  }

  fun drawBox(
    pos: BlockPos,
    r: Float,
    g: Float,
    b: Float,
    a: Float,
    fill: Boolean) {
    drawBox(AxisAlignedBB(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), (pos.x + 1).toDouble(), (pos.y + 1).toDouble(), (pos.z + 1).toDouble()), r, g, b, a, fill)
  }

  fun drawBox(
    boxx: AxisAlignedBB,
    r: Float,
    g: Float,
    b: Float,
    a: Float,
    fill: Boolean, thicc: Float = 2.5f) {
    val box = boxx.offset(
      -BubbyClient.MC.renderManager.viewerPosX,
      -BubbyClient.MC.renderManager.viewerPosY,
      -BubbyClient.MC.renderManager.viewerPosZ
    )

    GL11.glPushMatrix()
    GL11.glBlendFunc(770, 771)
    GL11.glDepthMask(false)
    GL11.glLineWidth(thicc)
    startGL(3042, 3553, 2848, 2929) {
      val tessellator = Tessellator.getInstance()
      val buffer = tessellator.buffer
      buffer.begin(3, DefaultVertexFormats.POSITION_COLOR)
      buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, 0f).endVertex()
      buffer.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, 0f).endVertex()
      buffer.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, 0f).endVertex()
      buffer.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex()
      tessellator.draw()
      if(fill) RenderGlobal.renderFilledBox(box, r, g, b, a)
    }
    GL11.glDepthMask(true)
    GL11.glPopMatrix()
  }

  fun drawBoxBottom(
    pos: BlockPos,
    r: Float,
    g: Float,
    b: Float,
    a: Float
    ) {
    drawBoxBottom(AxisAlignedBB(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), (pos.x + 1).toDouble(), (pos.y + 1).toDouble(), (pos.z + 1).toDouble()), r, g, b, a)
  }

  fun drawBoxBottom(
    boxx: AxisAlignedBB,
    r: Float,
    g: Float,
    b: Float,
    a: Float,
    thicc: Float = 2.5f) {
    val box = boxx.offset(
      -BubbyClient.MC.renderManager.viewerPosX,
      -BubbyClient.MC.renderManager.viewerPosY,
      -BubbyClient.MC.renderManager.viewerPosZ
    )
    GL11.glPushMatrix()
    GL11.glBlendFunc(770, 771)
    GL11.glDepthMask(false)
    GL11.glLineWidth(thicc)
    startGL(3042, 3553, 2848, 2929) {
      val tessellator = Tessellator.getInstance()
      val buffer = tessellator.buffer
      buffer.begin(3, DefaultVertexFormats.POSITION_COLOR)
      buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex()
      buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex()
      buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex()
      tessellator.draw()
    }
    GL11.glDepthMask(true)
    GL11.glPopMatrix()
  }

  fun drawLine(
    x1: Double, y1: Double, z1: Double,
    x2: Double, y2: Double, z2: Double,
    r: Float, g: Float, b: Float, a: Float,
    thicc: Float) {
    GL11.glPushMatrix()
    GL11.glBlendFunc(770, 771)
    GL11.glDepthMask(false)
    GL11.glLineWidth(thicc)
    startGL(3042, 3553, 2848, 2929) {
      val oX = BubbyClient.MC.renderManager.viewerPosX
      val oY = BubbyClient.MC.renderManager.viewerPosY
      val oZ = BubbyClient.MC.renderManager.viewerPosZ
      val tessellator = Tessellator.getInstance()
      val buffer = tessellator.buffer
      buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)
      buffer.pos(x1 - oX, y1 - oY, z1 - oZ).color(r, g, b, a).endVertex()
      buffer.pos(x2 - oX, y2 - oY, z2 - oZ).color(r, g, b, a).endVertex()
      tessellator.draw()
    }
    GL11.glDepthMask(true)
    GL11.glPopMatrix()
  }
}
