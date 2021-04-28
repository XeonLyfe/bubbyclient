package me.rina.gui.api

import me.rina.turok.util.TurokRect
import java.io.IOException

/**
 * @author SrRina
 * @since 17/11/20 at 11:12am
 */
interface IScreenBasic {
  /*
     * Get rect instance.
     */
  val rect: TurokRect?

  /*
   * Verify if booleans focus;
   */
  fun verifyFocus(mx: Int, my: Int): Boolean

  /*
     * Event opened on screen.
     */
  fun onScreenOpened()
  fun onCustomScreenOpened()

  /*
     * Event closed on screen.
     */
  fun onScreenClosed()
  fun onCustomScreenClosed()

  /*
     * Keyboard input.
     */
  fun onKeyboardPressed(charCode: Char, keyCode: Int)
  fun onCustomKeyboardPressed(charCode: Char, keyCode: Int)

  /*
     * Mouse clicks down.
     */
  fun onMouseClicked(button: Int)
  fun onCustomMouseClicked(button: Int)

  /*
     * Mouse clicks up.
     */
  fun onMouseReleased(button: Int)
  fun onCustomMouseReleased(button: Int)

  /*
     * Render ticks.
     */
  fun onRender()
  fun onCustomRender()

  /*
     * Saving.
     */
  @Throws(IOException::class)
  fun onSave()

  @Throws(IOException::class)
  fun onLoad()
}