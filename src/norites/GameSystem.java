package norites;

import java.util.Random;

import org.newdawn.slick.SlickException;

public class GameSystem extends Map {
	
	Random rnd;
	Noripie n;
	
	public GameSystem() throws SlickException {
		super();
		// TODO 自動生成されたコンストラクター・スタブ
		n = new Noripie();

		rnd = new Random();
	}

	public void collision(Noripie n, Character c){
		if((((int)n.x+32)/64 == ((int)c.x+32)/64) && ((int)n.y+32)/64 == ((int)c.y+32)/64)
			for(;;){
				n.x=rnd.nextInt(640-64);
				n.y=rnd.nextInt(400-64);
				if(map.getTileId((int)(n.x+50)/64, (int)(n.y+50)/64, map2)!=WALL2_ID &&
						map.getTileId((int)(n.x+50)/64, (int)(n.y+10)/64, map2)!=WALL2_ID && 
						map.getTileId((int)(n.x+10)/64, (int)(n.y+50)/64, map2)!=WALL2_ID && 
						map.getTileId((int)(n.x+10)/64, (int)(n.y+10)/64, map2)!=WALL2_ID &&
						map.getTileId((int)(n.x+50)/64, (int)(n.y+50)/64, map2)!=CANNON_ID &&
						map.getTileId((int)(n.x+50)/64, (int)(n.y+10)/64, map2)!=CANNON_ID && 
						map.getTileId((int)(n.x+10)/64, (int)(n.y+50)/64, map2)!=CANNON_ID && 
						map.getTileId((int)(n.x+10)/64, (int)(n.y+10)/64, map2)!=CANNON_ID)
					break;
			}
	}
}
