package io.github.ryuPC0.cjptt.speedSign.advanced.limitrule;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;

public class SimpleLimit extends AbstractLimitRule{
    float speed;
    @Override
    public float GetSpeed() {
        return 0;
    }

    @Override
    public void RenderSign(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, Vector2i posa, Vector2i posb, int light, int overlay, ResourceLocation resrifimage) {

    }

    @Override
    public void read(CompoundTag nbt) {
        speed = nbt.getFloat("speed");
    }

    @Override
    public void read(FriendlyByteBuf buffer) {
        speed = buffer.readFloat();
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = super.write();
        tag.putFloat("speed",speed);
        return tag;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        buffer.writeFloat(0);
    }
}
