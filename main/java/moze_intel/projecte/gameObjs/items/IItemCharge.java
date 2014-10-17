package moze_intel.projecte.gameObjs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemCharge 
{
	public byte getCharge(ItemStack stack);
	
	public void changeCharge(EntityPlayer player, ItemStack stack);
}
