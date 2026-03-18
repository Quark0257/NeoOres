package neo_ores.api.guide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import neo_ores.api.ColorUtils;
import neo_ores.api.StructureTemplate;
import neo_ores.api.Vec2I;
import neo_ores.client.gui.GuiGuidebook;
import neo_ores.main.NeoOresBlocks;
import neo_ores.main.NeoOresData;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;

public class StructurePanel extends AbstractPageComponent
{
	protected final ResourceLocation str;
	// degrees
	protected float rotX;
	protected float rotY;
	protected float rotZ;
	protected int slice;
	protected int maxSlice;

	public StructurePanel(int x, int y, int width, int height, ResourceLocation location)
	{
		super(x, y, width, height);
		this.str = location;
	}

	public void init(GuiGuidebook guide)
	{
		this.rotX = -30.0F;// 30.0F;
		this.rotY = -45.0F;
		this.rotZ = 0.0F;
		this.slice = 0;
		this.maxSlice = 0;
	}

	@Override
	public List<ComponentHover> drawScreen(GuiGuidebook guide, int mouseX, int mouseY, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
	{
		List<ComponentHover> list = new ArrayList<>();
		Template template = NeoOresData.getStructureTemplate(this.str);
		if (template != null)
		{
			int posEdge = 2;
			int edge = 2;
			SliceButton buttonDown = new SliceButton("<", I18n.format("guide.structure.slice_down"), this.posX + posEdge, this.posY + posEdge, edge, new Runnable()
			{
				@Override
				public void run()
				{
					sliceDown();
				}
			});
			int maxWidth = guide.mc.fontRenderer.getStringWidth(this.getSliceText(0));
			int currentWidth = guide.mc.fontRenderer.getStringWidth(this.getSliceText(this.slice));
			int textX = this.posX + posEdge + buttonDown.getWidth(guide) + edge;
			guide.mc.fontRenderer.drawString(this.getSliceText(this.slice), textX + (maxWidth - currentWidth) / 2, this.posY + posEdge + edge, 4210752);
			SliceButton buttonUp = new SliceButton(">", I18n.format("guide.structure.slice_up"), textX + edge + maxWidth, this.posY + posEdge, edge, new Runnable()
			{
				@Override
				public void run()
				{
					sliceUp();
				}
			});

			StructureTemplate str = new StructureTemplate(template);
			List<BlockInfo> blocks = str.getBlocks();
			List<ItemStack> stacks = new ArrayList<>();
			loop: for (BlockInfo info : blocks)
			{
				Item itemBlock = Item.getItemFromBlock(info.blockState.getBlock());
				int meta = info.blockState.getBlock().damageDropped(info.blockState);
				for (ItemStack result : stacks)
				{
					if (result.getItem() == itemBlock && result.getMetadata() == meta)
					{
						result.setCount(result.getCount() + 1);
						continue loop;
					}
				}
				ItemStack stack = new ItemStack(itemBlock, 1, meta);
				stacks.add(stack);
			}

			buttonDown.draw(guide, mouseX, mouseY, isMouseLeftClicked, false);
			buttonUp.draw(guide, mouseX, mouseY, isMouseLeftClicked, false);

			int offsetY = guide.mc.fontRenderer.FONT_HEIGHT + edge * 3 + posEdge;
			int offsetX = posEdge;
			int size = 16;
			int d = (this.height - offsetY) / size;
			int viewOffset = ((stacks.size() - 1) / d + 1) * size;
			List<StackView> stackViews = new ArrayList<>();
			for (int i = 0; i < stacks.size(); i++)
			{
				int y = i % d * size;
				int x = (i / d) * size;
				stackViews.add(new StackView(stacks.get(i), this.posX + offsetX + x, this.posY + offsetY + y));
			}
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate(this.posX + viewOffset, this.posY, 0.0);
			StructureWorldRenderer access = new StructureWorldRenderer(blocks)
			{
				@Override
				public WorldType getWorldType()
				{
					return guide.mc.world.getWorldType();
				}
			};
			AxisAlignedBB aabb = access.getBoundingBox();
			double sizeX = aabb.maxX - aabb.minX + 1;
			double sizeY = aabb.maxY - aabb.minY + 1;
			double sizeZ = aabb.maxZ - aabb.minZ + 1;
			this.maxSlice = (int) sizeY;
			access.setSlice(this.slice);
			double structureWidthR = Math.max(sizeY, Math.max(sizeX, sizeZ)) * 0.5D;
			if (mouseDragging && this.isMouseOver(mouseX, mouseY))
			{
				this.rotX -= draggedMouseDelta.getY();
				this.rotY += draggedMouseDelta.getX();
			}
			double r = 0.5D * Math.sqrt(sizeX * sizeX + sizeY * sizeY + sizeZ * sizeZ);
			double scale = Math.min((this.width - viewOffset) * 0.5D, this.height * 0.5D) / r;
			GlStateManager.pushMatrix();
			GlStateManager.translate((this.width - viewOffset) * 0.5, this.height * 0.5, 0.0);
			GlStateManager.translate(0.0, 0.0, 2.0 * structureWidthR * scale);
			GlStateManager.rotate(this.rotX, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(this.rotY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(this.rotZ, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate((sizeX % 2 == 1) ? -0.5 * scale : 0.0, (sizeY % 2 == 1) ? 0.5 * scale : 0.0, (sizeZ % 2 == 1) ? -0.5 * scale : 0.0);
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.0F, 0.0F, -sizeY * 0.5 * scale);
			int height = guide.mc.fontRenderer.FONT_HEIGHT;
			int width = guide.mc.fontRenderer.getStringWidth("N");
			double structureWidthXZ = Math.max(sizeX, sizeZ) * 0.5D;
			double additionalRadius = height / scale;
			guide.mc.fontRenderer.drawString("N", (int)(0.5D * scale) - width / 2, (int)((-(structureWidthXZ + additionalRadius) + 0.5D) * scale) - height /2, 4210752);
			guide.mc.fontRenderer.drawString("S", (int)(0.5D * scale) - width / 2, (int)((structureWidthXZ + additionalRadius + 0.5D) * scale) - height /2, 4210752);
			guide.mc.fontRenderer.drawString("W", (int)((-(structureWidthXZ + additionalRadius) + 0.5D) * scale) - width / 2, (int)(0.5D * scale) - height /2, 4210752);
			guide.mc.fontRenderer.drawString("E", (int)((structureWidthXZ + additionalRadius + 0.5D) * scale) - width / 2, (int)(0.5D * scale) - height /2, 4210752);
			GlStateManager.translate(0.0F, 0.0F, sizeY * 0.5 * scale);
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(scale, -scale, scale);
			guide.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
			for (BlockInfo block : access.getBlockMap().values())
			{
				if (access.hasSlice())
				{
					if (block.pos.getY() != access.getLayer())
					{
						continue;
					}
				}
				if (block.blockState.getBlock() != Blocks.AIR)
				{
					guide.mc.getBlockRendererDispatcher().renderBlock(block.blockState, block.pos, access, bufferbuilder);
				}
			}
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.translate(-(this.posX + viewOffset), -this.posY, 0.0);

			RenderHelper.enableGUIStandardItemLighting();
			for (StackView stackView : stackViews)
			{
				stackView.draw(guide, mouseX, mouseY, isMouseLeftClicked, false);
			}
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableBlend();

			list.add(buttonDown.draw(guide, mouseX, mouseY, isMouseLeftClicked, true));
			list.add(buttonUp.draw(guide, mouseX, mouseY, isMouseLeftClicked, true));
			for (StackView stackView : stackViews)
			{
				list.add(stackView.draw(guide, mouseX, mouseY, isMouseLeftClicked, true));
			}
		}
		else
		{
			NeoOresData.setStructure(this.str);
			NeoOresData.syncStructures();
		}
		return list;
	}

	public void sliceUp()
	{
		this.slice++;
		if (this.slice > this.maxSlice)
		{
			this.slice = 0;
		}
	}

	public void sliceDown()
	{
		this.slice--;
		if (this.slice < 0)
		{
			this.slice = this.maxSlice;
		}
	}

	public static class SliceButton
	{
		private final String s;
		private final int x;
		private final int y;
		private final int edge;
		private final Runnable clickAction;
		private final String hoverText;

		public SliceButton(String s, String hoverText, int x, int y, int edge, Runnable clickAction)
		{
			this.s = s;
			this.x = x;
			this.y = y;
			this.edge = edge;
			this.clickAction = clickAction;
			this.hoverText = hoverText;
		}

		public ComponentHover draw(GuiGuidebook gui, int mouseX, int mouseY, boolean isClicked, boolean isHover)
		{
			if (!isHover)
			{
				if (this.isMouseOver(mouseX, mouseY, gui))
				{
					GuiGuidebook.drawRect(this.x, this.y, this.x + this.getWidth(gui), this.y + this.getHeight(gui), ColorUtils.makeColor4d(0.9, 0.9, 0.9, 1.0));
				}
				gui.mc.fontRenderer.drawString(this.s, this.x + this.edge, this.y + this.edge, 4210752);
			}
			else if (this.isMouseOver(mouseX, mouseY, gui))
			{
				if (isClicked)
				{
					this.clickAction.run();
					gui.mc.world.playSound(gui.mc.player, gui.mc.player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0F, 1.0F);
				}
				return new ComponentHover()
				{
					@Override
					public void drawHover(GuiGuidebook guide, int mouseX1, int mouseY1, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
					{
						gui.drawHoveringText(Arrays.asList(hoverText), mouseX1, mouseY1);
					}
				};
			}
			return ComponentHover.EMPTY;
		}

		public int getWidth(GuiGuidebook gui)
		{
			return 2 * this.edge + gui.mc.fontRenderer.getStringWidth(this.s);
		}

		public int getHeight(GuiGuidebook gui)
		{
			return 2 * this.edge + gui.mc.fontRenderer.FONT_HEIGHT;
		}

		public boolean isMouseOver(int mouseX, int mouseY, GuiGuidebook gui)
		{
			return this.x <= mouseX && mouseX <= this.x + this.getWidth(gui) && this.y <= mouseY && mouseY <= this.y + this.getHeight(gui);
		}
	}

	public static class StackView
	{
		private final ItemStack stack;
		private final int x;
		private final int y;

		public StackView(ItemStack stack, int x, int y)
		{
			this.stack = stack;
			this.x = x;
			this.y = y;
		}

		public ComponentHover draw(GuiGuidebook gui, int mouseX, int mouseY, boolean isClicked, boolean isHover)
		{
			if (!isHover)
			{
				gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.mc.player, this.stack, this.x, this.y);
				gui.getItemRenderer().renderItemOverlayIntoGUI(gui.getFont(), this.stack, this.x, this.y, String.valueOf(this.stack.getCount()));
			}
			else if (this.isMouseOver(mouseX, mouseY, gui))
			{
				return new ComponentHover()
				{
					@Override
					public void drawHover(GuiGuidebook guide, int mouseX1, int mouseY1, float partialTicks, boolean isMouseLeftClicked, Vec2I draggedMouseDelta, boolean mouseDragging)
					{
						if (stack.getItem() == Item.getItemFromBlock(Blocks.SEA_LANTERN))
						{
							gui.drawHoveringText(I18n.format("guide.structure.level_max_light_block"), mouseX1, mouseY1);
						}
						else if (stack.getItem() == Item.getItemFromBlock(NeoOresBlocks.enhanced_pedestal))
						{
							gui.drawHoveringText(I18n.format("guide.structure.enhanced_pedestal"), mouseX1, mouseY1);
						}
						else
						{
							gui.renderToolTip(stack, mouseX1, mouseY1);
						}
					}
				};
			}
			return ComponentHover.EMPTY;
		}

		public boolean isMouseOver(int mouseX, int mouseY, GuiGuidebook gui)
		{
			return this.x <= mouseX && mouseX <= this.x + 16 && this.y <= mouseY && mouseY <= this.y + 16;
		}
	}

	private String getSliceText(int slice)
	{
		return slice == 0 ? "ALL" : String.valueOf(slice);
	}
}
