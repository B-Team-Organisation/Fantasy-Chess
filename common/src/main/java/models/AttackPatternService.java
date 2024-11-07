package models;

import java.util.ArrayList;

public class AttackPatternService extends PatternService {
	public AttackPatternService(AttackPatternModel patternModel) throws NoPieceInPatternException {
		super(patternModel);
	}

	public Vector2D[] getAreaOfAttack(Vector2D player, Vector2D target){
		Vector2D targetRelativeToPlayer = target.subtract(player);
		ArrayList<Vector2D> hitTiles = new ArrayList<>();
		char tileChar = pattern[targetRelativeToPlayer.getY()][targetRelativeToPlayer.getX()];
		switch(tileChar){
			case 'O':
				// Top 3
				hitTiles.add(new Vector2D(target.getX()-1, target.getY()-2));
				hitTiles.add(new Vector2D(target.getX(), target.getY()-2));
				hitTiles.add(new Vector2D(target.getX()+1, target.getY()-2));
				// Bottom 3
				hitTiles.add(new Vector2D(target.getX()-1, target.getY()+2));
				hitTiles.add(new Vector2D(target.getX(), target.getY()+2));
				hitTiles.add(new Vector2D(target.getX()+1, target.getY()+2));
				// Left 3
				hitTiles.add(new Vector2D(target.getX()-2, target.getY()-1));
				hitTiles.add(new Vector2D(target.getX()-2, target.getY()));
				hitTiles.add(new Vector2D(target.getX()-2, target.getY()+1));
				// Right 3
				hitTiles.add(new Vector2D(target.getX()+2, target.getY()-1));
				hitTiles.add(new Vector2D(target.getX()+2, target.getY()));
				hitTiles.add(new Vector2D(target.getX()+1, target.getY()+1));
			case 'o':
				// Top Left
				hitTiles.add(new Vector2D(target.getX()-1, target.getY()-1));
				// Top Right
				hitTiles.add(new Vector2D(target.getX()+1, target.getY()-1));
				// Bottom Left
				hitTiles.add(new Vector2D(target.getX()-1, target.getY()+1));
				// Bottom Right
				hitTiles.add(new Vector2D(target.getX()+1, target.getY()+1));
			case '+':
				// Top
				hitTiles.add(new Vector2D(target.getX(), target.getY()-1));
				// Bottom
				hitTiles.add(new Vector2D(target.getX(), target.getY()+1));
				// Right
				hitTiles.add(new Vector2D(target.getX()+1, target.getY()));
				// Left
				hitTiles.add(new Vector2D(target.getX()-1, target.getY()));
			case '.':
				hitTiles.add(new Vector2D(target.getX(), target.getY()));
				break;
			case '-':
				hitTiles.add(new Vector2D(target.getX()-1, target.getY()));
				hitTiles.add(new Vector2D(target.getX(), target.getY()));
				hitTiles.add(new Vector2D(target.getX()+1, target.getY()));
				break;
			case '|':
				hitTiles.add(new Vector2D(target.getX(), target.getY()-1));
				hitTiles.add(new Vector2D(target.getX(), target.getY()));
				hitTiles.add(new Vector2D(target.getX(), target.getY()+1));
				break;
		}
		return hitTiles.toArray(new Vector2D[0]);
	}


}
