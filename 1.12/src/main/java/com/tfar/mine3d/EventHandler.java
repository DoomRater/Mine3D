package com.tfar.mine3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
private Minecraft mc = Minecraft.getMinecraft();
  public static final ResourceLocation HOTBAR = new ResourceLocation(Mine3D.MODID, "textures/gui/hotbar.png");
  public static final ResourceLocation WIDGETS = new ResourceLocation("minecraft", "textures/gui/widgets.png");
  public static final int i1 = 66;
  public static int increment = 0;

  private FontRenderer fontRenderer = mc.fontRenderer;

  @SubscribeEvent//(priority = EventPriority.LOW)
  public void render(RenderGameOverlayEvent.Pre event) {
    RenderGameOverlayEvent.ElementType type = event.getType();
    Entity renderViewEntity = mc.getRenderViewEntity();
    if (!(renderViewEntity instanceof EntityPlayer))return;
    EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
    int scaledWidth = event.getResolution().getScaledWidth();
    int scaledHeight = event.getResolution().getScaledHeight();
    float xStart = scaledWidth / 2f - 130;
    float yStart = scaledHeight - 64;
    switch (type){
      case ARMOR:{
        event.setCanceled(true);
        break;
      }
      case HEALTH:{
        event.setCanceled(true);
        break;
      }
      case HOTBAR: {
        event.setCanceled(true);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(HOTBAR);
        drawTexturedModalRect(xStart + 99, yStart + 3, 99, 67, 59, 58);
        //draw player
       // GL11.glEnable(GL11.GL_SCISSOR_TEST);
      //  GL11.glScissor(xStart/2+80,yStart/2,800,2000);
        increment = (int)(Minecraft.getSystemTime()/1500)%4-1;
        if (increment == 2)increment=0;
        GuiStuff.drawEntityOnScreen(xStart + 128, yStart + 130, 60, increment * 10, mc.player);
      //  GL11.glDisable(GL11.GL_SCISSOR_TEST);

        //draw background
        mc.getTextureManager().bindTexture(HOTBAR);
        GlStateManager.disableDepth();
        drawTexturedModalRect(xStart, yStart, 0, 0, 256, 64);

        drawTexturedModalRect(xStart+165,yStart+17,4,134,26,24);
        GlStateManager.enableDepth();
        int slot = mc.player.inventory.currentItem;
        //draw widget
        mc.getTextureManager().bindTexture(WIDGETS);
        float xSlot = xStart + 20 * (slot % 3);
        float ySlot = yStart + 20 * (slot / 3);
        drawTexturedModalRect(xSlot + 193, ySlot + 1, 0, 22, 24, 24);
        drawItems();

        double armor = player.getTotalArmorValue();
        double armprPercentage = armor * 5;
        int croppedArmor = (int)Math.floor(armprPercentage);
        int len1 = fontRenderer.getStringWidth(croppedArmor+"%");
        drawStringOnHUD(croppedArmor+"%",xStart+45-len1,yStart+38,0xFFFFFF);
        GlStateManager.color(1,1,1);

        double health = player.getHealth();
        double healthPercentage = health * 5;
        int croppedHealth = (int)Math.floor(healthPercentage);
        int len2 = fontRenderer.getStringWidth(croppedHealth+"%");
        drawStringOnHUD(croppedHealth+"%",xStart+45-len2,yStart+5,0xFF0000);
        GlStateManager.color(1,1,1);
        GlStateManager.popMatrix();
        break;
      }
      case FOOD:{
        event.setCanceled(true);
        break;
      }
      case EXPERIENCE:{
        event.setCanceled(true);
        break;
      }
    }
  }
  public static void drawTexturedModalRect(float x, float y, int textureX, int textureY, int width, int height) {
    Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, textureX, textureY, width, height);
  }
  public void drawStringOnHUD(String string, float xOffset, float yOffset, int color) {
    fontRenderer.drawString(string, 2 + xOffset, 2 + yOffset, color, true);
  }
  private void drawItems() {
    // Draw items on hotbar
    GlStateManager.enableRescaleNormal();
    RenderHelper.enableGUIStandardItemLighting();
    for (int i = 0; i < 9; i++) {
      ItemStack item = mc.player.inventory.getStackInSlot(i);
      if (!item.isEmpty()) {
        int itemX = getXForSlot(i);
        int itemY = getYForSlot(i);
        float pickupAnimation = item.getAnimationsToGo() - 1;
        if (pickupAnimation > 0.0F) {
          GlStateManager.pushMatrix();
          float scale = 1 + pickupAnimation / 5;
          GlStateManager.translate(itemX + 8, itemY + 12, 0);
          GlStateManager.scale(1 / scale, scale + 1 / 2f, 1);
          GlStateManager.translate(-(itemX + 8), -(itemY + 12), 0);
        }
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, itemX, itemY);
        if (pickupAnimation > 0.0F)
          GlStateManager.popMatrix();
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, item, itemX, itemY);
      }
    }
    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
  }
  private int[] getHotbarCoords(int slot) {
    Minecraft minecraft = Minecraft.getMinecraft();
    ScaledResolution scaledResolution = new ScaledResolution(minecraft);
    int[] coords = new int[2];
    coords[0] = scaledResolution.getScaledWidth() / 2 - i1 / 2 + 99 + 20 * (slot % 3);
    coords[1] = scaledResolution.getScaledHeight() - i1 + 6 + 20 * (slot / 3);


    return coords;
  }

  private int getXForSlot(int slot) {
    int[] coords = getHotbarCoords(slot);
    return coords[0];
  }

  private int getYForSlot(int slot) {
    int[] coords = getHotbarCoords(slot);
    return coords[1];
  }
}
