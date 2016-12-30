package moze_intel.projecte.gameObjs.customRecipes;

import moze_intel.projecte.gameObjs.ObjHandler;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;

public class RecipesCovalenceRepair implements IRecipe
{
	private ItemStack output = ItemStack.EMPTY;

	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world)
	{
		ItemStack[] dust = new ItemStack[8];
		ItemStack tool = ItemStack.EMPTY;
		boolean foundItem = false;
		int dustCounter = 0;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack input = inv.getStackInSlot(i);
			
			if (input.isEmpty())
			{
				continue;
			}
			
			if (isItemRepairable(input))
			{
				if (!foundItem)
				{
					tool = input;
					foundItem = true;
				}
				else
				{
					return false;
				}
			}
			else if (input.getItem() == ObjHandler.covalence)
			{
				if (dustCounter < 8)
				{
					dust[dustCounter] = input;
					dustCounter++;
				}
				else
				{
					return false;
				}
			}
		}
		
		if (tool.isEmpty() || !foundItem || dustCounter == 0)
		{
			return false;
		}

		if (!correctDustCount(dustCounter, tool.getItem()))
		{
			return false;
		}

		int dustDamage = getDustType(tool);

		for (ItemStack stack : dust) {
			if (!stack.isEmpty() && stack.getItemDamage() < dustDamage) {
				return false;
			}
		}
		
		output = tool.copy();
		output.setItemDamage(0);
		return true;
	}

	private boolean correctDustCount(int dustCounter, Item toRepair)
	{
		if (toRepair instanceof ItemSpade || toRepair instanceof ItemShears
				|| toRepair instanceof ItemFlintAndSteel || toRepair instanceof ItemFishingRod)
		{
			return dustCounter == 1;
		}

		if (toRepair instanceof ItemSword)
		{
			return dustCounter == 2;
		}

		if (toRepair instanceof ItemAxe || toRepair instanceof ItemPickaxe || toRepair instanceof ItemBow)
		{
			return dustCounter == 3;
		}

		if (toRepair instanceof ItemArmor)
		{
			ItemArmor armor = ((ItemArmor) toRepair);
			switch(armor.armorType)
			{
				case HEAD: return dustCounter == 5;
				case CHEST: return dustCounter == 8;
				case LEGS: return dustCounter == 7;
				case FEET: return dustCounter == 4;
				default: return false;
			}
		}

		return dustCounter == 3;

	}

	private boolean isItemRepairable(ItemStack stack)
	{
		if (stack.getHasSubtypes())
		{
			return false;
		}

		if (stack.getMaxDamage() == 0 || stack.getItemDamage() == 0)
		{
			return false;
		}
		
		Item item = stack.getItem();

		if (item instanceof ItemShears || item instanceof ItemFlintAndSteel || item instanceof ItemFishingRod || item instanceof ItemBow)
		{
			return true;
		}

		return (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemHoe || item instanceof ItemArmor);
	}
	
	private int getDustType(ItemStack stack)
	{
		Item item = stack.getItem();
		
		if (item instanceof ItemShears || item instanceof ItemFlintAndSteel)
		{
			return 1;
		}

		if (item instanceof ItemBow || item instanceof ItemFishingRod)
		{
			return 0;
		}
		
		String name = "";
		
		if (item instanceof ItemTool)
		{
			name = ((ItemTool) item).getToolMaterialName();
		}
		else if (item instanceof ItemSword)
		{
			name = ((ItemSword) item).getToolMaterialName();
		}
		else if (item instanceof ItemHoe)
		{
			name = ((ItemHoe) item).getMaterialName();
		}
		else if (item instanceof ItemArmor)
		{
			name = ((ItemArmor) item).getArmorMaterial().toString();
		}
		
		if (name.equals("WOOD") || name.equals("STONE") || name.equals("CLOTH"))
		{
			return 0;
		}

		if (name.equals("IRON") || name.equals("GOLD") || name.equals("CHAIN"))
		{
			return 1;
		}

		return 2;
	}
	
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1)
	{
		return output.copy();
	}

	@Override
	public int getRecipeSize() 
	{
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return output;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv)
	{
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
