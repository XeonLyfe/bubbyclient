package bubby.client.events

import bubby.api.event.Event
import net.minecraft.network.Packet

class SendPacketEvent(var packet: Packet<*>): Event()
