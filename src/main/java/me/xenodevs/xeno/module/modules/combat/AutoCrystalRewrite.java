package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.entity.EntityFakePlayer;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import me.xenodevs.xeno.utils.render.builder.RenderBuilder;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AutoCrystalRewrite extends Module {

    ModeSetting targetPriority = new ModeSetting("TargetPriority", "Closest", "Health", "TotemCount");
    NumberSetting targetRange = new NumberSetting("TargetRange", 10, 1, 15, 1);
    BooleanSetting reverseTargetList = new BooleanSetting("RvsTargetList", false);

    // place
    BooleanSetting place = new BooleanSetting("Place", true);
    NumberSetting placeRange = new NumberSetting(place, "Range", 5, 1, 10, 1);

    EntityPlayer target = null;
    BlockPos placePos = null;

    public AutoCrystalRewrite() {
        super("ACRewrite", "better ca", Category.COMBAT);
        this.addSettings(targetPriority, targetRange, reverseTargetList); // target settings
        this.addSettings(place, placeRange);
    }

    @Override
    public void onEnable() {
        target = null;
        placePos = null;
    }

    @Override
    public void onDisable() {
        target = null;
        placePos = null;
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;

        findBestPlayer();

        if(place.isEnabled())
            placeCrystals();
    }

    @Override
    public void onRenderWorld() {
        if(nullCheck())
            return;

        if(getBestBlock() != null && target != null) {
            RenderUtils3D.solidBlockESPBox(getBestBlock(), 1, new Color(0, 128, 255, 100), 0.25f);
            RenderUtils3D.blockESPBox(getBestBlock(), 1, new Color(0, 128, 255, 255), 1f);
        }
    }

    public void breakCrystals() {}

    public void placeCrystals() {
        if(mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)
            return;

        if(target != null) {
            BlockUtil.placeCrystalOnBlock(getBestBlock(), EnumHand.MAIN_HAND, true);
        }
    }

    public List<BlockPos> allPositions() {
        List<BlockPos> returnPos = new ArrayList<>();

        if(target == null)
            return returnPos;

        for(BlockPos pos : BlockUtil.getNearbyBlocks(target, placeRange.getFloatValue(), false)) {
            if(mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
                if(mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR)
                    returnPos.add(pos);
            }
        }

        return returnPos;
    }

    public void findBestPlayer() {
        switch (targetPriority.getMode()) {
            case "Closest":
                target = getClosest();
                break;
            case "Health":
                target = getLowestHealth();
                break;
            case "TotemCount":
                target = getLowestTotemCount();
                break;
        }
    }

    public EntityPlayer getClosest() {
        List<Entity> players = mc.world.loadedEntityList.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
        players = players.stream().filter(entity -> entity.getDistance(mc.player) <= targetRange.getDoubleValue() && entity != mc.player && !entity.isDead && entity instanceof EntityPlayer).collect(Collectors.toList());

        players.sort(Comparator.comparingDouble(entity -> entity.getDistance(mc.player)));

        if(reverseTargetList.isEnabled())
            Collections.reverse(players);

        if(!players.isEmpty()) {
            return (EntityPlayer) players.get(0);
        } else {
            return null;
        }
    }

    public BlockPos getBestBlock() {
        float damage = 0;
        BlockPos finPos = null;
        for(BlockPos pos : allPositions()) {
            if(calculateDamage(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, target) >= damage)
                finPos = pos;
        }
        return finPos;
    }

    public EntityPlayer getLowestHealth() {
        List<Entity> players = mc.world.loadedEntityList.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
        players = players.stream().filter(entity -> entity.getDistance(mc.player) < targetRange.getDoubleValue() && entity != mc.player && !entity.isDead && entity instanceof EntityPlayer).collect(Collectors.toList());

        players.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth()));

        if(reverseTargetList.isEnabled())
            Collections.reverse(players);

        if(!players.isEmpty()) {
            return (EntityPlayer) players.get(0);
        } else {
            return null;
        }
    }

    public EntityPlayer getLowestTotemCount() {
        List<Entity> players = mc.world.loadedEntityList.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
        players = players.stream().filter(entity -> entity.getDistance(mc.player) < targetRange.getDoubleValue() && entity != mc.player && !entity.isDead && entity instanceof EntityPlayer).collect(Collectors.toList());

        players.sort(Comparator.comparingDouble(entity -> getTotems((EntityPlayer) entity)).reversed());
        
        if(reverseTargetList.isEnabled())
            Collections.reverse(players);

        if(!players.isEmpty()) {
            return (EntityPlayer) players.get(0);
        } else {
            return null;
        }
    }

    public int getTotems(EntityPlayer player) {
        if(nullCheck())
            return 0;

        NonNullList<ItemStack> inv;
        ItemStack offhand = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        int inventoryIndex;

        inv = player.inventory.mainInventory;

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

    public float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        try {
            double factor = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0) * entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());

            float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0f * 7.0f * 12.0f + 1.0f);

            double damage = 1.0;

            if (entity instanceof EntityLivingBase) {//maybe i broke this by making entity entity not null lol idk
                damage = getBlastReduction((EntityLivingBase) entity, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, entity, posX, posY, posZ, 6.0f, false, true));
            }

            return (float) damage;
        } catch (Exception ignored) {}

        return 0.0f;
    }

    public float getBlastReduction(EntityLivingBase entityLivingBase, float damage, Explosion explosion) {
        if (entityLivingBase instanceof EntityPlayer) {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(entityLivingBase.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0f, 20.0f) / 25.0f;

            if (entityLivingBase.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }

            return damage;
        }

        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        return damage;
    }

    @Override
    public String getHUDData() {
        return (target == null ? " No Target" : " " + target.getName());
    }
}
