package norites;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Enemy extends Map{

	public Enemy() throws SlickException {
		super();
	}

	float usax = 150, usay = 300;
	int utx=(int)usax/64;//うさたんのタイル位置
	int uty=(int)usay/64;
	
	float shimo_x=200, shimo_y=200;
	int stx=(int)shimo_x/64;//しもたんのタイル位置
	int sty=(int)shimo_y/64;


	boolean usamuki= true;//trueだと右向き
	boolean shimomuki =true; //trueだと下向き
	
	public void render(Graphics g){
		
		super.render(g);
		
		if(usamuki)
			usax+=0.1;
		
		else
			usax-=0.1;
		//System.out.println(usamuki);
		if(map.getTileId((int)usax/64, (int)usay/64, map2) == WALL2_ID && usamuki==false)
			usamuki = true;
		
		else if(map.getTileId((int)usax/64+1, (int)usay/64, map2) == WALL2_ID && usamuki==true)
			usamuki = false;
		
		if(map.getTileId((int)usax/64, (int)usay/64+1, map2) == WALL2_ID && usamuki==false)
			usamuki = true;
		
		else if(map.getTileId((int)usax/64+1, (int)usay/64+1, map2) == WALL2_ID && usamuki==true)
			usamuki = false;
			g.drawImage(usatan, usax, usay);
			
		if(shimomuki){
			g.drawImage(shimo_normal, shimo_x,shimo_y);
			shimo_y+=0.1;
		}else{
			shimo_y-=0.1;
			shimo_y-=0.1;
			g.drawImage(shimo_super, shimo_x,shimo_y);
		}
		if(map.getTileId((int)shimo_x/64, (int)shimo_y/64, map2) == WALL2_ID && shimomuki==false){
			shimomuki = true;
			g.drawImage(shimo_normal, shimo_x,shimo_y);
		}else if(map.getTileId((int)shimo_x/64+1, (int)shimo_y/64, map2) == WALL2_ID && shimomuki==true){
			shimomuki = false;
			g.drawImage(shimo_super, shimo_x,shimo_y);
		}
		if(map.getTileId((int)shimo_x/64, (int)shimo_y/64+1, map2) == WALL2_ID && shimomuki==false){
			shimomuki = true;
			g.drawImage(shimo_normal, shimo_x,shimo_y);
		}else if(map.getTileId((int)shimo_x/64+1, (int)shimo_y/64+1, map2) == WALL2_ID && shimomuki==true){
			shimomuki = false;
			g.drawImage(shimo_super, shimo_x,shimo_y);
		}
		
//		デバッグ用に枠表示
		
		g.drawRect(usax, usay, 64, 64);
		g.drawRect(shimo_x, shimo_y, 64, 64);
	}

}
