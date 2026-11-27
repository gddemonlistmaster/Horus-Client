package com.horus.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/** A touch-friendly, responsive dashboard for mobile and desktop clients. */
public final class HorusScreen extends Screen {
    private static boolean hudEnabled = true;
    private static boolean accentEnabled = true;
    private static boolean statusIndicatorsEnabled = true;

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
    private int rowHeight;
    private int rowStartY;
    private int toggleWidth;

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

    @Override
    protected void init() {
        panelWidth = Math.max(0, Math.min(560, width - 24));
        panelHeight = Math.max(0, Math.min(440, height - 24));
        panelX = (width - panelWidth) / 2;
        panelY = (height - panelHeight) / 2;
        rowHeight = Math.max(34, Math.min(48, (panelHeight - 198) / 3));
        rowStartY = panelY + panelHeight - (rowHeight * 3) - 72;
        toggleWidth = Math.min(118, Math.max(74, panelWidth / 3));

        int toggleX = panelX + panelWidth - toggleWidth - 20;
        addDrawableChild(ButtonWidget.builder(toggleText("Aurora HUD", hudEnabled), button -> {
                    hudEnabled = !hudEnabled;
                    button.setMessage(toggleText("Aurora HUD", hudEnabled));
                })
                .dimensions(toggleX, rowStartY, toggleWidth, rowHeight)
                .build());
        addDrawableChild(ButtonWidget.builder(toggleText("Accent", accentEnabled), button -> {
                    accentEnabled = !accentEnabled;
                    button.setMessage(toggleText("Accent", accentEnabled));
                })
                .dimensions(toggleX, rowStartY + rowHeight + 6, toggleWidth, rowHeight)
                .build());
        addDrawableChild(ButtonWidget.builder(toggleText("Profile", statusIndicatorsEnabled), button -> {
                    statusIndicatorsEnabled = !statusIndicatorsEnabled;
                    button.setMessage(toggleText("Profile", statusIndicatorsEnabled));
                })
                .dimensions(toggleX, rowStartY + (rowHeight + 6) * 2, toggleWidth, rowHeight)
                .build());
    }

    private static Text toggleText(String label, boolean enabled) {
        return Text.literal(label + (enabled ? "  ON" : "  OFF"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, BACKDROP);
        drawBackdrop(context);

        context.fill(panelX + 4, panelY + 6, panelX + panelWidth + 4, panelY + panelHeight + 6, 0x70000000);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, PANEL);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + 2, accent());

        drawHeader(context);
        drawPreview(context);
        drawOptions(context);
        drawFooter(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawBackdrop(DrawContext context) {
        int glowWidth = Math.max(1, width / 3);
        context.fillGradient(width / 2 - glowWidth, 0, width / 2 + glowWidth, height,
                0x183D2D72, 0x00202B46);
        for (int x = 20; x < width; x += 48) {
            for (int y = 18; y < height; y += 48) {
                context.fill(x, y, x + 1, y + 1, 0x334A5A79);
            }
        }
    }

    private void drawHeader(DrawContext context) {
        int x = panelX + 20;
        int y = panelY + 18;
        context.fill(x, y + 2, x + 34, y + 36, 0xFF252342);
        context.fill(x, y + 2, x + 3, y + 36, accent());
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("H"), x + 18, y + 12, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("HORUS"), x + 46, y + 2, WHITE);
        context.drawTextWithShadow(textRenderer, Text.literal("VISUAL CONTROL CENTER"), x + 46, y + 17, MUTED);
        context.fill(panelX + 18, panelY + 64, panelX + panelWidth - 18, panelY + 65, 0xFF283247);
    }

    private void drawPreview(DrawContext context) {
        int x = panelX + 18;
        int y = panelY + 76;
        int w = panelWidth - 36;
        int previewHeight = Math.max(62, Math.min(82, rowStartY - y - 54));
        context.fill(x, y, x + w, y + previewHeight, PANEL_LIGHT);
        context.fill(x, y, x + 3, y + previewHeight, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("LIVE HUD PREVIEW"), x + 12, y + 8, MUTED);
        context.fill(x + 12, y + 25, x + w - 12, y + previewHeight - 8, 0xD9141723);
        context.fill(x + 12, y + 25, x + 15, y + previewHeight - 8, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("HORUS  /  VISUALS"), x + 23, y + 30, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("A calm view of your world"), x + 23, y + 44, 0xFFE1E5F0);
    }

    private void drawOptions(DrawContext context) {
        int labelX = panelX + 22;
        context.drawTextWithShadow(textRenderer, Text.literal("PERSONALIZE"), labelX, rowStartY - 19, WHITE);
        drawOptionLabel(context, labelX, rowStartY, "Aurora HUD", "Floating visual panel");
        drawOptionLabel(context, labelX, rowStartY + rowHeight + 6, "Accent color", "Violet / teal theme");
        drawOptionLabel(context, labelX, rowStartY + (rowHeight + 6) * 2, "Profile indicator", "Aurora profile label");
    }

    private void drawOptionLabel(DrawContext context, int x, int y, String title, String subtitle) {
        int availableWidth = Math.max(40, panelWidth - toggleWidth - 64);
        context.drawTextWithShadow(textRenderer, Text.literal(title), x, y + 6, WHITE);
        if (availableWidth > 100) {
            context.drawTextWithShadow(textRenderer, Text.literal(subtitle), x, y + 21, MUTED);
        }
    }

    private void drawFooter(DrawContext context) {
        int y = panelY + panelHeight - 25;
        context.fill(panelX + 18, y - 8, panelX + panelWidth - 18, y - 7, 0xFF283247);
        context.drawTextWithShadow(textRenderer, Text.literal("TOUCH: OPEN FROM PAUSE MENU"), panelX + 20, y, MUTED);
    }

    private int accent() {
        return accentEnabled ? VIOLET : TEAL;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
