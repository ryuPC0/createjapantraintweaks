package io.github.ryuPC0.cjptt.speedSign.advanced.limitrule;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlock;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3d;

import static io.github.ryuPC0.cjptt.Cjptt.LOGGER;

public class Limitrem extends AbstractLimitRule{

    @Override
    public float GetSpeed() {
        return -1;
    }

    @Override
    public void RenderSign(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, Vector2i posa, Vector2i posb, int light, int overlay, ResourceLocation resrifimage) {
        ms.pushPose();
        int dir = be.getBlockState().getValue(AdvancedSpeedSignBlock.ROTATION);
        double degree = Getdegree(dir);
        Vector3d pos = GetDrawOffSet(be,degree,dir > 15);
        ms.translate(pos.x,pos.y,pos.z);
        ms.mulPose(Axis.YP.rotationDegrees((float)degree));
        ms.scale(1f /16,1f/16,1f/16);
        VertexConsumer vc = buffer.getBuffer(
                RenderType.entityTranslucent(resrifimage));
        Matrix4f m = ms.last().pose();
        int x1,x2,y1,y2;
        x1 = posa.x;
        x2 = posb.x;
        y1 = posa.y;
        y2 = posb.y;
        vc.vertex(m, x1, y1, 0).color(1f,1f,1f,1f).uv(0,0).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        vc.vertex(m, x2, y1, 0).color(1f,1f,1f,1f).uv(1,0).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        vc.vertex(m, x2, y2, 0).color(1f,1f,1f,1f).uv(1,1).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        vc.vertex(m, x1, y2, 0).color(1f,1f,1f,1f).uv(0,1).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        ms.popPose();
    }

    @Override
    public void read(CompoundTag nbt) {     }

    @Override
    public void read(FriendlyByteBuf buffer) {  }

    @Override
    public CompoundTag write() {
        return super.write();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
    }
}
