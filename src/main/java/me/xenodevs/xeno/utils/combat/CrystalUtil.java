package me.xenodevs.xeno.utils.combat;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CrystalUtil implements Globals {

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int) r;
        while ((float) x <= (float) cx + r) {
            int z = cz - (int) r;
            while ((float) z <= (float) cz + r) {
                int y = sphere ? cy - (int) r : cy;
                while (true) {
                    float f = y;
                    float f2 = sphere ? (float) cy + r : (float) (cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double) (r * r)) || hollow && dist < (double) ((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(PlayerUtil.getPlayerPos(), placeRange, (int) placeRange, false, true, 0).stream().filter(pos -> mc.world.getBlockState(pos).getBlock() != Blocks.AIR).filter(pos -> canPlaceCrystal(pos, specialEntityCheck, oneDot15)).collect(Collectors.toList()));
        return positions;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean onepointThirteen) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (!onepointThirteen) {
                if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            } else {
                if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static double calc(Entity target, BlockPos pos, boolean ignoreTerrain) {
        return getExplosionDamage(target, new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), 6.0f, ignoreTerrain);
    }

    public static float getDamageFromDifficulty(float damage) {
        switch (mc.world.getDifficulty()) {
            case PEACEFUL: return 0;
            case EASY:     return Math.min(damage / 2 + 1, damage);
            case HARD:     return damage * 3 / 2;
            default:       return damage;
        }
    }

    public static boolean rayTraceSolidCheck(Vec3d start, Vec3d end, boolean shouldIgnore) {
        if (!Double.isNaN(start.x) && !Double.isNaN(start.y) && !Double.isNaN(start.z)) {
            if (!Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z)) {
                int currX = MathHelper.floor(start.x);
                int currY = MathHelper.floor(start.y);
                int currZ = MathHelper.floor(start.z);

                int endX = MathHelper.floor(end.x);
                int endY = MathHelper.floor(end.y);
                int endZ = MathHelper.floor(end.z);

                BlockPos blockPos = new BlockPos(currX, currY, currZ);
                IBlockState blockState = mc.world.getBlockState(blockPos);
                net.minecraft.block.Block block = blockState.getBlock();

                if ((blockState.getCollisionBoundingBox(mc.world, blockPos) != Block.NULL_AABB) &&
                        block.canCollideCheck(blockState, false) && (getBlocks().contains(block) || !shouldIgnore)) {
                    return true;
                }

                double seDeltaX = end.x - start.x;
                double seDeltaY = end.y - start.y;
                double seDeltaZ = end.z - start.z;

                int steps = 200;

                while (steps-- >= 0) {
                    if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) return false;
                    if (currX == endX && currY == endY && currZ == endZ) return false;

                    boolean unboundedX = true;
                    boolean unboundedY = true;
                    boolean unboundedZ = true;

                    double stepX = 999.0;
                    double stepY = 999.0;
                    double stepZ = 999.0;
                    double deltaX = 999.0;
                    double deltaY = 999.0;
                    double deltaZ = 999.0;

                    if (endX > currX) {
                        stepX = currX + 1.0;
                    } else if (endX < currX) {
                        stepX = currX;
                    } else {
                        unboundedX = false;
                    }

                    if (endY > currY) {
                        stepY = currY + 1.0;
                    } else if (endY < currY) {
                        stepY = currY;
                    } else {
                        unboundedY = false;
                    }

                    if (endZ > currZ) {
                        stepZ = currZ + 1.0;
                    } else if (endZ < currZ) {
                        stepZ = currZ;
                    } else {
                        unboundedZ = false;
                    }

                    if (unboundedX) deltaX = (stepX - start.x) / seDeltaX;
                    if (unboundedY) deltaY = (stepY - start.y) / seDeltaY;
                    if (unboundedZ) deltaZ = (stepZ - start.z) / seDeltaZ;

                    if (deltaX == 0.0) deltaX = -1.0e-4;
                    if (deltaY == 0.0) deltaY = -1.0e-4;
                    if (deltaZ == 0.0) deltaZ = -1.0e-4;

                    EnumFacing facing;

                    if (deltaX < deltaY && deltaX < deltaZ) {
                        facing = endX > currX ? EnumFacing.WEST : EnumFacing.EAST;
                        start = new Vec3d(stepX, start.y + seDeltaY * deltaX, start.z + seDeltaZ * deltaX);
                    } else if (deltaY < deltaZ) {
                        facing = endY > currY ? EnumFacing.DOWN : EnumFacing.UP;
                        start = new Vec3d(start.x + seDeltaX * deltaY, stepY, start.z + seDeltaZ * deltaY);
                    } else {
                        facing = endZ > currZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        start = new Vec3d(start.x + seDeltaX * deltaZ, start.y + seDeltaY * deltaZ, stepZ);
                    }

                    currX = MathHelper.floor(start.x) - (facing == EnumFacing.EAST ? 1 : 0);
                    currY = MathHelper.floor(start.y) - (facing == EnumFacing.UP ? 1 : 0);
                    currZ = MathHelper.floor(start.z) - (facing == EnumFacing.SOUTH ? 1 : 0);

                    blockPos = new BlockPos(currX, currY, currZ);
                    blockState = mc.world.getBlockState(blockPos);
                    block = blockState.getBlock();

                    if (block.canCollideCheck(blockState, false) && (getBlocks().contains(block) || !shouldIgnore)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static List<Block> getBlocks() {
        return Arrays.asList(
                Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.COMMAND_BLOCK, Blocks.BARRIER, Blocks.ENCHANTING_TABLE, Blocks.ENDER_CHEST, Blocks.END_PORTAL_FRAME, Blocks.BEACON, Blocks.ANVIL
        );
    }

    public static float getExplosionDamage(Entity targetEntity, Vec3d explosionPosition, float explosionPower, boolean shouldIgnore) {
        Vec3d entityPosition = new Vec3d(targetEntity.posX, targetEntity.posY, targetEntity.posZ);
        if (targetEntity.isImmuneToExplosions()) return 0.0f;
        explosionPower *= 2.0f;
        double distanceToSize = entityPosition.distanceTo(explosionPosition) / explosionPower;
        double blockDensity = 0.0;
        // Offset to "fake position"
        AxisAlignedBB bbox = targetEntity.getEntityBoundingBox().offset(targetEntity.getPositionVector().subtract(entityPosition));
        Vec3d bboxDelta = new Vec3d(
                1.0 / ((bbox.maxX - bbox.minX) * 2.0 + 1.0),
                1.0 / ((bbox.maxY - bbox.minY) * 2.0 + 1.0),
                1.0 / ((bbox.maxZ - bbox.minZ) * 2.0 + 1.0)
        );

        double xOff = (1.0 - Math.floor(1.0 / bboxDelta.x) * bboxDelta.x) / 2.0;
        double zOff = (1.0 - Math.floor(1.0 / bboxDelta.z) * bboxDelta.z) / 2.0;

        if (bboxDelta.x >= 0.0 && bboxDelta.y >= 0.0 && bboxDelta.z >= 0.0) {
            int nonSolid = 0;
            int total = 0;

            for (double x = 0.0; x <= 1.0; x += bboxDelta.x) {
                for (double y = 0.0; y <= 1.0; y += bboxDelta.y) {
                    for (double z = 0.0; z <= 1.0; z += bboxDelta.z) {
                        Vec3d startPos = new Vec3d(
                                xOff + bbox.minX + (bbox.maxX - bbox.minX) * x,
                                bbox.minY + (bbox.maxY - bbox.minY) * y,
                                zOff + bbox.minZ + (bbox.maxZ - bbox.minZ) * z
                        );

                        if (!rayTraceSolidCheck(startPos, explosionPosition, shouldIgnore)) ++nonSolid;
                        ++total;
                    }
                }
            }
            blockDensity = (double) nonSolid / (double) total;
        }

        double densityAdjust = (1.0 - distanceToSize) * blockDensity;
        float damage = (float) (int) ((densityAdjust * densityAdjust + densityAdjust) / 2.0 * 7.0 * explosionPower + 1.0);

        if (targetEntity instanceof EntityLivingBase)
            damage = getBlastReduction((EntityLivingBase) targetEntity, getDamageFromDifficulty(damage),
                    new Explosion(mc.world, null, explosionPosition.x, explosionPosition.y, explosionPosition.z,
                            explosionPower / 2.0f, false, true));

        return damage;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(),
                (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        float enchantmentModifierDamage = 0.0f;
        try {
            enchantmentModifierDamage = (float) EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(),
                    DamageSource.causeExplosionDamage(explosion));
        } catch (Exception ignored) {
        }
        enchantmentModifierDamage = MathHelper.clamp(enchantmentModifierDamage, 0.0f, 20.0f);

        damage *= 1.0f - enchantmentModifierDamage / 25.0f;
        PotionEffect resistanceEffect = entity.getActivePotionEffect(MobEffects.RESISTANCE);

        if (entity.isPotionActive(MobEffects.RESISTANCE) && resistanceEffect != null)
            damage = damage * (25.0f - (resistanceEffect.getAmplifier() + 1) * 5.0f) / 25.0f;

        damage = Math.max(damage, 0.0f);
        return damage;
    }

    public static boolean canSeePos(BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), false, true, false) == null;
    }

    public static BlockPos getBestPos(Entity target, float placeRange, boolean specialEntityCheck, boolean fifteen, boolean ignoreTerrain, boolean seeCheck, double minDamage, double maxLocal) {
        double bestDmg = 0;
        BlockPos finalPlacement = null;
        for(BlockPos pos : possiblePlacePositions(placeRange, specialEntityCheck, fifteen)) {
            double posDmg = calc(target, pos, ignoreTerrain);
            double localDamage = calc(mc.player, pos, ignoreTerrain);

            if(posDmg > bestDmg && canPlaceCrystal(pos, specialEntityCheck, fifteen && posDmg > minDamage && localDamage <= maxLocal)) {
                if(seeCheck)
                    if(!canSeePos(finalPlacement))
                        continue;
                bestDmg = posDmg;
                finalPlacement = pos;
            }
        }

        return finalPlacement;
    }

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing) {
        if(!(mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal)) {
            if(!(mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal))
                return;
        }

        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double) pos.getX() + 0.5, (double) pos.getY() - 0.5, (double) pos.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if(swing) {
            mc.player.connection.sendPacket(new CPacketAnimation(hand));
        }
    }

    public static EntityPlayer getNearestTarget(float range) {
        EntityPlayer potentialTarget = null;
        double closest = MathUtils.square(range);
        for(EntityPlayer entityPlayer : mc.world.playerEntities) {
            if(mc.player.getDistanceSq(entityPlayer) <= MathUtils.square(range)) {
                if(mc.player.getDistanceSq(entityPlayer) < closest) {
                    potentialTarget = entityPlayer;
                    closest = mc.player.getDistanceSq(entityPlayer);
                }
            }
        }
        
        return potentialTarget;
    }

    public static void swingArm(String mode) {
        if (mode.equalsIgnoreCase("Both") && mc.player.getHeldItemOffhand() != null) { // Both
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.OFF_HAND);
        } else if (mode.equalsIgnoreCase("Offhand") && mc.player.getHeldItemOffhand() != null) { // Only Offhand
            mc.player.swingArm(EnumHand.OFF_HAND);
        } else if (mode.equalsIgnoreCase("Mainhand")) { // Only Main Hand
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}