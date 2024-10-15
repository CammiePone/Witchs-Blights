package dev.cammiescorner.client.models;

import com.google.common.collect.ImmutableList;
import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class VampireBeastEntityModel extends AnimalModel<VampireBeastEntity> implements ModelWithArms, ModelWithHead {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(WitchsBlights.id("vampire_beast"), "main");
	public final ModelPart body;
	public final ModelPart neck;
	public final ModelPart mouth;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightWing;
	public final ModelPart rightWingOuter;
	public final ModelPart leftWing;
	public final ModelPart leftWingOuter;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;

	public BipedEntityModel.ArmPose leftArmPose = BipedEntityModel.ArmPose.EMPTY;
	public BipedEntityModel.ArmPose rightArmPose = BipedEntityModel.ArmPose.EMPTY;

	public boolean sneaking;
	public boolean flying;
	public boolean hunting;
	public float leaningPitch;
	public float tickDelta;
	public float attackCooldownProgress;

	public VampireBeastEntityModel(ModelPart root) {
		body = root.getChild("body");
		neck = root.getChild("neck");
		mouth = neck.getChild("mouth");
		rightArm = root.getChild("right_arm");
		leftArm = root.getChild("left_arm");
		rightWing = root.getChild("right_wing");
		rightWingOuter = rightWing.getChild("right_wing2");
		leftWing = root.getChild("left_wing");
		leftWingOuter = leftWing.getChild("left_wing2");
		rightLeg = root.getChild("right_leg");
		leftLeg = root.getChild("left_leg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData neck = modelPartData.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0f, -8f, -6f));
		ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5f, -13f, -6.5f, 7f, 10f, 7f, new Dilation(0.1f)), ModelTransform.pivot(0f, 3f, 1f));
		head.addChild("cube_r1", ModelPartBuilder.create().uv(22, 28).cuboid(-3.5f, -3f, 0.5f, 0f, 3f, 6f, new Dilation(0f)), ModelTransform.of(0f, -8f, -3f, 0.1f, -0.25f, 0f));
		head.addChild("cube_r2", ModelPartBuilder.create().uv(22, 28).cuboid(3.5f, -3f, 0.5f, 0f, 3f, 6f, new Dilation(0f)), ModelTransform.of(0f, -8f, -3f, 0.1f, 0.25f, 0f));
		head.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5f, -3.5f, -3.75f, 3f, 4f, 0f, new Dilation(0f)), ModelTransform.of(0f, -8f, -3f, 0.2f, 0f, 0f));
		neck.addChild("mouth", ModelPartBuilder.create().uv(0, 17).cuboid(-3.5f, -7f, -6.5f, 7f, 10f, 7f, new Dilation(0f)), ModelTransform.pivot(0f, 0f, 1f));

		modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 34).cuboid(-4.5f, -18f, -1f, 9f, 18f, 4f, new Dilation(0f))
				.uv(0, 93).cuboid(-4.5f, -9f, -3f, 9f, 9f, 3f, new Dilation(-0.1f)), ModelTransform.of(0f, 7f, 0f, 0.25f, 0f, 0f));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(26, 41).cuboid(-3f, -2f, -2f, 3f, 12f, 4f, new Dilation(0.01f)), ModelTransform.of(-4.5f, -7f, -4f, 0f, 0f, 0.2f));
		right_arm.addChild("right_arm_2", ModelPartBuilder.create().uv(26, 58).cuboid(-0.5f, -2f, -3f, 4f, 14f, 5f, new Dilation(0f)), ModelTransform.of(-1.5f, 10f, 0f, -0.25f, 0f, 0f));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(26, 41).mirrored().cuboid(0f, -2f, -2f, 3f, 12f, 4f, new Dilation(0.01f)).mirrored(false), ModelTransform.of(4.5f, -7f, -4f, 0f, 0f, -0.2f));
		left_arm.addChild("left_arm_2", ModelPartBuilder.create().uv(26, 58).mirrored().cuboid(-3.5f, -2f, -3f, 4f, 14f, 5f, new Dilation(0f)).mirrored(false), ModelTransform.of(1.5f, 10f, 0f, -0.25f, 0f, 0f));

		ModelPartData right_wing = modelPartData.addChild("right_wing", ModelPartBuilder.create().uv(28, -22).cuboid(0f, -11f, 0f, 0f, 27f, 22f, new Dilation(0f)), ModelTransform.of(-2f, -9f, -2f, 0.5f, -0.9f, 0f));
		right_wing.addChild("right_wing2", ModelPartBuilder.create().uv(73, -22).cuboid(0f, 0f, 0f, 0f, 27f, 22f, new Dilation(0f)), ModelTransform.of(0f, -11f, 22f, -0.9f, 0.05f, 0f));

		ModelPartData left_wing = modelPartData.addChild("left_wing", ModelPartBuilder.create().uv(28, -22).cuboid(0f, -11f, 0f, 0f, 27f, 22f, new Dilation(0f)), ModelTransform.of(2f, -9f, -2f, 0.5f, 0.9f, 0f));
		left_wing.addChild("left_wing2", ModelPartBuilder.create().uv(73, -22).cuboid(0f, 0f, 0f, 0f, 27f, 22f, new Dilation(0f)), ModelTransform.of(0f, -11f, 22f, -0.9f, -0.05f, 0f));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.of(-2f, 5.25f, -3f, -0.2f, 0f, 0f));
		right_leg.addChild("cube_r4", ModelPartBuilder.create().uv(0, 58).cuboid(-3f, -0.75f, 1f, 4f, 8f, 5f, new Dilation(0.1f)), ModelTransform.of(0f, 0f, 0f, -0.25f, 0f, 0f));
		right_leg.addChild("right_leg_2", ModelPartBuilder.create().uv(0, 75).cuboid(-3f, -3.75f, 2f, 4f, 14f, 4f, new Dilation(0f)), ModelTransform.pivot(0f, 8f, 1f));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.of(2f, 5.25f, -3f, -0.2f, 0f, 0f));
		left_leg.addChild("cube_r5", ModelPartBuilder.create().uv(0, 58).mirrored().cuboid(-1f, -0.75f, 1f, 4f, 8f, 5f, new Dilation(0.1f)).mirrored(false), ModelTransform.of(0f, 0f, 0f, -0.25f, 0f, 0f));
		left_leg.addChild("left_leg_2", ModelPartBuilder.create().uv(0, 75).mirrored().cuboid(-1f, -3.75f, 2f, 4f, 14f, 4f, new Dilation(0f)).mirrored(false), ModelTransform.pivot(0f, 8f, 1f));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setAngles(VampireBeastEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		neck.yaw = headYaw * 0.017453292f;

		if(leaningPitch > 0f)
			neck.pitch = lerpAngle(leaningPitch, neck.pitch, headPitch * 0.017453292f);
		else
			neck.pitch = headPitch * 0.017453292f;

		body.yaw = 0f;
		neck.pivotY = -8f;
		neck.pivotZ = -6f;
		rightArm.pivotZ = -4f;
		rightArm.pivotX = -4.5f;
		leftArm.pivotZ = -4f;
		leftArm.pivotX = 4.5f;
		rightWing.pivotY = -9f;
		rightWing.pivotZ = -2f;
		leftWing.pivotY = -9f;
		leftWing.pivotZ = -2f;

		mouth.pitch = MathHelper.cos(animationProgress * 10f * 0.01f) * 0.0015f * 8f;
		mouth.pivotY = MathHelper.cos(animationProgress * 10f * 0.01f) * 0.05f * 16f;

		rightArm.pitch = MathHelper.cos(limbAngle * 0.6662f + MathHelper.PI) * 2f * limbDistance * 0.5f;
		leftArm.pitch = MathHelper.cos(limbAngle * 0.6662f) * 2f * limbDistance * 0.5f;

		rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance - 0.2f;
		leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + MathHelper.PI) * 1.4f * limbDistance - 0.2f;

		rightArm.roll = 0.2f;
		leftArm.roll = -0.2f;
		rightLeg.yaw = 0.005f;
		leftLeg.yaw = -0.005f;
		rightLeg.roll = 0.005f;
		leftLeg.roll = -0.005f;

		rightArm.yaw = 0f;
		leftArm.yaw = 0f;

		animateArms(entity);

		if(sneaking) {
			body.pitch = 1.2f;
			rightArm.pitch -= 0.2f;
			leftArm.pitch -= 0.2f;
			rightLeg.pivotZ = 3f;
			leftLeg.pivotZ = 3f;
			rightLeg.pivotY = 7.45f;
			leftLeg.pivotY = 7.45f;
			neck.pivotY = 1.2f;
			neck.pivotZ = -9.8f;
			body.pivotY = 8.2f;
			body.pivotZ = 6f;
			leftArm.pivotY = 3f;
			rightArm.pivotY = 3f;
			leftArm.pivotZ = -9f;
			rightArm.pivotZ = -9f;

			rightWing.pitch = 0.75f;
			rightWing.yaw = -0.2f;
			rightWing.roll = -0.5f;
			rightWing.pivotY = 1f;
			rightWing.pivotZ = -6f;

			leftWing.pitch = 0.75f;
			leftWing.yaw = 0.2f;
			leftWing.roll = 0.5f;
			leftWing.pivotY = 1f;
			leftWing.pivotZ = -6f;
		}
		else {
			body.pitch = 0.3f;
			rightLeg.pivotZ = -3f;
			leftLeg.pivotZ = -3f;
			rightLeg.pivotY = 5.25f;
			leftLeg.pivotY = 5.25f;
			neck.pivotY = -8f;
			body.pivotY = 7f;
			body.pivotZ = 0f;
			leftArm.pivotY = -7f;
			rightArm.pivotY = -7f;

			rightWing.pitch = 0.5f;
			rightWing.yaw = -0.9f;
			rightWing.roll = 0f;

			leftWing.pitch = 0.5f;
			leftWing.yaw = 0.9f;
			leftWing.roll = 0f;
		}

		if(flying) {
			float wingFlappies = MathHelper.cos(animationProgress * 30f * 0.0125f) * 0.025f * 16f;

			rightWing.pitch = 0.25f;
			leftWing.pitch = 0.25f;
			rightWing.yaw -= wingFlappies;
			leftWing.yaw += wingFlappies;
			rightWing.roll -= wingFlappies * 0.06f * 16f;
			leftWing.roll += wingFlappies * 0.06f * 16f;

			rightWingOuter.pitch = 0f;
			leftWingOuter.pitch = 0f;

			rightArm.pitch = MathHelper.cos(limbAngle * 0.1f + MathHelper.PI) * 2f * limbDistance * 0.01f;
			leftArm.pitch = MathHelper.cos(limbAngle * 0.1f) * 2f * limbDistance * 0.01f;

			rightLeg.pitch = 0f;
			leftLeg.pitch = 0f;
		}
		else {
			rightWing.roll += MathHelper.cos(animationProgress * 0.09F) * 0.05F + 0.05F;
			rightWing.pitch += MathHelper.sin(animationProgress * 0.067F) * 0.05F;

			leftWing.roll -= MathHelper.cos(animationProgress * 0.09F) * 0.05F + 0.05F;
			leftWing.pitch += MathHelper.sin(animationProgress * 0.067F) * 0.05F;

			rightWingOuter.pitch = -0.9f;
			leftWingOuter.pitch = -0.9f;
		}

		if(hunting || flying)
			mouth.pivotY = MathHelper.cos(animationProgress * 10f * 0.01f) * 0.05f * 16f + (4 * attackCooldownProgress);

		CrossbowPosing.swingArm(rightArm, animationProgress, 1f);
		CrossbowPosing.swingArm(leftArm, animationProgress, -1f);

		if(leaningPitch > 0f) {
			float l = limbAngle % 26f;
			Arm arm = getPreferredArm(entity);
			float m = arm == Arm.RIGHT && handSwingProgress > 0f ? 0f : leaningPitch;
			float n = arm == Arm.LEFT && handSwingProgress > 0f ? 0f : leaningPitch;
			float o;

			if(!entity.isUsingItem()) {
				if(l < 14f) {
					leftArm.pitch = lerpAngle(n, leftArm.pitch, 0f);
					rightArm.pitch = MathHelper.lerp(m, rightArm.pitch, 0f);
					leftArm.yaw = lerpAngle(n, leftArm.yaw, MathHelper.PI);
					rightArm.yaw = MathHelper.lerp(m, rightArm.yaw, MathHelper.PI);
					leftArm.roll = lerpAngle(n, leftArm.roll, MathHelper.PI + 1.8707964f * quadraticArmUpdate(l) / quadraticArmUpdate(14f));
					rightArm.roll = MathHelper.lerp(m, rightArm.roll, MathHelper.PI - 1.8707964f * quadraticArmUpdate(l) / quadraticArmUpdate(14f));
				}
				else if(l >= 14f && l < 22f) {
					o = (l - 14f) / 8f;
					leftArm.pitch = lerpAngle(n, leftArm.pitch, 1.5707964f * o);
					rightArm.pitch = MathHelper.lerp(m, rightArm.pitch, 1.5707964f * o);
					leftArm.yaw = lerpAngle(n, leftArm.yaw, MathHelper.PI);
					rightArm.yaw = MathHelper.lerp(m, rightArm.yaw, MathHelper.PI);
					leftArm.roll = lerpAngle(n, leftArm.roll, 5.012389f - 1.8707964f * o);
					rightArm.roll = MathHelper.lerp(m, rightArm.roll, 1.2707963f + 1.8707964f * o);
				}
				else if(l >= 22f && l < 26f) {
					o = (l - 22f) / 4f;
					leftArm.pitch = lerpAngle(n, leftArm.pitch, 1.5707964f - 1.5707964f * o);
					rightArm.pitch = MathHelper.lerp(m, rightArm.pitch, 1.5707964f - 1.5707964f * o);
					leftArm.yaw = lerpAngle(n, leftArm.yaw, MathHelper.PI);
					rightArm.yaw = MathHelper.lerp(m, rightArm.yaw, MathHelper.PI);
					leftArm.roll = lerpAngle(n, leftArm.roll, MathHelper.PI);
					rightArm.roll = MathHelper.lerp(m, rightArm.roll, MathHelper.PI);
				}
			}

			o = 0.3f;
			float p = 0.33333334f;
			leftLeg.pitch = MathHelper.lerp(leaningPitch, leftLeg.pitch, o * MathHelper.cos(limbAngle * p + MathHelper.PI));
			rightLeg.pitch = MathHelper.lerp(leaningPitch, rightLeg.pitch, o * MathHelper.cos(limbAngle * p));
		}
	}

	@Override
	public ModelPart getHead() {
		return neck;
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(neck);
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(body, rightArm, leftArm, rightWing, leftWing, rightLeg, leftLeg);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		getArm(arm).rotate(matrices);
	}

	@Override
	public void animateModel(VampireBeastEntity entity, float limbAngle, float limbDistance, float tickDelta) {
		leaningPitch = entity.getLeaningPitch(tickDelta);
		this.tickDelta = tickDelta;
		super.animateModel(entity, limbAngle, limbDistance, tickDelta);
	}

	protected void animateArms(VampireBeastEntity entity) {
		if(!(handSwingProgress <= 0.0F)) {
			Arm arm = getPreferredArm(entity);
			ModelPart modelPart = getArm(arm);
			float f = handSwingProgress;
			body.yaw = MathHelper.sin(MathHelper.sqrt(f) * MathHelper.TAU) * 0.2f;

			if(arm == Arm.LEFT)
				body.yaw *= -1f;

			rightArm.pivotZ = MathHelper.sin(body.yaw) * 4f;
			rightArm.pivotX = -MathHelper.cos(body.yaw) * 4.5f;
			leftArm.pivotZ = -MathHelper.sin(body.yaw) * 4f;
			leftArm.pivotX = MathHelper.cos(body.yaw) * 4.5f;
			rightArm.yaw += body.yaw;
			leftArm.yaw += body.yaw;
			leftArm.pitch += body.yaw;
			f = 1f - handSwingProgress;
			f *= f;
			f *= f;
			f = 1f - f;

			float g = MathHelper.sin(f * MathHelper.PI);
			float h = MathHelper.sin(handSwingProgress * MathHelper.PI) * -(neck.pitch - 0.7f) * 0.75f;

			modelPart.pitch -= g * 1.2f + h;
			modelPart.yaw += body.yaw * 2f;
			modelPart.roll += MathHelper.sin(handSwingProgress * MathHelper.PI) * -0.4f;
		}
	}

	private float quadraticArmUpdate(float f) {
		return -65f * f + f * f;
	}

	protected float lerpAngle(float angleOne, float angleTwo, float magnitude) {
		float f = (magnitude - angleTwo) % MathHelper.TAU;

		if(f < -MathHelper.PI)
			f += MathHelper.TAU;

		if(f >= MathHelper.PI)
			f -= MathHelper.TAU;

		return angleTwo + angleOne * f;
	}

	protected ModelPart getArm(Arm arm) {
		return arm == Arm.LEFT ? leftArm : rightArm;
	}

	private Arm getPreferredArm(VampireBeastEntity entity) {
		Arm arm = entity.getMainArm();
		
		return entity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
	}
}