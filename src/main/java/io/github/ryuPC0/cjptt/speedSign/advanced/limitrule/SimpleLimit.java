package io.github.ryuPC0.cjptt.speedSign.advanced.limitrule;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import org.joml.Vector3d;

public class SimpleLimit extends AbstractLimitRule{
    float speed;
    @Override
    public float GetSpeed() {
        return speed;
    }

    @Override
    public void RenderSign(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, Vector2i posa, Vector2i posb, int light, int overlay, ResourceLocation resrifimage) {
        ms.pushPose();
        double degree = GetDegree(be);
        Vector3d pos = GetDrawOffset(be,degree);
        ms.translate(pos.x,pos.y,pos.z);
        ms.mulPose(Axis.YP.rotationDegrees((float)degree));
        ms.scale(1f /16,1f/16,1f/16);
        Font font = Minecraft.getInstance().font;
        font.drawInBatch(String.valueOf ((int) (speed * 20)),posa.x,posa.y,0,false,ms.last().pose(),buffer,Font.DisplayMode.NORMAL ,0,15728880 );
        ms.popPose();
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
