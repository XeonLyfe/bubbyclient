package me.rina.gui.impl.module.module.widget;

import bubby.client.BubbyClient;
import me.rina.gui.impl.module.ModuleClickGUI;
import me.rina.gui.api.flag.Flag;
import me.rina.gui.api.widget.Widget;
import me.rina.gui.impl.module.category.CategoryFrame;
import me.rina.gui.impl.module.module.container.ModuleScrollContainer;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokTick;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author SrRina
 * @since 28/11/20 at 01:27pm
 */
public class ModuleBindingWidget extends Widget {
    private ModuleClickGUI master;
    private CategoryFrame frame;

    private ModuleScrollContainer container;
    private ModuleWidget module;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private int alphaAnimationPressed;

    private boolean isMouseClickedLeft;
    private boolean isBinding;

    public Flag flagMouse;
    public Flag flagAnimation;

    public ModuleBindingWidget(ModuleClickGUI master, CategoryFrame frame, ModuleScrollContainer container, ModuleWidget module) {
        super(module.getModule().getName());

        this.master = master;
        this.frame = frame;

        this.container = container;
        this.module = module;

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(3 + TurokFontManager.getStringHeight(this.master.fontWidgetModule, this.rect.getTag()) + 3);

        this.offsetX = 2;

        this.flagMouse = Flag.MouseNotOver;
        this.flagAnimation = Flag.AnimationEnd;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetWidth(int offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public int getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }

    @Override
    public void onScreenOpened() {

    }

    @Override
    public void onCustomScreenOpened() {

    }

    @Override
    public void onScreenClosed() {
        this.isMouseClickedLeft = false;
    }

    @Override
    public void onCustomScreenClosed() {

    }

    @Override
    public void onKeyboardPressed(char charCode, int keyCode) {
    }

    @Override
    public void onCustomKeyboardPressed(char charCode, int keyCode) {
        if (this.isBinding) {
            switch (keyCode) {
                case Keyboard.KEY_ESCAPE : {
                    this.isBinding = false;

                    this.master.setCanceledCloseGUI(false);

                    break;
                }

                case Keyboard.KEY_DELETE : {
                    this.isBinding = false;

                    this.master.setCanceledCloseGUI(false);

                    this.module.getModule().setBind(-1);

                    break;
                }

                default : {
                    this.isBinding = false;

                    this.master.setCanceledCloseGUI(false);

                    this.module.getModule().setBind(keyCode);

                    break;
                }
            }
            BubbyClient.INSTANCE.getModules().saveModule(this.module.getModule());
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.isBinding) {
            this.isBinding = false;

            this.master.setCanceledCloseGUI(false);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (this.isMouseClickedLeft) {
                this.isBinding = true;

                this.master.setCanceledCloseGUI(true);

                this.isMouseClickedLeft = false;
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.isBinding) {
            this.isBinding = false;

            this.master.setCanceledCloseGUI(false);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (button == 0) {
                this.isMouseClickedLeft = true;
            }
        }
    }

    @Override
    public void onRender() {
        this.rect.setX(this.module.getRect().getX() + this.offsetX);
        this.rect.setY(this.container.getRect().getY() + this.module.getOffsetY() + this.offsetY);

        this.rect.setWidth(this.container.getRect().getWidth() - this.offsetX);

        if (this.isBinding) {
            this.alphaAnimationPressed = (int) TurokMath.lerp(this.alphaAnimationPressed, this.master.guiColor.base[3], this.master.getDisplay().getPartialTicks());
        } else {
            this.alphaAnimationPressed = (int) TurokMath.lerp(this.alphaAnimationPressed, 0, this.master.getDisplay().getPartialTicks());
        }

        TurokRenderGL.color(this.master.guiColor.base[0], this.master.guiColor.base[1], this.master.guiColor.base[2], this.alphaAnimationPressed);
        TurokRenderGL.drawSolidRect(this.rect);

        if (this.flagMouse == Flag.MouseOver) {
            TurokRenderGL.color(this.master.guiColor.highlight[0], this.master.guiColor.highlight[1], this.master.guiColor.highlight[2], this.master.guiColor.highlight[3]);
            TurokRenderGL.drawSolidRect(this.rect);
        }

        String keyCodeName = (this.isBinding ? "<Binding>" : (this.module.getModule().getBind() != -1 ? ("<"+ Keyboard.getKeyName(this.module.getModule().getBind()) + ">") : "<NONE>"));

        /*
         * Render module name.
         */
        TurokFontManager.render(this.master.fontWidgetModule, keyCodeName, this.rect.getX() + 1, this.rect.getY() + 3, true, new Color(255, 255, 255));
    }

    @Override
    public void onCustomRender() {
        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
    }
}
