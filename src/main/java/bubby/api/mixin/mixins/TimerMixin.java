package bubby.api.mixin.mixins;
import bubby.api.mixin.interfaces.ITimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.util.Timer;

@Mixin(Timer.class)
public class TimerMixin implements ITimer
{
  @Shadow
  private float tickLength;

  @Override
  public void
  setTickLen(float newLen)
  {
    this.tickLength = newLen;
  }
}
