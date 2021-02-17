package com.fexli.particle;

import com.fexli.particle.blocks.GreenCurtainBlock;
import com.fexli.particle.commands.ParticleHelpCommand;
import com.fexli.particle.commands.QuickReloadCommand;
import com.fexli.particle.commands.TestCommand;
import com.fexli.particle.future.CustomParticleSettingManager;
import com.fexli.particle.future.pythonCommand;
import com.fexli.particle.future.ExtraParticleCommand;
import com.fexli.particle.future.JyProcessing;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FexMain implements ModInitializer {

    public static final Block GreenCurtain = new GreenCurtainBlock();
    // Register in CommandManagerMixin
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher){
        TestCommand.register(dispatcher);
        ParticleHelpCommand.register(dispatcher);
        pythonCommand.register(dispatcher);
        ExtraParticleCommand.register(dispatcher);
        QuickReloadCommand.register(dispatcher);
    }
    private static void registerBlocks(){
        Registry.register(Registry.BLOCK, new Identifier("fexli", "green_curtain"), GreenCurtain);
        Registry.register(Registry.ITEM, new Identifier("fexli", "green_curtain"), new BlockItem(GreenCurtain, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        System.out.println("Extra Particle Blocks Registered!");
//        JyMain.start(new String[]{"p"});
    }

    @Override
    public void onInitialize() {
        registerBlocks();
        JyProcessing.registerDir();
        CustomParticleSettingManager.register();
    }
}
