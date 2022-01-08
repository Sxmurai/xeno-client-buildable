package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.settings.KeybindSetting;
import org.lwjgl.input.Keyboard;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.TravelEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.MotionUtil;
import me.xenodevs.xeno.utils.player.PlayerUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ElytraFly extends Module {

    public static ElytraFly INSTANCE;

    public static ModeSetting mode = new ModeSetting("Mode", "Control", "Packet");
    public static NumberSetting speed = new NumberSetting("Speed", 2.5, 0.0, 5.0, 0.1);
    public static NumberSetting up = new NumberSetting("Up", 1.0, 0.0, 5.0, 0.1);
    public static NumberSetting down = new NumberSetting("Down", 1.0, 0.0, 5.0, 0.1);
    public static NumberSetting fall = new NumberSetting("Fall", 0.0, 0.0, 0.1, 0.1);
    public static BooleanSetting lockRotation = new BooleanSetting("LockRotation", false);
    public static BooleanSetting pauseLiquid = new BooleanSetting("StopInLiquid", true);
    public static BooleanSetting pauseCollision = new BooleanSetting("StopWhenColliding", false);
    KeybindSetting activateKey = new KeybindSetting("Activate Key", Keyboard.KEY_NONE);

    public ElytraFly() {
        super("ElytraFly", "ez fly with elytra", 0, Category.MOVEMENT);
        this.addSettings(mode, speed, up, down, fall, lockRotation, pauseLiquid, pauseCollision, activateKey);
        INSTANCE = this;
    }

    public static int flightKey;

    Timer timer = new Timer();

    @EventHandler
    private final Listener<TravelEvent> travelListener = new Listener<>(event -> {
        if(!nullCheck() && mc.player.isElytraFlying()) {
            if(PlayerUtil.isInLiquid() && pauseLiquid.getValue())
                return;

            else if (PlayerUtil.isCollided() && pauseCollision.getValue())
                return;

            elytraFlight(event);
        }
    });

    @Override
    public void onUpdate() {
        handleKey();
    }

    public void handleKey() {
        boolean var = false;

        if(Keyboard.getEventKeyState()) {
            if(Keyboard.isKeyDown(activateKey.code) && !var) {
                mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                var = true;
            }
        }
    }

    public void elytraFlight(TravelEvent event) {
        event.cancel();
        Xeno.tickManager.setClientTicks(1);

        MotionUtil.stopMotion(-fall.getDoubleValue());
        MotionUtil.setMoveSpeed(speed.getDoubleValue(), 0.6F);

        switch (mode.getMode()) {
            case "Control":
                handleControl();
                break;
            case "Packet":
                break;
        }

        PlayerUtil.lockLimbs();
    }

    public void handleControl() {
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = up.getDoubleValue();
        }

        else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -down.getDoubleValue();
        }
    }

    @Override
    public String getHUDData() {
        return " " + this.mode.getMode();
    }
}