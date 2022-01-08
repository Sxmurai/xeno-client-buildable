package me.xenodevs.xeno.managers;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.other.MathsHelper;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.common.MinecraftForge;

public class TickManager implements Globals {

    private long prevTime;
    private final float[] TPS = new float[20];
    private int currentTick;

    public TickManager() {
        prevTime = -1;

        for (int i = 0, len = TPS.length; i < len; i++) {
            TPS[i] = 0;
        }

        MinecraftForge.EVENT_BUS.register(this);
        Xeno.EVENT_BUS.subscribe(this);
    }

    public float getTPS(TPS tps) {
        switch (tps) {
            case CURRENT:
                return mc.isSingleplayer() ? 20 : (float) MathUtils.roundDouble(MathsHelper.clamp(TPS[0], 0, 20), 2);
            case AVERAGE:
                int tickCount = 0;
                float tickRate = 0;

                for (float tick : TPS) {
                    if (tick > 0) {
                        tickRate += tick;
                        tickCount++;
                    }
                }

                return mc.isSingleplayer() ? 20 : (float) MathUtils.roundDouble(MathsHelper.clamp((tickRate / tickCount), 0, 20), 2);
        }

        return 0;
    }

    @EventHandler
    private final Listener<PacketEvent.Receive> recieveListener = new Listener<>(event -> {
    	if (event.getPacket() instanceof SPacketTimeUpdate) {
            if (prevTime != -1) {
                TPS[currentTick % TPS.length] = MathsHelper.clamp((20 / ((float) (System.currentTimeMillis() - prevTime) / 1000)), 0, 20);
                currentTick++;
            }

            prevTime = System.currentTimeMillis();
        }
    });

    public void setClientTicks(float ticks) {
        // ((Minecraft) mc)).setTickLength((50 / ticks));
    	mc.timer.tickLength = ((50 / ticks));
    }

    // should tell the server to move forward by a number of ticks
    public void shiftServerTicks(int tickShift) {
        for (int ticks = 0; ticks < tickShift; ticks++) {
            // send vanilla movement & rotation packets
            // RotationUtil.sendRotationPackets(mc.player.rotationYaw, mc.player.rotationPitch);
        }
    }

    public enum TPS {
        CURRENT, AVERAGE, NONE
    }
}
