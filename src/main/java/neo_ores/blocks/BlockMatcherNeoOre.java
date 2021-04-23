package neo_ores.blocks;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;

public class BlockMatcherNeoOre implements Predicate<IBlockState>
{
	private final IBlockState state;

    private BlockMatcherNeoOre(IBlockState state)
    {
        this.state = state;
    }

    public static BlockMatcherNeoOre forBlock(IBlockState state)
    {
        return new BlockMatcherNeoOre(state);
    }	
	
	@Override
	public boolean apply(@Nullable IBlockState input) 
	{
		return input != null && input.getBlock() == this.state.getBlock() && input.getValue(BlockNeoOre.DIM) != null && input.getValue(BlockNeoOre.DIM) == state.getValue(BlockNeoOre.DIM);
	}
}
