package norites;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Noripie extends Enemy{

	float x = 180, y = 184;	
//	int ntx=(int)x/64; //のりぴーのタイル位置
//	int nty=(int)y/64; //のりぴーのタイル位置
		
	int right = 1;

	byte icount = 0;
	boolean ismove = false;
	static final float SPEED = 0.1f;
	Image[] sprite = new Image[7];
	private Animation noripie,walk,wait;
	Random rnd = new Random();
	
	
	public Noripie() throws SlickException{
		
		super();
		
		SpriteSheet ssheet = new SpriteSheet(new Image("./resource/img/noripyonsp.png"), 64, 64);
		byte i;
		for (i = 0; i < sprite.length; i++) {
			sprite[i] = ssheet.getSubImage(i, 0);
		}
		Image[] pyonning = {sprite[3],sprite[4],sprite[5],sprite[6]};
		Image[] waiting = {sprite[1],sprite[2],sprite[1],sprite[2]};
		int[] duration = {100,100,100,100};

		walk = new Animation(pyonning, duration, false);
		wait = new Animation(waiting, duration, true);

		noripie = wait;
		
	}
	
	public void controlling(GameContainer gc, int delta, Input in){
		
		float px=x,py=y;
		
		float move = SPEED * delta;
		if (in.isKeyDown(in.KEY_LEFT)) {
			x -= move;
			right = -1;
			
		} else if (in.isKeyDown(in.KEY_RIGHT)) {
			x += move;
			right = 1;
		}
		if (in.isKeyDown(in.KEY_UP)) {
			y -= move;
		} else if (in.isKeyDown(in.KEY_DOWN)) {
			y += move;
		}		
		
		if (in.isKeyDown(in.KEY_LEFT)||
				in.isKeyDown(in.KEY_RIGHT)||
				in.isKeyDown(in.KEY_UP)||
				in.isKeyDown(in.KEY_DOWN))
			{
				ismove = true;
				noripie = walk;
				noripie.update(delta);
			} else {
				ismove = false;
				noripie = wait;
				noripie.update(delta);
			};
			
		if(map.getTileId((int)(x+50)/64, (int)(y+50)/64, map2)==WALL2_ID ||
				map.getTileId((int)(x+50)/64, (int)(y+10)/64, map2)==WALL2_ID || 
				map.getTileId((int)(x+10)/64, (int)(y+50)/64, map2)==WALL2_ID || 
				map.getTileId((int)(x+10)/64, (int)(y+10)/64, map2)==WALL2_ID ||
				map.getTileId((int)(x+50)/64, (int)(y+50)/64, map2)==CANNON_ID ||
				map.getTileId((int)(x+50)/64, (int)(y+10)/64, map2)==CANNON_ID || 
				map.getTileId((int)(x+10)/64, (int)(y+50)/64, map2)==CANNON_ID || 
				map.getTileId((int)(x+10)/64, (int)(y+10)/64, map2) == CANNON_ID){
			x=px;
			y=py;
		}
//		System.out.print("50 : ("+(x+50)/64+", "+(y+50)/64+")\n");
//		System.out.print("10 : ("+(x+10)/64+", "+(y+10)/64+")\n");
		
		if(((int)x+32)/64==(((int)usax+32)/64) && ((int)y+32)/64 == ((int)usay+32)/64 || ((int)x+32)/64==(((int)shimo_x+32)/64) && ((int)y+32)/64 == ((int)shimo_y+32)/64|| ((int)x+32)/64==(((int)shell_x+32)/64) && ((int)y+32)/64 == ((int)cannon_y_list.get(cannon_number)+32)/64){
			for(;;){
				x=rnd.nextInt(640-64);
				y=rnd.nextInt(400-64);
				if(map.getTileId((int)(x+50)/64, (int)(y+50)/64, map2)!=WALL2_ID &&
						map.getTileId((int)(x+50)/64, (int)(y+10)/64, map2)!=WALL2_ID && 
						map.getTileId((int)(x+10)/64, (int)(y+50)/64, map2)!=WALL2_ID && 
						map.getTileId((int)(x+10)/64, (int)(y+10)/64, map2)!=WALL2_ID &&
						map.getTileId((int)(x+50)/64, (int)(y+50)/64, map2)!=CANNON_ID &&
						map.getTileId((int)(x+50)/64, (int)(y+10)/64, map2)!=CANNON_ID && 
						map.getTileId((int)(x+10)/64, (int)(y+50)/64, map2)!=CANNON_ID && 
						map.getTileId((int)(x+10)/64, (int)(y+10)/64, map2)!=CANNON_ID)
					break;
			}
		}
		
	}
	
	public void render(Graphics g){
		
		super.render(g);
		
		if(right==1){
			noripie.draw((int)x,(int)y,right*64,64);
		}else if(right == -1){
			noripie.draw((int)x+64,(int)y,right*64,64);
		}
		
		g.setColor(Color.blue);
		g.drawRect(x, y, 64, 64);
		
		g.setColor(Color.black);				
		g.drawRect((((int)x+50)/64)*64, (((int)y+50)/64)*64, 5, 5);
		g.drawRect((((int)x+50)/64)*64, (((int)y+10)/64)*64, 5, 5);
		g.drawRect((((int)x+10)/64)*64, (((int)y+50)/64)*64, 5, 5);
		g.drawRect((((int)x+10)/64)*64, (((int)y+10)/64)*64, 5, 5);
		
	}


}
