package bubby.api.mixin.mixins;


import bubby.client.BubbyClient;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.management.TurokFontManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin extends GuiScreen {
  /*
  public TurokFont font = new TurokFont(new Font("Verdana", 0, 16), true, true);

  @Inject(method = "drawScreen", at = @At("TAIL"))
  public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
    String builddate = "Build Date: " + BubbyClient.INSTANCE.getBuildate();
    String message = BubbyClient.name + " " + BubbyClient.version;
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    TurokFontManager.render(font, message, (int) ((width / 2f) - (TurokFontManager.getStringWidth(font, message) / 2f)), 320, true, new Color(255, 255, 255));
    TurokFontManager.render(font, builddate, (int) ((width / 2f) - (TurokFontManager.getStringWidth(font, message) / 2f)), 300, true, new Color(255, 255, 255));
    GL11.glEnable(GL11.GL_TEXTURE_2D);

   */

}
