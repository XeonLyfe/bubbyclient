package me.rina.gui.impl.module.module.container;

import bubby.api.module.Module;
import bubby.client.BubbyClient;
import me.rina.gui.impl.module.ModuleClickGUI;
import me.rina.gui.api.container.Container;
import me.rina.gui.api.flag.Flag;
import me.rina.gui.api.widget.Widget;
import me.rina.gui.impl.module.category.CategoryFrame;
import me.rina.gui.impl.module.module.widget.ModuleWidget;
import me.rina.turok.render.opengl.TurokRenderGL;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 20/11/20 at 7:52pm
 */
public class ModuleScrollContainer extends Container {
    private ModuleClickGUI master;
    private CategoryFrame frame;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    /*
     * Widget list of the components module.
     */
    private ArrayList<Widget> loadedWidgetList;

    public ModuleScrollContainer(ModuleClickGUI master, CategoryFrame frame) {
        super(frame.getRect().getTag() + "Scroll");

        this.master = master;
        this.frame = frame;

        this.offsetX = 1;

        this.init();
    }

    public void init() {
        this.loadedWidgetList = new ArrayList<>();

        int count = 0;

        for (Module modules : BubbyClient.INSTANCE.getModules().getMods()) {
            if (modules.getCategory() != this.frame.getCategory()) {
                continue;
            }

            ModuleWidget moduleWidget = new ModuleWidget(this.master, this.frame, this, modules);

            moduleWidget.setOffsetY(this.rect.getHeight());

            this.loadedWidgetList.add(moduleWidget);

            this.rect.height += moduleWidget.getRect().getHeight() + 1;

            if (count <= this.frame.getMaximumModule()) {
                this.frame.getRect().height += moduleWidget.getRect().getHeight() + 1;
            } else {
                this.frame.setAbleToScissor(true);
            }

            count++;
        }
    }

    public void refresh() {
        this.rect.height = 0;

        this.frame.getRect().setHeight(this.frame.getOffsetHeight());

        for (Widget widgets : this.loadedWidgetList) {
            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                moduleWidget.setOffsetY(this.rect.getHeight());

                if (moduleWidget.isWidgetOpened()) {
                    this.rect.height += moduleWidget.getOffsetHeight();
                    this.frame.getRect().height += moduleWidget.getOffsetHeight();
                } else {
                    this.rect.height += moduleWidget.getRect().getHeight() + 1;
                    this.frame.getRect().height += moduleWidget.getRect().getHeight() + 1;
                }
            }
        }
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

    public void clampScroll(int scissorHeight) {
        if (this.offsetY >= 0) {
            this.offsetY--;

            if (this.offsetY <= 1) {
                this.offsetY = 0;
            }
        }

        if (this.offsetY <= (scissorHeight - this.rect.getHeight())) {
            this.offsetY++;

            if (this.offsetX <= (scissorHeight - this.rect.getHeight())) {
                this.offsetX = (scissorHeight - this.rect.getHeight());
            }
        }
    }

    @Override
    public void onScreenOpened() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onScreenOpened();
        }
    }

    @Override
    public void onCustomScreenOpened() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomScreenOpened();
        }
    }

    @Override
    public void onScreenClosed() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onScreenClosed();
        }
    }

    @Override
    public void onCustomScreenClosed() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomScreenClosed();
        }
    }

    @Override
    public void onKeyboardPressed(char charCode, int keyCode) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onKeyboardPressed(charCode, keyCode);
        }
    }

    @Override
    public void onCustomKeyboardPressed(char charCode, int keyCode) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomKeyboardPressed(charCode, keyCode);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomMouseReleased(button);
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseClicked(button);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomMouseClicked(button);
        }
    }

    @Override
    public void onRender() {
        this.rect.setX(this.frame.getRect().getX() + this.offsetX);
        this.rect.setY(this.frame.getRect().getY() + this.frame.getOffsetHeight() + this.offsetY);

        this.rect.setWidth(this.frame.getRect().getWidth() - (this.offsetX * 2));

        int realHeightScissor = this.frame.getRect().getHeight() - this.frame.getOffsetHeight();

        for (Widget widgets : this.loadedWidgetList) {
            widgets.onRender();

            if (widgets instanceof ModuleWidget) {
                ModuleWidget moduleWidget = (ModuleWidget) widgets;

                moduleWidget.flagMouse = Flag.MouseNotOver;
            }
        }
    }

    @Override
    public void onCustomRender() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomRender();
        }
    }
}