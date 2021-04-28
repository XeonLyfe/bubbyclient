package bubby.client.events

import bubby.api.event.Event
import net.minecraft.network.Packet

class ReadPacketEvent(val packet: Packet<*>): Event()
