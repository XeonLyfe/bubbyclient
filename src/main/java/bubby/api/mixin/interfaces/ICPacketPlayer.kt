package bubby.api.mixin.interfaces

interface ICPacketPlayer
{
  fun setX(newX: Double)
  fun setY(newY: Double)
  fun setZ(newZ: Double)
  fun setPitch(newPitch: Float)
  fun setYaw(newYaw: Float)
  fun setOnGround(ground: Boolean)
}
