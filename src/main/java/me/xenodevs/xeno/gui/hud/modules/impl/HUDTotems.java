package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.awt.*;

public class HUDTotems extends HUDMod {

	public HUDTotems() {
		super("Totems", 0, 185, Xeno.moduleManager.getModule("Totems"));
	}	

	public int getTotems() {
		if(nullCheck())
			return 0;

		NonNullList<ItemStack> inv;
		ItemStack offhand = mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

		int inventoryIndex;

		inv = mc.player.inventory.mainInventory;

		int totems = 0;

		for(inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {
			if(inv.get(inventoryIndex).getItem() == Items.TOTEM_OF_UNDYING) {
				totems++;
			}
		}

		if(offhand.getItem() == Items.TOTEM_OF_UNDYING) {
			totems++;
		}

		return totems;
	}

	@Override
	public void draw() {
		TextUtil.drawStringWithShadow("Totems: " + getTotems(), getX() + 1, getY(), Colors.colourInt);
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {	
		super.renderDummy(mouseX, mouseY);
		
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		TextUtil.drawStringWithShadow("Totems: " + getTotems(), getX() + 1, getY(), this.parent.enabled ? Colors.colourInt : 0xFF900000);
	}
	
	@Override
	public int getWidth() {
		return TextUtil.getStringWidth("Totems: " + getTotems());
	}
	
	@Override
	public float getHeight() {
		return 11;
	}
	
}
