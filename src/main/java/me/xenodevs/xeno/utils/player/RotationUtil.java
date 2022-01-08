package me.xenodevs.xeno.utils.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.wolfsurge.api.util.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class RotationUtil implements Globals {
    public static void lockYaw(double rotation) {
        if (mc.player.rotationYaw >= rotation) {
            mc.player.rotationYaw = 0.0f;
        }

        if (mc.player.rotationYaw <= rotation) {
            mc.player.rotationYaw = 0.0f;
        }
    }

    public static void lockPitch(double rotation) {
        if (mc.player.rotationPitch >= rotation) {
            mc.player.rotationPitch = 0.0f;
        }

        if (mc.player.rotationPitch <= rotation) {
            mc.player.rotationPitch = 0.0f;
        }
    }

    public static String getFacing() {
        switch (MathHelper.floor(mc.player.rotationYaw + 8.0f / 360.0f + 0.5) & 7) {
            case 0:
            case 1:
                return " [" + ChatFormatting.WHITE + "+Z" + ChatFormatting.RESET + "]";
            case 2:
            case 3:
                return " [" + ChatFormatting.WHITE + "-X" + ChatFormatting.RESET + "]";
            case 4:
            case 5:
                return " [" + ChatFormatting.WHITE + "-Z" + ChatFormatting.RESET + "]";
            case 6:
            case 7:
                return " [" + ChatFormatting.WHITE + "+X" + ChatFormatting.RESET + "]";
        }

        return "Invalid Direction";
    }

    public static boolean isInFov(BlockPos pos) {
        return pos != null && (RotationUtil.mc.player.getDistanceSq(pos) < 4.0 || RotationUtil.yawDist(pos) < (double)(RotationUtil.getHalvedfov() + 2.0f));
    }

    public static boolean isInFov(Entity entity) {
        return entity != null && (RotationUtil.mc.player.getDistanceSq(entity) < 4.0 || RotationUtil.yawDist(entity) < (double)(RotationUtil.getHalvedfov() + 2.0f));
    }

    public static double yawDist(BlockPos pos) {
        if (pos != null) {
            Vec3d difference = new Vec3d((Vec3i)pos).subtract(RotationUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()));
            double d = Math.abs((double)RotationUtil.mc.player.rotationYaw - (Math.toDegrees(Math.atan2(difference.z, difference.x)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static float getFov() {
        return mc.gameSettings.fovSetting;
    }

    public static float getHalvedfov() {
        return RotationUtil.getFov() / 2.0f;
    }

    public static double yawDist(Entity e) {
        if (e != null) {
            Vec3d difference = e.getPositionVector().add(0.0, (double)(e.getEyeHeight() / 2.0f), 0.0).subtract(RotationUtil.mc.player.getPositionEyes(mc.getRenderPartialTicks()));
            double d = Math.abs((double)RotationUtil.mc.player.rotationYaw - (Math.toDegrees(Math.atan2(difference.z, difference.x)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static float transformYaw() {
        float yaw = RotationUtil.mc.player.rotationYaw % 360.0f;
        if (RotationUtil.mc.player.rotationYaw > 0.0f) {
            if (yaw > 180.0f) {
                yaw = -180.0f + (yaw - 180.0f);
            }
        } else if (yaw < -180.0f) {
            yaw = 180.0f + (yaw + 180.0f);
        }
        if (yaw < 0.0f) {
            return 180.0f + yaw;
        }
        return -180.0f + yaw;
    }

    private static float yaw;
    private static float pitch;

    public static void updatePlayerRotations() {
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    public static void setPlayerRotations(float yaw, float pitch, boolean packet) {
        if (packet) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));

            mc.player.renderYawOffset = yaw;
            mc.player.rotationYawHead = yaw;
        } else {
            mc.player.rotationYaw = yaw;
            mc.player.rotationPitch = pitch;
        }
    }

    public static void restorePlayerRotations() {
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationPitch = pitch;
    }

    public static void rotateToBlockPos(BlockPos blockPos, boolean packet) {
        float[] angle = calculateRotationAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) blockPos.getX() + 0.5f, (float) blockPos.getY() - 0.5f, (float) blockPos.getZ() + 0.5f));
        setPlayerRotations(angle[0], angle[1], packet);
    }

    public static void rotateToEntity(Entity entity, boolean packet) {
        float[] angle = calculateRotationAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
        setPlayerRotations(angle[0], angle[1], packet);
    }

    public static float[] calculateRotationAngle(Vec3d vec3d, Vec3d vec3d1) {
        double xDifference = vec3d1.x - vec3d.x;
        double yDifference = (vec3d1.y - vec3d.y) * -1.0;
        double zDifference = vec3d1.z - vec3d.z;
        double distance = MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference);

        return new float[] {(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(zDifference, xDifference)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(yDifference, distance)))};
    }
}
