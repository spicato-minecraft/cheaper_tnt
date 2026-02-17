# Cheaper TNT

A Minecraft Fabric mod that makes TNT cheaper to craft.

## Recipe

When enabled, TNT is crafted with **2 gunpowder** and **2 sand** in a criss-cross pattern instead of the vanilla 5 gunpowder + 4 sand:

```
  G
S   S
  G
```

(G = gunpowder, S = sand)

## Configuration

Config file: `config/cheaper_tnt.json`

| Option | Description | Default |
|--------|--------------|---------|
| `cheaperTntEnabled` | Enable or disable the cheaper TNT recipe | `true` |

Changing the config requires restarting the game or using `/reload` for the recipe change to take effect.

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
