package com.fexli.particle.mixin;


import com.fexli.particle.future.CustomParticleSettingManager;
import com.fexli.particle.future.JyProcessing;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerReloadMixin {
    @Inject(at = @At("RETURN"), method = "reload")
    private void reloadCustomParticle(CallbackInfo cb){
        CustomParticleSettingManager.reload();
        JyProcessing.loadDirs();
    }

}
