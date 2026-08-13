package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import io.github.ryuPC0.cjptt.registry.CjpttPackets;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.LimitRuleType;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;


import java.util.HashMap;
import java.util.Map;

public class AdvancedSpeedSignEditScreen extends AbstractSimiScreen {
    private SelectionScrollInput type;
    private Map<LimitRuleType<?>,Component> typeoptions;
    protected AdvancedSpeedSignBlockEntity be;
    protected AdvancedSpeedSign sign;
    private AbstractLimitRule buffer;
    public AdvancedSpeedSignEditScreen(AdvancedSpeedSignBlockEntity be,AdvancedSpeedSign sign){
        super();
        this.be = be;
        this.sign = sign;
    }
    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void init() {
        super.init();
        typeoptions = new HashMap<>();
        typeoptions.put(LimitRuleType.LIMITREM,Component.translatable("cjptt.Limitrem"));
        typeoptions.put(LimitRuleType.SIMPLELIMIT,Component.literal("通常/分岐機制限"));
        type = new SelectionScrollInput(guiLeft,guiTop,100,14);
        type.withStepFunction( ctx -> type.standardStep()
                .apply(ctx));
        buffer = sign.speed.get(be.front);
        if(buffer == null)
            buffer = LimitRuleType.LIMITREM.create();
        type.calling((c)-> {
            LimitRuleType<?> type = typeoptions.keySet().stream().toList().get(c);
            if(buffer.getType() != type) {
                buffer = type.create();
            }
        });
        type.forOptions(typeoptions.values().stream().toList());
        type.setState(typeoptions.keySet().stream().toList().indexOf(buffer.getType()));
        type.setActive(true).visible = true;
        addRenderableWidget(type);
    }

    @Override
    public void removed() {
        CjpttPackets.getChannel().sendToServer(new AdvancedSpeedSignbePacket(be.getBlockPos(),buffer));
        super.removed();
    }

    @Override
    protected void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font,"制限標識" ,100,100, 0xffffff);
        guiGraphics.drawString(font,typeoptions.values().stream().toList().get(type.getState()),guiLeft,guiTop,0xffffff);
    }
}
