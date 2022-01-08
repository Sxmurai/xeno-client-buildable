package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Offhand extends Module {
	
	public ModeSetting mode = new ModeSetting("Mode", "Totem", "Gapple", "Crystal");
	public BooleanSetting fallback = new BooleanSetting("Fallback", true);
	public ModeSetting fallbackMode = new ModeSetting("Fallback", "Totem", "Gapple", "Crystal");
	public BooleanSetting healthSwap = new BooleanSetting("TotemSwap", true);
	public NumberSetting health = new NumberSetting("TotemHealth", 16, 0, 36, 1);
	public NumberSetting cooldown = new NumberSetting("Cooldown", 0, 0, 40, 1);
	private int timer = 0;

	public Offhand() {
		super("Offhand", "moves stuff to ur offhand", 0, Category.COMBAT);
		this.addSettings(mode, healthSwap, health, cooldown, fallback, fallbackMode);
	}

	public void onUpdate() {
		if(nullCheck())
			return;

		timer = timer + 1;

		float hp = EntityUtil.getHealth(mc.player);
		if (hp <= health.getFloatValue()) {
			this.swapItems(getItemSlot(Items.TOTEM_OF_UNDYING));
			return;
		}

		doOffhand();
	}

	public void doOffhand() {
		boolean doFallback = fallback.isEnabled();
		if(InventoryUtil.hasItem(getItem(mode.getMode()))) {
			if(!mode.is("Gapple"))
				this.swapItems(getItemSlot(getItem(mode.getMode())));
			else {
				// this.swapItems(getItemSlot(getItem(mode.getMode())));
				EntityPlayerSP player = mc.player;
				NonNullList<ItemStack> inv;
				ItemStack offhand = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
				int inventoryIndex = 0;
				inv = player.inventory.mainInventory;
				if ((offhand == null) || (offhand.getItem() != Items.GOLDEN_APPLE)) {
					for (inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {
						if (inv.get(inventoryIndex) != ItemStack.EMPTY) {
							if (inv.get(inventoryIndex).getItem() == Items.GOLDEN_APPLE) {
								// this.replaceOffhand(inventoryIndex);
								if(mc.player.openContainer instanceof ContainerPlayer) {
									mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
									mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
									mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
								}
								break;
							}
						}
					}
				}
			}
			doFallback = false;
			return;
		}

		// if(doFallback)
			// this.swapItems(getItemSlot(getItem(fallbackMode.getMode())));

		if(doFallback) {
			if(!InventoryUtil.hasItem(getItem(mode.getMode())) && mc.player.getHeldItemOffhand().getItem() != getItem(mode.getMode()))
				this.swapItems(getItemSlot(getItem(fallbackMode.getMode())));
		}
	}

	public Item getItem(String item) {
		if(item.equalsIgnoreCase("Totem")) {
			return Items.TOTEM_OF_UNDYING;
		} else if(item.equalsIgnoreCase("Gapple")) {
			return Items.GOLDEN_APPLE;
		} else if(item.equalsIgnoreCase("Crystal")) {
			return Items.END_CRYSTAL;
		} else {
			return Items.TOTEM_OF_UNDYING; // If the user somehow breaks it
		}
	}

	public void swapItems(int slot) {
		if (slot == -1 || (timer <= cooldown.getDoubleValue()) && mc.player.inventory.getStackInSlot(slot).getItem() != getItem(mode.getMode())) return;
		timer = 0;
		mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
		mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
		mc.playerController.updateController();
	}

	private int getItemSlot(Item input) {
		if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
		for (int i = 36; i >= 0; i--) {
			final Item item = mc.player.inventory.getStackInSlot(i).getItem();
			if (item == input) {
				if (i < 9) {
					if (input == Items.GOLDEN_APPLE) {
						return -1;
					}
					i += 36;
				}
				return i;
			}
		}
		return -1;
	}

    @Override
    public String getHUDData() {
    	return " " + this.mode.getMode();
    }
}
