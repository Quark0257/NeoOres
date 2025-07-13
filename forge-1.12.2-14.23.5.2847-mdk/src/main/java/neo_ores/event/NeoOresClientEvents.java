package neo_ores.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import neo_ores.client.particle.TexturedParticle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NeoOresClientEvents
{
	private static NeoOresClientEvents instance = null;
	private final List<TexturedParticle> list;

	private NeoOresClientEvents()
	{
		this.list = new ArrayList<>();
	}

	public static NeoOresClientEvents getInstance()
	{
		if (instance == null)
		{
			instance = new NeoOresClientEvents();
		}
		return instance;
	}

	public void addParticle(TexturedParticle particle)
	{
		if (Minecraft.getMinecraft().isGamePaused())
		{
			return;
		}
		this.list.add(particle);
	}

	public void reset()
	{
		this.list.clear();
	}

	private void update()
	{
		for (TexturedParticle particle : this.list)
		{
			particle.update();
		}
		this.list.removeIf(new Predicate<TexturedParticle>()
		{
			@Override
			public boolean test(TexturedParticle particle)
			{
				return particle.isExpired();
			}
		});
	}

	private void render(float partialTicks)
	{
		for (TexturedParticle particle : this.list)
		{
			particle.render(partialTicks);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onRenderLast(RenderWorldLastEvent event)
	{
		float partialTicks = event.getPartialTicks();
		this.render(partialTicks);
	}

	@SubscribeEvent
	public void onClientTickEnd(TickEvent.ClientTickEvent event)
	{
		if (event.phase != TickEvent.Phase.END)
		{
			return;
		}
		this.update();
	}

	public void removeParticle(Predicate<TexturedParticle> predicate)
	{
		this.list.removeIf(predicate);
	}
}
