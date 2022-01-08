package me.xenodevs.xeno.module.modules.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.xenodevs.xeno.event.impl.RenderTileEntityEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.OutlineUtils;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;

public class StorageESP extends Module {

	public static BooleanSetting Chests = new BooleanSetting("Chests", true);
	public static ColourPicker chestColour = new ColourPicker("Chest Colour", new Colour(Color.YELLOW));
	public BooleanSetting Shulkers = new BooleanSetting("Shulkers", true);
	public static ColourPicker shulkerColour = new ColourPicker("Shulker Colour", new Colour(Color.MAGENTA));
	public BooleanSetting EChests = new BooleanSetting("EnderChests", true);
	public static ColourPicker enderChestColour = new ColourPicker("EChest Colour", new Colour(Color.PINK));
	public static NumberSetting lineThickness = new NumberSetting("LineWidth", 3, 0.5, 3, 0.5);
	
	public static StorageESP instance = new StorageESP();
	
	public ModeSetting mode = new ModeSetting("Mode", "Outline", "Box", "FilledBox");
	
	public StorageESP() {
		super("StorageESP", "highlights storage blocks eg chests", 0, Category.RENDER);
		this.addSettings(Chests, Shulkers, EChests, chestColour, shulkerColour, enderChestColour, lineThickness, mode);
	}
	
	@EventHandler
	private final Listener<RenderTileEntityEvent> renderListener = new Listener<>(event -> {
		if(this.mode.is("Outline")) {
			GL11.glPushMatrix();
	        GlStateManager.depthMask(true);
	        
	        int multiplier = 1;
	        
	        if (Minecraft.getMinecraft().world != null && event.getTileentityIn() instanceof TileEntityChest && Chests.enabled) {
		        TileEntity tileentityIn = event.getTileentityIn();
		        float partialTicks = event.getPartialTicks();
		        int destroyStage = event.getDestroyStage();
		        BlockPos blockpos = tileentityIn.getPos();
		        
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderOne((float) lineThickness.value * multiplier);
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderTwo();
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderThree();
		        OutlineUtils.renderFour(chestColour.getColor());
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderFive();
	        }
	        
	        if (Minecraft.getMinecraft().world != null && event.getTileentityIn() instanceof TileEntityShulkerBox && Shulkers.enabled) {
		        TileEntity tileentityIn = event.getTileentityIn();
		        float partialTicks = event.getPartialTicks();
		        int destroyStage = event.getDestroyStage();
		        BlockPos blockpos = tileentityIn.getPos();
		        
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderOne((float) lineThickness.value * multiplier);
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderTwo();
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderThree();
		        OutlineUtils.renderFour(shulkerColour.getColor());
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderFive();
	        }
	        
	        if (Minecraft.getMinecraft().world != null && event.getTileentityIn() instanceof TileEntityEnderChest && EChests.enabled) {
		        TileEntity tileentityIn = event.getTileentityIn();
		        float partialTicks = event.getPartialTicks();
		        int destroyStage = event.getDestroyStage();
		        BlockPos blockpos = tileentityIn.getPos();
		        
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderOne((float) lineThickness.value * multiplier);
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderTwo();
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderThree();
		        OutlineUtils.renderFour(enderChestColour.getColor()); // Or new Color(0, 128, 255)
		        event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
		        OutlineUtils.renderFive();
	        }
	        
	        GL11.glPopMatrix();
		}
    });
	
	@Override
	public void onRenderWorld() {		
		for(Object e : mc.world.loadedTileEntityList) {
			if(this.mode.is("Box")) {
				if(e instanceof TileEntityChest && Chests.enabled) {
					RenderUtils3D.blockESPBox(((TileEntity) e).getPos(), (float) lineThickness.getDoubleValue(), chestColour.getColor(), 1);
				}
				
				if(e instanceof TileEntityShulkerBox && Shulkers.enabled) {
					RenderUtils3D.blockESPBox(((TileEntity) e).getPos(), (float) lineThickness.getDoubleValue(), shulkerColour.getColor(), 1);
				}
				
				if(e instanceof TileEntityEnderChest && EChests.enabled) {
					RenderUtils3D.blockESPBox(((TileEntity) e).getPos(), (float) lineThickness.getDoubleValue(), enderChestColour.getColor(), 1);
				}
			}
			
			if(this.mode.is("FilledBox")) {
				if(e instanceof TileEntityChest && Chests.enabled) {
					RenderUtils3D.solidBlockESPBox(((TileEntity) e).getPos(), (float) lineThickness.getDoubleValue(), chestColour.getColor(), 1);
				}
				
				if(e instanceof TileEntityShulkerBox && Shulkers.enabled) {
					RenderUtils3D.solidBlockESPBox(((TileEntity) e).getPos(), (float) lineThickness.getDoubleValue(), shulkerColour.getColor(), 1);
				}
				
				if(e instanceof TileEntityEnderChest && EChests.enabled) {
					RenderUtils3D.solidBlockESPBox(((TileEntity) e).getPos(), (float) lineThickness.getDoubleValue(), enderChestColour.getColor(), 1);
				}
			}
		}
	}
	
	@Override
	public String getHUDData() {
		return " " + this.mode.getMode();
	}
}

