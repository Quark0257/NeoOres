package neo_ores.command;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import neo_ores.mana.PlayerManaDataServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandNeoOres extends CommandBase
{
	private final List<String> length2 = Lists.newArrayList("magic_xp","reset");
	private final List<String> length3 = Lists.newArrayList("add");
	
	public String getName()
    {
        return "neo_ores";
    }

	@Override
	public String getUsage(ICommandSender sender) 
	{
		return "commands.neo_ores.usage";
	}
	
	public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		if (args.length < 2)
        {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
		else
		{
			EntityPlayerMP entityplayer = getPlayer(server, sender, args[0]);
			PlayerManaDataServer pmd = new PlayerManaDataServer(entityplayer);
			if(args[1].equals("reset"))
			{
				pmd.setLevel(0);
				pmd.setMana(0);
				pmd.setMaxMana(0);
				pmd.setMXP(0);
			}
			else if(args[1].equals("magic_xp") && args[2].equals("add"))
			{
				pmd.addMXP(Integer.parseInt(args[3]));
			}
			else
			{
				throw new WrongUsageException("commands.give.usage", new Object[0]);
			}
		}
	}
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, this.length2);
        }
        else if(args.length == 3 && args[1].equals("magic_xp"))
        {
        	return getListOfStringsMatchingLastWord(args, this.length3);
        }
        else
        {
        	return Collections.emptyList();
        }
    }
		
	public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}