package me.rina.gui.impl.module.setting;

import bubby.api.setting.Setting;
import bubby.client.BubbyClient;
import me.rina.gui.api.flag.Flag;
import me.rina.gui.api.widget.Widget;
import me.rina.gui.impl.module.ModuleClickGUI;
import me.rina.gui.impl.module.category.CategoryFrame;
import me.rina.gui.impl.module.module.container.ModuleScrollContainer;
import me.rina.gui.impl.module.module.widget.ModuleWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;

import java.awt.*;

/**
 * @author SrRina
 * @since 25/11/20 at 10:50pm
 */
public class SettingNumberWidget extends Widget {
    private ModuleClickGUI master;
    private CategoryFrame frame;

    private ModuleScrollContainer container;
    private ModuleWidget module;

    private int offsetX;
    private int offsetY;

    private double offsetWidth;
    private int offsetHeight;

    private double value;

    private double minimum;
    private double maximum;

    private int offsetAnimation;

    private Setting<Number> setting;

    private boolean isMouseClickedLeft;
    private boolean isRendering;

    public Flag flagMouse;

    public SettingNumberWidget(ModuleClickGUI master, CategoryFrame frame, ModuleScrollContainer container, ModuleWidget module, final Setting<Number> setting) {
        super(setting.getName().replace(setting.getParent().getName() + " ", ""));

        this.master = master;
        this.frame = frame;

        this.container = container;
        this.module = module;

        this.setting = setting;

        this.flagMouse = Flag.MouseNotOver;

        this.init();
    }

    public void init() {
        this.offsetX = 2;

        this.rect.setWidth(this.module.getRect().getWidth() - this.offsetX);
        this.rect.setHeight(3 + TurokFontManager.getStringHeight(this.master.fontWidgetModule, this.rect.getTag()) + 3);
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

    public void setOffsetWidth(double offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public double getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }

    public void setRendering(boolean rendering) {
        isRendering = rendering;
    }

    public boolean isRendering() {
        return isRendering;
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

    }

    @Override
    public void onMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (this.isMouseClickedLeft) {
                this.isMouseClickedLeft = false;
            }
        } else {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (button == 0) {
                this.isMouseClickedLeft = true;
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onRender() {
        this.rect.setX(this.module.getRect().getX() + this.offsetX);
        this.rect.setY(this.container.getRect().getY() + this.module.getOffsetY() + this.offsetY);

        this.rect.setWidth(this.container.getRect().getWidth() - this.offsetX);

        this.value = this.setting.getValue().doubleValue();

        this.maximum = this.setting.getMax().doubleValue();
        this.minimum = this.setting.getMin().doubleValue();

        this.offsetWidth = ((this.rect.getWidth()) * (this.value - this.minimum) / (this.maximum - this.minimum));

        double mouse = Math.min(this.rect.getWidth(), Math.max(0, this.master.getMouse().getX() - this.rect.getX()));

        if (this.isMouseClickedLeft) {
            if (mouse == 0) {
                this.setting.set(this.setting.getMin());
            } else {
                if (this.setting.getValue().getClass() == Integer.class) {
                    double rounded = TurokMath.round(((mouse / this.rect.getWidth()) * (this.maximum - this.minimum) + this.minimum));

                    Integer decimal = (int) rounded;

                    this.setting.set(decimal);
                } else if (this.setting.getValue().getClass() == Double.class) {
                    double rounded = TurokMath.round(((mouse / this.rect.getWidth()) * (this.maximum - this.minimum) + this.minimum));

                    Double decimal = (double) rounded;

                    this.setting.set(decimal);
                } else if (this.setting.getValue().getClass() == Float.class) {
                    double rounded = TurokMath.round(((mouse / this.rect.getWidth()) * (this.maximum - this.minimum) + this.minimum));

                    Float decimal = (float) rounded;

                    this.setting.set(decimal);
                } else if (this.setting.getValue().getClass() == Long.class) {
                    double rounded = TurokMath.round(((mouse / this.rect.getWidth()) * (this.maximum - this.minimum) + this.minimum));

                    Long decimal = (long) rounded;

                    this.setting.set(decimal);
                }
            }

            //BubbyClient.INSTANCE.getModules().saveModule(this.setting.getParent());
        }

        TurokRenderGL.color(this.master.guiColor.base[0], this.master.guiColor.base[1], this.master.guiColor.base[2], this.master.guiColor.base[3]);
        TurokRenderGL.drawSolidRect(this.rect.getX(), this.rect.getY(), (float) this.offsetWidth, this.rect.getHeight());

        String name = this.rect.getTag() + " | " + this.setting.getValue().toString();

        if (this.flagMouse == Flag.MouseOver) {
            TurokRenderGL.color(this.master.guiColor.highlight[0], this.master.guiColor.highlight[1], this.master.guiColor.highlight[2], this.master.guiColor.highlight[3]);
            TurokRenderGL.drawSolidRect(this.rect);
        }

        /*
         * Render module name.
         */
        TurokFontManager.render(this.master.fontWidgetModule, name, this.rect.getX() + 1 + this.offsetAnimation, this.rect.getY() + 3, true, new Color(255, 255, 255));
    }

    @Override
    public void onCustomRender() {
        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
    }
}
