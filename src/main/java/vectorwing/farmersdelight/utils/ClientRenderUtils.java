package vectorwing.farmersdelight.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;

/**
 * Util for helping with rendering elements across the mod, when vanilla methods don't expose enough to use.
 */
public class ClientRenderUtils
{
	// TODO: Decide whether these RenderMaterials and similar methods belong here, or on their own class.
	
	public static final RenderMaterial CANVAS_SIGN_MATERIAL = new RenderMaterial(Atlases.SIGN_ATLAS, new ResourceLocation(FarmersDelight.MODID, "entity/signs/canvas_blank"));

	public static RenderMaterial getSignMaterial(@Nullable DyeColor dyeType) {
		ResourceLocation location = new ResourceLocation(dyeType != null ? dyeType.name() : "blank");
		return new RenderMaterial(Atlases.SIGN_ATLAS, new ResourceLocation(location.getNamespace(), "entity/signs/canvas_" + location.getPath()));
	}

	/**
	 * Renders an Item into the GUI, allowing the size to be defined instead of hardcoded.
	 * This function is ripped right from the game's rendering code. I am probably doing something stupid.
	 */
	public static void renderItemIntoGUIScalable(ItemStack stack, int width, int height, IBakedModel bakedmodel, ItemRenderer renderer, TextureManager textureManager) {
		RenderSystem.pushMatrix();
		textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		Texture texture = textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		if (texture != null) {
			texture.setBlurMipmapDirect(false, false);
		}
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.translatef((float) 0, (float) 0, 100.0F + renderer.zLevel);
		RenderSystem.translatef(8.0F, 8.0F, 0.0F);
		RenderSystem.scalef(1.0F, -1.0F, 1.0F);
		RenderSystem.scalef(48.0F, 48.0F, 48.0F);
		MatrixStack matrixstack = new MatrixStack();
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		boolean flag = !bakedmodel.isSideLit();
		if (flag) {
			RenderHelper.setupGuiFlatDiffuseLighting();
		}

		renderer.renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
		irendertypebuffer$impl.finish();
		RenderSystem.enableDepthTest();
		if (flag) {
			RenderHelper.setupGui3DDiffuseLighting();
		}

		RenderSystem.disableAlphaTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}
}
