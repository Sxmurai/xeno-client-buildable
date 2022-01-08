package me.xenodevs.xeno.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.sun.org.apache.xpath.internal.operations.Bool;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.render.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/*

Partially from WP3

 */

public class Nametags extends Module {

    public BooleanSetting customFont = new BooleanSetting("Custom Font", true);
    public BooleanSetting gameMode = new BooleanSetting("Gamemode", false);
    public BooleanSetting armour = new BooleanSetting("Armour", true);
    public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);

    // profiler thing that fyre suggested
    public BooleanSetting profiler = new BooleanSetting("Profiler", false);
    public BooleanSetting background = new BooleanSetting(profiler, "Background", true);
    public BooleanSetting selected = new BooleanSetting(profiler, "Selected", true);
    public ColourPicker selectedCol = new ColourPicker(profiler, "Colour", Colour.DARK_GRAY);

    public NumberSetting distance = new NumberSetting("Distance", 250, 0, 500, 1);
    public NumberSetting scale = new NumberSetting("Scale", 0.05, 0.01, 0.1, 0.01);
    public NumberSetting height = new NumberSetting("Height", 2.5, 0.5, 5.0, 0.1);
    public NumberSetting textOffset = new NumberSetting("TextOffset", 1, -5, 5, 1);

    public BooleanSetting outline = new BooleanSetting("Outline", true);
    public ModeSetting outlineWhere = new ModeSetting("Outline", "Top", "Left", "Right", "Bottom", "All");
    public NumberSetting outlineWidth = new NumberSetting("Width", 1, 0, 1.5, 0.05);
    public ColourPicker outlineColourFriend = new ColourPicker("Friend Colour", new Colour(0, 128, 255, 255));
    public ColourPicker outlineColourEnemy = new ColourPicker("Enemy Colour", new Colour(255, 0, 0, 255));

    private final ICamera camera = new Frustum();
    private boolean shownItem;

    public Nametags() {
        super("Nametags", "adds stuff above players nametags", Category.RENDER);
        this.addSettings(customFont, gameMode, armour, invisibles, distance, scale, height, textOffset, outline, outlineWhere, outlineWidth, outlineColourFriend, outlineColourEnemy);
    }

    @Override
    public void onRenderWorld() {
        if (nullCheck()) return;

        double cx = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.getRenderPartialTicks();
        double cy = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.getRenderPartialTicks();
        double cz = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.getRenderPartialTicks();

        camera.setPosition(cx, cy, cz);

        try {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (!player.isEntityAlive() || player == mc.getRenderViewEntity() ||
                        player.getDistance(mc.player) > this.distance.getDoubleValue() ||
                        !camera.isBoundingBoxInFrustum(player.getEntityBoundingBox())) continue;
                NetworkPlayerInfo npi = mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
                double pX = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX;
                double pY = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY;
                double pZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ;

                if (this.getShortGamemode(npi.getGameType().getName()).equalsIgnoreCase("SP") && !invisibles.getValue()) continue;
                renderNametag(player, pX, pY, pZ);
            }
        } catch (Exception e) {}

        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
    }

    public void renderNametag(EntityPlayer player, double x, double y, double z) {
        shownItem = false;
        GlStateManager.pushMatrix();
        FontRenderer var13 = mc.fontRenderer;
        NetworkPlayerInfo npi = mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
        boolean isFriend = Xeno.friendManager.isFriend(player.getName());
        boolean isEnemy = (Xeno.friendManager.isFriend(player.getName()) ? false : true);
        String name = ((isFriend || isEnemy) && player.isSneaking() ? SECTIONSIGN + "9" : SECTIONSIGN + "r")
                + player.getName() + ChatFormatting.WHITE
                + (gameMode.getValue() ? " [" + getShortGamemode(npi.getGameType().getName()) + "]" : "")
                + " " + SECTIONSIGN + getPing(npi.getResponseTime()) + npi.getResponseTime() + "ms"
                + " " + SECTIONSIGN + getHealth(player.getHealth() + player.getAbsorptionAmount())
                + MathHelper.ceil(player.getHealth() + player.getAbsorptionAmount());
        name = name.replace(".0", "");

        float distance = mc.player.getDistance(player);
        float var15 = (float) (((distance / 5 <= 2 ? 2.0F : (distance / 5) * ((scale.getDoubleValue() * 10) + 1)) * 2.5f) * (scale.getDoubleValue() / 10));
        boolean far = distance / 5 > 2;
        GL11.glTranslated((float) x, (float) y + height.getDoubleValue() - (player.isSneaking() ? 0.4 : 0), (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(mc.renderManager.playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, (float)0);
        GL11.glScalef(-var15, -var15, var15);
        GLUtil.disableGlCap(GL_LIGHTING, GL_DEPTH_TEST);
        GLUtil.enableGlCap(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        int width;

        if (customFont.getValue()) {
            width = FontManager.comfortaa.getStringWidth(name) / 2 + 1;
        } else {
            width = var13.getStringWidth(name) / 2 + 1;
        }

        int height = (customFont.isEnabled() ? FontManager.comfortaa.getHeight() : mc.fontRenderer.FONT_HEIGHT) + 2;

        RenderUtils2D.drawRect(-width - 2, 0, (-width - 2) + (width + 1) * 2, height + 2 + (customFont.getValue() ? 0.5 : 0), 0x70000000);
        int outlineCol = isFriend ? outlineColourFriend.getColor().getRGB() : outlineColourEnemy.getColor().getRGB();

        // opengl decided to be weird, so here we are
        if(outlineWhere.is("Top"))
            RenderUtils2D.drawRect(-width - 2, outlineWidth.getDoubleValue(), (-width - 2) + (width + 1) * 2, 0, outlineCol);
        else if(outlineWhere.is("Left"))
            RenderUtils2D.drawRect(-width - 2 - outlineWidth.getDoubleValue(), 0, -width - 2, height + 1 + (customFont.getValue() ? 0.5 : 0), outlineCol);
        else if(outlineWhere.is("Right"))
            RenderUtils2D.drawRect((-width - 2) + (width + 1) * 2, 0, (-width - 2) + (width + 1) * 2 + outlineWidth.getDoubleValue(), height + 1 + (customFont.getValue() ? 0.5 : 0), outlineCol);
        else if(outlineWhere.is("Bottom"))
            RenderUtils2D.drawRect(-width - 2, height + 1 + (customFont.getValue() ? 0.5 : 0), (-width - 2) + (width + 1) * 2, height + 1 + (customFont.getValue() ? 0.5 : 0) + outlineWidth.getDoubleValue(), outlineCol);
        else if(outlineWhere.is("All")) {
            RenderUtils2D.drawRect(-width - 2, outlineWidth.getDoubleValue(), (-width - 2) + (width + 1) * 2, 0, outlineCol);
            RenderUtils2D.drawRect(-width - 2 - outlineWidth.getDoubleValue(), 0, -width - 2, height + 1 + (customFont.getValue() ? 0.5 : 0) + outlineWidth.getDoubleValue(), outlineCol);
            RenderUtils2D.drawRect((-width - 2) + (width + 1) * 2, 0, (-width - 2) + (width + 1) * 2 + outlineWidth.getDoubleValue(), height + 1 + (customFont.getValue() ? 0.5 : 0) + outlineWidth.getDoubleValue(), outlineCol);
            RenderUtils2D.drawRect(-width - 2, height + 1 + (customFont.getValue() ? 0.5 : 0), (-width - 2) + (width + 1) * 2, height + 1 + (customFont.getValue() ? 0.5 : 0) + outlineWidth.getDoubleValue(), outlineCol);
        }

        if (customFont.getValue()) {
            FontManager.comfortaa.drawStringWithShadow(name, -width, 3 + (float) textOffset.getDoubleValue(), outlineCol);
        } else {
            mc.fontRenderer.drawStringWithShadow(name, -width, 2 + (float) textOffset.getDoubleValue(), outlineCol);
        }

        if(profiler.isEnabled()) {
            int count = -81;
            int itemCount = 0;
            if(background.isEnabled())
                RenderUtils2D.drawRect(count - 1, -44, 18 * 5 - 5, -21, 0x90000000);
            /* for(ItemStack itemStack : player.inventory.mainInventory) {
                if(itemCount < 9) {
                    if(selected.isEnabled() && player.inventory.currentItem == itemCount) {
                        RenderUtils2D.drawRoundedRect(count - 0.75, -44, 18, 23, 1, selectedCol.getColor().getRGB());
                    }
                    renderItem(player, itemStack, count, -50, count, false);
                }

                count += 18;
                itemCount++;
            } */

            NonNullList<ItemStack> inventoryPlayer = player.inventory.mainInventory;

            for(int i = 0; i < 9; i++) {
                renderItem(player, inventoryPlayer.get(i), count, -50, count, false);
                count += 18;
                itemCount++;
            }
        }

        if (armour.getValue()) {
            int xOffset = -6;
            int count = 0;
            for (ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack != null) {
                    xOffset -= 8;
                    if (armourStack.getItem() != Items.AIR) ++count;
                }
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) ++count;

            int cacheX = xOffset - 8;
            xOffset += 8 * (5 - count) - (count == 0 ? 4 : 0);

            ItemStack renderStack;
            if (player.getHeldItemMainhand().getItem() != Items.AIR) {
                xOffset -= 8;
                renderStack = player.getHeldItemMainhand().copy();
                renderItem(player, renderStack, xOffset, -7, cacheX, true);
                xOffset += 16;
            } else {
                shownItem = true;
            }
            for (int index = 3; index >= 0; --index) {
                ItemStack armourStack = player.inventory.armorInventory.get(index);
                if (armourStack.getItem() != Items.AIR) {
                    ItemStack renderStack1 = armourStack.copy();
                    renderItem(player, renderStack1, xOffset, -7, cacheX, false);
                    xOffset += 16;
                }
            }
            ItemStack renderOffhand;
            if ( player.getHeldItemOffhand().getItem() != Items.AIR) {
                renderOffhand = player.getHeldItemOffhand().copy();
                renderItem(player, renderOffhand, xOffset, -7, cacheX, false);
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        } else {
            if(armour.getValue()) {
                int xOffset = -6;
                int count = 0;
                for (ItemStack armourStack : player.inventory.armorInventory) {
                    if (armourStack != null) {
                        xOffset -= 8;
                        if (armourStack.getItem() != Items.AIR) count++;
                    }
                }
                if (player.getHeldItemOffhand().getItem() != Items.AIR) count++;
                xOffset += 8 * (5 - count) - (count == 0 ? 4 : 0);
                for (int index = 3; index >= 0; --index) {
                    ItemStack armourStack = player.inventory.armorInventory.get(index);
                    if (armourStack.getItem() != Items.AIR) {
                        ItemStack renderStack1 = armourStack.copy();
                        renderDurabilityText(renderStack1, xOffset);
                        xOffset += 16;
                    }
                }
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.enableTexture2D();
            }
        }
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GLUtil.reset();
        GlStateManager.resetColor();
        glColor4f(1F, 1F, 1F, 1F);
        glPopMatrix();
    }

    private void renderDurabilityText(ItemStack stack, int x) {
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword
                || stack.getItem() instanceof ItemTool) {
            float green = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
            float red = 1 - green;
            int dmg = 100 - (int) (red * 100);
            if (customFont.getValue()) {
                FontManager.comfortaa.drawStringWithShadow(dmg + "%", x * 2 + 4, -7 - 10,
                        (new Color(red, green, 0)).getRGB());
            } else {
                mc.fontRenderer.drawStringWithShadow(dmg + "%", x * 2 + 4, -7 - 10,
                        (new Color(red, green, 0)).getRGB());
            }
        }
    }

    public void renderItem(EntityPlayer player, ItemStack stack, int x, int y, int nameX, boolean showHeldItemText) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);

        // enchant fix
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();

        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -100.0F;
        GlStateManager.scale(1, 1, 0.01f);
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, (y / 2) - 15);
        if (armour.getValue()) {
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, (y / 2) - 15);
        }
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.scale(1, 1, 1);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        renderEnchantText(player, stack, x, y - 22);
        if (!shownItem && showHeldItemText) {
            if (customFont.getValue()) {
                // FontManager.comfortaa.drawStringWithShadow(stack.getDisplayName().equalsIgnoreCase("Air") ? "" : stack.getDisplayName(), nameX, y - 42, -1);
            } else {
                // mc.fontRenderer.drawStringWithShadow(stack.getDisplayName().equalsIgnoreCase("Air") ? "" : stack.getDisplayName(), nameX - 45, y - 42, -1);
            }
            shownItem = true;
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    public void renderEnchantText(EntityPlayer player, ItemStack stack, int x, int y) {
        int yCount = y;
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
            if(armour.getValue()) {
                float green = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
                float red = 1 - green;
                int dmg = 100 - (int) (red * 100);
                if (customFont.getValue()) {
                    FontManager.comfortaa.drawStringWithShadow(dmg + "%", x * 2 + 4, y - 10, (new Color(red, green, 0)).getRGB());
                } else {
                    mc.fontRenderer.drawStringWithShadow(dmg + "%", x * 2 + 4, y - 10, (new Color(red, green, 0)).getRGB());
                }
            }
        }

        NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            short id = enchants.getCompoundTagAt(index).getShort("id");
            short level = enchants.getCompoundTagAt(index).getShort("lvl");
            Enchantment enc = Enchantment.getEnchantmentByID(id);
            if (enc != null) {
                if (enc.isCurse()) continue;
                String encName = level == 1 ? enc.getTranslatedName(level).substring(0, 3).toLowerCase() : enc.getTranslatedName(level).substring(0, 2).toLowerCase() + level;

                encName = encName.substring(0, 1).toUpperCase() + encName.substring(1);
                GL11.glPushMatrix();
                GL11.glScalef(1f, 1f, 0);
                if (customFont.getValue()) {
                    FontManager.comfortaa.drawStringWithShadow(encName, (x * 2 + 3), yCount, -1);
                } else {
                    mc.fontRenderer.drawStringWithShadow(encName, (x * 2 + 3), yCount, -1);
                }
                GL11.glScalef(1f, 1f, 1);
                GL11.glPopMatrix();
                yCount += 10;
            }
        }
    }

    public boolean isMaxEnchants(ItemStack stack) {
        return stack.getEnchantmentTagList().tagCount() > 2;
    }

    public String getHealth(float health) {
        if (health > 18) {
            return "a";
        }
        else if (health > 16) {
            return "2";
        }
        else if (health > 12) {
            return "e";
        }
        else if (health > 8) {
            return "6";
        }
        else if (health > 5) {
            return "c";
        }
        else {
            return "4";
        }
    }

    public String getPing(float ping) {
        if (ping > 200) {
            return "c";
        }
        else if (ping > 100) {
            return "e";
        }
        else {
            return "a";
        }
    }

    public String getPop(int pops) {
        return "";
    }

    public static int changeAlpha(int origColor, int userInputedAlpha) {
        origColor = origColor & 0x00ffffff;
        return (userInputedAlpha << 24) | origColor;
    }

    public String getShortGamemode(String gameType) {
        if (gameType.equalsIgnoreCase("survival")) {
            return "S";
        }
        else if (gameType.equalsIgnoreCase("creative")) {
            return "C";
        }
        else if (gameType.equalsIgnoreCase("adventure")) {
            return "A";
        }
        else if (gameType.equalsIgnoreCase("spectator")) {
            return "SP";
        }
        else {
            return "NONE";
        }
    }
}
