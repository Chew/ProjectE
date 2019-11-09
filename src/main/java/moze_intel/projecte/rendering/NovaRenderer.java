package moze_intel.projecte.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

//Only used on the client
public class NovaRenderer<T extends TNTEntity> extends EntityRenderer<T> {

	private final Supplier<BlockState> stateSupplier;

	public NovaRenderer(EntityRendererManager manager, Supplier<BlockState> stateSupplier) {
		super(manager);
		this.stateSupplier = stateSupplier;
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x, (float) y + 0.5F, (float) z);
		float f2;

		if ((float) entity.getFuse() - partialTicks + 1.0F < 10.0F) {
			f2 = 1.0F - ((float) entity.getFuse() - partialTicks + 1.0F) / 10.0F;
			f2 = MathHelper.clamp(f2, 0.0F, 1.0F);
			f2 *= f2;
			f2 *= f2;
			float f3 = 1.0F + f2 * 0.3F;
			GlStateManager.scalef(f3, f3, f3);
		}

		f2 = (1.0F - ((float) entity.getFuse() - partialTicks + 1.0F) / 100.0F) * 0.8F;
		this.bindEntityTexture(entity);
		GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
		blockrendererdispatcher.renderBlockBrightness(stateSupplier.get(), entity.getBrightness());
		GlStateManager.translatef(0.0F, 0.0F, 1.0F);

		if (entity.getFuse() / 5 % 2 == 0) {
			GlStateManager.disableTexture();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 772);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, f2);
			GlStateManager.polygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			blockrendererdispatcher.renderBlockBrightness(stateSupplier.get(), 1.0F);
			GlStateManager.polygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture();
		}

		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull T entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}