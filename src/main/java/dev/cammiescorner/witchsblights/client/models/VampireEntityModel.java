package dev.cammiescorner.witchsblights.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Arm;

public class VampireEntityModel extends AnimalModel<Entity> implements ModelWithArms, ModelWithHead {
	private final ModelPart head;
	private final ModelPart arms;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart body;
	private final ModelPart cape;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public VampireEntityModel(ModelPart root) {
		this.head = root.getChild("head");
		this.arms = root.getChild("arms");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.body = root.getChild("body");
		this.cape = body.getChild("cape");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4f, -10f, -4f, 8f, 10f, 8f, new Dilation(0f))
		.uv(0, 0).cuboid(-6f, -4f, 0f, 2f, 2f, 0f, new Dilation(0f))
		.uv(0, 0).mirrored().cuboid(4f, -4f, 0f, 2f, 2f, 0f, new Dilation(0f)).mirrored(false), ModelTransform.pivot(0f, 0f, 0f));
		head.addChild("nose", ModelPartBuilder.create().uv(24, 0).cuboid(-1f, -1f, -6f, 2f, 4f, 2f, new Dilation(0f)), ModelTransform.pivot(0f, -2f, 0f));

		ModelPartData arms = modelPartData.addChild("arms", ModelPartBuilder.create(), ModelTransform.pivot(0f, 3.5f, 0.3f));
		ModelPartData arms_rotation = arms.addChild("arms_rotation", ModelPartBuilder.create().uv(44, 22).cuboid(-8f, 0f, -2.05f, 4f, 8f, 4f, new Dilation(0f))
		.uv(40, 38).cuboid(-4f, 4f, -2.05f, 8f, 4f, 4f, new Dilation(0f)), ModelTransform.of(0f, -2f, 0.05f, -0.7505f, 0f, 0f));
		arms_rotation.addChild("arms_flipped", ModelPartBuilder.create().uv(44, 22).mirrored().cuboid(4f, -24f, -2.05f, 4f, 8f, 4f, new Dilation(0f)).mirrored(false), ModelTransform.pivot(0f, 24f, 0f));

		modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 46).cuboid(-2f, -2f, -2f, 4f, 12f, 4f, new Dilation(0f)), ModelTransform.pivot(-6f, 2f, 0f));

		modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(40, 46).mirrored().cuboid(-2f, -2f, -2f, 4f, 12f, 4f, new Dilation(0f)).mirrored(false), ModelTransform.of(6f, 2f, 0f, 0f, 0f, 0f));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 20).cuboid(-4f, 0f, -3f, 8f, 12f, 6f, new Dilation(0f))
		.uv(0, 38).cuboid(-4f, 0f, -3f, 8f, 18f, 6f, new Dilation(0.25f)), ModelTransform.pivot(0f, 0f, 0f));
		body.addChild("cape", ModelPartBuilder.create().uv(36, 0).cuboid(-5f, 0f, -0.5f, 10f, 16f, 1f, new Dilation(0f)), ModelTransform.of(0f, 0f, 3.5f, 0.1309f, 0f, 0f));

		modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 22).cuboid(-6f, 0f, -2f, 4f, 12f, 4f, new Dilation(0f)), ModelTransform.pivot(2f, 12f, 0f));

		modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 22).mirrored().cuboid(2f, 0f, -2f, 4f, 12f, 4f, new Dilation(0f)).mirrored(false), ModelTransform.pivot(-2f, 12f, 0f));
		
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {

	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(head);
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(arms, right_arm, left_arm, body, right_leg, left_leg);
	}

	@Override
	public ModelPart getHead() {
		return head;
	}
}