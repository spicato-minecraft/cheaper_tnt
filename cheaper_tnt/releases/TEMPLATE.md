# Release changelog template

Copy this file to `releases/{minecraft_version}-{mod_version}.md` before creating a GitHub Release.

Write for players, not developers. Focus on what changed in the game.

---

## Example

```markdown
- TNT now crafts with 2 gunpowder and 2 sand instead of the vanilla recipe
- Added config option to toggle the cheaper recipe on or off
```

## Guidelines

- Use plain language ("TNT" not "TntBlock recipe registration")
- One bullet per player-visible change
- Omit internal refactors, test additions, and CI changes unless players notice them
- Do not copy PR titles or commit messages
