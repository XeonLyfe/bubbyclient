package me.rina.gui

import bubby.api.setting.Setting
import bubby.client.BubbyClient.settings


/**
 * @author SrRina
 * @since 28/11/20 at 06:26pm
 */
class GUIColor {

    lateinit var background: IntArray
    lateinit var base: IntArray
    lateinit var highlight: IntArray

    /*
     * Update the colors.
     */
     @Suppress("UNCHECKED_CAST")
    fun onUpdate() {
        val backgroundRed = (settings.getSettingByName("BG Red") as Setting<Double>).value.toInt()
        val backgroundGreen = (settings.getSettingByName("BG Green") as Setting<Double>).value.toInt()
        val backgroundBlue = (settings.getSettingByName("BG Blue") as Setting<Double>).value.toInt()
        val baseRed = (settings.getSettingByName("Red") as Setting<Double>).value.toInt()
        val baseGreen = (settings.getSettingByName("Green") as Setting<Double>).value.toInt()
        val baseBlue = (settings.getSettingByName("Blue") as Setting<Double>).value.toInt()
        background = intArrayOf(
                backgroundRed, backgroundGreen, backgroundBlue, 100
        )
        base = intArrayOf(
                baseRed, baseGreen, baseBlue, 150
        )
        highlight = intArrayOf(
                baseRed, baseGreen, baseBlue, 50
        )
    }
}
