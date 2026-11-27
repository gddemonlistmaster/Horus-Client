package com.horus.client.mixin;

import com.horus.client.HorusScreen;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class SkyColorMixin {
    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void horus$applyCustomSky(CallbackInfoReturnable<Vec3d> cir) {
        cir.setReturnValue(HorusScreen.applySkyPalette(cir.getReturnValue()));
    }
}
