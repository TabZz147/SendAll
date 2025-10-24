# 🔁 SendAll – Spieler einfach zwischen Servern verschieben

**SendAll** ist ein leichtgewichtiges **FlameCord / BungeeCord / Waterfall**-Plugin,  
mit dem du **alle Spieler gleichzeitig oder in Batches** auf einen Zielserver verschieben kannst.  
Ideal für Wartungen, Neustarts oder Events, wenn du z. B. alle Spieler in die Lobby schicken möchtest.

---

## ✨ Features

- 📦 Verschiebt **alle Spieler** im Netzwerk auf einen bestimmten Server  
- 🧩 Sendet Spieler **in Batches** (z. B. 5 pro Sekunde) – verhindert Lag oder Timeouts  
- ⚙️ Konfigurierbar per `config.yml`:
  - `batch-size`: Anzahl Spieler pro Schub  
  - `delay-ms`: Wartezeit zwischen den Schüben  
  - `skip-if-already-there`: Spieler überspringen, die schon am Ziel sind  
- 🔁 Befehl auch über Konsole nutzbar  
- 🔐 Ingame-Permission: `manacity.sendall`  
- 🪶 Minimaler Ressourcenverbrauch

---

## 💡 Befehle

| Befehl | Beschreibung |
|--------|---------------|
| `/sendall <server>` | Verschiebt alle Spieler zum angegebenen Server |
| `/sendall reload` | Lädt die `config.yml` neu |

**Beispiel:**  
```bash
/sendall lobby
