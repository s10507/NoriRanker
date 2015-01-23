package norites;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.newdawn.slick.util.xml.XMLElement;

public class Main extends BasicGame {
	/* 1. Main クラスまたはオブジェクトに所属するメンバー変数の宣言を書く所 */

	int WARP_ID = 0;
	int WALL1_ID = 1;
	int WALL2_ID = 2;
	int FLOOR = 2;
	int CEILING = 5;
	int CANNON_ID = 3;
	int TAKARA_ID = 4;
	int KUMO_ID = 6;
	int HOOK_ID = 7;

	float x = 261+64*10, y = 64*1;
//	int ntx=(int)x/64; //のりぴーのタイル位置
//	int nty=(int)y/64; //のりぴーのタイル位置

	float usax = 150, usay = 64*5;
	int utx=(int)usax/64;//うさたんのタイル位置
	int uty=(int)usay/64;

	float shimo_x=200, shimo_y=200;
	int stx=(int)shimo_x/64;//しもたんのタイル位置
	int sty=(int)shimo_y/64;

	float doragon_x=64*16, doragon_y=64;
	int dtx=(int)doragon_x/64;
	int dty=(int)doragon_y/64;

	int kumo_count=0;
	int kumo_flg=0;
	
	int menu_id = 0;
	int screen_mapx = 0;
	int screen_mapy = 0;
	int screen_tx = 0;
	int screen_ty = 0;

	float draw_x = 0;
	float draw_y = 0;

	int right = 1;
	boolean usamuki= true;//trueだと右向き
	boolean shimomuki =true; //trueだと下向き
	Random rnd = new Random();
	byte icount = 0;
	boolean iswalk = false;
	static final float SPEED = 0.3f;
	Image[] sprite = new Image[7];   //移動の絵
	Image[] sprite_k = new Image[6]; //攻撃の絵
	Image[] sprite_h = new Image[3]; //クリアの絵(ジャンプも使えるかも)
	Image[] sprite_d = new Image[4]; //ダメージの絵
	Image[] sprite_hook = new Image[2];
	private Animation noripie,walk,wait,attack,damage,jump;

	String path =null;
	Image kabe1, kabe2, usatan,cannon,shell,shimo_normal,shimo_super,bless,kumo,hook;
	Image takara,clear,doragon,gameover;
	
	ArrayList<Point> hook_list = new ArrayList<>(); //フックの座標リスト
	int hook_number = 0;
	Point hook_p = new Point();

	ArrayList<Integer> cannon_x_list = new ArrayList<>();
	ArrayList<Integer> cannon_y_list = new ArrayList<>();
	int cannon_number = 0;
	int shell_x = 0;
	float bless_x = doragon_x;
	float bless_y = doragon_y;
	boolean doragon_flg = false;
	boolean doragon_up = false;
	TiledMap map = null;

	int map1, map2, map3;

	boolean onground;
	boolean ishooking;
	final float leg_mussle = 1f; //脚力
	final float gravity = .05f; //重力
	
	float vspeed = 0.0f;
	int next_cannon;

	Point N_P = new Point(x,y);
	Point USA_P = new Point(usax, usay);
	Point SHIMO_P = new Point(shimo_x, shimo_y);

	float wid_between_x; //のりぴーとうさの間の長さ
	float wid_between_y; //のりぴーとうさの間の長さ

	Color col = new Color(180,230,250);

	boolean is_shimo_super = false;

	float angle = 0;
	boolean jump_flg = false;
	
	float dra_jump = 90;
	float darara = 256;

	int life = 0;

