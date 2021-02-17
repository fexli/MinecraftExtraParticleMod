package com.fexli.particle.mixin;


import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.fexli.particle.FexMain;
@Mixin(CommandManager.class)
public abstract class CommandManagerMixin{
    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>",at = @At("RETURN"))
    private void onRegister(boolean boolean_1, CallbackInfo cb){
        FexMain.registerCommands(this.dispatcher);
    }
}