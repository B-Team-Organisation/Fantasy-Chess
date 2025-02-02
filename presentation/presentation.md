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
3.2 Technologien <code>Adnan</code> 
4. "Spiel an sich"
4.a Screens <code>Dania</code> 
4.b Hauptmenu <code>Albano</code> 
4.c GameScreen + Spielzyklus <code>Lukas</code> 
4.d CharacterEntities + Animationen <code>Lukas</code> 
4.dd Patterns <code>Lukas</code>
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

- Scrum Master: Marc
- Sprints gingen 2 Wochen
- Meetings wöchentlich 1x online (DC), 1x in Person

---

# Clockify

![w:900](./assets/img/jacinto/Clockify.png)

---

# Github

- Das Fantasy-Chess Repository wurde auf Github gehostet
- Mit Github Pages wurde die Doku gehostet
- Mit Github Projects haben wir unser Task Management gemacht

---

# Kanban

![w:900](./assets/img/jacinto/kanban.png)

---

# Guidelines
![w:900](./assets/img/jacinto/guidelines.png)

---

# Prototyping

![w:900](./assets/img/jacinto/prototype.png)

---

# Scrum

---

# Technologie

---

# Spiel an sich

---

# Game Interaction

---

# Coordinate Systems

![w:900](./assets/img/jacinto/CoordinateMaps.png)

---

# Grid to World

![w:900](./assets/img/jacinto/gridToWorld.png)

---

# World to Grid

![w:900](./assets/img/jacinto/worldToGrid.png)

---

# Turn Logic

---

# Rundenbasierte Regeln

- Jeder Charakter darf nur einen Befehl (Angriff oder Movement) haben
- Befehle dürfen nicht außerhalb der Map liegen
- Befehle müssen sich an die Movement / Attack Patterns der Charaktere halten -> Charaktere vom Server als anticheat
- Charaktere dürfen sich nicht dahin bewegen, wo andere Charaktere sind<br />-> Auch nicht wenn der sich wegbewegt
- Mehrere Spieler dürfen sich nicht an die gleiche Stelle bewegen
- Eigener Check für Charktere gegnerischer Spieler -> "Bounces"

<!--Ggf. Gif von Bounces oder so-->
<!--Ggf. Architekturmodell oder so-->

---

# Sequenzdiagram

![w:900](./assets/img/jacinto/sequence_diagram.png)

---

# Klassendiagramm

![h:500](./assets/img/jacinto/turn_logic_classes.png)

---

# Qualität

---

# Doku

---

# Zukunft

