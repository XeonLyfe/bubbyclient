package bubby.api.mixin.mixins;
import bubby.api.mixin.interfaces.*;
import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CPacketPlayer.class)
public class CPacketPlayerMixin implements ICPacketPlayer
{
  @Shadow
  protected double x;

  @Shadow
  protected double y;

  @Shadow
  protected double z;

  @Shadow
  protected float pitch;

  @Shadow
  protected float yaw;

  @Shadow
  protected boolean onGround;

  @Override
  public void
  setX(double newX)
  {
    this.x = newX;
  }

  @Override
  public void
  setY(double newY)
  {
    this.y = newY;
  }

  @Override
  public void
  setZ(double newZ)
  {
    this.z = newZ;
  }

  @Override
  public void
  setOnGround(boolean ground)
  {
    this.onGround = ground;
  }

  @Override
  public void
  setPitch(float newPitch)
  {
    this.pitch = newPitch;
  }

  @Override
  public void
  setYaw(float newYaw)
  {
    this.yaw = newYaw;
  }
}
