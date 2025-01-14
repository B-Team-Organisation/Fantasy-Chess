package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.CharacterDataModel;
import com.bteam.common.models.PatternService;
import com.bteam.common.models.Vector2D;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.logging.Level;

/**
 * An animation object for animating a valid attack
 * <p>
 * Animates the attack of a piece on the grid.
 *
 * @author jacinto lukas
 */
public class AttackAnimation extends AbstractAnimation {
    private final TileMathService mathService;
    private final AttackDataModel attackDataModel;
    private final CharacterSprite sprite;
    private final StaticTiledMapTile damageTile;

    /**
     * Constructor
     *
     * @param attackDataModel {@link AttackDataModel} of the animated attack
     * @param sprite {@link CharacterSprite} of the {@link CharacterEntity} performing the attack
     */
    public AttackAnimation(AttackDataModel attackDataModel, CharacterSprite sprite, TileMathService mathService, TextureAtlas atlas) {
        this.attackDataModel = attackDataModel;
        this.sprite = sprite;
        this.mathService = mathService;

        TextureRegion region = atlas.findRegion("special_tiles/filled-red");
        damageTile = new StaticTiledMapTile(region);
    }

    @Override
    public boolean isAnimationOver(){
        return !sprite.isInAnimation();
    }

    @Override
    public void startAnimation(TiledMapTileLayer outcomeLayer){
        this.outcomeLayer = outcomeLayer;
        Vector2 initialPos = new Vector2(sprite.getX(), sprite.getY());

        CharacterEntity attacker = Main.getGameStateService().getCharacterById(attackDataModel.getAttacker());

        if (attacker.getPosition() != attackDataModel.getAttackPosition()){
            sprite.moveToGridPos(attackDataModel.getAttackPosition());
            sprite.moveToWorldPos(initialPos);
        } else {
            Vector2 leftPos = new Vector2(initialPos.x - 5,initialPos.y);
            Vector2 rightPos = new Vector2(initialPos.x + 5,initialPos.y);

            sprite.moveToWorldPos(leftPos);
            sprite.moveToWorldPos(rightPos);
            sprite.moveToWorldPos(leftPos);
            sprite.moveToWorldPos(rightPos);

            sprite.moveToWorldPos(initialPos);
        }

        PatternService attackPattern = attacker.getCharacterBaseModel().getAttackPatterns()[0];
        if (Main.getGameStateService().getEnemyCharacters().contains(attacker)){
            Main.getLogger().log(Level.SEVERE,"Enemy!");
            attackPattern = attackPattern.reversePattern();
        }

        Vector2D[] areaOfEffect = attackPattern.getAreaOfEffect(attacker.getPosition(), attackDataModel.getAttackPosition());

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(damageTile);

        for (Vector2D position : areaOfEffect) {
            Main.getLogger().log(Level.SEVERE,"AttackPosition marked");
            Vector2D tilePosition = mathService.gridToTiled(position);
            outcomeLayer.setCell(tilePosition.getX(), tilePosition.getY(), cell);
        }

    }

}
