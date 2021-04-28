package me.rina.gui.impl.overlay.component.frame;

import me.rina.gui.api.component.Component;
import me.rina.gui.api.flag.Flag;
import me.rina.gui.api.frame.Frame;
import me.rina.gui.impl.overlay.ComponentClickGUI;
import me.rina.turok.Turok;
import me.rina.turok.render.opengl.TurokRenderGL;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 03/12/20 at 09:04pm
 */
public class ComponentFrame extends Frame {
    private ComponentClickGUI master;

    private Component component;

    private int dragX;
    private int dragY;

    private boolean isMouseClickedLeft;

    private int alpha;

    public Flag flagMouse;

    public ComponentFrame(ComponentClickGUI master, Component component) {
        super(component.getName());

        this.master = master;
        this.component = component;

        this.flagMouse = Flag.MouseNotOver;
    }

    public boolean isEnabled() {
        return component.isEnabled();
    }

    @Override
    public boolean verifyFocus(int mx, int my) {
        boolean verified = false;

        if (isEnabled()) {
            if (this.component.getRect().collideWithMouse(this.master.getMouse())) {
                verified = true;
            }
        }

        return verified;
    }

    @Override
    public void onMouseReleased(int button) {
        if (this.isMouseClickedLeft) {
            this.isMouseClickedLeft = false;
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        this.master.moveFocusedFrameToTopMatrix();
    }

    @Override
    public void onMouseClicked(int button) {
        if (this.component.isEnabled()) {
            if (this.flagMouse == Flag.MouseOver) {
                if (button == 0) {
                    this.dragX = this.master.getMouse().getX() - this.component.getRect().getX();
                    this.dragY = this.master.getMouse().getY() - this.component.getRect().getY();

                    this.isMouseClickedLeft = true;
                }
            }
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        this.master.moveFocusedFrameToTopMatrix();
    }

    @Override
    public void onRender() {
        if (this.component.isEnabled()) {
            TurokRenderGL.color(0, 0, 0, 100);
            TurokRenderGL.drawSolidRect(this.component.getRect());

            if (this.flagMouse == Flag.MouseOver) {
                TurokRenderGL.color(255, 255, 255, 50);
                TurokRenderGL.drawSolidRect(this.component.getRect());
            }

            if (this.isMouseClickedLeft) {
                this.component.getRect().setX(this.master.getMouse().getX() - dragX);
                this.component.getRect().setY(this.master.getMouse().getY() - dragY);

                this.component.cornerDetector();

                this.alpha = 50;
            } else {
                this.alpha = 0;
            }

            TurokRenderGL.color(0, 0, 255, this.alpha);
            TurokRenderGL.drawSolidRect(this.component.getRect());

            TurokRenderGL.drawOutlineRectFadingMouse(this.component.getRect(), 50, new Color(0, 0, 0, 255));

            this.component.onRender(this.master.getDisplay().getPartialTicks());
        }
    }

    @Override
    public void onCustomRender() {
        if (this.component.isEnabled()) {
            this.flagMouse = this.component.getRect().collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;
        }
    }
}
