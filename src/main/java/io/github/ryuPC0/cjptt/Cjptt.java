package io.github.ryuPC0.cjptt;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsBlock;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsInteractionBehaviour;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsMovementBehaviour;
import io.github.ryuPC0.cjptt.schedule.CjpttSchedule;
import io.github.ryuPC0.cjptt.speedSign.simple.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("removal")
@Mod(Cjptt.MODID)
public class Cjptt {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cjptt";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "cjptt" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "cjptt" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "cjptt" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(MODID));

    public static final EdgePointType<SpeedSign> SPEEDSIGN = EdgePointType.register(new ResourceLocation(MODID,"speedsign"),SpeedSign::new);
    // Creates a new Block with the id "cjptt:example_block", combining the namespace and path
    public static final BlockEntry<SpeedSignBlock> SPEEDSIGN_BLOCK = REGISTRATE.get().block("simplespeedsign",SpeedSignBlock::new).item(SpeedSignBlockItem.ofType(SPEEDSIGN)).transform(customItemModel()).register();

    public static final BlockEntityEntry<SpeedSignBlockEntity> SPEEDSIGN_BLOCKENTITY = REGISTRATE.get().blockEntity("simplespeedsign",SpeedSignBlockEntity::new)/*.visual(() -> SpeedSignBlockRenderer::new)*/.renderer(() -> SpeedSignBlockRenderer::new).validBlock(SPEEDSIGN_BLOCK).register();

    public static final BlockEntry<ExtendedControlsBlock> EXTENDED_TRAIN_CONTROLS = REGISTRATE.get().block("extendedcontrols", ExtendedControlsBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)
                    .sound(SoundType.NETHERITE_BLOCK))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.get(),
                    s -> AssetLookup.partialBaseModel(c, p,
                            s.getValue(ExtendedControlsBlock.VIRTUAL) ? "virtual" : s.getValue(ExtendedControlsBlock.OPEN) ? "open" : "closed")))
            .onRegister(movementBehaviour(new ExtendedControlsMovementBehaviour()))
            .onRegister(interactionBehaviour(new ExtendedControlsInteractionBehaviour()))
            .lang("Extended Train Controls")
            .item()
            .transform(customItemModel())
            .register();

    // Creates a new BlockItem with the id "cjptt:example_block", combining the namespace and path
    //public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "cjptt:example_id", nutrition 1 and saturation 2
    //public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(1).saturationMod(2f).build())));

    //Creates a creative tab with the id "cjptt:example_tab" for the example item, that is placed after the combat tabs
    public static final RegistryObject<CreativeModeTab> CJPTTTAB = CREATIVE_MODE_TABS.register("createjapantraintweaks", () -> CreativeModeTab.builder().displayItems((parameters, output) -> {
        output.accept(SPEEDSIGN_BLOCK.get()); output.accept(EXTENDED_TRAIN_CONTROLS);// Add the example item to the tab. For your own tabs, this method is preferred over the event
    }).build());
    @SuppressWarnings({"removal"})
    public Cjptt() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        CjpttSchedule.register();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        //LOGGER.info("HELLO FROM COMMON SETUP");
        //LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(MODID, name);
    }
}
