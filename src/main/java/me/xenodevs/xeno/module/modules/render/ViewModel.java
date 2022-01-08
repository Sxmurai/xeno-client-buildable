package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.event.impl.TransformFirstPersonEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public final class ViewModel extends Module {
    public static final BooleanSetting cancelEating = new BooleanSetting("Cancel Eating", true);

    public static final BooleanSetting leftPosition = new BooleanSetting("Left Position", true);
    public static final NumberSetting leftX = new NumberSetting("Left X", -2.0, 0.0, 2.0, 0.01);
    public static final NumberSetting leftY = new NumberSetting("Left Y", -2.0, 0.2, 2.0, 0.01);
    public static final NumberSetting leftZ = new NumberSetting("Left Z", -2.0, -1.2, 2.0, 0.01);

    public static final BooleanSetting rightPosition = new BooleanSetting("Right Position", true);
    public static final NumberSetting rightX = new NumberSetting("Right X", -2.0, 0.0, 2.0, 0.01);
    public static final NumberSetting rightY = new NumberSetting("Right Y", -2.0, 0.2, 2.0, 0.01);
    public static final NumberSetting rightZ = new NumberSetting("Right Z", -2.0, -1.2, 2.0, 0.01);

    public static final BooleanSetting leftRotation = new BooleanSetting("Left Rotation", true);
    public static final NumberSetting leftYaw = new NumberSetting("L Yaw", -100, 0, 100, 1);
    public static final NumberSetting leftPitch = new NumberSetting("L Pitch", -100, 0, 100, 1);
    public static final NumberSetting leftRoll = new NumberSetting("L Roll", -100, 0, 100, 1);

    public static final BooleanSetting rightRotation = new BooleanSetting("Right Rotation", true);
    public static final NumberSetting rightYaw = new NumberSetting("R Yaw", -100, 0, 100, 1);
    public static final NumberSetting rightPitch = new NumberSetting("R Pitch", -100, 0, 100, 1);
    public static final NumberSetting rightRoll = new NumberSetting("R Roll", -100, 0, 100, 1);

    public ViewModel() {
        super("ViewModel", "Changes the way you look in first person", Category.RENDER);
        this.addSetting(cancelEating);
        this.addSettings(leftPosition, leftX, leftY, leftZ);
        this.addSettings(rightPosition, rightX, rightY, rightZ);
        this.addSettings(leftRotation, leftYaw, leftPitch, leftRoll);
        this.addSettings(rightRotation, rightYaw, rightPitch, rightRoll);
    }

    @EventHandler
    private final Listener<TransformFirstPersonEvent.Pre> transformFirstPersonEventPreListener = new Listener<>(event -> {
        if (nullCheck()) return;

        if (leftPosition.getValue() && event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(leftX.getDoubleValue(), leftY.getDoubleValue(), leftZ.getDoubleValue());
        }

        if (rightPosition.getValue() && event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightX.getDoubleValue(), rightY.getDoubleValue(), rightZ.getDoubleValue());
        }
    });

    @EventHandler
    private final Listener<TransformFirstPersonEvent.Post> transformFirstPersonEventPostListener = new Listener<>(event -> {
        if (nullCheck()) return;

        if (leftRotation.getValue() && event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.rotate(leftYaw.getFloatValue(),0,1,0);
            GlStateManager.rotate(leftPitch.getFloatValue(),1,0,0);
            GlStateManager.rotate(leftRoll.getFloatValue(),0,0,1);
        }

        if (rightRotation.getValue() && event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.rotate(rightYaw.getFloatValue(),0,1,0);
            GlStateManager.rotate(rightPitch.getFloatValue(),1,0,0);
            GlStateManager.rotate(rightRoll.getFloatValue(),0,0,1);
        }
    });
}
