# Fantasy-Chess

![IMG_9157](https://github.com/user-attachments/assets/3a0cfd4b-e72a-4584-8318-62f36099bb4a)

DAS B-TEAM

Fantasy chess is a digital turn based board game, akin to chess. Each turn, both players give fixed orders to their figures to either move or attack. After the turn has endes, the battle rages on following these commands. The battlefield is prepared for the next planing phase.
The last team standing will emerge victorious.

**Contributors:**

Marc Matija             - Coordination  
Lukas Walker            - Quality  
Adnan Bennis            - Documentation  
Dania Anwar             - Design Lead  
Jacinto Schwarzwälder   - Repository and PM Tools  
Albano Vukelaj

## The Game

![image](https://github.com/user-attachments/assets/7fd655e3-d8e6-412a-b17f-7f31a5788276)

## Getting Started

For the best experience please install <a href="https://www.jetbrains.com/idea/">Jetbrains IntelliJ IDEA</a> or
download one of the existing binaries under [releases](https://github.com/B-Team-Organisation/Fantasy-Chess/releases).

### Running the Project

#### Downloaded binaries

If you have running binaries, you can run the client by hosting it on any webserver. For local use we recommend using
[`npx serve`](https://www.npmjs.com/package/serve).


Running the Client:
```bash
    npx serve dist
```

Running the Server:
```bash
    java -jar fantasychess-server-{version}.jar
```

#### Building from source

If you do not have existing binaries, you can build them by using the following commands, once you have cloned the
project:

Building the Client:
```bash
    cd fantasychess-client/ && ./gradlew dist
```

Building the Server:
```bash
    cd fantasychess-server/ && ./gradlew bootJar
```

## Documentation

The documentation and infos on the implementation can be found [here](https://b-team-organisation.github.io/Fantasy-Chess).
