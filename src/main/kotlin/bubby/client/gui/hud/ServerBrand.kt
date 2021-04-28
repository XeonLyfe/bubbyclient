package bubby.client.gui.hud

import me.rina.gui.api.component.Component

class ServerBrand:
    Component("Server Brand", "Server Brand", "Displays the client and its version", StringType.Use) {


    override fun onRender(partialTicks: Float) {
        var brand = mc.currentServerData.toString()
        this.render(brand, 0, 0)

        this.rect.setWidth(getStringWidth(brand))
        this.rect.setHeight(getStringHeight(brand))
    }
}
