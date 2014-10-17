package norites;

import org.newdawn.slick.SlickException;

abstract class Character extends Map{

	public Character() throws SlickException {
		super();
	}

	float x, y;
	boolean muki= true;//trueだと右向き
	
	boolean turn (float x, float y, boolean muki){
		if(map.getTileId((int)x/64, (int)y/64, map2) == WALL2_ID && muki==false)
			muki = true;
		
		else if(map.getTileId((int)x/64+1, (int)y/64, map2) == WALL2_ID && muki==true)
			muki = false;
		
		if(map.getTileId((int)x/64, (int)y/64+1, map2) == WALL2_ID && muki==false)
			muki = true;
		
		else if(map.getTileId((int)x/64+1, (int)y/64+1, map2) == WALL2_ID && muki==true)
			muki = false;
		
		return muki;
	}
}

class Shimo extends Character {
	public Shimo() throws SlickException {
		super();
		
		float x = 200, y = 200;
		
		if(muki){
			y+=0.1;
		}else{
			y-=0.1;
			y-=0.1;
		}
	}
}

class Usatan extends Character {
	public Usatan() throws SlickException {
		
	}
}
