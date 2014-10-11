package norites;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

public class Main extends BasicGame {
	/* 1. Main クラスまたはオブジェクトに所属するメンバー変数の宣言を書く所 */
	
	final int WALL1_ID = 1;	
	final int WALL2_ID = 2;
	final int CANNON_ID = 3;

	float x = 180, y = 184;	
//	int ntx=(int)x/64; //のりぴーのタイル位置
//	int nty=(int)y/64; //のりぴーのタイル位置
		
	float usax = 150, usay = 300;
	int utx=(int)usax/64;//うさたんのタイル位置
	int uty=(int)usay/64;
	
	float shimo_x=200, shimo_y=200;
	int stx=(int)shimo_x/64;//しもたんのタイル位置
	int sty=(int)shimo_y/64;


	int right = 1;
	boolean usamuki= true;//trueだと右向き
	boolean shimomuki =true; //trueだと下向き
	Random rnd = new Random();
	byte icount = 0;
	boolean ismove = false;
	static final float SPEED = 0.1f;
	Image[] sprite = new Image[7];
	private Animation noripie,walk,wait;
	
	String path =null;
	Image kabe1, kabe2, usatan,cannon,shell,shimo_normal,shimo_super;
	
	ArrayList<Integer> cannon_x_list = new ArrayList<>();
	ArrayList<Integer> cannon_y_list = new ArrayList<>();
	int cannon_number = 0;
	boolean shell_flag = true;
	int shell_x = 0;

	TiledMap map = null;
	
	
	int map1,map2;
	
	public Main(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		/* 3. 初期化ルーチン
		（フォントや画像、サウンド等のデータをファイルから読み込んで
		オブジェクトとして変数名に関連付けたりする）
		当然、ここはループしない */
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
		
		try{
			kabe1 = new Image("./resource/kabe1.png");

			kabe2 = new Image("./resource/kabe2.png");
			
			usatan = new Image("./resource/usatan.gif");
			
			cannon = new Image("./resource/砲台1.gif");
			
			shell = new Image("./resource/砲弾.gif");
			
			shimo_normal = new Image("./resource/シタ.gif");
			
			shimo_super = new Image("./resource/ウエ.gif");
		}catch(Exception e){
		}
		
		path = "./resource/untitled.tmx";
		//System.out.println(path);
		
		try{
			map = new TiledMap(path, false);
		}catch(SlickException e){
			System.out.println("Error Loading Map!");		
		}	
				
		map1 = map.getLayerIndex("タイル・レイヤー1");
		map2 = map.getLayerIndex("collision");
		try{
			//System.out.println(map.getTileProperty(3, "number", "true"));			
		}catch(NullPointerException e){
			System.err.println(e.getMessage());
		}		
//		System.out.println("map1: "+map1+" map2: "+map2);
							
		for(int tx = 0;tx < 10; tx++){
			for(int ty = 0;ty < 7; ty++){
				if(map.getTileId(tx, ty, map2) == CANNON_ID){
					cannon_x_list.add(tx*64);
					cannon_y_list.add(ty*64);
				}
			}
		}		
		
		for(int tx = 0;tx < cannon_x_list.size(); tx++){
//			System.out.println("cannon_x_list: "+ cannon_x_list.get(tx));
		}
		for(int ty = 0;ty < cannon_y_list.size(); ty++){
//			System.out.println("cannon_y_list: "+ cannon_y_list.get(ty));
		}		
	}
	@SuppressWarnings("static-access")
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		/* 4. ゲームの内部状態（変数等）の更新に関するルーチン
		 * 
		（ゲームのロジックや入力に関する本体・メインループ） */
		float px=x,py=y;
		
		float move = SPEED * delta;
		Input input = gc.getInput();
		if (input.isKeyDown(input.KEY_LEFT)) {
			x -= move;
			right = -1;
			
		} else if (input.isKeyDown(input.KEY_RIGHT)) {
			x += move;
			right = 1;
		}
		if (input.isKeyDown(input.KEY_UP)) {
			y -= move;
		} else if (input.isKeyDown(input.KEY_DOWN)) {
			y += move;
		}		
		if(input.isKeyDown(input.KEY_1)){
			cannon_number = 0;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
		if(input.isKeyDown(input.KEY_2)){
			cannon_number = 1;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
		if(input.isKeyDown(input.KEY_3)){
			cannon_number = 2;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
		if(input.isKeyDown(input.KEY_4)){
			cannon_number = 3;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
				
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
		
		
		if (input.isKeyDown(input.KEY_LEFT)||
			input.isKeyDown(input.KEY_RIGHT)||
			input.isKeyDown(input.KEY_UP)||
			input.isKeyDown(input.KEY_DOWN))
		{
			ismove = true;
			noripie = walk;
			noripie.update(delta);
		} else {
			ismove = false;
			noripie = wait;
			noripie.update(delta);
		};
	}
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		/* 5. 画面描画に関するルーチン
		（ゲームの結果出力に関する本体・メインループ） */
		
		
		for(int tx = 0; tx < 10; tx++){
			for(int ty =0; ty<7;ty++){
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
		if(right==1){
			noripie.draw((int)x,(int)y,right*64,64);
		}else if(right == -1){
			noripie.draw((int)x+64,(int)y,right*64,64);
		}
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
			
		shell_x--;
		
		if(shell_x<=64)
			shell_x = cannon_x_list.get(cannon_number)-64;
								
		if(shell_flag)
			g.drawImage(shell, shell_x, cannon_y_list.get(cannon_number));
				           
		g.setColor(Color.blue);
		g.drawRect(x, y, 64, 64);
		g.drawRect(usax, usay, 64, 64);
		g.drawRect(shimo_x, shimo_y, 64, 64);
		g.setColor(Color.black);
		g.drawRect((((int)x+50)/64)*64, (((int)y+50)/64)*64, 5, 5);
		g.drawRect((((int)x+50)/64)*64, (((int)y+10)/64)*64, 5, 5);
		g.drawRect((((int)x+10)/64)*64, (((int)y+50)/64)*64, 5, 5);
		g.drawRect((((int)x+10)/64)*64, (((int)y+10)/64)*64, 5, 5);
		
//			System.out.println("noriko"+(int)x+":"+(int)y);
//			System.out.println("usagi"+(int)usax+":"+(int)usay);
	}
	

	public static void main(String[] args) throws SlickException {
		/* 6. JVM 側がこの Main クラスを実体化するための、
		いわば着火メソッド。便宜上、このクラスに埋め込まれているだけで、
		ゲームプログラム本体とは基本的に関係がない部分 */
		AppGameContainer app = new AppGameContainer(new Main("骨組"));
		app.setDisplayMode(680, 500, false);
		app.start();
	}
}
