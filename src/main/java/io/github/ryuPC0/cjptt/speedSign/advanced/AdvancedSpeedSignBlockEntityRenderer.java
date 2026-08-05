package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3d;

import static io.github.ryuPC0.cjptt.Cjptt.LOGGER;

public class AdvancedSpeedSignBlockEntityRenderer extends SafeBlockEntityRenderer<AdvancedSpeedSignBlockEntity> {
    public AdvancedSpeedSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,int overlay) {
        if(be.getedgePoint() == null) {
            return;
        }
        DrawRest(be,partialTicks,ms,buffer,light,overlay);
        DrawTrack(be,partialTicks,ms,buffer, light,overlay);
    }
    protected void DrawRest(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,int overlay){
        AdvancedSpeedSign edgepoint = be.getedgePoint();
        if(edgepoint == null) return;
        boolean front = be.front;
        AbstractLimitRule lim = edgepoint.speed.get(front);
        if(lim != null)
            lim.RenderSign(be,partialTicks,ms,buffer,new Vector2i(-5,-4),new Vector2i(5,4),light,overlay,ResourceLocation.fromNamespaceAndPath("cjptt","textures/block/resrif.png"));
        //DrawResrif(be,partialTicks,ms,buffer,light,overlay,ResourceLocation.fromNamespaceAndPath("cjptt","textures/block/resrif.png"));
    }
    protected void DrawResrif(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,int overlay,ResourceLocation resrifimage){
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
        x1 = -5;
        x2 = 5;
        y1 = -4;
        y2 = 4;
        vc.vertex(m, x1, y1, 0).color(1f,1f,1f,1f).uv(0,0).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        vc.vertex(m, x2, y1, 0).color(1f,1f,1f,1f).uv(1,0).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        vc.vertex(m, x2, y2, 0).color(1f,1f,1f,1f).uv(1,1).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        vc.vertex(m, x1, y2, 0).color(1f,1f,1f,1f).uv(0,1).overlayCoords(overlay).uv2(15728880).normal(0,0,1).endVertex();
        ms.popPose();
    }
    protected Vector3d GetDrawOffSet(AdvancedSpeedSignBlockEntity be,double degree,boolean mode){
        double radian = Math.toRadians(-(degree+90));
        Vector3d pos = new Vector3d(0.5,0.5,0.5);
        Vector3d pos2 = new Vector3d(Math.cos(radian),0,Math.sin(radian)).mul(mode ? -7.0/16 + 0.005:1.0/16 + 0.005);
        return pos.add(pos2);
    }
    protected Double Getdegree(int dir){
        return -(dir > 15 ? (dir - 16) * 90 : (dir * 22.5));
    }

    protected void DrawTrack(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,int overlay){

    }
}
