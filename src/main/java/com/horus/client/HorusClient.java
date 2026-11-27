package com.horus.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GameMenuScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class HorusClient implements ClientModInitializer {
    public static final String MOD_ID = "horus";
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of(MOD_ID, "controls"));
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.horus.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new HorusScreen());
                }
            }
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GameMenuScreen) {
                screen.addDrawableChild(net.minecraft.client.gui.widget.ButtonWidget.builder(
                                Text.literal("Horus Visuals"), button -> client.setScreen(new HorusScreen()))
                        .dimensions(scaledWidth - 108, scaledHeight - 30, 100, 20)
                        .build());
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> renderHud(MinecraftClient.getInstance(), drawContext));
    }

    private static void renderHud(MinecraftClient client, net.minecraft.client.gui.DrawContext context) {
        if (client.player == null || client.options.hudHidden || !HorusScreen.isHudEnabled()) {
            return;
        }

        int x = 12;
        int y = 12;
        int width = 154;
        int height = HorusScreen.areStatusIndicatorsEnabled() ? 48 : 34;
        int accent = HorusScreen.isAccentEnabled() ? 0xFFAA8BFF : 0xFF62E0D0;

        context.fill(x, y, x + width, y + height, 0xD9141723);
        context.fill(x, y, x + 3, y + height, accent);
        context.fill(x + 3, y, x + width, y + 1, 0x553FFFFFF);
        context.drawTextWithShadow(client.textRenderer, Text.literal("HORUS  /  VISUALS"), x + 11, y + 7, accent);
        context.drawTextWithShadow(client.textRenderer, Text.literal("A calm view of your world"), x + 11, y + 20, 0xFFE1E5F0);

        if (HorusScreen.areStatusIndicatorsEnabled()) {
            context.drawTextWithShadow(client.textRenderer, Text.literal("PROFILE  •  AURORA"), x + 11, y + 34, 0xFF9BAAC2);
        }
    }
}