	public Main(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		/* 3. 初期化ルーチン
		（フォントや画像、サウンド等のデータをファイルから読み込んで
		オブジェクトとして変数名に関連付けたりする）
		当然、ここはループしない */
		
		HashMap MapId = readgid();
		
		WALL1_ID = (int) MapId.get("kabe1");
		WALL2_ID = (int) MapId.get("kabe2");
		FLOOR = (int) MapId.get("kabe2");
		CANNON_ID = (int) MapId.get("cannon");
		TAKARA_ID = (int) MapId.get("takara");
		KUMO_ID = (int) MapId.get("kumo");
		HOOK_ID = (int) MapId.get("hook");

		SpriteSheet ssheet = new SpriteSheet(new Image("./resource/img/noripyonsp.png"), 64, 64);
		SpriteSheet ssheet_k = new SpriteSheet(new Image("./resource/img/norikousp.gif"), 64, 64);
		SpriteSheet ssheet_h = new SpriteSheet(new Image("./resource/img/norihappysp.gif"), 64, 64);
		SpriteSheet ssheet_d = new SpriteSheet(new Image("./resource/img/noridamesp.gif"), 64, 64);
		//SpriteSheet ssheet_hook = new SpriteSheet(new Image("./resource/img/.gif"), 128, 64);
		
		byte i;
		for (i = 0; i < sprite.length; i++)
			sprite[i] = ssheet.getSubImage(i,0);
		for(i = 0; i < sprite_k.length; i++)
			sprite_k[i] = ssheet_k.getSubImage(i,0);
		for(i = 0; i < sprite_h.length; i++)
			sprite_h[i] = ssheet_h.getSubImage(i,0);
		for(i = 0; i < sprite_d.length; i++)
			sprite_d[i] = ssheet_d.getSubImage(i,0);
	//	for(i = 0; i < sprite_hook.length; i++)
	//		sprite_hook[i] = ssheet_hook.getSubImage(i,0);

		Image[] pyonning = {sprite[3],sprite[4],sprite[5],sprite[6]};
		Image[] waiting = {sprite[1],sprite[2],sprite[1],sprite[2]};
		Image[] attacking = {sprite_k[0],sprite_k[1],sprite_k[2],sprite_k[3],sprite_k[4],sprite_k[5]};
		Image[] damaging = {sprite_d[0],sprite_d[1],sprite_d[2],sprite_d[3]};
		Image[] jumping = {sprite_h[0],sprite_h[1],sprite_h[2]};
		int[] duration = {100,100,100,100};
		int[] duration_k = {50,50,50,50,50,100};
		int[] duration_d = {500,500,500,500};
		int[] duration_h = {60,70,100};
		

		walk = new Animation(pyonning, duration, false);
		wait = new Animation(waiting, duration, true);
		jump = new Animation(jumping, duration_h, false);
		attack = new Animation(attacking,duration_k, false);
		damage = new Animation(damaging,duration_d,false);
		noripie = wait;
		
		jump.setLooping(false);

		try{
//			kabe1 = new Image("./resource/kabe1.png");
			kabe2 = new Image("./resource/kabe3.png");
			kumo = new Image("./resource/kumo.gif");
			usatan = new Image("./resource/usatan.gif");
			cannon = new Image("./resource/cannon.gif");
			shell = new Image("./resource/ball.gif");
			shimo_normal = new Image("./resource/シタ.gif");
			shimo_super = new Image("./resource/ウエ.gif");
			takara = new Image("./resource/takara.gif");
			clear = new Image("./resource/クリア.gif");
			doragon = new Image("./resource/ha-chan.gif");
			bless = new Image("./resource/mizu.gif");
			gameover = new Image("./resource/gameover.gif");
			hook = new Image("./resource/hook.gif");
		}catch(Exception e){
		}

		path = "./resource/sample.tmx";
		//System.out.println(path);

		try{
			map = new TiledMap(path, false);
		}catch(SlickException e){
			System.out.println("Error Loading Map!");
		}

		map1 = map.getLayerIndex("base");
		map2 = map.getLayerIndex("collision");
		map3 = map.getLayerIndex("floor");
//		System.out.println("map1: "+map1+" map2: "+map2);

		for(int ty = 0;ty < map.getHeight(); ty++){
			for(int tx = 0;tx < map.getWidth(); tx++){
				if(map.getTileId(tx, ty, map2) == CANNON_ID){
					cannon_x_list.add(tx*64);
					cannon_y_list.add(ty*64);
				}
				if(map.getTileId(tx, ty, map2) == HOOK_ID){
					hook_p.setPoint(tx*64, ty*64);
					hook_list.add(hook_p);
					System.out.println("hook x:"+hook_p.x+" y:"+hook_p.y);
				}
			}
		}

//		for(int tx = 0;tx < cannon_x_list.size(); tx++){
//			System.out.println("cannon_x_list: "+ cannon_x_list.get(tx));
//		}
//		for(int ty = 0;ty < cannon_y_list.size(); ty++){
//			System.out.println("cannon_y_list: "+ cannon_y_list.get(ty));
//		}
		screen_mapx = (int) x/640;
		screen_mapy = (int) y/448;

		life = 3;
	}

