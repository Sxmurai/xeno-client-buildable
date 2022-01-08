
package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import me.xenodevs.xeno.utils.render.builder.RenderBuilder;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Wolfsurge
 * @since 29/11/2020
 */

public class HoleESP extends Module {
    ModeSetting main = new ModeSetting("Main", "Glow", "Frame");
    ModeSetting secondary = new ModeSetting("Secondary", "Flat", "None");

    NumberSetting range = new NumberSetting("Range", 5, 1, 10, 1);
    NumberSetting height = new NumberSetting("Height", -1, -2, 1, 0.1);

    BooleanSetting obsidian = new BooleanSetting("Obsidian", true);
    BooleanSetting mixed = new BooleanSetting("Mixed", true);
    BooleanSetting bedrock = new BooleanSetting("Bedrock", true);

    ColourPicker obsidianCol = new ColourPicker("Obsidian", new Colour(144, 0, 255, 255));
    ColourPicker mixedCol = new ColourPicker("Mixed", Color.CYAN);
    ColourPicker bedrockCol = new ColourPicker("Bedrock", Color.GREEN);

    public HoleESP() {
        super("HoleESP", "highlights holes to stand in while crystalling", Category.RENDER);
        this.addSettings(main, secondary, range, height, obsidian, mixed, bedrock, obsidianCol, mixedCol, bedrockCol);
    }

    @Override
    public void onRenderWorld() {
        for(BlockPos pos : BlockUtil.getNearbyBlocks(mc.player, range.getFloatValue(), false)) {
            if(getType(pos) == HoleType.OBSIDIAN && obsidian.isEnabled())
                renderHole(pos, obsidianCol.getColor());
            else if(getType(pos) == HoleType.MIXED && mixed.isEnabled())
                renderHole(pos, mixedCol.getColor());
            else if(getType(pos) == HoleType.BEDROCK && bedrock.isEnabled())
                renderHole(pos, bedrockCol.getColor());
        }
    }

    public void renderHole(BlockPos pos, Color col) {
        // Main
        if(main.is("Glow"))
            RenderUtils3D.drawBoxBlockPos(pos, height.getDoubleValue(), 0, 0, col, RenderBuilder.RenderMode.Glow);
        else if(main.is("Frame"))
            RenderUtils3D.blockESPBox(pos, 1, col, col.getAlpha());

        // Secondary
        if(secondary.is("Flat"))
            RenderUtils3D.drawBoxBlockPos(pos, height.getDoubleValue(), 0, 0, col, RenderBuilder.RenderMode.Outline);
    }

    public HoleType getType(BlockPos pos) {
        if(isSurroundedByBlock(pos, Blocks.OBSIDIAN))
            return HoleType.OBSIDIAN;
        else if(isSurroundedByBlock(pos, Blocks.BEDROCK))
            return HoleType.BEDROCK;
        else if(isHoleMixed(pos))
            return HoleType.MIXED;
        else
            return null;
    }

    public boolean isSurroundedByBlock(BlockPos pos, Block blockCheck) {
        return getBlock(pos) == Blocks.AIR && getBlock(pos.north()) == blockCheck && getBlock(pos.south()) == blockCheck && getBlock(pos.east()) == blockCheck && getBlock(pos.west()) == blockCheck && getBlock(pos.down()) == blockCheck;
    }

    // spaghetti is nice
    public boolean isHoleMixed(BlockPos pos) {
        // return getBlock(pos) == Blocks.AIR && getBlock(pos.north()) == (Blocks.OBSIDIAN || Blocks.BEDROCK) && getBlock(pos.south()) == blockCheck && getBlock(pos.east()) == blockCheck && getBlock(pos.west()) == blockCheck && getBlock(pos.down()) == blockCheck;
        BlockPos north = pos.north();
        BlockPos south = pos.south();
        BlockPos east = pos.east();
        BlockPos west = pos.west();
        boolean isMixed = false;

        boolean northCheck = false;
        if(getBlock(north) == Blocks.OBSIDIAN)
            northCheck = true;
        else if(getBlock(north) == Blocks.BEDROCK)
            northCheck = true;

        boolean southCheck = false;
        if(getBlock(south) == Blocks.OBSIDIAN)
            southCheck = true;
        else if(getBlock(south) == Blocks.BEDROCK)
            southCheck = true;

        boolean westCheck = false;
        if(getBlock(west) == Blocks.OBSIDIAN)
            westCheck = true;
        else if(getBlock(west) == Blocks.BEDROCK)
            westCheck = true;

        boolean eastCheck = false;
        if(getBlock(east) == Blocks.OBSIDIAN)
            eastCheck = true;
        else if(getBlock(east) == Blocks.BEDROCK)
            eastCheck = true;

        boolean downCheck = false;
        if(getBlock(pos.down()) == Blocks.OBSIDIAN)
            downCheck = true;
        else if(getBlock(pos.down()) == Blocks.BEDROCK)
            downCheck = true;

        return northCheck && southCheck && westCheck && eastCheck && downCheck && getBlock(pos) == Blocks.AIR;
    }

    public Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    class Hole {
        BlockPos pos;
        HoleType type;
        public Hole(BlockPos position, HoleType type) {
            this.pos = position;
            this.type = type;
        }
    }

    public enum HoleType {
        OBSIDIAN, MIXED, BEDROCK
    }
}