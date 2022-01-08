package me.wolfsurge.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void preSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.Send event = new PacketEvent.Send(packet);
		Xeno.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
	private void preChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.Receive event = new PacketEvent.Receive(packet);
		Xeno.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("TAIL"), cancellable = true)
	private void postSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.PostSend event = new PacketEvent.PostSend(packet);
		Xeno.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "channelRead0", at = @At("TAIL"), cancellable = true)
	private void postChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.PostReceive event = new PacketEvent.PostReceive(packet);
		Xeno.EVENT_BUS.post(event);

		if (event.isCancelled()) {
			callbackInfo.cancel();
		}
	}
}