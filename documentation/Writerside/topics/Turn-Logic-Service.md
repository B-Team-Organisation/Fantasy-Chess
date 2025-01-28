# Turn Logic Service

The Turn Logic handles the application of all turn-related rules *after the commands have been sent by the players*.
Thus, the scope are all movement and attack commands as well as the potential result of the game.

For the application of a turn, the TurnLogic component is structured into two steps:
1. Check if commands follow all rules
2. Apply the commands that are valid

Both these requirements are met with service classes exposing not a general method but all important helper methods.
This way, the server can perform all checks at once, whilst additional features can use individual rules and procedures.

The general procedure is as follows: <br/>
The client sends all commands to the server. The server takes those commands and adds its own version of the 
CharacterEntities. These will be given to the `TurnLogicService`, which will firstly validate it, then apply
valid commands and return it back to the server.

> Commands are validated with characters stored in the server to filter any client mistakes and to avert any cheating.

**UML Diagram for Classes here**

#### 1. Command Validation
<p>The `CommandValidator` class validates that attack and movement commands follow all game rules. They are as follows:</p>
<anchor id="rule1" name="rule1"></anchor>
1. Each Character can only have one command.
<anchor id="rule2" name="rule2"></anchor>
2. Characters may not attack or move out of bounds regarding the grid map.
<anchor id="rule3" name="rule3"></anchor>
3. Characters may not move or attack different to the movement/attack pattern as defined by their `CharacterDataModel`.
4. A character may not move to where another character already is.
5. Multiple characters of the same player may not move to the same position.
<anchor id="rule6" name="rule6"></anchor>
6. Opposing players may not move to the same position.


> Movement to the same position is separately checked for opposing players, because we treat them individually as `bounces`.
> This is because it's impossible to know if it will happen in advance, so the players are shown a special animation.
{style="note"}

To check the rules, the `CommandValidator` will not need not only the moves and characters, but also a copy of the
`GridService` in use and the PlayerID of the host. The GridService is needed for 
<a href="Turn-Logic-Service.md#rule2" summary="Characters may not attack or move out of Bounds regarding the grid map">rule 2</a>. The playerID of the host is needed to reverse the Patterns of his opponent
before checking <a href="Turn-Logic-Service.md#rule3" summary="Characters may not move or attack different to the movement/attack pattern as defined by their `CharacterDataModel`">rule 3</a>.

> Internally, the opposing player moves in the opposite direction to the host. However, the patterns are only stored
> with the direction of the host in mind. For the opponent, the `PatternService` has a `reversePattern` method.
{style="note"}

Running the method `validateCommands` will check all the rules. Movement and attack checks are further consolidated, 
whereas <a href="Turn-Logic-Service.md#rule1" summary="Each Character can only have one command">rule 1</a> 
and <a href="Turn-Logic-Service.md#rule6" summary="Opposing players may not move to the same position">rule 6</a> are not.
The resulting `ValidationResult` object contains a list of `MovementConflict`-Pairs ('bounces') and lists of valid
moves and valid attacks.

**UML Diagram for methods here**

#### 2. Command Application
After filtering all commands by validation, they can be safely applied to the characters. Besides applying attacks and movements, 
there is a check for characters with no remaining health and if there is a winner.

The `applyCommands` method will do all the aforementioned and return a `TurnResult` object, storing 
the state of all characters after command application, as well as the movement conflicts, valid movements and attacks from
the `ValidationResult` and the id of a winner or `null`, if there is none.

> A draw can be ascertained if there are no characters left and the winner is still `null`.
