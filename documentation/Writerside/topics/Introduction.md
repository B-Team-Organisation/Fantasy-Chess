# Introduction

Fantasy-Chess ist a PvP, turn-based action game, in which players command characters
to fight against each other in order to claim victory. The game is fought out on a 
9x9 Board that 

## Getting Started

For the best experience please install <a href="https://www.jetbrains.com/idea/">Jetbrains IntelliJ IDEA</a> or
download one of the existing binaries under [releases](https://github.com/B-Team-Organisation/Fantasy-Chess/releases).

### Running the Project

#### Downloaded binaries

If you have running binaries, you can run the client by hosting it on any webserver. For local use we recommend using
[`npx serve`](https://www.npmjs.com/package/serve).


Running the Client
:
```bash
    npx serve dist
```

Running the Server
:
```bash
    java -jar fantasychess-server-{version}.jar
```

#### Building from source

If you do not have existing binaries, you can build them by using the following commands, once you have cloned the
project:

Building the Client
:
```bash
    cd fantasychess-client/ && ./gradlew dist
```

Building the Server
:
```bash
    cd fantasychess-server/ && ./gradlew bootJar
```

### Development

<procedure>
<p>Ensure you've installed <a href="https://www.oracle.com/de/java/technologies/downloads/">Java 21</a> or higher, 
along with either <a href="https://www.jetbrains.com/idea/">Jetbrains IntelliJ IDEA</a> or a local installation of 
<a href="https://gradle.org/releases/">Gradle 8.9</a>.
</p>
<step>
Clone the repository from <a href="https://github.com/B-Team-Organisation/Fantasy-Chess">
https://github.com/B-Team-Organisation/Fantasy-Chess </a>
</step>
<step>
Open up the project with IDEA
</step>
<step>
Add the click on <code>File > Add module from existing sources</code> and add the <code>common</code> module
</step>
<step>
Repeat the previous step for the <code>fantasychess-server</code> and <code>fantasychess-client</code> module 
<b>IN THAT ORDER.</b>
</step>
<p>
You are now ready to start working on the Project.
</p>
</procedure>

## Team

Marc Matija
: **Email:** [info@casqan.net](mailto:info@casqan.net)  
**Website:** [casqan.net](https://casqan.net/)

Jacinto Schwarzwälder
: **Email:** [schwarzwaelder.jacinto@gmail.com](mailto:schwarzwaelder.jacinto@gmail.com)  
**Website:** [schwarzwaelder.dev](https://schwarzwaelder.dev/)

Lukas Walker
: **Email:** [lukas.walker@student.hs-rm.de](mailto:lukas.walker@student.hs-rm.de)

Dania Anwar
: **Email:** [daniairam.anwar@gmail.com](mailto:daniairam.anwar@gmail.com)

Adnan Benis
: **Email:** [adnan.benis@student.hs-rm.de](mailto:adnan.benis@student.hs-rm.de)

Albano Vukelaj
: **Email:** [albano.vukelaj@student.hs-rm.de](mailto:albano.vukelaj@student.hs-rm.de)
