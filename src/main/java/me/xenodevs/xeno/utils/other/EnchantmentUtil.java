package me.xenodevs.xeno.utils.other;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;

public class EnchantmentUtil {
	public static final Enchantment PROTECTION = Enchantments.PROTECTION;
	public static final Enchantment EFFICIENCY = Enchantments.EFFICIENCY;
	public static final Enchantment SILK_TOUCH = Enchantments.SILK_TOUCH;
	  
	public static final Enchantment LUCK_OF_THE_SEA = Enchantments.LUCK_OF_THE_SEA;
	public static final Enchantment LURE = Enchantments.LURE;
	public static final Enchantment UNBREAKING = Enchantments.UNBREAKING;
	public static final Enchantment MENDING = Enchantments.MENDING;


	  
	public static int getEnchantmentLevel(Enchantment enchantment, ItemStack stack) {
		if (enchantment == null) {
			return 0;
		}
    	return EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
	}

  
	public static boolean hasVanishingCurse(ItemStack stack) {
		return EnchantmentHelper.hasVanishingCurse(stack);
	}
}
