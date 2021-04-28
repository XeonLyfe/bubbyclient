package bubby.api.mixin.mixins;
import bubby.api.mixin.interfaces.*;
import bubby.client.BubbyClient;
import bubby.client.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Mixin(Minecraft.class)
public class MinecraftMixin implements IMinecraft
{
  @Shadow
  private final Timer timer = new Timer(20.f);

  @Shadow
  private int rightClickDelayTimer;

  @Override
  public ITimer
  getTimer()
  {
    return (ITimer)this.timer;
  }

  @Override
  public void
  setRightClickDelayTimer(int newTime)
  {
    this.rightClickDelayTimer = newTime;
  }

  @Inject(at = @At("RETURN"), method = "init")
  public void
  init(CallbackInfo ci)
  {
    BubbyClient.INSTANCE.onInit();
  }

  @Redirect(at = @At(
      value = "INVOKE",
      target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"),
      method = "createDisplay")
  public void
  createDisplay(String name)
  {
    Display.setTitle(BubbyClient.name + " " + BubbyClient.version);
  }

  @Inject(at = @At("HEAD"), method = "runTick")
  private void
  runTick(CallbackInfo ci)
  {
    if(BubbyClient.INSTANCE.getMC().world == null || BubbyClient.INSTANCE.getMC().player == null) {
      BubbyClient.INSTANCE.getEvents().call(new UnsafeTickEvent());
    } else {
      BubbyClient.INSTANCE.getEvents().call(new TickEvent());
    }
  }

  @Inject(at = @At(
    value = "INVOKE",
    remap = false,
    target = "Lorg/lwjgl/input/Keyboard;getEventKey()I",
    ordinal = 0,
    shift = At.Shift.BEFORE),
    method = "runTickKeyboard")
  private void
  runTickKeyboard(CallbackInfo ci)
  {
    int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
    if(Keyboard.getEventKeyState())
      BubbyClient.INSTANCE.getEvents().call(new KeyEvent(i));
  }

  @Inject(at = @At(
    value = "INVOKE",
    remap = false,
    target = "Lorg/lwjgl/input/Mouse;getEventButton()I",
    ordinal = 0,
    shift = At.Shift.BEFORE),
    method = "runTickMouse")
  private void runTickMouse(CallbackInfo ci) {
    int i = Mouse.getEventButton();
    if(i >= 0)
      BubbyClient.INSTANCE.getEvents().call(new MouseClickEvent(i));
  }
}
