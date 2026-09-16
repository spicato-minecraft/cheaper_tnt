# Release changelog template

Copy this file to `releases/{minecraft_version}-{mod_version}.md` before creating a GitHub Release.

Write for players, not developers. List **only what changed since the previous released version** — not what the mod already does.

---

## Example (feature release)

```markdown
- Renamed from cheaper_tnt — remove the old JAR; config migrates from cheaper_tnt.json
- TNT explosions no longer destroy dropped items (chained blasts keep loot)
- TNT still crafts with 2 gunpowder and 2 sand
```

## Example (Minecraft version port, no behavior change)

```markdown
- Minecraft 26.1 support
```

## Example (first Modrinth release)

```markdown
- Initial release for Minecraft 26.1
```

## Guidelines

- Use plain language ("TNT" not "TntBlock recipe registration")
- One bullet per player-visible change **new in this release**
- Do not restate existing mod features from README or player guides
- Omit internal refactors, test additions, and CI changes unless players notice them
- Do not copy PR titles or commit messages
