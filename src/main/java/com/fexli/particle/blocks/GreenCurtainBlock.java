package com.fexli.particle.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
public class GreenCurtainBlock extends Block {

    public GreenCurtainBlock() {
        super(FabricBlockSettings.of(Material.WOOL).breakByHand(true).sounds(BlockSoundGroup.WOOL).lightLevel(12).strength(2, 0.6F).build());

    }
}
