package me.xenodevs.xeno.utils.other;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtils {
	  public static int clamp(int num, int min, int max) {
	    return (num < min) ? min : ((num > max) ? max : num);
	  }

	  public static double roundDouble(double number, int scale) {
			BigDecimal bd = new BigDecimal(number);
			bd = bd.setScale(scale, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}

		public static double roundAvoid(double value, int places) {
			double scale = Math.pow(10, places);
			return Math.round(value * scale) / scale;
		}

		public static int roundUp(double value) {
			double difference = 1 - value;
			return (int) (value + difference);
		}
	  
	  public static float clamp(float num, float min, float max) {
	    return (num < min) ? min : ((num > max) ? max : num);
	  }

	  
	  public static double clamp(double num, double min, double max) {
	    return (num < min) ? min : ((num > max) ? max : num);
	  }

	  
	  public static int floor(float value) {
	    return MathHelper.floor(value);
	  }

	  
	  public static int floor(double value) {
	    return MathHelper.floor(value);
	  }

	  
	  public static int ceil(float value) {
	    return MathHelper.ceil(value);
	  }

	  
	  public static int ceil(double value) {
	    return MathHelper.ceil(value);
	  }

	  
	  public static float sin(float value) {
	    return MathHelper.sin(value);
	  }

	  
	  public static float cos(float value) {
	    return MathHelper.cos(value);
	  }

	public static double roundToHalf(double d) {
		return Math.round(d * 2) / 2.0;
	}

	  
	  public static float wrapDegrees(float value) {
	    return MathHelper.wrapDegrees(value);
	  }

	  
	  public static double wrapDegrees(double value) {
	    return MathHelper.wrapDegrees(value);
	  }
	  
	  public static float[] calcAngle(Vec3d from, Vec3d to) {
	        double difX = to.x - from.x;
	        double difY = (to.y - from.y) * -1.0;
	        double difZ = to.z - from.z;
	        double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
	        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
	    }
	  
	  public static double[] directionSpeed(double speed) {
	        final Minecraft mc = Minecraft.getMinecraft();
	        float forward = mc.player.movementInput.moveForward;
	        float side = mc.player.movementInput.moveStrafe;
	        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
	
	        if (forward != 0) {
	            if (side > 0) {
	                yaw += (forward > 0 ? -45 : 45);
	            } else if (side < 0) {
	                yaw += (forward > 0 ? 45 : -45);
	            }
	            side = 0;
	
	            //forward = clamp(forward, 0, 1);
	            if (forward > 0) {
	                forward = 1;
	            } else if (forward < 0) {
	                forward = -1;
	            }
	        }
	
	        final double sin = Math.sin(Math.toRadians(yaw + 90));
	        final double cos = Math.cos(Math.toRadians(yaw + 90));
	        final double posX = (forward * speed * cos + side * speed * sin);
	        final double posZ = (forward * speed * sin - side * speed * cos);
	        return new double[]{posX, posZ};
	  }
	  
	  public static double square(float input) {
	        return input * input;
	  }
}
