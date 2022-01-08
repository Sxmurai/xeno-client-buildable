package me.xenodevs.xeno.gui.hud.modules.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.hud.Armour;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class HUDArmour extends HUDMod {

	private final Minecraft mc = Minecraft.getMinecraft();
	private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
	
	public HUDArmour() {
		super("Armour", 0, 60, Xeno.moduleManager.getModule("Armour"));
	}	

	@Override
	public void draw() {
		if(Armour.orientation.is("Down")) {
			int yOffset = 69;

			GL11.glPushMatrix();
			RenderUtils2D.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 3, 0x90000000);

			for (ItemStack i : mc.player.inventory.armorInventory) {
				yOffset -= 17;

				if (i.isEmpty)
					continue;

				RenderUtils2D.drawRoundedRect(getX() + 1, getY() + yOffset, getWidth() - 2, 16, 3, new Colour(new Colour(Color.GRAY), 100).getRGB());

				GlStateManager.enableDepth();
				itemRender.zLevel = 200F;
				itemRender.renderItemAndEffectIntoGUI(i, getX() + 2, getY() + yOffset);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, i, getX() + 2, getY() + yOffset, "");
				itemRender.zLevel = 0F;

				GlStateManager.enableTexture2D();
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();

				float green = ((float) i.getMaxDamage() - (float) i.getItemDamage()) / (float) i.getMaxDamage();
				float red = 1 - green;
				int dmg = 100 - (int) (red * 100);

				TextUtil.drawStringWithShadow(dmg + "%", getX() + 19, getY() + yOffset + 3, this.parent.enabled ? Colors.colourInt : 0xFF900000);
			}
			GL11.glPopMatrix();
		} else if(Armour.orientation.is("Across")) {
			GlStateManager.enableTexture2D();

			RenderUtils2D.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 3, 0x90000000);

			int iteration = 0;
			int y = getY() + 1;
			ArrayList<ItemStack> armourList = new ArrayList<>();

			for(ItemStack is : mc.player.inventory.armorInventory) {armourList.add(is);}

			Collections.reverse(armourList);

			for(ItemStack is : armourList) {
				if (is.isEmpty()) continue;

				int x = (getX() + 1 + (iteration*27));

				RenderUtils2D.drawRoundedRect(x, y, 26, 24, 3, new Colour(new Colour(Color.GRAY), 100).getRGB());

				GlStateManager.enableDepth();
				itemRender.zLevel = 200F;
				itemRender.renderItemAndEffectIntoGUI(is, x + 4, y + 8);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x + 4, y + 8, "");
				itemRender.zLevel = 0F;

				GlStateManager.enableTexture2D();
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();

				float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
				float red = 1 - green;
				int dmg = 100 - (int) (red * 100);

				TextUtil.drawCenteredString(dmg + "%", x + 13, y, Colors.colourInt);

				iteration++;
			}

			GlStateManager.enableDepth();
			GlStateManager.disableLighting();
		}

		drag.setHeight(getHeight());
		drag.setWidth(getWidth());

		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {
		if(Armour.orientation.is("Down")) {
			int yOffset = 69;

			GL11.glPushMatrix();
			RenderUtils2D.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 3, 0x90000000);

			for (ItemStack i : mc.player.inventory.armorInventory) {
				yOffset -= 17;

				if (i.isEmpty)
					continue;

				RenderUtils2D.drawRoundedRect(getX() + 1, getY() + yOffset, getWidth() - 2, 16, 3, new Colour(new Colour(Color.GRAY), 100).getRGB());

				GlStateManager.enableDepth();
				itemRender.zLevel = 200F;
				itemRender.renderItemAndEffectIntoGUI(i, getX() + 2, getY() + yOffset);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, i, getX() + 2, getY() + yOffset, "");
				itemRender.zLevel = 0F;

				GlStateManager.enableTexture2D();
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();

				float green = ((float) i.getMaxDamage() - (float) i.getItemDamage()) / (float) i.getMaxDamage();
				float red = 1 - green;
				int dmg = 100 - (int) (red * 100);

				TextUtil.drawStringWithShadow(dmg + "%", getX() + 19, getY() + yOffset + 3, this.parent.enabled ? Colors.colourInt : 0xFF900000);
			}
			GL11.glPopMatrix();
		} else if(Armour.orientation.is("Across")) {
			GlStateManager.enableTexture2D();

			RenderUtils2D.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 3, 0x90000000);

			int iteration = 0;
			int y = getY() + 1;
			ArrayList<ItemStack> armourList = new ArrayList<>();

			for(ItemStack is : mc.player.inventory.armorInventory) {armourList.add(is);}

			Collections.reverse(armourList);

			for(ItemStack is : armourList) {
				if (is.isEmpty()) continue;

				int x = (getX() + 1 + (iteration*27));

				RenderUtils2D.drawRoundedRect(x, y, 26, 24, 3, new Colour(new Colour(Color.GRAY), 100).getRGB());

				GlStateManager.enableDepth();
				itemRender.zLevel = 200F;
				itemRender.renderItemAndEffectIntoGUI(is, x + 4, y + 8);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x + 4, y + 8, "");
				itemRender.zLevel = 0F;

				GlStateManager.enableTexture2D();
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();

				float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
				float red = 1 - green;
				int dmg = 100 - (int) (red * 100);

				TextUtil.drawCenteredString(dmg + "%", x + 13, y, Colors.colourInt);

				iteration++;
			}

			GlStateManager.enableDepth();
			GlStateManager.disableLighting();
		}

		drag.setHeight(getHeight());
		drag.setWidth(getWidth());

		super.renderDummy(mouseX, mouseY);
	}
	
	@Override
	public int getWidth() {
		return (Armour.orientation.is("Down") ? Xeno.moduleManager.isModuleEnabled("CustomFont") ? 44 : 46 : 1 + (4*27));
	}
	
	@Override
	public float getHeight() {
		return (Armour.orientation.is("Down") ? 69 : 26);
	}
	
}
