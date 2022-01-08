package me.xenodevs.xeno.module.modules.misc;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import me.xenodevs.xeno.module.settings.StringSetting;
import org.lwjgl.input.Keyboard;

import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class AutoEZ extends Module {

    public StringSetting msg = new StringSetting("MSG", "ez");
	public BooleanSetting greenText = new BooleanSetting("Green Text", true);
	
	public AutoEZ() {
		super("AutoEZ", "lets people know you're clouted", Keyboard.KEY_NONE, Category.MISC);
		this.addSettings(msg);
	}
	
	int delay = 0;
    private static final ConcurrentHashMap<Object, Integer> targetedPlayers = new ConcurrentHashMap<Object, Integer>();

    public static String customMsgArg = "";
    
    public static void setMessage(String msg) {
    	customMsgArg = msg;
    }

    @Override
    public void onUpdate() {
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityPlayer) {
            	EntityPlayer player = (EntityPlayer) entity;
                if (player.getHealth() <= 0) {
                    if (targetedPlayers.containsKey(player.getName())) {
                        announce(player.getName());
                    }
                }
            }
        }

        targetedPlayers.forEach((name, timeout) -> {
            if ((int)timeout <= 0) {
            	targetedPlayers.remove(name);
            } else {
            	targetedPlayers.put(name, (int)timeout - 1);
            }

        });

        delay++;

    } 
    
    @EventHandler
    private Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
        if (mc.player == null) return;

        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.getPacket();
            if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);
                if (targetEntity instanceof EntityPlayer) {
                    addTarget(targetEntity.getName());
                }
            }
        }

    });

    @EventHandler
    private Listener<LivingDeathEvent> livingDeathListener = new Listener<>(event -> {
        if (mc.player == null) return;

        EntityLivingBase e = event.getEntityLiving();
        if (e == null) return;

        if (e instanceof EntityPlayer) {
        	EntityPlayer player = (EntityPlayer) e;

            if (player.getHealth() <= 0) {
                if (targetedPlayers.containsKey(player.getName())) {
                    announce(player.getName());
                }
            }
        }

    });

    public void announce(String name) {
        if (delay < 150) {
            return;
        }
        delay = 0;
        targetedPlayers.remove(name);
        
        String starter = "";
        if(greenText.isEnabled()) starter = "> ";

        String message = "";
        message = starter + this.msg.getText();
        
        mc.player.connection.sendPacket(new CPacketChatMessage(message));
    }

    public static void addTarget(String name) {
        if (!Objects.equals(name, Minecraft.getMinecraft().player.getName())) {
            targetedPlayers.put(name, 20);
        }
    }
	
    @Override
    public String getHUDData() {
    	return "";
    }
}
