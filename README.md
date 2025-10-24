# 🚀 SendPlayers v1.1.0 – Neuer Befehl & Verbesserungen

Diese Version ersetzt den alten `/sendall`-Befehl durch den neuen, klareren:

```
/sendplayers <fromServer> <toServer>
```

**Beispiel:**  
`/sendplayers citybuild lobby`

---

## ✨ Änderungen in v1.1.0
- 🔁 Neuer Befehl: `/sendplayers` (statt `/sendall`)
- 🧭 Kürzel hinzugefügt: `/sp`, `/sendp`
- ⚙️ Gleiche Funktionen wie zuvor (Batch, Delay, Skip)
- 💬 Reload-Befehl: `/sendplayers reload`
- 🧠 Bessere interne Struktur & Logging-Verbesserungen
- 🪶 Voll kompatibel mit FlameCord, Waterfall und BungeeCord

---

## ⚙️ Konfiguration (`plugins/SendPlayers/config.yml`)
```yaml
batch-size: 5
delay-ms: 1000
skip-if-already-there: true
```

---

## 💡 Beispiel-Nutzung
Alle Spieler vom Citybuild zur Lobby verschieben:
```
/sendplayers citybuild lobby
```

Serverwartung vorbereiten:
```
/sendplayers oneblock lobby
```

Konfiguration neu laden:
```
/sendplayers reload
```

---

## 🔐 Berechtigungen
| Permission | Beschreibung |
|-------------|---------------|
| `manacity.sendall` | Erlaubt den `/sendplayers`-Befehl |

---

## 🧱 Installation
1. Lade die **SendPlayers-v1.1.0.jar** aus diesem Release herunter  
2. Lege sie in den `plugins/`-Ordner deines **Proxys (FlameCord / BungeeCord / Waterfall)**  
3. Starte den Proxy neu  
4. Fertig ✅

---

## 🧾 Changelog
- 🔄 Neuer Befehl: `/sendplayers <from> <to>`
- 🧠 Reload-Befehl hinzugefügt
- 🕒 Konfigurierbares Intervall & Batchgröße
- 🧹 Optimierter Scheduler (stabiler bei vielen Spielern)
- 💬 Klareres Feedback im Chat & in der Konsole

---

## 🏷️ Release-Informationen
**Tag:** `v1.1.0`  
**Titel:** *SendPlayers v1.1.0 – Neuer Befehl & Verbesserungen*  
**Branch:** `main`  
**Maintainer:** [TabZz147](https://github.com/TabZz147)

---

© 2025 ManaCity.de – Entwickelt mit ❤️ von TabZz147
