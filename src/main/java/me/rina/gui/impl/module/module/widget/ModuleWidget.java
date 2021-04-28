package me.rina.gui.impl.module.module.widget;

import bubby.api.module.Module;
import bubby.api.setting.Setting;
import bubby.client.BubbyClient;
import me.rina.gui.impl.module.ModuleClickGUI;
import me.rina.gui.api.flag.Flag;
import me.rina.gui.api.widget.Widget;
import me.rina.gui.impl.module.category.CategoryFrame;
import me.rina.gui.impl.module.module.container.ModuleScrollContainer;
import me.rina.gui.impl.module.setting.SettingBooleanWidget;
import me.rina.gui.impl.module.setting.SettingNumberWidget;
import me.rina.gui.impl.module.setting.SettingStringWidget;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokTick;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 20/11/20 at 12:38pm
 */
public class ModuleWidget extends Widget {
    private ModuleClickGUI master;

    private CategoryFrame frame;

    private ModuleScrollContainer container;

    private Module module;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private int alphaAnimationPressed;

    private boolean isMouseClickedLeft;
    private boolean isMouseClickedRight;

    private boolean isWidgetOpened;

    private TurokTick tickAnimationPressed = new TurokTick();

    private ArrayList<Widget> loadedWidgetList;

    /*
     * Flags to widget.
     */
    public Flag flagMouse;

    public ModuleWidget(ModuleClickGUI master, CategoryFrame frame, ModuleScrollContainer container, Module module) {
        super(module.getName());

        this.master = master;
        this.module = module;

        this.container = container;
        this.frame = frame;

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));
        this.rect.setHeight(3 + TurokFontManager.getStringHeight(this.master.fontWidgetModule, this.rect.getTag()) + 3);

        this.flagMouse = Flag.MouseNotOver;

        this.init();
    }

    public void init() {
        this.loadedWidgetList = new ArrayList<>();

        this.offsetHeight = this.rect.getHeight() + 1;

        for (Setting settings : BubbyClient.INSTANCE.getSettings().getSettingsByMod(this.module)) {
            if (settings.getValue() instanceof Boolean) {
                SettingBooleanWidget settingBooleanWidget = new SettingBooleanWidget(this.master, this.frame, this.container, this, settings);

                settingBooleanWidget.setOffsetY(this.offsetHeight);

                this.loadedWidgetList.add(settingBooleanWidget);

                this.offsetHeight += settingBooleanWidget.getRect().getHeight() + 1;
            }

            if (settings.getValue() instanceof Integer || settings.getValue() instanceof Double || settings.getValue() instanceof Float || settings.getValue() instanceof Long) {
                SettingNumberWidget settingNumberWidget = new SettingNumberWidget(this.master, this.frame, this.container, this, (Setting<Number>) settings);

                settingNumberWidget.setOffsetY(this.offsetHeight);

                this.loadedWidgetList.add(settingNumberWidget);

                this.offsetHeight += settingNumberWidget.getRect().getHeight() + 1;
            }

            if (settings.getValue() instanceof String) {
                SettingStringWidget settingStringWidget = new SettingStringWidget(this.master, this.frame, this.container, this, settings);

                settingStringWidget.setOffsetY(this.offsetHeight);

                this.loadedWidgetList.add(settingStringWidget);

                this.offsetHeight += settingStringWidget.getRect().getHeight() + 1;
            }
        }

        ModuleBindingWidget moduleBindingWidget = new ModuleBindingWidget(this.master, this.frame, this.container, this);

        moduleBindingWidget.setOffsetY(this.offsetHeight);

        this.loadedWidgetList.add(moduleBindingWidget);

        this.offsetHeight += moduleBindingWidget.getRect().getHeight() + 1;
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

    public void setWidgetOpened(boolean widgetOpened) {
        isWidgetOpened = widgetOpened;
    }

    public boolean isWidgetOpened() {
        return isWidgetOpened;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void onScreenOpened() {
        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onScreenOpened();
            }
        }
    }

    @Override
    public void onCustomScreenOpened() {
        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomScreenOpened();
            }
        }
    }

    @Override
    public void onScreenClosed() {
        this.isMouseClickedLeft = false;

        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onScreenClosed();
            }
        }
    }

    @Override
    public void onCustomScreenClosed() {
        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomScreenClosed();
            }
        }
    }

    @Override
    public void onKeyboardPressed(char charCode, int keyCode) {
        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onKeyboardPressed(charCode, keyCode);
            }
        }
    }

    @Override
    public void onCustomKeyboardPressed(char charCode, int keyCode) {
        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomKeyboardPressed(charCode, keyCode);
            }
        }
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (this.isMouseClickedLeft) {
                this.module.toggle();

                this.tickAnimationPressed.reset();

                this.isMouseClickedLeft = false;
            }

            if (this.isMouseClickedRight) {
                this.isWidgetOpened = !this.isWidgetOpened;

                this.container.refresh();

                this.isMouseClickedRight = false;
            }
        } else {
            this.isMouseClickedLeft = false;
            this.isMouseClickedRight = false;
        }

        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onMouseReleased(button);
            }
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomMouseReleased(button);
            }
        }
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.flagMouse == Flag.MouseOver) {
            if (button == 0) {
                this.isMouseClickedLeft = true;
            }

            if (button == 1) {
                this.isMouseClickedRight = true;
            }
        }

        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onMouseClicked(button);
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomMouseClicked(button);
            }
        }
    }

    @Override
    public void onRender() {
        this.rect.setX(this.container.getRect().getX() + this.offsetX);
        this.rect.setY(this.container.getRect().getY() + this.offsetY);

        this.rect.setWidth(this.container.getRect().getWidth() - (this.offsetX * 2));

        if (this.module.isToggled()) {
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

        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onRender();

                if (widgets instanceof ModuleBindingWidget) {
                    ((ModuleBindingWidget) widgets).flagMouse = Flag.MouseNotOver;
                }

                if (widgets instanceof SettingBooleanWidget) {
                    ((SettingBooleanWidget) widgets).flagMouse = Flag.MouseNotOver;
                }

                if (widgets instanceof SettingNumberWidget) {
                    ((SettingNumberWidget) widgets).flagMouse = Flag.MouseNotOver;
                }

                if (widgets instanceof SettingStringWidget) {
                    ((SettingStringWidget) widgets).flagMouse = Flag.MouseNotOver;
                }
            }

            TurokRenderGL.drawSolidRectFadingMouse(this.rect.getX(), this.rect.getY() + this.rect.getHeight() + 1, 1, this.offsetHeight - (this.rect.getHeight() + 2), 50, new Color(this.master.guiColor.base[0], this.master.guiColor.base[1], this.master.guiColor.base[2], 255));
        }

        /*
         * Render module name.
         */
        TurokFontManager.render(this.master.fontWidgetModule, this.rect.getTag(), this.rect.getX() + 1, this.rect.getY() + 3, true, new Color(255, 255, 255));
    }

    @Override
    public void onCustomRender() {
        this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

        if (this.isWidgetOpened) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomRender();
            }
        }
    }
}
