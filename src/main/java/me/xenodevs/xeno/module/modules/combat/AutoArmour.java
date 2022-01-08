package me.xenodevs.xeno.module.modules.combat;

import java.util.*;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.other.EnchantmentUtil;
import me.xenodevs.xeno.utils.other.ItemUtil;
import me.xenodevs.xeno.utils.other.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class AutoArmour extends Module {

	NumberSetting delay = new NumberSetting("Delay", 300, 0, 1000, 50);
	ModeSetting prefer = new ModeSetting("Prefer", "Chestplate", "Elytra");
	Timer timerr = new Timer();
	
	public AutoArmour() {
		super("AutoArmour", "Automatically equips your best armour.", Category.COMBAT);
		this.addSettings(delay);
	}

	public void onEnable() {}

	public void onUpdate() {
		if(nullCheck())
			return;

	    if(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer) {
			if (!(mc.currentScreen instanceof net.minecraft.client.renderer.InventoryEffectRenderer)) {
				return;
			}
		}

	    if(timerr.hasTimeElapsed((long) delay.getDoubleValue(), true)) {
		    EntityPlayerSP player = mc.player;
		    InventoryPlayer inventory = player.inventory;

		    if (player.moveForward != 0.0F ||
		    	player.movementInput.moveStrafe != 0.0F) {
		    	return;
		    }

		    int[] bestArmorSlots = new int[4];
		    int[] bestArmorValues = new int[4];


		    for(int type = 0; type < 4; type++) {
		    	bestArmorSlots[type] = -1;

		    	ItemStack stack = inventory.armorItemInSlot(type);
		    	ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

		    	if(!ItemUtil.isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
		    		ItemArmor item = (ItemArmor)stack.getItem();
		    		bestArmorValues[type] = getArmorValue(item, stack);
				}
		    }

		    for (int slot = 0; slot < 36; slot++) {
				ItemStack stack = inventory.getStackInSlot(slot);

				if (!ItemUtil.isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
					ItemArmor item = (ItemArmor)stack.getItem();
					int armorType = ItemUtil.getArmorType(item);
					int armorValue = getArmorValue(item, stack);

					if (armorValue > bestArmorValues[armorType]) {
						bestArmorSlots[armorType] = slot;
						bestArmorValues[armorType] = armorValue;
					}
				}
		    }

		    ArrayList<Integer> types = new ArrayList<>(Arrays.asList(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) }));
		    Collections.shuffle(types);
		    for (Iterator<Integer> iterator = types.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();

		    int j = bestArmorSlots[i];
		    if (j == -1) {
				continue;
		    }

		    ItemStack oldArmor = inventory.armorItemInSlot(i);
		    if (!ItemUtil.isNullOrEmpty(oldArmor) &&
				inventory.getFirstEmptyStack() == -1) {
				continue;
		    }

		    if (j < 9) {
				j += 36;
		    }

		    if (!ItemUtil.isNullOrEmpty(oldArmor))
				mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, mc.player);
				mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, mc.player);
			    break;
		    }
		}
	}

	private int getArmorValue(ItemArmor item, ItemStack stack) {
		int armorPoints = item.damageReduceAmount;
		int prtPoints = 0;
		int armorToughness = (int)ItemUtil.getArmorToughness(item);
		int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);

		Enchantment protection = Enchantments.PROTECTION;
		int prtLvl = EnchantmentUtil.getEnchantmentLevel(protection, stack);

		EntityPlayerSP player = mc.player;
		DamageSource dmgSource = DamageSource.causePlayerDamage((EntityPlayer)player);
		prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);

		return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
	}
}
