package me.xenodevs.xeno.utils.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
	
	  private static final EntityPlayerSP player = Minecraft.getMinecraft().player;
	  private static final Minecraft mc = Minecraft.getMinecraft();
	  
	  public static boolean getHeldItem(Item item) {
	        return mc.player.getHeldItemMainhand().getItem().equals(item) || mc.player.getHeldItemOffhand().getItem().equals(item);
	    }

	    public static void switchToSlotGhost(int slot) {
	        if (slot != -1 && mc.player.inventory.currentItem != slot)
	            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
	    }

	    public static void switchToSlotGhost(Block block) {
	        if (getBlockInHotbar(block) != -1 && mc.player.inventory.currentItem != getBlockInHotbar(block))
	            mc.player.connection.sendPacket(new CPacketHeldItemChange(getBlockInHotbar(block)));
	    }

	    public static void switchToSlotGhost(Item item) {
	        if (getHotbarItemSlot(item) != -1 && mc.player.inventory.currentItem != getHotbarItemSlot(item))
	            switchToSlotGhost(getHotbarItemSlot(item));
	    }

	    public static void moveItemToOffhand(int slot) {
	        int returnSlot = -1;

	        if (slot == -1)
	            return;

	        mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
	        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

	        for (int i = 0; i < 45; i++) {
	            if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
	                returnSlot = i;
	                break;
	            }
	        }

	        if (returnSlot != -1)
	            mc.playerController.windowClick(0, returnSlot < 9 ? returnSlot + 36 : returnSlot, 0, ClickType.PICKUP, mc.player);
	    }

	    public static int getInventoryItemSlot(Item item, boolean hotbar) {
	        for (int i = hotbar ? 9 : 0; i < 45; i++) {
	            if (mc.player.inventory.getStackInSlot(i).getItem() == item)
	                return i;
	        }

	        return -1;
	    }

	    public static int getAnyBlockInHotbar() {
	        for (int i = 0; i < 9; i++) {
	            Item item = mc.player.inventory.getStackInSlot(i).getItem();
	            if (item instanceof ItemBlock)
	                return i;
	        }

	        return -1;
	    }

	    public static int getItemCount(Item item) {
	        return mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
	    }

	    public static int getItemCount(EntityPlayer entityPlayer, Item item) {
	        return entityPlayer.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
	    }

	    public static boolean Is32k(ItemStack stack) {
	        stack.getEnchantmentTagList();
	        for (int i = 0; i < stack.getEnchantmentTagList().tagCount(); i++) {
	            stack.getEnchantmentTagList().getCompoundTagAt(i);
	            if (Enchantment.getEnchantmentByID(stack.getEnchantmentTagList().getCompoundTagAt(i).getByte("id")) != null) {
	                if (Enchantment.getEnchantmentByID(stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("id")) != null) {
	                    if (Enchantment.getEnchantmentByID(stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("id")).isCurse())
	                        continue;

	                    if (stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("lvl") >= 1000)
	                        return true;
	                }
	            }
	        }

	        return false;
	    }
	  
	  public static boolean isHolding(Item item) {
	        return mc.player.getHeldItemMainhand().getItem().equals(item) || mc.player.getHeldItemOffhand().getItem().equals(item);
	  }
	  
	  public static boolean hasItem(Item input) {
	        for (int i = 0; i < 36; i++) {
	            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
	            if (item == input) {
	                return true;
	            }
	        }

	        return false;
	  }
	  
	  public static boolean isInventoryFull() {
	        for (int i = 0; i < 36; i++) {
	            if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
	                return false;
	            }
	        }
	        return true;
	  }
	  
	  public static int getBestHotbarSword() {
	    float best = -1.0F;
	    int index = -1;
	    
	    for (int i = 0; i < 9; i++) {
	      
	      ItemStack itemStack = player.inventory.getStackInSlot(i);
	      if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
	        
	        ItemSword sword = (ItemSword)itemStack.getItem();
	        
	        float damage = 
	          sword.getAttackDamage() + EnchantmentHelper.getEnchantmentLevel(
	            Enchantments.SHARPNESS, itemStack) * 1.25F;
	        
	        if (damage > best) {
	          
	          best = damage;
	          index = i;
	        } 
	      } 
	    } 
	    
	    return index;
	  }
	  
	  public static int getBestArmour() {
		    float best = -1.0F;
		    int index = -1;
		    
		    for (int i = 0; i < 9; i++) {
		      
		      ItemStack itemStack = player.inventory.getStackInSlot(i);
		      if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
		        
		        ItemArmor armour = (ItemArmor)itemStack.getItem();
		        
		        float damage = 
		          armour.toughness + EnchantmentHelper.getEnchantmentLevel(
		            Enchantments.SHARPNESS, itemStack) * 1.25F;
		        
		        if (damage > best) {
		          
		          best = damage;
		          index = i;
		        } 
		      } 
		    } 
		    
		    return index;
	  }
	  
	public static void switchToSlot(int slot) {
        if (slot != -1 && mc.player.inventory.currentItem != slot)
            mc.player.inventory.currentItem = slot;
    }

    public static void switchToSlot(Block block) {
        if (getBlockInHotbar(block) != -1 && mc.player.inventory.currentItem != getBlockInHotbar(block))
            mc.player.inventory.currentItem = getBlockInHotbar(block);
    }

    public static void switchToSlot(Item item) {
        if (getHotbarItemSlot(item) != -1 && mc.player.inventory.currentItem != getHotbarItemSlot(item))
            mc.player.inventory.currentItem = getHotbarItemSlot(item);
    }

    public static int getBlockInHotbar(Block block) {
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block))
                return i;
        }

        return -1;
    }
    
    public static int getHotbarItemSlot(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                return i;
        }

        return -1;
    }

	public static int getHotbarItemSlot(Class<? extends Item> item) {
		int slot = -1;

		for (int i = 0; i < 9; i++) {
			if (mc.player.inventory.getStackInSlot(i).getItem().getClass().isAssignableFrom(item)) {
				slot = i;

				break;
			}
		}

		return slot;
	}

	public static void switchToSlot(Class<? extends Item> clazz) {
		if (mc.player.getHeldItemMainhand().getItem().getClass().isAssignableFrom(clazz)) return;

		int slot = getHotbarItemSlot(clazz);

		if (slot == -1) return;

		mc.player.inventory.currentItem = slot;
	}

	public static void switchToSlotGhost(Class<? extends Item> clazz) {
		if (mc.player.getHeldItemMainhand().getItem().getClass().isAssignableFrom(clazz)) return;

		int slot = getHotbarItemSlot(clazz);

		if (slot == -1) return;

		if (mc.player.inventory.currentItem != slot)
			mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
	}
}

