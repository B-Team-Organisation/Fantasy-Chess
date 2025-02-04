---
marp: true
title: Fantasy-Chess
header: Fantasy-Chess
footer: B-Team 
---

# Fantasy-Chess

A PvP multiplayer browser game.

> Marc Matija, Jacinto Schwarzwälder, Lukas Walker, Albano Vukelaj, Dania Anwar, Adnan Bennis

---

# Pitch

- 1v1-Taktikkämpfe auf einem 9×9-Board
- Bluffen & Vorausdenken
- Weniger Wartezeiten, mehr Action
- Vielfältige Charakterfähigkeiten

- Einfache Netzwerk Struktur durch runden-basiertes Kampfsystem

<!-- _something funny here idk_ -->

![bg right:30%](assets/img/pitch-chess-diplomacy.png)

<!-- _footer: "B-Team: Marc Matija"-->

---

# Live Demo

<!--
    Live Demo ähnlich zum Games Day, einer hat einen Laptop offen
    und ist auf dem Server vorbereitet. Um den lobby Browser zu zeigen
    wir öffnen eine Lobby und spielen ein wenig.
-->

<!-- _footer: "B-Team: Marc Matija"-->

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
3.2 Technologien <code>Adnan</code> 
4. "Spiel an sich"
4.a Screens <code>Dania</code> Welche haben wir, wozu dienen sie (grob)
4.b Hauptmenu <code>Albano</code> 
4.c CharacterDataModel + Entity <code>Albano</code>
4.d Balancing <code>Albano</code>
4.e GameScreen + Spielzyklus <code>Lukas</code> 
4.f CharacterSprite + Animationen <code>Lukas</code> 
4.g Patterns <code>Lukas</code>
4.h Interaktion mit dem Spiel (Mausumrechnung) <code>Jacinto</code> 
4.i Turn Logic + Validation <code>Jacinto</code> 
4.j Character Integration <code>daniiaaa</code> 
4.k Design (bzw. Map Design) <code>daniiaaa</code>
4.l Figma + Tileset + MapDesign <code>daniiaaa</code>
4.m Server <code>Marc</code> 
4.n Kommunikation Client <-> Server <code>Marc</code> 
5. Qualität <code>Albano</code>
5.1 Wie wurde getestet <code>Albano</code> 
5.2 Was haben wir getestet <code>Albano</code> 
6. Doku <code>Adnan</code>
6.1 Java Doc <code>Adnan</code> 
6.2 Doku zeigen (Writerside) <code>Adnan</code> 
6.3 Spielanleitung <code>Adnan</code> 
Z. Zukunft <code>Wer noch was braucht</code>
-->

---

# Introduction

---

# Projektorganisation

---

# Scrum

---

# Technologie

---

# Spiel an sich

---

# Game Screen

<!-- Bild vom Gamescreen: Command Mode -->

