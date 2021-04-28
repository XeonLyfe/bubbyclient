package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import bubby.client.events.MoveUpdateEvent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public abstract class MovementInputFromOptionsMixin extends MovementInput {
  @Inject(method = "updatePlayerMoveState", at = @At("RETURN"))
  public void updatePlayerMoveState(CallbackInfo ci) {
    BubbyClient.INSTANCE.getEvents().call(new MoveUpdateEvent());
  }
}
