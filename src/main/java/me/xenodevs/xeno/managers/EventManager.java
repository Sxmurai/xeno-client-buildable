package me.xenodevs.xeno.managers;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import me.xenodevs.xeno.gui.hud.HudConfig;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Mouse;
import com.google.common.collect.Maps;
import me.xenodevs.xeno.Xeno;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class EventManager {
	
	public static EventManager instance;
	Minecraft mc = Minecraft.getMinecraft();
	
	public EventManager() {
		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
		
		Xeno.logger.info("Initialized Event Processor");
	}
	
	@SubscribeEvent
	public void onKey(KeyInputEvent event) {
		Xeno.EVENT_BUS.post(event);
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		Xeno.EVENT_BUS.post(event);
	}
	
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent event) {
		Xeno.EVENT_BUS.post(event);
		
		if(!(Minecraft.getMinecraft().currentScreen instanceof HudConfig) && event.getType() == ElementType.TEXT && Xeno.moduleManager.isModuleEnabled("HUD"))
			Xeno.hudManager.renderMods();
	}
	
	@SubscribeEvent
	public void onRenderWorld(RenderWorldLastEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Post event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onMouseInput(InputEvent.MouseInputEvent event) {
		if(Mouse.getEventButtonState()) {
			Xeno.EVENT_BUS.post(event);
		}
	}

	@SubscribeEvent
	public void onRenderScreen(RenderGameOverlayEvent.Text event) {
		Xeno.EVENT_BUS.post(event);
	}
	
	@SubscribeEvent
    public void onChat(ClientChatEvent event) {
        Xeno.EVENT_BUS.post(event);
    }

	@SubscribeEvent
	public void onChatReceived(ClientChatReceivedEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onLivingDamage(LivingDamageEvent event) {
		Xeno.EVENT_BUS.post(event);
	}
	@SubscribeEvent
	public void onLivingEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onInputUpdate(InputUpdateEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		Xeno.EVENT_BUS.post(event);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		Xeno.EVENT_BUS.post(event);
	}

	private final Map<String, String> uuidNameCache = Maps.newConcurrentMap();

	public String resolveName(String uuid) {
		uuid = uuid.replace("-", "");
		if (uuidNameCache.containsKey(uuid)) {
			return uuidNameCache.get(uuid);
		}

		final String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
		try {
			final String nameJson = IOUtils.toString(new URL(url));
			if (nameJson != null && nameJson.length() > 0) {
				final JSONArray jsonArray = (JSONArray) JSONValue.parseWithException(nameJson);
				if (jsonArray != null) {
					final JSONObject latestName = (JSONObject) jsonArray.get(jsonArray.size() - 1);
					if (latestName != null) {
						return latestName.get("name").toString();
					}
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	
}
