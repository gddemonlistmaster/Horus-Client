package com.horus.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

/** A polished, touch-friendly control panel for Horus visual options. */
public final class HorusScreen extends Screen {
    private static boolean hudEnabled = true;
    private static boolean skyEnabled = false;
    private static boolean accentEnabled = true;
    private static boolean statusIndicatorsEnabled = true;
    private static SkyPalette skyPalette = SkyPalette.AURORA;

    private static final int BACKDROP = 0xE9090D18;
    private static final int PANEL = 0xFF111827;
    private static final int PANEL_LIGHT = 0xFF182235;
    private static final int MUTED = 0xFF9BAAC2;
    private static final int WHITE = 0xFFF4F6FC;
    private static final int VIOLET = 0xFFAA8BFF;
    private static final int TEAL = 0xFF62E0D0;

    private int panelX;
    private int panelY;
    private int panelWidth;
    private int panelHeight;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;

    private enum SkyPalette {
        AURORA("Aurora", 0xFF6978C8),
        SUNSET("Sunset", 0xFFE58B78),
        OCEAN("Ocean", 0xFF4F9DB6),
        DREAM("Dream", 0xFFAA78C8);

        private final String label;
        private final int color;

        SkyPalette(String label, int color) {
            this.label = label;
            this.color = color;
        }

        private SkyPalette next() {
            SkyPalette[] values = values();
            return values[(ordinal() + 1) % values.length];
        }
    }

    public HorusScreen() {
        super(Text.literal("Horus Visuals"));
    }

    public static boolean isHudEnabled() {
        return hudEnabled;
    }

    public static boolean isAccentEnabled() {
        return accentEnabled;
    }

    public static boolean areStatusIndicatorsEnabled() {
        return statusIndicatorsEnabled;
    }

    /** Returns the selected client-side sky tint, or the vanilla color if disabled. */
    public static Vec3d applySkyPalette(Vec3d vanillaColor) {
        if (!skyEnabled || vanillaColor == null) {
            return vanillaColor;
        }

        int color = skyPalette.color;
        return new Vec3d(
                ((color >> 16) & 0xFF) / 255.0,
                ((color >> 8) & 0xFF) / 255.0,
                (color & 0xFF) / 255.0
        );
    }

    @Override
    protected void init() {
        panelWidth = Math.max(0, Math.min(560, width - 20));
        panelHeight = Math.max(0, Math.min(460, height - 20));
        panelX = (width - panelWidth) / 2;
        panelY = (height - panelHeight) / 2;
        buttonWidth = Math.max(90, Math.min(224, (panelWidth - 54) / 2));
        buttonHeight = 36;
        buttonY = panelY + Math.min(230, Math.max(178, panelHeight / 2));

        int leftX = panelX + 20;
        int rightX = panelX + panelWidth - buttonWidth - 20;
        int gap = 9;

        addDrawableChild(toggleButton("HUD panel", hudEnabled, leftX, buttonY, () -> hudEnabled = !hudEnabled));
        addDrawableChild(toggleButton("Custom sky", skyEnabled, rightX, buttonY, () -> skyEnabled = !skyEnabled));
        addDrawableChild(ButtonWidget.builder(skyPaletteText(), button -> {
                    skyPalette = skyPalette.next();
                    button.setMessage(skyPaletteText());
                })
                .dimensions(leftX, buttonY + buttonHeight + gap, buttonWidth, buttonHeight)
                .build());
        addDrawableChild(toggleButton("Accent theme", accentEnabled, rightX,
                buttonY + buttonHeight + gap, () -> accentEnabled = !accentEnabled));
        addDrawableChild(toggleButton("Profile label", statusIndicatorsEnabled, leftX,
                buttonY + (buttonHeight + gap) * 2, () -> statusIndicatorsEnabled = !statusIndicatorsEnabled));
    }

    private ButtonWidget toggleButton(String label, boolean enabled, int x, int y, Runnable toggle) {
        ButtonWidget button = ButtonWidget.builder(toggleText(label, enabled), clicked -> {
                    toggle.run();
                    clicked.setMessage(toggleText(label, !enabled));
                })
                .dimensions(x, y, buttonWidth, buttonHeight)
                .build();
        return button;
    }

    private static Text toggleText(String label, boolean enabled) {
        return Text.literal(label + "  " + (enabled ? "ON" : "OFF"));
    }

    private static Text skyPaletteText() {
        return Text.literal("Sky style  ·  " + skyPalette.label);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, BACKDROP);
        drawBackdrop(context);

        context.fill(panelX + 5, panelY + 7, panelX + panelWidth + 5, panelY + panelHeight + 7, 0x70000000);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, PANEL);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + 3, accent());
        drawHeader(context);
        drawPreview(context);
        drawSectionLabel(context);
        drawFooter(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawBackdrop(DrawContext context) {
        int glowWidth = Math.max(1, width / 3);
        context.fillGradient(width / 2 - glowWidth, 0, width / 2 + glowWidth, height,
                0x263D2D72, 0x00202B46);
        for (int x = 20; x < width; x += 48) {
            for (int y = 18; y < height; y += 48) {
                context.fill(x, y, x + 1, y + 1, 0x334A5A79);
            }
        }
    }

    private void drawHeader(DrawContext context) {
        int x = panelX + 22;
        int y = panelY + 18;
        context.fill(x, y + 2, x + 38, y + 40, 0xFF252342);
        context.fill(x, y + 2, x + 3, y + 40, accent());
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("H"), x + 20, y + 13, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("HORUS"), x + 50, y + 4, WHITE);
        context.drawTextWithShadow(textRenderer, Text.literal("VISUAL CONTROL CENTER"), x + 50, y + 20, MUTED);
        context.drawTextWithShadow(textRenderer, Text.literal("A softer, more personal view of your world"),
                x, y + 51, MUTED);
        context.fill(panelX + 20, panelY + 82, panelX + panelWidth - 20, panelY + 83, 0xFF283247);
    }

    private void drawPreview(DrawContext context) {
        int x = panelX + 20;
        int y = panelY + 96;
        int w = Math.max(0, panelWidth - 40);
        int h = Math.max(54, Math.min(72, buttonY - y - 45));
        context.fill(x, y, x + w, y + h, PANEL_LIGHT);
        context.fill(x, y, x + 3, y + h, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("LIVE PREVIEW"), x + 12, y + 8, MUTED);
        int sky = skyPalette.color;
        context.fill(x + 12, y + 25, x + w - 12, y + h - 8, skyEnabled ? sky : 0xFF26364B);
        context.fill(x + 12, y + h - 19, x + w - 12, y + h - 8, 0xFF182235);
        context.drawTextWithShadow(textRenderer, Text.literal("HORUS  /  " + skyPalette.label.toUpperCase()),
                x + 20, y + 31, WHITE);
    }

    private void drawSectionLabel(DrawContext context) {
        context.drawTextWithShadow(textRenderer, Text.literal("VISUAL MODULES"),
                panelX + 22, buttonY - 19, WHITE);
        context.drawTextWithShadow(textRenderer, Text.literal("Customize the look without changing gameplay"),
                panelX + 22, buttonY - 7, MUTED);
    }

    private void drawFooter(DrawContext context) {
        int y = panelY + panelHeight - 22;
        context.fill(panelX + 20, y - 8, panelX + panelWidth - 20, y - 7, 0xFF283247);
        context.drawTextWithShadow(textRenderer, Text.literal("RIGHT SHIFT  ·  ESC TO CLOSE"),
                panelX + 22, y, MUTED);
    }

    private int accent() {
        return accentEnabled ? VIOLET : TEAL;
    }
}
