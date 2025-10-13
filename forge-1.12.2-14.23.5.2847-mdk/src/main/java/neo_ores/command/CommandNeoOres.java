package neo_ores.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import neo_ores.api.PlayerTrigger;
import neo_ores.main.NeoOresData;
import neo_ores.util.PlayerMagicData;
import neo_ores.util.SpellUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommandNeoOres extends CommandBase
{
	private final List<String> length2 = Lists.newArrayList("magic_xp", "study", "reset_all", "triggers");
	private final List<String> length3 = Lists.newArrayList("add");
	private final List<String> length3_s = Lists.newArrayList("all");
	private final List<String> length3_s1 = Lists.newArrayList("reset");

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
			throw new WrongUsageException("commands.neo_ores.usage", new Object[0]);
		}
		else
		{
			EntityPlayerMP entityplayer = getPlayer(server, sender, args[0]);
			PlayerMagicData pmd = NeoOresData.instance.getPMD(entityplayer);
			if (args[1].equals("reset_all"))
			{
				PlayerMagicData newPmd = new PlayerMagicData(false);
				pmd.readFromNBT(newPmd.writeToNBT(new NBTTagCompound()));
				pmd.markDirty();
				pmd.markSending();
			}
			else if (args[1].equals("magic_xp") && args[2].equals("add"))
			{
				pmd.addMXP(Long.parseLong(args[3]));
			}
			else if (args[1].equals("study") && args[2].equals("all"))
			{
				for (Map.Entry<String, List<String>> data : SpellUtils.getAll().entrySet())
				{
					for (String id : data.getValue())
					{
						pmd.set(data.getKey(), id);
					}
				}
			}
			else if (args[1].equals("triggers") && args[2].equals("reset")) 
			{
				for (PlayerTrigger trigger : GameRegistry.findRegistry(PlayerTrigger.class).getValuesCollection()) 
				{
					pmd.resetTrigger(trigger);
				}
			}
			else
			{
				throw new WrongUsageException("commands.neo_ores.usage", new Object[0]);
			}
		}
	}

	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		else if (args.length == 2)
		{
			return getListOfStringsMatchingLastWord(args, this.length2);
		}
		else if (args.length == 3 && args[1].equals("magic_xp"))
		{
			return getListOfStringsMatchingLastWord(args, this.length3);
		}
		else if (args.length == 3 && args[1].equals("study"))
		{
			return getListOfStringsMatchingLastWord(args, this.length3_s);
		}
		else if (args.length == 3 && args[1].equals("triggers"))
		{
			return getListOfStringsMatchingLastWord(args, this.length3_s1);
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