package me.rina.gui.api.frame;

import me.rina.gui.api.IScreenBasic;
import me.rina.turok.util.TurokRect;

import java.io.IOException;

/**
 * @author SrRina
 * @since 17/11/20 at 11:12am
 */
public class Frame implements IScreenBasic {
    public TurokRect rect;

    public Frame(String tag) {
        this.rect = new TurokRect(tag, 0, 0);
    }

    @Override
    public TurokRect getRect() {
        return this.rect;
    }

    @Override
    public boolean verifyFocus(int mx, int my) {
        return false;
    }

    @Override
    public void onScreenOpened() {

    }

    @Override
    public void onCustomScreenOpened() {

    }

    @Override
    public void onScreenClosed() {

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
    public void onMouseClicked(int button) {

    }

    @Override
    public void onCustomMouseClicked(int button) {

    }

    @Override
    public void onMouseReleased(int button) {

    }

    @Override
    public void onCustomMouseReleased(int button) {

    }

    @Override
    public void onRender() {

    }

    @Override
    public void onCustomRender() {

    }

    @Override
    public void onSave() throws IOException {

    }

    @Override
    public void onLoad() throws IOException {

    }
}
