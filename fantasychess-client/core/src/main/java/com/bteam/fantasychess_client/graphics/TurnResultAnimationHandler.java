package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.TurnResult;
import com.bteam.common.utils.PairNoOrder;
import com.bteam.fantasychess_client.ui.GameScreen;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.logging.Level;

import static com.bteam.fantasychess_client.Main.getLogger;

/**
 * Class that handles the animation of a turn outcome
 * <p>
 * Animated the turn outcome on basis of a turn result object.
 * Provides methods to start, progress and tell the status of the entire animation.
 *
 * @author lukas jacinto
 * @version 1.1
 */
public class TurnResultAnimationHandler {

    private final ArrayDeque<AbstractAnimation> animationQueue;
    private TiledMap tiledMap;
    private TileMathService mathService;
    private TiledMapTileLayer outcomeLayer;

    private boolean animationOver = false;
    private boolean animationStarted;

    /**
     * Constructor
     *
     * @param turnResult   {@link TurnResult} to animate
     * @param spriteMapper Object for getting the sprite corresponding to a {@link CharacterEntity}
     */
    public TurnResultAnimationHandler(TurnResult turnResult, Map<String,CharacterSprite> spriteMapper, TiledMap tiledMap, TileMathService mathService, TextureAtlas atlas){
        animationQueue = new ArrayDeque<>();

        this.tiledMap = tiledMap;
        this.mathService = mathService;
        refreshOutcomeLayer();

        for (PairNoOrder<MovementDataModel, MovementDataModel> conflict : turnResult.getMovementConflicts()) {
            animationQueue.add(new CollisionAnimation(
                conflict.getFirst(), spriteMapper.get(conflict.getFirst().getCharacterId()),
                conflict.getSecond(), spriteMapper.get(conflict.getSecond().getCharacterId()))
            );
        }

        for (MovementDataModel movementDataModel : turnResult.getValidMoves()) {
            animationQueue.add(new MovementAnimation(movementDataModel, spriteMapper.get(movementDataModel.getCharacterId())));
        }

        for (AttackDataModel attackDataModel : turnResult.getValidAttacks()) {
            animationQueue.add(new AttackAnimation(attackDataModel, spriteMapper.get(attackDataModel.getAttacker()),mathService,atlas));
        }

        animationStarted = false;
    }

    private void refreshOutcomeLayer() {
        if (outcomeLayer != null) {
            tiledMap.getLayers().remove(outcomeLayer);
        }
        this.outcomeLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), GameScreen.TILE_PIXEL_WIDTH,GameScreen.TILE_PIXEL_HEIGHT);
        tiledMap.getLayers().add(outcomeLayer);
    }

    /**
     * Test if the animation has finished playing
     *
     * @return {@code true} if animation is over, else {@code false}
     */
    public boolean isDoneWithAnimation() {
        return animationOver;
    }

    /**
     * Starts the next part of the animation if the last has finished.
     * <p>
     * Does nothing if the animation hasn't started yet
     */
    public void progressAnimation() {
        if (!animationStarted) {
            return;
        }

        if (animationQueue.isEmpty()) {
            getLogger().log(Level.SEVERE, "Tried to progress Animation queue, which is empty.");
            return;
        }

        if (!animationQueue.getFirst().isAnimationOver()) return;
        animationQueue.pop();

        if (animationQueue.isEmpty()) {
            tiledMap.getLayers().remove(outcomeLayer);
            animationOver = true;
            return;
        }

        refreshOutcomeLayer();
        animationQueue.getFirst().startAnimation(outcomeLayer);
    }

    /**
     * Starts the animation
     */
    public void startAnimation() {
        animationStarted = true;
        refreshOutcomeLayer();
        if (animationQueue.isEmpty()) {
            getLogger().log(Level.SEVERE, "Animation queue is empty, abandoning animation start!");
            return;
        }
        animationQueue.getFirst().startAnimation(outcomeLayer);
    }
}
