package neo_ores.item;

import java.util.HashSet;

import neo_ores.world.dimension.DimensionHelper.ToolType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPaxel extends ItemTool implements IItemNeoTool, INeoOresItem
{
	private boolean shielding;
	private boolean shielded;
	private ToolType name;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ItemPaxel(ToolMaterial material)
    {
        super(5.0F, material.getAttackDamage() - 3.0F, material, new HashSet());
        this.setMaxDamage(material.getMaxUses() * 5);
    }
	
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
	    return this.canHarvestBlock(state) ? this.efficiency : super.getDestroySpeed(stack, state);
	}
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(1, attacker);
        return true;
    }
	
	public boolean canHarvestBlock(IBlockState blockIn)
    {
        return blockIn.getBlock().getHarvestLevel(blockIn) <= this.toolMaterial.getHarvestLevel();
    }
	
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	this.setShielding(true);
    	this.setShielded(true);
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }
	
	@SuppressWarnings("incomplete-switch")
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()))
            {
                if (block == Blocks.GRASS_PATH)
                {
                    this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                    return EnumActionResult.SUCCESS;
                }
                else if(block == Blocks.GRASS)
                {
                	this.setBlock(itemstack, player, worldIn, pos, Blocks.GRASS_PATH.getDefaultState());
                	return EnumActionResult.SUCCESS;
                }

                if (block == Blocks.DIRT)
                {
                    switch ((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
                    {
                        case DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                            return EnumActionResult.SUCCESS;
                        case COARSE_DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                            return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
    }
	
	protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!worldIn.isRemote)
        {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }
	
	public boolean canApplyAtEnchantingTable(ItemStack stack,Enchantment enchantment)
	{
		return enchantment.type == EnumEnchantmentType.WEAPON ? true : super.canApplyAtEnchantingTable(stack, enchantment);
	}
	
	public void setShielding(boolean bool)
	{
		this.shielding = bool;
	}
	
	public boolean isShielding()
	{
		return this.shielding;
	}
	
	public void setShielded(boolean bool)
	{
		this.shielded = bool;
	}
	
	public boolean wasShielding()
	{
		return this.shielded;
	}
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		this.setShielding(false);
		this.setShielded(false);
	}

	@Override
	public Item setToolType(ToolType name) 
	{
		this.name = name;
		return this;
	}

	@Override
	public ToolType getToolType() 
	{
		return this.name;
	}
}
