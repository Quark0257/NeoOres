package neo_ores.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMechanicalMagician extends ModelBase
{
	private final ModelRenderer bb_main;

	public ModelMechanicalMagician()
	{
		textureWidth = 32;
		textureHeight = 32;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, -6, 0, -3.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, -6, 0, -2.0F, -3.0F, -3.0F, 4, 6, 6, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -2.0F, -3.0F, 6, 4, 6, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 16, 20, -2.0F, -4.0F, -2.0F, 4, 8, 4, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -2.0F, -2.0F, 8, 4, 4, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -2.0F, -2.0F, -4.0F, 4, 4, 8, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 2.0F, -4.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, 2.0F, 3.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, 3.0F, 2.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -4.0F, -3.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -4.0F, 2.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, 2.0F, -4.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, 3.0F, -3.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, 4.0F, -1.0F, 2, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -3.0F, -4.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -3.0F, 3.0F, 2, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 3.0F, -1.0F, 2.0F, 1, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 2.0F, 3.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -4.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, 2.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 3.0F, -3.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 3.0F, 2.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -3.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, 3.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -1.0F, -4.0F, 1, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -1.0F, -3.0F, 1, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 2.0F, -1.0F, 3.0F, 1, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 3.0F, -1.0F, -3.0F, 1, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 4.0F, -1.0F, -1.0F, 1, 2, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 2.0F, -1.0F, -4.0F, 1, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -1.0F, 4.0F, 2, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -1.0F, -5.0F, 2, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -5.0F, -1.0F, -1.0F, 1, 2, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -1.0F, 2.0F, 1, 2, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -1.0F, 3.0F, 1, 2, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		float f = netHeadYaw;
		float f1 = headPitch;
		bb_main.rotateAngleY = f1;
		bb_main.rotateAngleX = f;
		bb_main.render(scale);
	}

	public void render(float rotOffsetX, float rotOffsetY, float rotOffsetZ, float rotX, float rotY, float rotZ, float scale)
	{
		bb_main.rotateAngleY = rotY;
		bb_main.rotateAngleX = rotX;
		bb_main.rotateAngleZ = rotZ;
		bb_main.rotationPointX = rotOffsetX;
		bb_main.rotationPointY = rotOffsetY;
		bb_main.rotationPointZ = rotOffsetZ;
		bb_main.render(scale);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{

	}
}
