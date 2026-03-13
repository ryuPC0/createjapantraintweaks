package io.github.ryuPC0.cjptt.speedSign;

import com.mojang.blaze3d.vertex.PoseStack;
import com.railwayteam.railways.util.CustomTrackOverlayRendering;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour.RenderedTrackOverlayType;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpeedSignBlockRenderer extends SmartBlockEntityRenderer<SpeedSignBlockEntity> {
    public SpeedSignBlockRenderer(Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(SpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        /*
        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        BlockPos pos = be.getBlockPos();

        TrackTargetingBehaviour<SpeedSign> target = be.edgePoint;
        BlockPos targetPosition = target.getGlobalPosition();
        Level level = be.getLevel();
        BlockState trackState = level.getBlockState(targetPosition);
        Block block = trackState.getBlock();

        if (!(block instanceof ITrackBlock))
            return;

        ms.pushPose();
        TransformStack.of(ms)
                .translate(targetPosition.subtract(pos));
        RenderedTrackOverlayType type = RenderedTrackOverlayType.SIGNAL;
        TrackTargetingBehaviour.render(level, targetPosition, target.getTargetDirection(), target.getTargetBezier(), ms,
                buffer, light, overlay, type, 1);

        ms.popPose();
        */
        TrackTargetingBehaviour<SpeedSign> target = be.edgePoint;
        BlockPos pos = be.getBlockPos();
        boolean offsetToSide = CustomTrackOverlayRendering.overlayWillOverlap(target);

        BlockPos targetPosition = target.getGlobalPosition();
        Level level = be.getLevel();
        BlockState trackState = level.getBlockState(targetPosition);
        Block block = trackState.getBlock();

        if (!(block instanceof ITrackBlock))
            return;

        ms.pushPose();
        TransformStack.of(ms)
                .translate(targetPosition.subtract(pos));
        CustomTrackOverlayRendering.renderOverlay(level, targetPosition, target.getTargetDirection(), target.getTargetBezier(), ms,
                buffer, light, overlay, AllPartialModels.TRACK_SIGNAL_OVERLAY, 1, offsetToSide);
        ms.popPose();
    }
}
