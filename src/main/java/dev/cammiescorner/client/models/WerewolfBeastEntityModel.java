package dev.cammiescorner.client.models;

import com.google.common.collect.ImmutableList;
import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.entities.WerewolfBeastEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class WerewolfBeastEntityModel extends AnimalModel<WerewolfBeastEntity> implements ModelWithArms, ModelWithHead {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(WitchsBlights.id("werewolf_beast"), "main");

	private final ModelPart neck;
	private final ModelPart mouth;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public BipedEntityModel.ArmPose leftArmPose = BipedEntityModel.ArmPose.EMPTY;
	public BipedEntityModel.ArmPose rightArmPose = BipedEntityModel.ArmPose.EMPTY;

	public boolean sneaking;
	public boolean climbing;
	public boolean hunting;
	public float leaningPitch;
	public float tickDelta;
	public float attackCooldownProgress;

	public WerewolfBeastEntityModel(ModelPart root) {
		this.neck = root.getChild("neck");
		this.mouth = neck.getChild("head").getChild("bottom_snout");
		this.body = root.getChild("body");
		this.tail = body.getChild("tail");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData neck = modelPartData.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0f, -12f, -4f));

		ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5f, -4f, -4f, 7f, 7f, 7f, new Dilation(0f)).uv(0, 14).cuboid(-3.5f, 3f, -4f, 7f, 2f, 7f, new Dilation(0f)), ModelTransform.pivot(0f, 0.25F, -3f));
		head.addChild("cube_r1", ModelPartBuilder.create().uv(82, 0).cuboid(-3.5f, 0.25F, 0.9f, 7f, 7f, 4f, new Dilation(0.1f)), ModelTransform.of(0f, 0f, 0f, 1.0036F, 0f, 0f));
		head.addChild("cube_r2", ModelPartBuilder.create().uv(21, 0).mirrored().cuboid(-5.25F, -2f, -4f, 4f, 7f, 0f, new Dilation(0f)).mirrored(false), ModelTransform.of(0f, 0f, 0f, 0f, 0.6545F, 0f));
		head.addChild("cube_r3", ModelPartBuilder.create().uv(21, 0).cuboid(1.25F, -2f, -4f, 4f, 7f, 0f, new Dilation(0f)), ModelTransform.of(0f, 0f, 0f, 0f, -0.6545F, 0f));
		head.addChild("cube_r4", ModelPartBuilder.create().uv(41, 0).mirrored().cuboid(-3.5f, -4.75F, -3.25F, 2f, 2f, 3f, new Dilation(0.2f)).mirrored(false)
				.uv(41, 0).cuboid(1.5f, -4.75F, -3.25F, 2f, 2f, 3f, new Dilation(0.2f)), ModelTransform.of(0f, 0f, 0f, 0.3054F, 0f, 0f));
		head.addChild("top_snout", ModelPartBuilder.create().uv(28, 2).cuboid(-1.5f, -2f, -5f, 3f, 2f, 5f, new Dilation(0f))
				.uv(46, 2).cuboid(-1.5f, -0.5f, -5f, 3f, 2f, 5f, new Dilation(-0.15F)), ModelTransform.pivot(0f, 0f, -4f));
		head.addChild("bottom_snout", ModelPartBuilder.create().uv(23, 9).cuboid(-2f, 0f, -5f, 3f, 2f, 5f, new Dilation(0f))
				.uv(41, 9).cuboid(-2f, -1.5f, -5f, 3f, 2f, 5f, new Dilation(-0.1f))
				.uv(28, 16).cuboid(-2f, 2f, -5f, 3f, 2f, 5f, new Dilation(0f)), ModelTransform.pivot(0.5f, 0f, -4f));

		ModelPartData right_ear = head.addChild("right_ear", ModelPartBuilder.create(), ModelTransform.pivot(-3.5f, -1f, -1f));
		right_ear.addChild("cube_r5", ModelPartBuilder.create().uv(54, 4).cuboid(0f, -2f, 0f, 0f, 4f, 6f, new Dilation(0f)), ModelTransform.of(0f, 0f, 0f, 0.7854F, -0.6109F, 0f));

		ModelPartData left_ear = head.addChild("left_ear", ModelPartBuilder.create(), ModelTransform.pivot(3.5f, -1f, -1f));
		left_ear.addChild("cube_r6", ModelPartBuilder.create().uv(54, 4).cuboid(0f, -2f, 0f, 0f, 4f, 6f, new Dilation(0f)), ModelTransform.of(0f, 0f, 0f, 0.7854F, 0.6109F, 0f));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 39).cuboid(-4.5f, -10.5f, -2f, 9f, 10f, 5f, new Dilation(0f))
				.uv(0, 23).cuboid(-5f, -20.5f, -3f, 10f, 10f, 6f, new Dilation(0f)), ModelTransform.of(0f, 8.5f, 0.5f, 0.35f, 0f, 0f));
		body.addChild("cube_r7", ModelPartBuilder.create().uv(64, 23).cuboid(-4f, 0.5f, 5.5f, 8f, 10f, 6f, new Dilation(0f)), ModelTransform.of(0f, -15f, -5.5f, 0.0873F, 0f, 0f));
		body.addChild("cube_r8", ModelPartBuilder.create().uv(92, 23).cuboid(-4.5f, -3.5f, 5.5f, 9f, 10f, 6f, new Dilation(0f)), ModelTransform.of(0f, -15f, -5.5f, 0.3927F, 0f, 0f));
		body.addChild("tail", ModelPartBuilder.create().uv(56, 39).cuboid(-1.5f, 0f, -1.5f, 3f, 13f, 3f, new Dilation(0f)), ModelTransform.pivot(0f, -1.5f, 2f));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(28, 41).cuboid(-3f, -2f, -2f, 3f, 12f, 4f, new Dilation(0.01F))
				.uv(42, 39).cuboid(-3f, 10f, -2f, 3f, 3f, 4f, new Dilation(0.01F)), ModelTransform.of(-4.5f, -7.5f, -5f, 0f, 0f, 0.175f));
		right_arm.addChild("right_arm_2", ModelPartBuilder.create().uv(18, 57).cuboid(-0.5f, -3f, -3f, 5f, 14f, 5f, new Dilation(0f)), ModelTransform.of(-1.5f, 10f, 0f, -0.2618F, 0f, 0f));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(28, 41).mirrored().cuboid(0f, -2f, -2f, 3f, 12f, 4f, new Dilation(0.01F)).mirrored(false)
				.uv(42, 39).mirrored().cuboid(0f, 10f, -2f, 3f, 3f, 4f, new Dilation(0.01F)).mirrored(false), ModelTransform.of(4.5f, -7.5f, -5f, 0f, 0f, -0.175f));
		left_arm.addChild("left_arm_2", ModelPartBuilder.create().uv(18, 57).mirrored().cuboid(-4.5f, -3f, -3f, 5f, 14f, 5f, new Dilation(0f)).mirrored(false), ModelTransform.of(1.5f, 10f, 0f, -0.2618F, 0f, 0f));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.of(-3f, 7f, 0f, -0.175f, 0f, 0f));
		right_leg.addChild("cube_r9", ModelPartBuilder.create().uv(0, 54).cuboid(-3f, 1f, 2f, 4f, 8f, 5f, new Dilation(0.1f)), ModelTransform.of(1f, -2f, -4f, -0.2618F, 0f, 0f));
		right_leg.addChild("right_leg_2", ModelPartBuilder.create().uv(0, 71).cuboid(-3f, -2f, 2f, 4f, 14f, 4f, new Dilation(0f)), ModelTransform.pivot(1f, 6f, -3f));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.of(3f, 7f, 0f, -0.175f, 0f, 0f));
		left_leg.addChild("cube_r10", ModelPartBuilder.create().uv(0, 54).mirrored().cuboid(-1f, 1f, 2f, 4f, 8f, 5f, new Dilation(0.1f)).mirrored(false), ModelTransform.of(-1f, -2f, -4f, -0.2618F, 0f, 0f));
		left_leg.addChild("left_leg_2", ModelPartBuilder.create().uv(0, 71).mirrored().cuboid(-1f, -2f, 2f, 4f, 14f, 4f, new Dilation(0f)).mirrored(false), ModelTransform.pivot(-1f, 6f, -3f));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(neck);
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(body, tail, rightArm, leftArm, rightLeg, leftLeg);
	}

	@Override
	public void setAngles(WerewolfBeastEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		neck.yaw = headYaw * 0.017453292f;

		if(leaningPitch > 0f)
			neck.pitch = lerpAngle(leaningPitch, neck.pitch, headPitch * 0.017453292f);
		else
			neck.pitch = headPitch * 0.017453292f;

		body.yaw = 0f;
		neck.pivotY = -12f;
		neck.pivotZ = -4f;
		rightArm.pivotZ = -5f;
		rightArm.pivotX = -4.5f;
		leftArm.pivotZ = -5f;
		leftArm.pivotX = 4.5f;

		mouth.pitch = MathHelper.cos(animationProgress * 10f * 0.01f) * 0.0015f * 8f;

		rightArm.pitch = MathHelper.cos(limbAngle * 0.6662f + MathHelper.PI) * 2f * limbDistance * 0.5f;
		leftArm.pitch = MathHelper.cos(limbAngle * 0.6662f) * 2f * limbDistance * 0.5f;

		rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance - 0.2f;
		leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + MathHelper.PI) * 1.4f * limbDistance - 0.2f;

		rightArm.roll = 0.175f;
		leftArm.roll = -0.175f;
		rightLeg.yaw = 0f;
		leftLeg.yaw = 0f;
		rightLeg.roll = 0f;
		leftLeg.roll = 0f;
		rightArm.yaw = 0f;
		leftArm.yaw = 0f;

		animateArms(entity);

		if(climbing) {
			// TODO climbing anim
		}
		else if(sneaking || hunting) {
			// TODO sneaking
		}
		else {
			body.pitch = 0.35f;
			rightLeg.pivotZ = 0f;
			leftLeg.pivotZ = 0f;
			rightLeg.pivotY = 7f;
			leftLeg.pivotY = 7f;
			body.pivotY = 8.5f;
			body.pivotZ = 0.5f;
			leftArm.pivotY = -7.5f;
			rightArm.pivotY = -7.5f;
		}

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
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		getArm(arm).rotate(matrices);
	}

	@Override
	public ModelPart getHead() {
		return neck;
	}

	@Override
	public void animateModel(WerewolfBeastEntity entity, float limbAngle, float limbDistance, float tickDelta) {
		leaningPitch = entity.getLeaningPitch(tickDelta);
		this.tickDelta = tickDelta;
		super.animateModel(entity, limbAngle, limbDistance, tickDelta);
	}

	protected void animateArms(WerewolfBeastEntity entity) {
		if(!(handSwingProgress <= 0f)) {
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

	private Arm getPreferredArm(WerewolfBeastEntity entity) {
		Arm arm = entity.getMainArm();

		return entity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
	}
}