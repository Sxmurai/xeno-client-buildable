package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender extends Module {

	public static NoRender INSTANCE;

	BooleanSetting fire = new BooleanSetting("Fire", true);
	BooleanSetting water = new BooleanSetting("Water", true);
	BooleanSetting block = new BooleanSetting("Block", false);
	public static BooleanSetting portal = new BooleanSetting("Portal", true);
	public static BooleanSetting potions = new BooleanSetting("Potions", true);
	BooleanSetting bossBars = new BooleanSetting("Bossbars", true);
	BooleanSetting time = new BooleanSetting("Change Time", false);
	NumberSetting timeSet = new NumberSetting("Time", 0, 0, 23000, 100);

	public NoRender() {
		super("NoRender", "stops things from being rendered", 0, Category.RENDER);
		this.addSettings(fire, water, block, portal, bossBars, potions, time, timeSet);
		INSTANCE = this;
	}

	@Override
	public void onUpdate() {
		if (time.isEnabled()) {
			mc.world.setWorldTime((long) timeSet.getDoubleValue());
		}
	}

	@SubscribeEvent
	public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
		if (nullCheck())
			return;

		if (event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.FIRE) && fire.getValue())
			event.setCanceled(true);

		if (event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.WATER) && water.getValue())
			event.setCanceled(true);

		if (event.getOverlayType().equals(RenderBlockOverlayEvent.OverlayType.BLOCK) && block.getValue())
			event.setCanceled(true);
	}

	@EventHandler
	private final Listener<PacketEvent.Receive> pktRec = new Listener<>(event -> {
		if (event.getPacket() instanceof SPacketTimeUpdate && time.isEnabled()) {
			event.cancel();
		}
		if (event.getPacket() instanceof SPacketUpdateBossInfo && bossBars.isEnabled()) {
			event.cancel();
		}
	});

}
