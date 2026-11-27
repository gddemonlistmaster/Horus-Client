package com.horus.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/** A clean, responsive visual dashboard for the Horus client mod. */
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
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, BACKDROP);
        drawBackdrop(context);

        panelWidth = Math.min(540, Math.max(300, width - 32));
        panelHeight = Math.min(390, Math.max(330, height - 32));
        panelX = (width - panelWidth) / 2;
        panelY = (height - panelHeight) / 2;

        context.fill(panelX + 4, panelY + 6, panelX + panelWidth + 4, panelY + panelHeight + 6, 0x70000000);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, PANEL);
        context.fill(panelX, panelY, panelX + panelWidth, panelY + 2, accent());

        drawHeader(context);
        drawPreview(context);
        drawOptions(context, mouseX, mouseY);
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
        int x = panelX + 24;
        int y = panelY + 21;
        context.fill(x, y + 2, x + 34, y + 36, 0xFF252342);
        context.fill(x, y + 2, x + 3, y + 36, accent());
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("H"), x + 18, y + 12, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("HORUS"), x + 46, y + 2, WHITE);
        context.drawTextWithShadow(textRenderer, Text.literal("VISUAL CONTROL CENTER"), x + 46, y + 17, MUTED);
        context.drawTextWithShadow(textRenderer, Text.literal("1.21.11  •  CLIENT"), panelX + panelWidth - 112, y + 12, MUTED);
        context.fill(panelX + 20, panelY + 68, panelX + panelWidth - 20, panelY + 69, 0xFF283247);
    }

    private void drawPreview(DrawContext context) {
        int x = panelX + 22;
        int y = panelY + 83;
        int w = panelWidth - 44;
        context.fill(x, y, x + w, y + 105, PANEL_LIGHT);
        context.fill(x, y, x + 3, y + 105, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("LIVE HUD PREVIEW"), x + 14, y + 11, MUTED);
        context.fill(x + 14, y + 31, x + w - 14, y + 86, 0xD9141723);
        context.fill(x + 14, y + 31, x + 17, y + 86, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("HORUS  /  VISUALS"), x + 26, y + 40, accent());
        context.drawTextWithShadow(textRenderer, Text.literal("A calm view of your world"), x + 26, y + 55, 0xFFE1E5F0);
        if (statusIndicatorsEnabled) {
            context.drawTextWithShadow(textRenderer, Text.literal("PROFILE  •  AURORA"), x + 26, y + 70, MUTED);
        }
        context.fill(x + w - 30, y + 40, x + w - 21, y + 49, hudEnabled ? TEAL : 0xFF657083);
    }

    private void drawOptions(DrawContext context, int mouseX, int mouseY) {
        int x = panelX + 22;
        int y = panelY + 202;
        int w = panelWidth - 44;
        context.drawTextWithShadow(textRenderer, Text.literal("PERSONALIZE"), x + 2, y, WHITE);
        drawOption(context, x, y + 23, w, "Aurora HUD", "Show the floating visual panel", hudEnabled, mouseX, mouseY);
        drawOption(context, x, y + 67, w, "Accent color", accentEnabled ? "Violet / teal · enabled" : "Violet / teal · disabled", accentEnabled, mouseX, mouseY);
        drawOption(context, x, y + 111, w, "Profile indicator", "Show the Aurora profile label", statusIndicatorsEnabled, mouseX, mouseY);
    }

    private void drawOption(DrawContext context, int x, int y, int w, String title, String subtitle,
                            boolean enabled, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + 38;
        context.fill(x, y, x + w, y + 38, hovered ? 0xFF202C40 : 0xFF192335);
        context.drawTextWithShadow(textRenderer, Text.literal(title), x + 11, y + 6, WHITE);
        context.drawTextWithShadow(textRenderer, Text.literal(subtitle), x + 11, y + 20, MUTED);
        int toggleX = x + w - 39;
        context.fill(toggleX, y + 10, toggleX + 27, y + 28, enabled ? accent() : 0xFF475267);
        int knobX = enabled ? toggleX + 16 : toggleX + 3;
        context.fill(knobX, y + 13, knobX + 9, y + 25, 0xFFFFFFFF);
    }

    private void drawFooter(DrawContext context) {
        int y = panelY + panelHeight - 31;
        context.fill(panelX + 20, y - 10, panelX + panelWidth - 20, y - 9, 0xFF283247);
        context.drawTextWithShadow(textRenderer, Text.literal("RIGHT SHIFT  ·  OPEN DASHBOARD"), panelX + 22, y, MUTED);
        context.drawTextWithShadow(textRenderer, Text.literal("ESC  ·  CLOSE"), panelX + panelWidth - 91, y, MUTED);
    }

    private int accent() {
        return accentEnabled ? VIOLET : TEAL;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int x = panelX + 22;
            int w = panelWidth - 44;
            int y = panelY + 225;
            if (mouseX >= x && mouseX <= x + w) {
                if (mouseY >= y && mouseY <= y + 38) {
                    hudEnabled = !hudEnabled;
                    return true;
                }
                if (mouseY >= y + 44 && mouseY <= y + 82) {
                    accentEnabled = !accentEnabled;
                    return true;
                }
                if (mouseY >= y + 88 && mouseY <= y + 126) {
                    statusIndicatorsEnabled = !statusIndicatorsEnabled;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
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
