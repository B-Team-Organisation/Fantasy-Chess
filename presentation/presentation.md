---
marp: true
title: Fantasy-Chess
header: Fantasy-Chess
footer: B-Team

---

# Fantasy-Chess

A PvP multiplayer browser game.

> Marc Matija, Jacinto Schwarzwälder, Lukas Walker, Albano, Dania, Adnan Bennis

---

# Begrüßung

---

# Agenda (WIP)
- Projektorganisation
- Generelles Spiel
- Technische Umsetzung
- Qualität
- Doku
- (Zukunft)
<!-- 1. Begrüßung `Marc`
2. Pitch <code>Marc</code>
3. Projektorganisation <code>Jacinto</code>
3.1 Scrum + Sprintstruktur <code>Jacinto</code>
3.2 Technologien <code>Lukas</code> 
4. "Spiel an sich"
4.a Spielablauf <code>Adnan</code> 
4.b Hauptmenu <code>Albano</code> 
4.c Spielzyklus <code>Lukas</code> 
4.d Patterns <code>Lukas</code> 
4.e Interaktion mit dem Spiel (Mausumrechnung) <code>Jacinto</code> 
4.f Turn Logic + Validation <code>Albano</code> + <code>Jacinto</code> 
4.w Character Integration <code>daniiaaa</code> 
4.x Design (bzw. Map Design) <code>daniiaaa</code>
4.y Server <code>Marc</code> 
4.4 Kommunikation Client <-> Server <code>Marc</code> 
5. Qualität <code>Albano</code>
5.1 Wie wurde getestet <code>Albano</code> 
5.2 Was haben wir getestet <code>Albano</code> 
6. Doku <code>Adnan</code>
6.1 Doku zeigen <code>Adnan</code> 
6.2 Spielanleitung <code>Adnan</code> 
Z. Zukunft <code>Wer noch was braucht</code>
-->

---

# Introduction

---

# Projektorganisation

---

# Scrum

---

# Spiel

---
<!-- _footer: "B-Team: Albano "-->
## 4.1 Hauptmenü
<style scoped>
  img.center {
    float: center;
    margin-left : 230px;
    
  }
</style>
<img src="assets/img/MainMenu/MainMenu_.png" alt="Main Menu" class="center" width ="700" />

--- 
<!-- _footer: "B-Team: Albano "-->
<style scoped>
  img.right {
    float: right;
    margin-left: 20px; 
    margin-top: 0px;
    max-width: 60%; 
  }
</style>
<img src="assets/img/MainMenu/MainMenu_CreateLobby.png" alt="Main Menu Create Lobby" class="right" />

### Funktionen
- Lobby-Suche
- Datenaktualisierung
- Lobby-Erstellung

### Vorteile des Hauptmenüs
- Benutzerfreundlichkeit
- Flexibilität
- Effizienz
---

# Technologie

---
<!-- _footer: "B-Team: Albano "-->
# 5. Qualität

 **Ziele:**
  - Fehlerfreiheit und robuste Funktionen.
  - Intuitive und reibungslose Spielerfahrung.
  - Erweiterbarkeit des Codes für zukünftige Features.
  - Kompatibilität auf allen Geräten.

---
<!-- _footer: "B-Team: Albano "-->
## 5.1 Wie wurde getestet (Teil 1)

### Common Module

- **Automatisierte Tests:** Getestet mit **JUnit** 
- **Ansatz:**
  - **Unit Tests:** Methoden lieferten korrekte Ergebnisse.
  - **Edge Cases:** Null-Werte und Extreme getestet.

---
<!-- _footer: "B-Team: Albano "-->
## 5.1 Wie wurde getestet (Teil 2)

### Server Module

- **Automatisierte Tests:** Backend-Logik und WebSocket-Kommunikation mit **JUnit** 
- **Ansatz:**
  - WebSocket-Verbindungen und Nachrichten validiert.
  - REST-Endpunkte getestet.


---

<!-- _footer: "B-Team: Albano "-->

## 5.1 Wie wurde getestet (Teil 3)

### Client Module

- **Manual Testing:**  
  Aufgrund von **GWT**-Einschränkungen wurden manuelle Tests verwendet.
- **Ansatz:**
  - **Funktionstests:** UI-Reaktionen und Gameplay-Funktionen wurden geprüft.
  - **Szenarien:** Spielzustände (Anfang, Mitte, Ende) wurden simuliert.
 
---
<!-- _footer: "B-Team: Albano "-->
## 5.2 Was haben wir getestet (Teil 1)

### Common Module


- **Models:**  
  Datenintegrität 
- **DTOs:**  
  Korrekte Serialisierung/Deserialisierung 
- **Services:**  
  Spiel-Logik 
- **Utilities:**  
  Hilfsklassen-zuverlässigkeit 

---

<!-- _footer: "B-Team: Albano "-->
## 5.2 Was haben wir getestet (Teil 2)

### Server Module

- **WebSocket Services:**  
  Stabilität von Verbindungen und Nachrichtennutzlasten 
- **Controllers:**  
  REST-Endpunkte validiert.
- **Handlers:**  
  Ereignis- und Paketbearbeitung sichergestellt.
- **Utilities:**  
  Hilfsklassen-zuverlässigkeit 
  

---
<!-- _footer: "B-Team: Albano "-->

## 5.2 Was haben wir getestet (Teil 3)

### Client Module

- **UI Testing:**  
  - HealthBar-Rendering und Updates  
  - Charakterdarstellung und Stat-Overlays 
  - Korrekte CommandPreview and TurnOutcome
- **Scroll-Funktionalität:**  
  Scrollbare UI-Komponenten wie Listen und Dialoge wurden geprüft.

---
# Doku

---

# Zukunft