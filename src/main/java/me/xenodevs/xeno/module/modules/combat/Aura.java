package me.xenodevs.xeno.module.modules.combat;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.*;
import me.xenodevs.xeno.utils.entity.EntityFakePlayer;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.other.RaytraceUtil;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import me.xenodevs.xeno.utils.render.builder.RenderBuilder;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/*
 * 
 * Author: Wolfsurge
 * Notes: Probably a very bad Aura
 *
 */

public class Aura extends Module {
	
	public Timer timer = new Timer();
	
	public ModeSetting priority = new ModeSetting("Priority", "Closest", "Health");
	public ModeSetting rotationMode = new ModeSetting("Rotation", "Legit", "Packet");
	public NumberSetting range = new NumberSetting("Range", 4, 1, 5, 0.1);
	public NumberSetting speed = new NumberSetting("Speed", 10, 1, 20, 1);
	public BooleanSetting noSwing = new BooleanSetting("No Swing", false);
	public BooleanSetting rotate = new BooleanSetting("Rotate", false);
	public BooleanSetting rayTrace = new BooleanSetting("Raytrace", false);
	public ModeSetting rayTraceMode = new ModeSetting("RT Mode", "Body", "Feet");
	public BooleanSetting cooldown = new BooleanSetting("UseCooldown", true);
	public BooleanSetting autoWeapon = new BooleanSetting("AutoWeapon", false);
	public ModeSetting autoWeaponMode = new ModeSetting("AW Mode", "Target", "All");
	public BooleanSetting mobs = new BooleanSetting("Mobs", true);
	public BooleanSetting passive = new BooleanSetting("Passive", true);
	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting attackFriends = new BooleanSetting("AttackFriends", false);

	public BooleanSetting render = new BooleanSetting("Render", true);
	public ModeSetting renderMode = new ModeSetting("R Mode", "Circle", "2D");
	public ColourPicker renderColour = new ColourPicker("Render Colour", new Colour(Color.CYAN.darker()));

	public static Entity target;
	public static String entityName;
	public ArrayList<Item> weapons;
	
	public Aura() {
		super("Aura", "attacks things for u", Category.COMBAT);
		
		this.addSetting(priority);
		this.addSettings(passive, mobs, players);
		this.addSettings(autoWeapon, autoWeaponMode, noSwing, cooldown, attackFriends);
		this.addSettings(rotate, rotationMode);
		this.addSettings(rayTrace, rayTraceMode);
		this.addSettings(range, speed);
		this.addSettings(render, renderMode, renderColour);
	}
	
	@Override
	public void setup() {
		weapons = new ArrayList<Item>();
		
		weapons.add(Items.DIAMOND_SWORD);
		weapons.add(Items.DIAMOND_AXE);
		
		weapons.add(Items.IRON_SWORD);
		weapons.add(Items.IRON_AXE);
		
		weapons.add(Items.GOLDEN_SWORD);
		weapons.add(Items.STONE_AXE);
		
		weapons.add(Items.STONE_SWORD);
		weapons.add(Items.STONE_AXE);
		
		weapons.add(Items.WOODEN_SWORD);
		weapons.add(Items.WOODEN_AXE);
	}
	
	@SubscribeEvent
	public void attackEntity(AttackEntityEvent event) {
		if(autoWeapon.isEnabled() && (autoWeaponMode.is("Target") ? entityName == event.getTarget().getName() : true)) {
			for(Item i : weapons) {
				if(InventoryUtil.hasItem(i)) {
					InventoryUtil.switchToSlot(i);
					break;
				}
			}
		}
	}
	
	public void attack(Entity e) {
		if(Xeno.friendManager.isFriend(name) || e instanceof EntityEnderCrystal)
			return;

		entityName = e.getName();

		if(e instanceof EntityPlayer) {
			if(attackFriends.isEnabled()) {
				rotate(e);
				rayTrace(e);

				if(timer.hasTimeElapsed((long) ((cooldown.enabled ? 5000 : 1000) / speed.getDoubleValue()), true)) {
					mc.getConnection().sendPacket(new CPacketUseEntity(e));

					if(!noSwing.isEnabled())
						mc.player.swingArm(EnumHand.MAIN_HAND);
				}
			} else {
				if(!Xeno.friendManager.isFriend(name) && !attackFriends.isEnabled()) {
					rotate(e);
					rayTrace(e);

					if(timer.hasTimeElapsed((long) ((cooldown.enabled ? 5000 : 1000) / speed.getDoubleValue()), true)) {
						mc.getConnection().sendPacket(new CPacketUseEntity(e));

						if(!noSwing.isEnabled())
							mc.player.swingArm(EnumHand.MAIN_HAND);
					}
				}
			}
		} else {
			rotate(e);
			rayTrace(e);

			if(timer.hasTimeElapsed((long) ((cooldown.enabled ? 5000 : 1000) / speed.getDoubleValue()), true)) {
				mc.getConnection().sendPacket(new CPacketUseEntity(e));

				if(!noSwing.isEnabled())
					mc.player.swingArm(EnumHand.MAIN_HAND);
			}
		}

		target = e;
	}
	
	public void rayTrace(Entity e) {
		if(rayTrace.isEnabled()) {
			if(rayTraceMode.is("Body")) {
				RaytraceUtil.raytraceEntity(e);
			} else if(rayTraceMode.is("Feet")) {
				RaytraceUtil.raytraceFeet(e);
			}
		}
	}
	
