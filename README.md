# 🛡️ RevisePlugin

A powerful, lightweight player inspection (cheat check) plugin for Minecraft **1.16.5+** (Spigot, Bukkit, Paper). Secure your server from cheaters with an automated and safe freeze system.

---

## ✨ Features

* **Complete Freeze:** Stop suspected players instantly.
* **God Mode:** Invulnerability for both players and staff during inspection.
* **Grief Protection:** Blocks breaking underneath the suspect is strictly forbidden.
* **Private Chat:** Isolated communication channel between the staff and the suspect.

---

## 🛠️ Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/revise <player> start` | Start the cheat check process. | `revise.admin` |
| `/revise <player> stop` | Finish the check and unfreeze player. | `revise.admin` |
| `/cc <message>` | Send a message to the private inspection chat. | `revise.chat` |

---

## ⚙️ Installation

1. **Download** the `.jar` file.
2. **Drop** it into your server's `plugins/` folder.
3. **Restart** or reload your server.
4. **Configure** permissions in your luckperms/permission plugin.

---

## 📋 Configuration (`config.yml`)

```yaml
# Default configuration example
messages:
  prefix: "&7[&4Revise&7] &r"
  start-checking: "&cYou are under review! Follow staff instructions."
  stop-checking: "&aCheck finished. Thank you for cooperation."
  cannot-break: "&cYou cannot break blocks during the inspection!"
  cannot-damage: "&cYou cannot deal or take damage right now!"
```

---

## 💻 Compatibility
* **Core:** Paper, Spigot, Bukkit.
* **Versions:** 1.16.5, 1.17+, 1.18+, 1.19+, 1.20+