![Game Screen in Command Mode w:900px center](assets/img/GameScreenCommandMode.jpg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Game Screen - Zyklus

<!-- Zyklus Diagram -->

![Game Screen Cycle w:900px center](assets/img/ComplexCycle.svg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Game Screen - Initialisation

<!-- Bild vom Gamescreen: Init Mode -->

![Game Screen in Command Mode w:900px center](assets/img/GameScreenInit.jpg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Game Screen - Command Mode

<!-- Bild vom Gamescreen: Command Mode -->

![Game Screen in Command Mode w:900px center](assets/img/GameScreenCommandMode.jpg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Game Screen - Command Menu

<!-- Bild vom Gamescreen: Command Menu -->

![Game Screen in Command Menu w:900px center](assets/img/CommandModeCommands.jpg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Game Screen - Move Command Mode

<!-- Bild vom Gamescreen: Command Mode -->

![Game Screen in Movement Preview Mode w:900px center](assets/img/CommandModeMove.jpg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Game Screen - Attack Command Mode

<!-- Bild vom Gamescreen: Command Mode -->

![Game Screen in Attack Preview Mode w:900px center](assets/img/CommandModeAttack.jpg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Game Screen - Turn Outcome Animation

<!-- Video vom Gamescreen: Turn Outcome Animation -->

<div align="center">
  <video width="900" autoplay loop muted playsinline>
    <source src="assets/vid/TurnOutcomeAnimation.mp4" type="video/mp4">
    Your browser does not support the video tag.
  </video>
</div>

<!-- _footer: "B-Team: Lukas Walker"-->

---

# CharacterSprite

## Graphische Representation eines CharacterEntities
- Methoden zum Rendern
- Methoden zum Bewegen
- Methoden zum Überprüfen/Vorantreiben der Animation

![bg right width:300px right](assets/img/stablin-high-res.png)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Turn Result Animation Handler

## Orchestriert Animation eines TurnResults
- Queue mit Animationsobjekten füllen
- Animationen abarbeiten
- Abschluss kommunizieren

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Turn Result Animation Handler

<!-- UML: Animations -->

![Name w:900px center](assets/img/AnimationUMLsimple.svg)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Patterns

## Datenstruktur zum Speichern von Befehlsmustern
- Einfach zu erstellen und lesen
- Gut skalierbar
- Alle vorstellbaren Muster speichern
- Wiederverwendbar

Umsetzung: Name + String-Representation + Subpattern-Mappings

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Patterns - Beispiel

Name: plus
<pre>
String: " x 
         xxx
          x "
</pre>
Subpattern Mappings: -

![Plus Pattern bg right width:300px](assets/img/plusPatternHighRes.png)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Patterns - Beispiel

Name: bombThrow
<pre>
String: " +++ \n
         ++ ++\n
         +   +\n
         ++ ++\n
          +++ "
</pre>
Subpattern Mappings: '+' -> "plus"

![Bombo Pattern bg right width:300px](assets/img/bomboPatternHighRes.png)

<!-- _footer: "B-Team: Lukas Walker"-->

---

# Turn Logic

Rundenbasierte Regeln:

- Jeder Charakter darf nur einen Befehl (Angriff oder Movement) haben
- Befehle dürfen nicht außerhalb der Map liegen
- Befehle müssen sich an die Movement / Attack Patterns der Charaktere halten -> Charaktere vom Server als anticheat
- Charaktere dürfen sich nicht dahin bewegen, wo andere Charaktere sind<br />-> Auch nicht wenn der sich wegbewegt
- Mehrere Spieler dürfen sich nicht an die gleiche Stelle bewegen
- Eigener Check für Charktere gegnerischer Spieler -> "Bounces"

<!--Ggf. Gif von Bounces oder so-->
<!--Ggf. Architekturmodell oder so-->

---

# Server

- Springboot Projekt agierend als WebSocket Server
- Eigene WebSocket Protokoll lösung wegen limitationen auf der client seite.
- H2 zum Speichern der Daten während der Entwicklung 
- Umstellungsmöglichkeit auf PostgreSQL für Produktions Deployment

![bg right:30%](assets/img/server-technology.png)

<!-- _footer: "B-Team: Marc Matija"-->

---

![bg right:50% 95%](../documentation/Writerside/img/server/server-packet-handling.drawio.svg)

# Aufbau

**Controller:**  
- Verarbeiten HTTP Request

**Service:**
- Verbindung zwischen WebSocket Nachrichten und der Datenbank
- Eigentliche Logik des Servers

<!-- _footer: "B-Team: Marc Matija"-->

---

# Architecture

![bg right](../documentation/Writerside/img/architecture/architecture.drawio.svg)

<!-- _footer: "B-Team: Marc Matija"-->

---

# Auth

![width:600px bg right](assets/img/auth-sequence.svg)

- Client regestriert sich auf dem Server
- Erhält UUID
- Fordert Token mit UUID an
- Bekommt Token und Auslaufdatum
- Verbindet sich mit Token

> Für nähere infos: [`Authentication`](https://b-team-organisation.github.io/Fantasy-Chess/authentication.html)

<!-- _footer: "B-Team: Marc Matija"-->

---

# [Token Service](https://b-team-organisation.github.io/Fantasy-Chess/services.html#token-service)

- Generiert Single-Use Token
- Speichert diesen im `TokenRepository`
- Validiert Token
- Invalidiert Token

**Token Format**: `V3D5qNVl8IpP2y_qEgQ24`
- 14 Character Base64URL String mit CRC8 Checksum

<!-- _footer: "B-Team: Marc Matija"-->

---

## [Packets](https://b-team-organisation.github.io/Fantasy-Chess/packet.html)

Simple Json struktur, welche die gesendeten Daten in `data` und die id des packets in `id` angibt

```json
{
  // strukturiert nach TOPIC_PACKET
  "id": "<PACKETID>", 
  "data" : {}
}
```

> Serialisiert mit [JsonWriter](https://b-team-organisation.github.io/Fantasy-Chess/json-writer.html)

<!-- _footer: "B-Team: Marc Matija"-->

---

# Packet Handling

**[WebSocketService](https://b-team-organisation.github.io/Fantasy-Chess/services.html#websocket-service)**: 
Routed Pakete zu passenden `PacketHandler` instanzen

**[PacketHandler](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/server/com/bteam/fantasychess_server/client/PacketHandler.html):**
```java
public interface PacketHandler {
    void handle(Client client,String id, String packet);
    String getPacketPattern();
}
```

**[Game Packet Handler](https://b-team-organisation.github.io/Fantasy-Chess/packet-handler.html#game-packet-handler):** Packet Pattern: `GAME_`

**[Lobby Packet Handler](https://b-team-organisation.github.io/Fantasy-Chess/packet-handler.html#lobby-packet-handler):** Packet Pattern: `LOBBY_`

**[Player Packet Handler](https://b-team-organisation.github.io/Fantasy-Chess/packet-handler.html#player-packet-handler):** Packet Pattern: `PLAYER_`

<!-- _footer: "B-Team: Marc Matija"-->

---

![bg 60%](assets/img/packet-handler-diagram.svg)

<!-- _footer: "B-Team: Marc Matija"-->

---

# Services

- GameStateService
  - benutzt Turn Logic Service
- LobbyService
- PlayerService
- TokenService
- WebSocketService

![bg right](assets/img/server-services.drawio.svg)

<!-- _footer: "B-Team: Marc Matija"-->

---

# Client-Side

## Packet Handler
- 1 Packet Handler pro Packet
- Java Functional interface
```java
@FunctionalInterface
public interface PacketHandler {
  void handle(String packet);
}
```

![bg right 100%](assets/img/client-side-packet-handling.svg)

<!-- _footer: "B-Team: Marc Matija"-->

---

# Qualität

---

# Doku

---

# Zukunft