	public void rotate(Entity e) {
		if(rotate.isEnabled()) {
			if(rotationMode.is("Legit")) {
				mc.player.rotationYaw = getRotations(e)[0];
				mc.player.rotationPitch = getRotations(e)[1];
			} else if(rotationMode.is("Packet")) {
				mc.getConnection().sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, getRotations(e)[0], getRotations(e)[1], mc.player.onGround));
			}
		}
	}

	@Override
	public void onUpdate() {
		if(nullCheck())
			return;

		if(target != null && target.isDead || target != null && mc.player.getDistance(target) > range.getDoubleValue() || target == mc.player)
			target = null;

		if(priority.is("Closest")) {
			if(players.enabled) {
				List<Entity> targetsPlayers = (List<Entity>) mc.world.loadedEntityList.stream().filter(EntityOtherPlayerMP.class::isInstance).collect(Collectors.toList());	
				
				targetsPlayers = targetsPlayers.stream().filter(entity -> entity.getDistance(mc.player) < range.getDoubleValue() && entity != mc.player && !entity.isDead && entity instanceof EntityOtherPlayerMP && !(entity instanceof EntityFakePlayer) && !((EntityOtherPlayerMP) entity).isDead).collect(Collectors.toList());
				
				targetsPlayers.sort(Comparator.comparingDouble(entity -> ((EntityOtherPlayerMP)entity).getDistance(mc.player)));
				
				if(!targetsPlayers.isEmpty() && !(targetsPlayers.get(0) == mc.player)) {
					target = targetsPlayers.get(0);
					
					String name = target.getName();

					if(!attackFriends.isEnabled() && Xeno.friendManager.isFriend(name))
						return;

					attack(target);
				}	
			}
			
			if(passive.enabled) {
				List<Entity> targetsMobs = (List<Entity>) mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());				
				targetsMobs = targetsMobs.stream().filter(entity -> entity.getDistance(mc.player) < range.getDoubleValue() && entity != mc.player && !entity.isDead && (entity instanceof EntityLivingBase) && !(entity instanceof EntityOtherPlayerMP) && !(entity instanceof EntityFakePlayer) && ((EntityLivingBase) entity).getHealth() > 0).collect(Collectors.toList());
			
				targetsMobs.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistance(mc.player)));
				
				if(!targetsMobs.isEmpty()) {
					target = targetsMobs.get(0);
					
					rotate(target);		
					
					rayTrace(target);
					
					entityName = target.getName().toString();
					
					attack(target);
				}	
			}		
    	} else if(priority.is("Health")) {
    		if(players.enabled) {
				List<Entity> players = (List<Entity>) mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());				
				players = players.stream().filter(entity -> entity.getDistance(mc.player) < range.getDoubleValue() && entity != mc.player && !entity.isDead && entity instanceof EntityOtherPlayerMP && !(entity instanceof EntityFakePlayer) && !entity.isDead).collect(Collectors.toList());
			
				if(players.size() == 1) {
					entityName = players.get(0).getName().toString();

					if(!attackFriends.isEnabled() && Xeno.friendManager.isFriend(entityName))
						return;
					attack(players.get(0));
					return;
				}
				
				players.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));
				
				if(!players.isEmpty()) {
					target = players.get(0);

					entityName = target.getName().toString();

					if(!attackFriends.isEnabled() && Xeno.friendManager.isFriend(entityName))
						return;

					attack(target);
				}	
			}
    		if(mobs.enabled) {
				List<Entity> mobs = (List<Entity>) mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());				
				mobs = mobs.stream().filter(entity -> entity.getDistance(mc.player) < range.getDoubleValue() && entity != mc.player && !entity.isDead && (entity instanceof EntityMob)).collect(Collectors.toList());
				
				if(mobs.size() == 1) {
					rotate(mobs.get(0));		
					rayTrace(mobs.get(0));					
					entityName = mobs.get(0).getName().toString();					
					attack(mobs.get(0));
					return;
				}
				
				mobs.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));
				
				if(!mobs.isEmpty()) {
					target = mobs.get(0);
					
					rotate(target);					
					rayTrace(target);
					entityName = target.getName().toString();			
					attack(target);
				}	
			}
			if(passive.enabled) {
				List<Entity> passives = (List<Entity>) mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());				
				passives = passives.stream().filter(entity -> entity.getDistance(mc.player) < range.getDoubleValue() && entity != mc.player && !entity.isDead && (entity instanceof EntityLivingBase) && !(entity instanceof EntityOtherPlayerMP)  && ((EntityLivingBase) entity).getHealth() > 0).collect(Collectors.toList());
			
				if(passives.size() == 1) {
					rotate(passives.get(0));		
					rayTrace(passives.get(0));					
					entityName = passives.get(0).getName().toString();					
					attack(passives.get(0));
					return;
				}
				
				passives.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));
				
				if(!passives.isEmpty()) {
					target = passives.get(0);				
					rotate(target);		
					rayTrace(target);				
					entityName = target.getName().toString();
					attack(target);
				}	
			}
    	}
    }

	@Override
	public void onRenderWorld() {
		if(target != null && render.isEnabled() && target != mc.player) {
			if(renderMode.is("Circle"))
				RenderUtils3D.drawCircle(new RenderBuilder().setup().line(1.5F).depth(true).blend().texture(), EntityUtil.getInterpolatedPos(target, 1), target.width, target.height * (0.5 * (Math.sin((mc.player.ticksExisted * 3.5) * (Math.PI / 180)) + 1)), renderColour.getColor());
			else if(renderMode.is("2D"))
				RenderUtils3D.draw2D(target, renderColour.getColor());
		}
	}

	public float[] getRotations(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.player.posX;
		double deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.player.posY + mc.player.getEyeHeight();
		double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.player.posZ;
		double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
		
		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
			  pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		
		if(deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}else if(deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}
		
		return new float[] {yaw, pitch};
	}
}
