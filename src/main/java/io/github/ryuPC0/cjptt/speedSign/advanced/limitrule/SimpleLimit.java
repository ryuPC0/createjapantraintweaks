package io.github.ryuPC0.cjptt.speedSign.advanced.limitrule;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntity;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import org.joml.Vector3d;

import java.util.Collection;
import java.util.List;

public class SimpleLimit extends AbstractLimitRule{
    int speed;
    @Override
    public float GetSpeed() {
        return speed / 3.6f;
    }

    @Override
    public void RenderSign(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, Vector2i posa, Vector2i posb, int light, int overlay, ResourceLocation resrifimage) {
        ms.pushPose();
        double degree = GetDegree(be);
        Vector3d pos = GetDrawOffset(be,degree);
        ms.translate(pos.x,pos.y,pos.z);
        ms.mulPose(Axis.YP.rotationDegrees((float)degree));
        float scale = (-posa.x + posb.x) / 10f * 1.5f;
        ms.scale(-1f /(16*scale),-1f/(16*scale),1f/(16*scale));
        Font font = Minecraft.getInstance().font;
        font.drawInBatch(String.valueOf (speed),posa.x*scale,posa.y*scale,0,false,ms.last().pose(),buffer,Font.DisplayMode.NORMAL ,0,15728880 );
        ms.popPose();
    }

    @Override
    public void RenderAdditionalSettings(Collection<AbstractSimiWidget> wigetlist) {
        Label label = new Label(50,50, Component.literal("")).setActive(true);
        ScrollInput speed = new ScrollInput(50,50,150,50)
                .withShiftStep(5).withRange(5,500)
                .writingTo(label).titled(Component.translatable("cjptt.simplelim.speed"))
                .calling((i) -> {
                    this.speed = i;
                }).setState(this.speed).setActive(true);
        wigetlist.add(speed);
        wigetlist.add(label);
    }

    @Override
    public void read(CompoundTag nbt) {
        speed = nbt.getInt("speed");
    }

    @Override
    public void read(FriendlyByteBuf buffer) {
        speed = buffer.readInt();
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = super.write();
        tag.putInt("speed",speed);
        return tag;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        buffer.writeInt(speed);
    }
}
