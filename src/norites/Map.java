
package norites;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Map extends Trap{
	
	TiledMap map;
	
	int map1,map2;
	
	final int WALL1_ID = 1;	
	final int WALL2_ID = 2;
	
	Image kabe1, kabe2, kabe3, usatan;
	
	public Map() throws SlickException{
		
		String path = "./resource/untitled.tmx";
		
		try{
			map = new TiledMap(path, false);
		}catch(SlickException e){
			System.out.println("Error Loading Map!");		
		}	
		
		map1 = map.getLayerIndex("タイル・レイヤー1");
		map2 = map.getLayerIndex("collision");
		
//		System.out.println("map1: "+map1+" map2: "+map2);

		
		for(int tx = 0;tx < 10; tx++){
			for(int ty = 0;ty < 7; ty++){
				if(map.getTileId(tx, ty, map2) == CANNON_ID){
					cannon_x_list.add(tx*64);
					cannon_y_list.add(ty*64);
				}
			}
		}
		
//		for(int tx = 0;tx < cannon_x_list.size(); tx++)
//			System.out.println("cannon_x_list: "+ cannon_x_list.get(tx));
		
//		for(int ty = 0;ty < cannon_y_list.size(); ty++)
//			System.out.println("cannon_y_list: "+ cannon_y_list.get(ty));
		
		
		
		kabe1 = new Image("./resource/kabe1.png");

		kabe2 = new Image("./resource/kabe2.png");
		
		kabe3 = new Image("./resource/kabe3.png");
		
		usatan = new Image("./resource/usatan.gif");
		
		cannon = new Image("./resource/砲台1.gif");
		
		shell = new Image("./resource/砲弾.gif");
		
		shimo_normal = new Image("./resource/シタ.gif");
		
		shimo_super = new Image("./resource/ウエ.gif");
	}
	
	public void render(Graphics g, Character c){
		
		float draw_x, draw_y;
		int draw_tx = 0, draw_ty = 0;
		
		for(int tx = draw_tx; tx < 10; tx++){
			for(int ty =draw_ty; ty < 7;ty++){
				//	System.out.println(map.getTileId(tx,ty,map2));
				if(map.getTileId(tx, ty, map1) == WALL1_ID){									
					g.drawImage(kabe1,tx*64,ty*64);
				}
				if(map.getTileId(tx, ty, map1) == WALL2_ID){					
					g.drawImage(kabe2,tx*64,ty*64);
				}
				//System.out.println(map.getTileId(tx, ty, map1));
				
				if(map.getTileId(tx, ty, map2) == CANNON_ID){
					g.drawImage(cannon,tx*64,ty*64);
				}	
				g.setColor(Color.magenta);
				g.drawRect(tx*64, ty*64, 64, 64);
			}
		}
		
		super.Trap_render(g);
	}
	
	public boolean turn (Character c, boolean muki){
		
		if(map.getTileId((int)c.x/64, (int)c.y/64, map2) == WALL2_ID && muki==false)
			muki = true;
		
		else if(map.getTileId((int)c.x/64+1, (int)c.y/64, map2) == WALL2_ID && muki==true)
			muki = false;
		
		if(map.getTileId((int)c.x/64, (int)c.y/64+1, map2) == WALL2_ID && muki==false)
			muki = true;
		
		else if(map.getTileId((int)c.x/64+1, (int)c.y/64+1, map2) == WALL2_ID && muki==true)
			muki = false;
		
		return muki;
	}
}