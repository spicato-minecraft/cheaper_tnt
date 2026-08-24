package useful_tnt.gametest;

import useful_tnt.UsefulTntConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

final class TntGameTestHelper {

	private TntGameTestHelper() {
	}

	static CraftingInput grid3x3(Item... cells) {
		if (cells.length != 9) {
			throw new IllegalArgumentException("Expected 9 cells");
		}
		List<ItemStack> stacks = new ArrayList<>(9);
		for (Item cell : cells) {
			stacks.add(cell == Items.AIR ? ItemStack.EMPTY : new ItemStack(cell));
		}
		return CraftingInput.of(3, 3, stacks);
	}

	static CraftingInput cheaperTntGrid() {
		return grid3x3(
				Items.AIR, Items.GUNPOWDER, Items.AIR,
				Items.SAND, Items.AIR, Items.SAND,
				Items.AIR, Items.GUNPOWDER, Items.AIR
		);
	}

	static CraftingInput vanillaTntGrid() {
		return grid3x3(
				Items.GUNPOWDER, Items.GUNPOWDER, Items.GUNPOWDER,
				Items.GUNPOWDER, Items.SAND, Items.GUNPOWDER,
				Items.SAND, Items.SAND, Items.SAND
		);
	}

	static ItemStack craft(GameTestHelper context, CraftingInput input) {
		return context.getLevel().getServer().getRecipeManager()
				.getRecipeFor(RecipeType.CRAFTING, input, context.getLevel())
				.map(holder -> assemble(context, holder, input))
				.orElse(ItemStack.EMPTY);
	}

	private static ItemStack assemble(GameTestHelper context, RecipeHolder<CraftingRecipe> holder, CraftingInput input) {
		return holder.value().assemble(input, context.getLevel().registryAccess());
	}

	static void assertThat(GameTestHelper context, boolean value, String message) {
		context.assertTrue(value, Component.literal(message));
	}

	static void assertNot(GameTestHelper context, boolean value, String message) {
		context.assertFalse(value, Component.literal(message));
	}

	static void assertEqual(GameTestHelper context, Object expected, Object actual, String message) {
		context.assertValueEqual(expected, actual, Component.literal(message));
	}

	static void detonatePrimedTnt(GameTestHelper context, BlockPos relative) {
		ServerLevel level = context.getLevel();
		Vec3 pos = context.absoluteVec(Vec3.atCenterOf(relative));
		PrimedTnt tnt = new PrimedTnt(level, pos.x, pos.y, pos.z, null);
		level.addFreshEntity(tnt);
		level.explode(tnt, pos.x, pos.y, pos.z, 4.0F, Level.ExplosionInteraction.TNT);
		tnt.discard();
	}

	static void explodeCreeper(Creeper creeper) {
		ServerLevel level = (ServerLevel) creeper.level();
		level.explode(creeper, creeper.getX(), creeper.getY(), creeper.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
		creeper.discard();
	}

	static void withDropProtection(boolean enabled, Runnable action) {
		synchronized (UsefulTntConfig.class) {
			boolean previous = UsefulTntConfig.isDropProtectionEnabled();
			UsefulTntConfig.setDropProtectionEnabled(enabled);
			try {
				action.run();
			} finally {
				UsefulTntConfig.setDropProtectionEnabled(previous);
			}
		}
	}

	static void withCheaperRecipe(boolean enabled, Runnable action) {
		synchronized (UsefulTntConfig.class) {
			boolean previous = UsefulTntConfig.isEnabled();
			UsefulTntConfig.setCheaperTntEnabled(enabled);
			try {
				action.run();
			} finally {
				UsefulTntConfig.setCheaperTntEnabled(previous);
			}
		}
	}
}
