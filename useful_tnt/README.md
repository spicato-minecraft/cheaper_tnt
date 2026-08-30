# Useful TNT

A Minecraft Fabric mod that makes TNT cheaper to craft, keeps dropped items alive through TNT explosions, and lets you drop a lit TNT at your feet while moving.

This release is a rename of **cheaper_tnt**. Remove the old JAR. Existing `config/cheaper_tnt.json` is copied to `config/useful_tnt.json` on first load.

Vanilla Java already drops blocks from a single TNT blast. This mod stops a later TNT blast from **destroying those item entities**, so chained quarries keep their loot. It does **not** change mob damage, knockback, or how chests and other block entities break.

## Recipe

When enabled, TNT is crafted with **2 gunpowder** and **2 sand** in a criss-cross pattern instead of the vanilla 5 gunpowder + 4 sand:

```
  G
S   S
  G
```

(G = gunpowder, S = sand)

## Configuration

Config file: `config/useful_tnt.json` (migrates from `config/cheaper_tnt.json` if that is the only file present)

| Option | Description | Default |
|--------|--------------|---------|
| `cheaperTntEnabled` | Enable or disable the cheaper TNT recipe | `true` |
| `dropProtectionEnabled` | TNT explosions do not destroy item entities (Primed TNT and TNT minecarts only) | `true` |
| `trailDropEnabled` | Air-use trail-drop with TNT + offhand igniter | `true` |
| `trailDropFuseTicks` | Fuse length for trail-dropped TNT (vanilla placed TNT is 80) | `100` |

Changing the recipe toggle requires restarting the game or using `/reload`. Drop protection is read at explosion time. Trail-drop settings are read when you use the item (restart after editing them).

Creeper, bed, respawn-anchor, and wither explosions still destroy items.

## Trail-drop

With **TNT in the main hand** and **flint and steel or a fire charge in the off hand**, right-click **empty air** (look at the sky, not a block or mob). A primed TNT appears at your feet with a 5-second fuse (100 ticks). Survival spends one TNT and uses the igniter as if you had lit a placed TNT block. Creative spends nothing. Adventure still works.

Holding right-click does not dump a stack — one bomb per click. Right-clicking a block still **places** unlit TNT as vanilla. This is not a throw; the TNT does not spawn behind you or from swapped hands (igniter in the main hand).

Trail-drop is read when you use the item. Changing `trailDropEnabled` or `trailDropFuseTicks` requires restarting the game.

## Compatibility

- **Minecraft:** 1.21.10
- **Fabric Loader:** ≥ 0.18.4
- **Fabric API:** Required
- **Java:** ≥ 21

## Build

```bash
./gradlew build
```

The built JAR will be in `build/libs/`.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.10
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Copy the built JAR from `build/libs/` to your Minecraft `mods` folder

## Branch Policy

Each supported Minecraft version has its own long-lived branch named after the exact `minecraft_version` in `gradle.properties` (e.g. `1.21.10`). Version branches are the source of truth; `main` is not used.

- **Default branch:** latest supported version (`1.21.10`)
- **Feature/fix work:** branch from the target version as `{version}/feature-name`, merge back into the version branch
- **New MC version:** create `{version}` from the prior version branch, bump `gradle.properties`, push to origin
- **Retiring a version:** tag the final release, then archive the branch (do not delete)

```bash
git clone https://github.com/spicato-spicato/cheaper_tnt.git
cd cheaper_tnt
git checkout 1.21.10
```

The Gradle project lives in the `useful_tnt/` subdirectory. GitHub / Modrinth repo rename to `useful_tnt` is deferred to the 2.0.0 ship.

## Releases and Modrinth

Merging code does **not** publish to Modrinth. Publishing happens only when a GitHub Release is created.

### Version tags

Release tags use `{minecraft_version}-{mod_version}` (e.g. `1.21.10-1.0.0`), matching `gradle.properties`.

### How to publish

1. Merge your changes into the version branch (e.g. `1.21.10`).
2. Bump `mod_version` in `gradle.properties` if shipping new player-facing changes.
3. Add a changelog at `releases/{tag}.md` — user-facing bullets in plain language (see `releases/TEMPLATE.md`).
4. Create a GitHub Release with tag `{minecraft_version}-{mod_version}` targeting the version branch.
5. CI runs `publish.yml`: validates tag + changelog, runs `./gradlew build`, uploads to Modrinth.

```bash
gh release create 1.21.10-1.0.0 \
  --target 1.21.10 \
  --title "1.21.10-1.0.0" \
  --notes-file releases/1.21.10-1.0.0.md
```

Infra-only merges (tests, CI, docs) never need a release unless you intend to ship a new mod version.
