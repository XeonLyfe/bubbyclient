package bubby.api.mixin.mixins;
import bubby.api.mixin.interfaces.*;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements IKeyBinding
{
  @Shadow
  private boolean pressed;

  @Override
  public void
  setPressed(boolean pressed)
  {
    this.pressed = pressed;
  }
}
