package useful_tnt.clientgametest;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;

/**
 * Loads the client mixin target. The injected method body is empty; a renamed Minecraft class still fails here.
 */
public class ClientMixinApplySweepGameTest implements FabricClientGameTest {

	@Override
	public void runTest(ClientGameTestContext context) {
		context.computeOnClient(client -> {
			try {
				Class.forName("net.minecraft.client.Minecraft");
			} catch (ClassNotFoundException e) {
				throw new AssertionError("Mixin target failed to load: Minecraft", e);
			}
			return null;
		});
	}
}
