package useful_tnt;

/**
 * Loadout + config checks for trail-drop. Hit result and click-not-hold stay in the use hook.
 */
public final class TrailDropEligibility {

	private TrailDropEligibility() {
	}

	public static boolean isEligible(boolean trailDropEnabled, boolean mainHandIsTnt, boolean offhandIsFlintAndSteel, boolean offhandIsFireCharge) {
		if (!trailDropEnabled || !mainHandIsTnt) {
			return false;
		}
		return offhandIsFlintAndSteel || offhandIsFireCharge;
	}
}
