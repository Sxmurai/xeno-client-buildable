package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.awt.*;

public class HUDInventory extends HUDMod {

	private final Minecraft mc = Minecraft.getMinecraft();
	private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

	public HUDInventory() {
		super("Inventory", 0, 130, Xeno.moduleManager.getModule("Inventory"));
	}	

	@Override
	public void draw() {
		super.draw();

		drag.setHeight(getHeight());
		drag.setWidth(getWidth());

		RenderUtils2D.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 3, 0x90000000);

		NonNullList<ItemStack> items = Minecraft.getMinecraft().player.inventory.mainInventory;
		for(int size = items.size(), item = 9; item < size; ++item) {
			int slotX = getX() + 1 + item % 9 * 18;
			int slotY = getY() + 1 + (item / 9 - 1) * 18;

			RenderUtils2D.drawRoundedRect(slotX, slotY, 16, 16, 2, new Colour(new Colour(Color.GRAY), 50).getRGB());

			String text = (items.get(item).getCount() > 1 ? String.valueOf(items.get(item).getCount()) : "");

			GlStateManager.enableDepth();
			itemRender.zLevel = 200F;
			itemRender.renderItemAndEffectIntoGUI(items.get(item), slotX, slotY);
			itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, items.get(item), slotX, slotY, "");
			itemRender.zLevel = 0F;

			GlStateManager.enableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();

			TextUtil.drawStringWithShadow(text, slotX + 16 - TextUtil.getStringWidth(text) - 1, slotY + 7, -1);
		}
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {	
		super.renderDummy(mouseX, mouseY);
		
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		RenderUtils2D.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 3, 0x90000000);

		NonNullList<ItemStack> items = Minecraft.getMinecraft().player.inventory.mainInventory;
		for (int size = items.size(), item = 9; item < size; ++item) {
			int slotX = getX() + 1 + item % 9 * 18;
			int slotY = getY() + 1 + (item / 9 - 1) * 18;

			RenderUtils2D.drawRoundedRect(slotX, slotY, 16, 16, 2, new Colour(new Colour(Color.GRAY), 50).getRGB());

			String text = (items.get(item).getCount() > 1 ? String.valueOf(items.get(item).getCount()) : "");

			GlStateManager.enableDepth();
			itemRender.zLevel = 200F;
			itemRender.renderItemAndEffectIntoGUI(items.get(item), slotX, slotY);
			itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, items.get(item), slotX, slotY, "");
			itemRender.zLevel = 0F;

			GlStateManager.enableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();

			TextUtil.drawStringWithShadow(text, slotX + 16 - TextUtil.getStringWidth(text) - 1, slotY + 7, -1);
		}
	}
	
	@Override
	public int getWidth() {
		return 162;
	}
	
	@Override
	public float getHeight() {
		return 54;
	}
	
}