	@SuppressWarnings("static-access")
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		/*
		 * 4. ゲームの内部状態（変数等）の更新に関するルーチン
		 * 
		 * （ゲームのロジックや入力に関する本体・メインループ）
		 */

		if (menu_id != 2) {

			float px = x, py = y;

			float move = SPEED * delta;
			Input input = gc.getInput();
			if (input.isKeyDown(input.KEY_LEFT)) {
				x -= move;
				if(detect_collision(x, y, map2, WALL2_ID)
						|| detect_collision(x, y, map2, CANNON_ID)){
					x += move;
				}	
				right = -1;

			} else if (input.isKeyDown(input.KEY_RIGHT)) {
				x += move;
				if(detect_collision(x, y, map2, WALL2_ID)
						|| detect_collision(x, y, map2, CANNON_ID)){
					x -= move;
				}
				right = 1;
			}
			if (input.isKeyDown(input.KEY_DOWN)) {
				y += move;
				if(detect_collision(x, y, map2, WALL2_ID)
						|| detect_collision(x, y, map2, CANNON_ID)){
					y -= move;
				}
			}
			
			

			// //////じゅｍｐ////////////////////////////
			
			if (onground) { // ongroundなら上下加速度ゼロ
				vspeed = 0;
			} else if (ishooking) { //フック中でも上下加速度ゼロ
				vspeed = 0;
			} else {// ongroundじゃなければ下加速度どんどん追加
				vspeed += gravity;
			}
			if (input.isKeyDown(input.KEY_SPACE) && (onground||ishooking)) { // ongroundでSPACE押すと脚力分に上加速度
				vspeed = -leg_mussle;
				jump.restart();
			}
			
			if(detect_collision(x, y, map2, WALL2_ID)
					|| detect_collision(x, y, map2, CANNON_ID)){
				vspeed = 0;
				vspeed += gravity*2;
			}
			
			y += vspeed * delta; // 加速度分だけyに盛り付ける
			
			

//			System.out.println("x="+x+",y="+y+","+onground);

			if (input.isKeyDown(input.KEY_1)) {
				cannon_number = 0;
				shell_x = cannon_x_list.get(cannon_number) - 64;
			}
			if (input.isKeyDown(input.KEY_2)) {
				cannon_number = 1;
				shell_x = cannon_x_list.get(cannon_number) - 64;
			}
			if (input.isKeyDown(input.KEY_3)) {
				cannon_number = 2;
				shell_x = cannon_x_list.get(cannon_number) - 64;
			}
			

//			if (map.getTileId((int) (x+50)/64, (int) (y+50)/64, map2) == WALL2_ID
//					|| // のりぴーの右下と壁判定
//					map.getTileId((int) (x+50)/64, (int) (y+10)/64,
//							map2) == WALL2_ID
//					|| // のりぴーの右上と壁判定
//					map.getTileId((int) (x+10)/64, (int) (y+50)/64, map2) == WALL2_ID || // のりぴーの左下と壁判定
//					map.getTileId((int) (x+10)/64, (int) (y+10)/64, map2) == WALL2_ID || // のりぴーの左上と壁判定
//					map.getTileId((int) (x+50)/64, (int) (y+50)/64, map2) == CANNON_ID || // キャノンの右下と壁判定
//					map.getTileId((int) (x+50)/64, (int) (y+10)/64, map2) == CANNON_ID || // キャノンの右上と壁判定
//					map.getTileId((int) (x+10)/64, (int) (y+50)/64, map2) == CANNON_ID || // キャノンの左下と壁判定
//					map.getTileId((int) (x+10)/64, (int) (y+10)/64, map2) == CANNON_ID) { // キャノンの左上と壁判定
//				x = px; // 前の位置にもどす
//				y = py;
//				
//				System.out.println("coll!");
//			}

			if (map.getTileId((int) (x+50)/64, (int) (y+10)/64, map1) == WARP_ID
					|| map.getTileId((int) (x+10)/64, (int) (y+10)/64,
							map1) == WARP_ID) { // 落下ワープとのりぴーの判定
				x = 100;
				y = 100;
			}

			if (map.getTileId((int) (x+10)/64, (int) (y+10)/64, map2) == TAKARA_ID) {
				menu_id = 2;
			}

			N_P = N_P.setPoint(N_P, x, y);
			USA_P = USA_P.setPoint(USA_P, usax, usay);
			SHIMO_P = SHIMO_P.setPoint(SHIMO_P, shimo_x, shimo_y);

			if ((((int) x+32)/64 == ((int) usax+32)/64)
					&& ((int) y+32)/64 == ((int) usay+32)/64
					|| (((int) x+32)/64 == ((int) shimo_x+32)/64)
					&& ((int) y+32)/64 == ((int) shimo_y+32)/64
					|| (((int) x+32)/64 == ((int) shell_x+32)/64)
					&& ((int) y+32)/64 == ((int) cannon_y_list
							.get(cannon_number)+32)/64
					|| (((int) doragon_x/64 <= ((int) x+32)/64) && ((int) x+32)/64 <= ((int) doragon_x+64)/64)
					&& (((int) doragon_y/64 <= ((int) y+32)/64) && ((int) y+32)/64 <= ((int) doragon_y+64)/64)

			) { // 障害物たちのあたり判定
				// N_P = blowing(N_P);
				x = N_P.x;
				y = N_P.y;
				// life--;
			}
			// System.out.println("Life: "+life);

			wid_between_x = x - usax;
			wid_between_y = y - usay;
			if (life == 0) {
				menu_id = 3;
				noripie = damage;
				noripie.setLooping(false);
			}

			if (input.isKeyDown(input.KEY_LEFT)
					|| input.isKeyDown(input.KEY_RIGHT)
					|| input.isKeyDown(input.KEY_DOWN)) {
				iswalk = true;
				noripie = walk;
			} else if (input.isKeyDown(input.KEY_V)) {
				iswalk = false;
				noripie = attack;

				if (right == 1 && (wid_between_x > -50 && wid_between_x < 0)
						&& (wid_between_y > -10 && wid_between_y < 10)) {
					USA_P = blowing(USA_P);
					System.out.println("USA L: "+(int) usax/64+" "
							+ (int) usay/64);
					System.out.println("NORI L: "+(int) x/64+" "
							+ (int) y/64+"\nwidth: "+wid_between_x);
				} else if (right == -1
						&& (wid_between_x < 50 && wid_between_x > 0)
						&& (wid_between_y > -10 && wid_between_y < 10)) {
					USA_P = blowing(USA_P);
					System.out.println("USA R: "+((int) usax+64)/64+" "
							+ (int) usay/64);
					System.out.println("NORI R: "+((int) x+64)/64+" "
							+ (int) y/64+"\nwidth: "+wid_between_x);
				}

				usax = USA_P.x;
				usay = USA_P.y;

			} else {
				iswalk = false;
				noripie = wait;
			}
			;

			if (y+51 >= detect_ground_top(x+58, map3, FLOOR) * 64 ) {// のりぴーの右下と床判定
				onground = true;
				y = detect_ground_top(x+58, map3, FLOOR) * 64 - 51;
			}else if (y+51 >= detect_ground_top(x+20, map3, FLOOR) * 64 ) { // のりぴーの左下と床判定
				onground = true;
				y = detect_ground_top(x+20, map3, FLOOR) * 64 - 51;
			}else {
				onground = false;
				if(ishooking){
					noripie=wait;
				}else{
					noripie = jump;
				}
			}
			
			if (!ishooking){
				if(detect_collision(x, y, map2, HOOK_ID)&&input.isKeyDown(input.KEY_UP)){
					ishooking = true;
				}
			}else if(!detect_collision(x, y, map2, HOOK_ID)){
				ishooking = false;
			}
				
			
			
//			System.out.println(y+60-detect_ground_top(x+60, map, map3, FLOOR) * 64);

		}
		noripie.update(delta);
		
//		if(kumo_flg==0){
//			for(kumo_count=0;kumo_count<10000;kumo_count++){
//			kumo_flg=1;
//			}
//		}
	}
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		/* 5. 画面描画に関するルーチン
		（ゲームの結果出力に関する本体・メインループ） */
		Input input = gc.getInput();
		if(menu_id==0){			//メニュー画面の起動

			g.setColor(Color.magenta);
			g.drawString("start!!!!!!!!!!!!!!!!!!!!", 100, 200);	//メニュー内の文字
			if(input.isKeyDown(input.KEY_A)){						//裏ワザ←ただの脱線
				g.setColor(Color.red);
				g.drawString("start!!!!!!!!!!!!!!!!!!!!", 100, 200);
			}
			if(input.isKeyDown(input.KEY_ENTER)){					//enter押すとゲームスタート
				menu_id=1;
			}
		}else if(menu_id==2){
			int a=0;
			for(int i = 0; i < 3; i++){
				g.drawImage(clear, 0,64+a,128,192+a,0,0,64,64);
				g.drawImage(clear, 500,64+a,628,192+a,0,0,-64,64);
				a+=128;
			}
			g.setColor(Color.red);
			g.drawString("clear!!!!!!!!!!!!!!!!!!!!",200, 200);
		}else if(menu_id==3){

			g.setColor(Color.black);
			g.fillRect(0, 0, 640, 448);
			damage.setLooping(false);
			damage.draw(64*5-32,64*4);
			if(damage.isStopped())
				gameover.draw();
			
		}else{

		draw_x = x % 640;
		draw_y = y % 448;
		screen_mapx = (int)x/640;
		screen_mapy = (int)y/448;
		screen_tx = screen_mapx * 10;
		screen_ty = screen_mapy * 7;

		g.setColor(col);
		g.fillRect(0, 0, 640, 448);
		for(int tx = 0; tx < 10; tx++){							//マップ描画
			for(int ty =0; ty < 7;ty++){
				//System.out.println(draw_y+"s");
				if(map.getTileId(tx+screen_tx, ty+screen_ty, map1) == WALL1_ID){			//背景の壁
//					g.drawImage(kabe1,tx*64,ty*64);
				}
				if(map.getTileId(tx+screen_tx, ty+screen_ty, map1) == WALL2_ID){			//存在する壁
					g.drawImage(kabe2,tx*64,ty*64);
				}
				//System.out.println(map.getTileId(tx, ty, map1));

				if(map.getTileId(tx+screen_tx, ty+screen_ty, map2) == CANNON_ID){			//キャノン
					g.drawImage(cannon,tx*64,ty*64);
				}

				if(map.getTileId(tx+screen_tx, ty+screen_ty, map2)==TAKARA_ID){
					g.drawImage(takara,tx*64,ty*64);
				}
				
				if(map.getTileId(tx+screen_tx, ty+screen_ty, map2)==KUMO_ID && kumo_flg==1){
					g.drawImage(kumo,tx*64,ty*64);					
					kumo_flg=0;
				}
				if(map.getTileId(tx+screen_tx, ty+screen_ty, map2)==HOOK_ID){
					g.drawImage(hook,tx*64,ty*64);
				}
				
				g.setColor(Color.magenta);
				g.drawRect(tx*64, ty*64, 64, 64);
			}
		}
		if(right==1){
			noripie.draw((int)draw_x,(int)draw_y,right*64,64);
		}else if(right == -1){
			noripie.draw((int)draw_x+80,(int)draw_y,right*64,64);
		}
		if(usamuki)
			usax+=0.25f;
		else
			usax-=0.25f;
		if(map.getTileId((int)(usax+10)/64, (int)(usay+10)/64, map2) == WALL2_ID && usamuki==false)
			usamuki = true;

		else if(map.getTileId((int)(usax+50)/64, (int)(usay+10)/64, map2) == WALL2_ID && usamuki==true)
			usamuki = false;

		if(map.getTileId((int)(usax+10)/64, (int)(usay+50)/64, map2) == WALL2_ID && usamuki==false)
			usamuki = true;

		else if(map.getTileId((int)(usax+50)/64, (int)(usay+50)/64, map2) == WALL2_ID && usamuki==true)
			usamuki = false;

		int draw_usax = (int) (usax % 640);
		int draw_usay = (int) (usay % 448);

		if((screen_mapx*640 < usax && (screen_mapx+1)*640-1 > usax ) && (screen_mapy*448 < usay && (screen_mapy+1)*448-1 > usay))
			g.drawImage(usatan, draw_usax, draw_usay);

		if(shimomuki){
			shimo_y+=0.1f;
		}else{
			shimo_y-=0.1f;
		}

		shimo_normal.setRotation(angle);
		angle++;

		if(map.getTileId((int)(shimo_x+10)/64, (int)(shimo_y+10)/64, map2) == WALL2_ID && shimomuki==false){
			shimomuki = true;
			is_shimo_super = false;
		}else if(map.getTileId((int)(shimo_x+50)/64, (int)(shimo_y+10)/64, map2) == WALL2_ID && shimomuki==true){
			shimomuki = false;
			is_shimo_super = true;
		}
		if(map.getTileId((int)(shimo_x+10)/64, (int)(shimo_y+50)/64, map2) == WALL2_ID && shimomuki==false){
			shimomuki = true;
			is_shimo_super = false;
		}else if(map.getTileId((int)(shimo_x+50)/64, (int)(shimo_y+50)/64, map2) == WALL2_ID && shimomuki==true){
			shimomuki = false;
			is_shimo_super = true;
		}

		int draw_shimo_x = (int) (shimo_x % 640);
		int draw_shimo_y = (int) (shimo_y % 448);

		if((screen_mapx*640 < shimo_x && (screen_mapx+1)*640-1 > shimo_x ) && (screen_mapy*448 < shimo_y && (screen_mapy+1)*448-1 > shimo_y))
			if(is_shimo_super)
				g.drawImage(shimo_super, draw_shimo_x, draw_shimo_y);
			else
				g.drawImage(shimo_normal, draw_shimo_x, draw_shimo_y);

		int draw_shell_x = (int) (shell_x % 640);
		int draw_shell_y = (int) (cannon_y_list.get(cannon_number) % 640);
		if(cannon_x_list.size()!=0){
			shell_x -= 0.5;
			if(shell_x<=64){
				for (int i = 0;i < cannon_y_list.size(); i++){
					if(cannon_y_list.get(i)/64 == (int)(y+50)/64){
//						System.out.println("a"+i);
						cannon_number = i;
					}
				}
				shell_x = cannon_x_list.get(cannon_number)-64;
			}
			if((screen_mapx*640 < shell_x && (screen_mapx+1)*640-1 > shell_x ) &&
					(screen_mapy*448 < cannon_y_list.get(cannon_number) && (screen_mapy+1)*448-1 > cannon_y_list.get(cannon_number) ))
				g.drawImage(shell, draw_shell_x, draw_shell_y);
		}

		
		int draw_doragon_x = (int) (doragon_x % 640);
		int draw_doragon_y = (int) (doragon_y % 448);
		int draw_bless_x = (int)(bless_x % 640);
		int draw_bless_y = (int)(bless_y % 640);
		
		
		
		if((screen_mapx*640 < doragon_x && (screen_mapx+1)*640-1 > doragon_x ) && (screen_mapy*448 < doragon_y && (screen_mapy+1)*448-1 > doragon_y))
				g.drawImage(doragon, draw_doragon_x, draw_doragon_y);
		if(doragon_up==false&&screen_mapx*640 < doragon_x && (screen_mapx+1)*640-1 > doragon_x  && map.getTileId((int)(doragon_x)/64, (int)(doragon_y+128)/64, map2) != WALL2_ID){
			doragon_y +=0.5;
			
//		if((map.getTileId((int)(doragon_x)/64, (int)(doragon_y+128)/64, map2) == WALL2_ID)){
//			doragon_y -= 128;
//		
//		}
		}else if(map.getTileId((int)(doragon_x)/64, (int)(doragon_y+128)/64, map2) == WALL2_ID){
			doragon_up=true;
			darara = doragon_y;
			
		}
		if(doragon_up){
			if(dra_jump >= 180)
				dra_jump = 0;

			dra_jump += 0.5;
			doragon_y = (float) (darara - 128+Math.sin(Math.toRadians(dra_jump)) * 128);
		}
		//System.out.println(doragon_up);
				
		//System.out.println(doragon_y);
			
		
		if(((int)doragon_y+64)/64 <= y/64 && y/64<=((int)doragon_y+128)/64)
			doragon_flg = true;
		
		if(doragon_flg){
			if((screen_mapx*640 < bless_x && (screen_mapx+1)*640-1 > bless_x ) && (screen_mapy*448 < bless_y && (screen_mapy+1)*448-1 > bless_y))
				g.drawImage(bless,draw_bless_x-64,draw_bless_y+64);
			bless_x -= 0.5;
			//bless_y += 0.1;
			if(map.getTileId((int)bless_x/64-1, (int)bless_y/64,map2 )== WALL2_ID){
				doragon_flg=false;
				bless_x = doragon_x;
				bless_y = doragon_y;
			}
		}
		
		for(int i = 0;i < life; i++)
			g.drawImage(sprite[1],i*32,0,i*32+32,32,0,0,64,64);

		g.setColor(Color.red);
		g.drawRect(x, y, 64, 64);
		g.drawRect(draw_usax, draw_usay, 64, 64);
		g.drawRect(shimo_x, shimo_y, 64, 64);
		g.setColor(Color.black);
		g.drawRect(draw_x+18, draw_y+3, 42, 47);
		g.setColor(Color.pink);
		g.drawRect((((int)x+32)/64)*64, (((int)y+32)/64)*64, 5, 5);
		g.setColor(Color.orange);
		g.drawRect((((int)draw_usax+32)/64)*64, (((int)draw_usay+32)/64)*64, 5, 5);

		}
