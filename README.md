# 🔁 SendAll – Spieler zwischen Servern verschieben (FlameCord / BungeeCord)

**SendAll** ist ein leichtgewichtiges Plugin für **FlameCord / Waterfall / BungeeCord**,  
mit dem du alle Spieler **von einem Server auf einen anderen** verschieben kannst – perfekt für Wartungen, Neustarts oder Events.

---

## ✨ Features
- 📦 Verschiebt **alle Spieler von Server A → Server B**
- ⚙️ Konfigurierbare Übertragungsrate:
  - `batch-size` – Anzahl Spieler pro Schub
  - `delay-ms` – Zeit zwischen Schüben (ms)
  - `skip-if-already-there` – Spieler überspringen, die schon am Ziel sind
- 💻 Konsolen- und Ingame-Befehl
- 🔐 Rechteverwaltung: `manacity.sendall`
- 🪶 Minimal & ressourcenschonend

---

## 💡 Befehle
| Befehl | Beschreibung |
|--------|---------------|
| `/sendall <vonServer> <zuServer>` | Verschiebt alle Spieler vom Quell- zum Zielserver |
| `/sendall reload` | Lädt die Konfiguration neu |

**Beispiel:**  
/sendall citybuild lobby

---

## ⚙️ Konfiguration (`plugins/SendAll/config.yml`)
batch-size: 5
delay-ms: 1000
skip-if-already-there: true

Nach Änderungen: `/sendall reload`.

---

## 🔐 Permissions
| Permission | Beschreibung |
|-----------|---------------|
| `manacity.sendall` | Erlaubt Spielern, `/sendall` zu nutzen |

*(Konsole darf immer.)*

---

## 🧱 Installation
1. Jar bauen (`mvn clean install`) oder von Releases laden  
2. In `plugins/` deines **FlameCord/Bungee**-Proxys legen  
3. Proxy neu starten  
4. Fertig ✅

---

## 📜 Changelog (v1.0.0)
- ✨ Neues Schema: `/sendall <vonServer> <zuServer>`
- ⚙️ Batch-Verarbeitung + Delay über `config.yml`
- 🔁 `/sendall reload` für Live-Neuladen
- 🪶 Klein, simpel, proxyseitig

---

## 📦 GitHub Release Beschreibung (für „Releases → Draft a new release“)
**Titel:**  
SendAll v1.0.0 – Spieler zwischen Servern verschieben

**Beschreibung:**  
SendAll ist ein leichtgewichtiges Plugin für FlameCord / BungeeCord / Waterfall,  
mit dem du alle Spieler von einem Server auf einen anderen verschieben kannst – ideal für Wartungen, Neustarts oder Events.

### Features
- Verschiebe alle Spieler von `<Quelle>` → `<Ziel>`
- Konfigurierbare Übertragungsrate:
  - `batch-size`: Anzahl Spieler pro Schub  
  - `delay-ms`: Zeit zwischen den Schüben (ms)  
  - `skip-if-already-there`: Spieler am Ziel überspringen  
- Konsolen- und Ingame-Befehl  
- Permission: `manacity.sendall`

### Befehle
/sendall <vonServer> <zuServer>  
/sendall reload  

Beispiel: `/sendall citybuild lobby`

### Config (`plugins/SendAll/config.yml`)
batch-size: 5
delay-ms: 1000
skip-if-already-there: true

### Installation
1. Jar aus dem Release herunterladen  
2. In `plugins/` des Proxys legen  
3. Proxy neu starten  

### Changelog
- Neues Argument-Layout: `/sendall <von> <zu>`  
- Batch + Delay via `config.yml`  
- Reload-Befehl  

---

## 🧹 .gitignore
# Maven
/target/

# Eclipse
.classpath
.project
.settings/

# IntelliJ
.idea/
*.iml

# OS
.DS_Store
Thumbs.db

---

© 2025 ManaCity.de – Entwickelt mit ❤️ von TabZz147
