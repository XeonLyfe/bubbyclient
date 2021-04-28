package me.rina.gui.impl.module;

import bubby.api.module.Category;
import me.rina.gui.GUIColor;
import me.rina.gui.api.flag.Flag;
import me.rina.gui.api.frame.Frame;
import me.rina.gui.impl.module.category.CategoryFrame;
import me.rina.turok.render.font.TurokFont;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.hardware.mouse.TurokMouse;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 17/11/20 at 11:12am
 */
public class ModuleClickGUI extends GuiScreen {
    public TurokDisplay display;
    public TurokMouse mouse;

    public GUIColor guiColor;

    public ArrayList<Frame> loadedFrameList;

    private int closedWidth;

    private boolean isClosingGUI;

    /*
     * We need get the focused frame to render things widgets no glitch.
     */
    private Frame focusedFrame;

    private boolean isCanceledCloseGUI;

    /*
     * Fonts used in GUI.
     */
    //public TurokFont fontFrameCategory = new TurokFont(new Font("Whitney", 0, 24), true, true);
    //public TurokFont fontWidgetModule = new TurokFont(new Font("Whitney", 0, 16), true, true);
    public TurokFont fontFrameCategory = new TurokFont(new Font("Verdana", 0, 24), true, true);
    public TurokFont fontWidgetModule = new TurokFont(new Font("Verdana", 0, 16), true, true);

    public ModuleClickGUI() {
        TurokRenderGL.init();

        if (!TurokRenderGL.isShaderProgramInitialized()) {
            TurokRenderGL.init(TurokRenderGL.TUROKGL_SHADER);
        }

        this.guiColor = new GUIColor();

        this.init();
    }

    public void init() {
        this.loadedFrameList = new ArrayList<>();

        int cacheX = 1;

        /*
         * List the categories and create widgets and registry them in loaded frame list.
         */
        for (Category categories : Category.values()) {
            CategoryFrame categoryFrame = new CategoryFrame(this, categories);

            categoryFrame.getRect().setX(cacheX);
            categoryFrame.getRect().setY(1);

            this.loadedFrameList.add(categoryFrame);

            cacheX += categoryFrame.getRect().getWidth() + 1;

            this.focusedFrame = categoryFrame;
        }
    }

    public void matrixMoveFocusedFrameToLast() {
        this.loadedFrameList.remove(this.focusedFrame);
        this.loadedFrameList.add(this.focusedFrame);
    }

    public void setCanceledCloseGUI(boolean canceledCloseGUI) {
        isCanceledCloseGUI = canceledCloseGUI;
    }

    public boolean isCanceledCloseGUI() {
        return isCanceledCloseGUI;
    }

    public TurokDisplay getDisplay() {
        return display;
    }

    public TurokMouse getMouse() {
        return mouse;
    }

    public void onSaveList() {
        try {
            for (Frame frames : this.loadedFrameList) {
                frames.onSave();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public void onLoadList() {
        try {
            for (Frame frames : this.loadedFrameList) {
                frames.onLoad();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        this.onSaveList();

        /*
         * List frames in a for to make event on GUI closed.
         */
        for (Frame frames : this.loadedFrameList) {
            frames.onScreenClosed();
        }

        /*
         * Not remove to null for when init again the focused frame keep normal.
         */
        this.focusedFrame.onCustomScreenClosed();
    }

    @Override
    public void initGui() {
        if (this.isClosingGUI) {
            this.isClosingGUI = false;
        }

        /*
         * All frames when GUI is open.
         */
        for (Frame frames : this.loadedFrameList) {
            frames.onScreenOpened();
        }

        this.focusedFrame.onCustomScreenOpened();
    }

    @Override
    public void keyTyped(char charCode, int keyCode) {
        for (Frame frames : this.loadedFrameList) {
            frames.onKeyboardPressed(charCode, keyCode);
        }

        /*
         * Cancel the escape for close GUI and override the keyboard.
         */
        if (this.isCanceledCloseGUI) {
           this.focusedFrame.onCustomKeyboardPressed(charCode, keyCode);
        } else {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                this.isClosingGUI = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mx, int my, int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseReleased(button);

            if (frames instanceof CategoryFrame) {
                CategoryFrame categoryFrame = (CategoryFrame) frames;

                if (categoryFrame.verify(this.mouse)) {
                    this.focusedFrame = categoryFrame;
                }
            }
        }

        this.focusedFrame.onCustomMouseReleased(button);
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        for (Frame frames : this.loadedFrameList) {
            frames.onMouseClicked(button);

            if (frames instanceof CategoryFrame) {
                CategoryFrame categoryFrame = (CategoryFrame) frames;

                if (categoryFrame.verify(this.mouse)) {
                    this.focusedFrame = categoryFrame;
                }
            }
        }

        this.focusedFrame.onCustomMouseClicked(button);
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        /*
         * Init display, mouse & stuff.
         */
        this.display = new TurokDisplay(mc);
        this.display.setPartialTicks(partialTicks);

        this.mouse = new TurokMouse(mx, my);

        this.guiColor.onUpdate();

        /*
         * Init TurokRenderGL.
         */
        TurokRenderGL.init(this.display);
        TurokRenderGL.init(this.mouse);

        this.drawDefaultBackground();

        /*
         * Auto scale so, fix the screen.
         */
        TurokRenderGL.autoScale();

        /*
         * Disable texture 2D to render rect.
         */
        TurokRenderGL.disable(GL11.GL_TEXTURE_2D);

        /*
         * We cal all frames to render.
         */
        for (Frame frames : this.loadedFrameList) {
            TurokRenderGL.enable(GL11.GL_SCISSOR_TEST);
            TurokRenderGL.drawScissor(0, 0, this.closedWidth, this.display.getScaledHeight());

            frames.onRender();

            TurokRenderGL.disable(GL11.GL_SCISSOR_TEST);

            if (frames instanceof CategoryFrame) {
                CategoryFrame categoryFrame = (CategoryFrame) frames;

                /*
                 * Flag mouse to scroll the frame if over.
                 */
                if (categoryFrame.flagMouse == Flag.MouseOver) {
                    if (this.mouse.hasWheel()) {
                        frames.getRect().y += -this.mouse.getScroll();
                    }
                }

                /*
                 * Verify mouse over stuffs to focus frame.
                 */
                if (categoryFrame.verify(this.mouse)) {
                    this.focusedFrame = categoryFrame;
                }

                /*
                 * Disable flag to mouse, so, no glitches.
                 */
                categoryFrame.flagOffsetMouse = Flag.MouseNotOver;
                categoryFrame.flagMouse = Flag.MouseNotOver;
            }
        }

        this.focusedFrame.onCustomRender();

        TurokRenderGL.disable(GL11.GL_TEXTURE_2D);
        TurokRenderGL.disable(GL11.GL_BLEND);

        /*
         * Enable again for screen matrix.
         */
        TurokRenderGL.enable(GL11.GL_TEXTURE_2D);
        TurokRenderGL.color(255, 255, 255);

        int closingValueCalculated = 0;

        if (this.isClosingGUI) {
            this.closedWidth = (int) TurokMath.lerp(this.closedWidth, 0, this.display.getPartialTicks());

            if (this.closedWidth <= closingValueCalculated) {
                this.onGuiClosed();

                mc.displayGuiScreen(null);
            }
        } else {
            this.closedWidth = (int) TurokMath.lerp(this.closedWidth, this.display.getScaledWidth(), this.display.getPartialTicks());
        }
    }
}