//			System.out.println("noriko"+(int)x+":"+(int)y);
//			System.out.println("usagi"+(int)usax+":"+(int)usay);
	}

	boolean detect_collision (float x, float y, int layer, int ID){
		boolean result;
		if(map.getTileId((int)(x+20)/64, (int)(y+3)/64, layer )== ID || 			//		のりぴーの左上と壁判定
				map.getTileId((int)(x+58)/64, (int)(y+3)/64, layer )== ID||		//		のりぴーの右上と壁判定
				map.getTileId((int)(x+20)/64, (int)(y+50)/64, layer )== ID||		//		のりぴーの左下と壁判定
				map.getTileId((int)(x+58)/64, (int)(y+50)/64, layer )== ID)		//		のりぴーの右下と壁判定
			result = true;
		else
			result = false;

		return result;
	}
	
	float detect_ground_top(float x,  int layer, int ID){	//のりぴーの現在地より下の床座標取得
		int min = 10000;
		for(int i = 0;i < 7; i++)
			if(map.getTileId((int)x/64, i, layer) == ID)
				if(min > i)
					min = i;
		return min;
		
	}
	
	HashMap readgid() throws SlickException {	//マップのID取得

		TMXRead t = new TMXRead();
		ArrayList<XMLElement> gid_xml = t.read("./resource/sample.tmx");
//		ArrayList<Integer> id = new ArrayList<Integer> (gid_xml.size());
//		ArrayList<String> name = new ArrayList<String> (gid_xml.size());
		
		HashMap gid = new HashMap();
		
		for (int i = 0;i < gid_xml.size();i++){
			String str = gid_xml.get(i).getAttribute("name");
			int id = gid_xml.get(i).getIntAttribute("firstgid");
			
			gid.put(str,id);
		}
		
		return gid;

	}

	public static void main(String[] args) throws SlickException {
		/* 6. JVM 側がこの Main クラスを実体化するための、
		いわば着火メソッド。便宜上、このクラスに埋め込まれているだけで、
		ゲームプログラム本体とは基本的に関係がない部分 */
		TMXRead t = new TMXRead();
		AppGameContainer app = new AppGameContainer(new Main("骨組"));
		app.setDisplayMode(64*10, 64*7, false);
		app.setTargetFrameRate(60);
		app.start();
	}
	Point attack(Point enemy_P){
		if(right>0){
			if(((int)x)/64==(((int)enemy_P.x)/64) && ((int)y+32)/64 == ((int)enemy_P.y+32)/64){
				enemy_P = blowing(enemy_P);
			}

		} else {
			if(((int)x+64)/64==(((int)enemy_P.x+64)/64) && ((int)y+32)/64 == ((int)enemy_P.y+32)/64)
				enemy_P = blowing(enemy_P);
		}

		return enemy_P;
	}

	Point blowing(Point P){
		for(;;){
			P.x=rnd.nextInt(map.getWidth()*32-64);	//当たったらランダムにふっとばす
			P.y=rnd.nextInt(map.getHeight()*32-64)+map.getHeight()*32;
			if(
					!detect_collision(P.x,P.y,map2,WALL2_ID) &&		//ランダムで吹っ飛んだ先が壁や障害物にならないように
					!detect_collision(P.x,P.y,map2,CANNON_ID)&&
					!detect_collision(P.x,P.y,map1,WARP_ID)
					)
				break;
		}
		return P;
	}
}

class Point {
	float x;
	float y;

	Point(float _x, float _y){
		x = _x;
		y = _y;
	}

	public Point() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	Point setPoint(Point _P, float _x, float _y){
		_P.x = _x;
		_P.y = _y;
		return _P;
	}
	Point setPoint(float _x, float _y){
		this.x = _x;
		this.y = _y;
		return this;
	}
	void Print(Point _P){
		System.out.println("P.x :"+_P.x+" P.y :"+_P.y);
	}

}
