package me.xenodevs.xeno.utils.other;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemUtil {
	public static boolean isNullOrEmpty(Item item) {
	    return !(item != null);
	}

	public static boolean isNullOrEmpty(ItemStack stack) {
		return !(stack != null && !stack.isEmpty());
	}
	
	  
	public static int getArmorType(ItemArmor armor) {
		return armor.armorType.ordinal() - 2;
	}
	
	  
	public static float getArmorToughness(ItemArmor armor) {
		return armor.toughness;
	}
}
