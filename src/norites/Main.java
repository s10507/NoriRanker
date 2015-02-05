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
	//int CEILING = 5;
	int CANNON_ID = 3;
	int TAKARA_ID = 4;
	int KUMO_ID = 6;
	int HOOK_ID = 7;
	int DOKU_ID = 8;

	int OBSTACLE_FOR_NORI[] = {WALL2_ID, CANNON_ID};
	int OBSTACLE_FOR_ENEMY[] = {WALL2_ID, CANNON_ID, TAKARA_ID, DOKU_ID};


	float x, y;
	int ntx, nty; //のりぴーのタイル位置



	float usax, usay;
	int utx, uty;//うさたんのタイル位置

	float shimo_x, shimo_y;
	int stx, sty;//しもたんのタイル位置

	float doragon_x, doragon_y;
	int dtx, dty;

	int kumo_count;
	int kumo_flg;

	int menu_id;
	int screen_mapx, screen_mapy;
	int screen_tx, screen_ty;

	float draw_x, draw_y;

	int right;
	boolean usamuki;//trueだと右向き
	boolean shimomuki; //trueだと下向き
	Random rnd;
	byte icount;
	boolean iswalk;
	static final float SPEED = 0.2f;
	Image[] sprite = new Image[7];   //移動の絵
	Image[] sprite_k = new Image[6]; //攻撃の絵
	Image[] sprite_h = new Image[3]; //クリアの絵(ジャンプも使えるかも)
	Image[] sprite_d = new Image[4]; //ダメージの絵

	Image[] sprite_doku = new Image[7];
	Image[] sprite_kumo = new Image[2];
	Image[] sprite_hook = new Image[2];
	Image[] sprite_kake = new Image[6];
	Image[] sprite_hooked = new Image[2];

	private Animation noripie,walk,wait,attack,damage,jump,doku,kumo,hooked,kake;

	String path =null;
	Image kabe1, kabe2, usatan,cannon,shell,shimo_normal,shimo_super,bless,hook,afterhooked;
	;
	Image takara,clear,doragon,gameover;

	ArrayList<Point> hook_list = new ArrayList<>(); //フックの座標リスト
	int hook_number = 0;
	Point hook_p = new Point();

	ArrayList<Float> cannon_x_list = new ArrayList<>();
	ArrayList<Float> cannon_y_list = new ArrayList<>();
	int cannon_number = 0;

	ArrayList<Float> kumo_x_list = new ArrayList<>();
	ArrayList<Float> kumo_y_list = new ArrayList<>();
	ArrayList<Integer> kouho = new ArrayList<Integer>();

	int kumo_number = 0;

	float shell_x;
	int draw_shell_x, draw_shell_y;

	float bless_x, bless_y;
	boolean doragon_flg;
	boolean doragon_up;
	TiledMap map = null;

	int map1, map2, map3;

	boolean onground;
	boolean ishooking;
	final float leg_mussle = 9.2f; //脚力
	final float gravity = .3f; //重力

	float vspeed = 0.0f;
	int next_cannon;

	Point N_P = new Point(x,y);
	Point USA_P = new Point(usax, usay);
	Point SHIMO_P = new Point(shimo_x, shimo_y);

	Color col = new Color(180,230,250);

	boolean is_shimo_super;

	float angle;
	boolean jump_flg;

	float dra_jump;
	float darara;

	int life;

	Point nori_hook = new Point();

	Point goal_point = new Point(1,1);

	public Main(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		/* 3. 初期化ルーチン
		（フォントや画像、サウンド等のデータをファイルから読み込んで
		オブジェクトとして変数名に関連付けたりする）
		当然、ここはループしない */

		gc.setShowFPS(false);

		path = "./resource/demo.tmx";

		HashMap MapId = readgid();

		WALL1_ID = (int) MapId.get("kabe1");
		WALL2_ID = (int) MapId.get("kabe2");
		FLOOR = (int) MapId.get("kabe2");
		CANNON_ID = (int) MapId.get("cannon");
		TAKARA_ID = (int) MapId.get("takara");
		KUMO_ID = (int) MapId.get("kumo");
		HOOK_ID = (int) MapId.get("hook");
		DOKU_ID = (int) MapId.get("doku1");

		SpriteSheet ssheet = new SpriteSheet(new Image("./resource/img/noripyonsp.png"), 64, 64);
		SpriteSheet ssheet_k = new SpriteSheet(new Image("./resource/img/norikousp.gif"), 64, 64);
		SpriteSheet ssheet_h = new SpriteSheet(new Image("./resource/img/norihappysp.gif"), 64, 64);
		SpriteSheet ssheet_d = new SpriteSheet(new Image("./resource/img/noridamesp.gif"), 64, 64);
		SpriteSheet ssheet_hook = new SpriteSheet(new Image("./resource/img/norihooksp.gif"), 64, 64);
		SpriteSheet ssheet_kake = new SpriteSheet(new Image("./resource/img/norikakesp.gif"), 64 ,64);
		SpriteSheet ssheet_doku = new SpriteSheet(new Image("./resource/dokusp.gif"),64,64);
		SpriteSheet ssheet_kumo = new SpriteSheet(new Image("./resource/mokumoku.gif"),64,64);
		SpriteSheet ssheet_hooked = new SpriteSheet(new Image("./resource/hookkake.gif"),64,64);

		byte i;
		for (i = 0; i < sprite.length; i++)
			sprite[i] = ssheet.getSubImage(i,0);
		for(i = 0; i < sprite_k.length; i++)
			sprite_k[i] = ssheet_k.getSubImage(i,0);
		for(i = 0; i < sprite_h.length; i++)
			sprite_h[i] = ssheet_h.getSubImage(i,0);
		for(i = 0; i < sprite_d.length; i++)
			sprite_d[i] = ssheet_d.getSubImage(i,0);
		for(i = 0; i < sprite_hook.length; i++)
			sprite_hook[i] = ssheet_hook.getSubImage(i,0);
		for(i = 0; i < sprite_doku.length; i++)
			sprite_doku[i] = ssheet_doku.getSubImage(i,0);
		for(i = 0; i < sprite_kumo.length; i++)
			sprite_kumo[i] = ssheet_kumo.getSubImage(i,0);
		for(i = 0; i < sprite_kake.length; i++)
			sprite_kake[i] = ssheet_kake.getSubImage(i,0);
		for(i = 0; i < sprite_hooked.length; i++)
			sprite_hooked[i] = ssheet_hooked.getSubImage(i,0);

		Image[] pyonning = {sprite[3],sprite[4],sprite[5],sprite[6]};
		Image[] waiting = {sprite[1],sprite[2],sprite[1],sprite[2]};
		Image[] attacking = {sprite_k[0],sprite_k[1],sprite_k[2],sprite_k[3],sprite_k[4],sprite_k[5]};
		Image[] damaging = {sprite_d[0],sprite_d[1],sprite_d[2],sprite_d[3]};
		Image[] jumping = {sprite_h[0],sprite_h[1],sprite_h[2]};
		Image[] dokudoku = {sprite_doku[0],sprite_doku[1],sprite_doku[2],sprite_doku[3],sprite_doku[4],sprite_doku[5],sprite_doku[6]};
		Image[] kumokumo = {sprite_kumo[0],sprite_kumo[1]};
		Image[] hooking = {sprite_hook[0],sprite_hook[1]};
		Image[] kakeing = {sprite_kake[0],sprite_kake[1],sprite_kake[2],sprite_kake[3],sprite_kake[4],sprite_kake[5]};


		int[] duration = {100,100,100,100};
		int[] duration_k = {50,50,50,50,50,100};
		int[] duration_d = {500,500,500,500};
		int[] duration_h = {60,70,100};
		int[] duration_kake = {50,50,50,50,50,50};
		int[] duration_hook = {1000,1000};
		int[] duration_doku = {100,100,100,100,100,100,100};
		int[] duration_kumo = {2000,3000};


		walk = new Animation(pyonning, duration, false);
		wait = new Animation(waiting, duration, true);
		jump = new Animation(jumping, duration_h, false);
		attack = new Animation(attacking,duration_k, false);
		damage = new Animation(damaging,duration_d,true);
		doku = new Animation(dokudoku,duration_doku,true);
		kumo = new Animation(kumokumo,duration_kumo,true);
		kake = new Animation(kakeing,duration_kake,true);
		hooked = new Animation(hooking,duration_hook,true);
		noripie = wait;

		jump.setLooping(false);

		try{
//			kabe1 = new Image("./resource/kabe1.png");
			kabe2 = new Image("./resource/kabe3.png");
			//kumo = new Image("./resource/kumo.gif");
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
			afterhooked = sprite_hooked[0];
		}catch(Exception e){
		}
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
					cannon_x_list.add((float) (tx*64));
					cannon_y_list.add((float) (ty*64));
				}
				if(map.getTileId(tx, ty, map2) == HOOK_ID){
					hook_list.add(new Point(tx, ty));
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

		reset(gc);

	}

	@SuppressWarnings("static-access")
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		/*
		 * 4. ゲームの内部状態（変数等）の更新に関するルーチン
		 *
		 * （ゲームのロジックや入力に関する本体・メインループ）
		 */

		if (menu_id != 2 || menu_id!=3) {

			float px = x, py = y;


			float move = SPEED * delta;
			Input input = gc.getInput();
			if (input.isKeyDown(input.KEY_LEFT)) {
				x -= move;
				if(detect_collision(x, y, right, map2, OBSTACLE_FOR_NORI)){
					x += move;
				}
				right = -1;

			} else if (input.isKeyDown(input.KEY_RIGHT)) {
				x += move;
				if(detect_collision(x, y, right, map2, OBSTACLE_FOR_NORI))
					x -= move;
				right = 1;
			}
			if (input.isKeyDown(input.KEY_DOWN)) {
				y += move;
				if(detect_collision(x, y, right, map2, OBSTACLE_FOR_NORI)){
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

			if(detect_collision(x, y, right, map2, OBSTACLE_FOR_NORI)){
				vspeed = 0;
				vspeed += gravity*2;
			}

			y += vspeed; // 加速度分だけyに盛り付ける

//			System.out.println("x="+x+",y="+y+","+onground);

//			if (input.isKeyDown(input.KEY_1)) {
//				cannon_number = 0;
//				shell_x = (int) (cannon_x_list.get(cannon_number) - 64);
//			}
//			if (input.isKeyDown(input.KEY_2)) {
//				cannon_number = 1;
//				shell_x = (int) (cannon_x_list.get(cannon_number) - 64);
//			}
//			if (input.isKeyDown(input.KEY_3)) {
//				cannon_number = 2;
//				shell_x = (int) (cannon_x_list.get(cannon_number) - 64);
//			}


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

			N_P.setPoint(x, y);
			USA_P.setPoint(usax, usay);
			SHIMO_P.setPoint(shimo_x, shimo_y);

			if(detect_collision(x, y-20, right, map2, DOKU_ID)){
				life=0;
				x=64*2;
				y=64;
			}
			if (
					nori_damage(usax, usay) ||
					nori_damage(shimo_x, shimo_y) ||
					nori_damage(shell_x, cannon_y_list.get(cannon_number)) ||
					nori_damage(doragon_x, doragon_y, 128) ||
					nori_damage_bless(bless_x, bless_y)
//					|| ((((int) doragon_x/64 <= ((int) x+32)/64) && ((int) x+32)/64 <= ((int) doragon_x+64)/64)
//					&& (((int) doragon_y/64 <= ((int) y+32)/64) && ((int) y+32)/64 <= ((int) doragon_y+64)/64))
					) { // 障害物たちのあたり判定
				N_P = blowing(N_P);
				x = N_P.x;
				y = N_P.y;
				life--;
				if(ishooking){
					ishooking = false;
				}
			}
			// System.out.println("Life: "+life);

			if (life == 0) {
				menu_id = 3;
				noripie = damage;
				noripie.setLooping(false);
			}

			float between_right; //のりぴーとうさの間の長さ
			float between_left; //のりぴーとうさの間の長さ

			between_left = (x+20) - (usax+10);
			between_right = (x+58) - (usax+50);

			if (input.isKeyDown(input.KEY_LEFT)
					|| input.isKeyDown(input.KEY_RIGHT)
					|| input.isKeyDown(input.KEY_DOWN)) {
				iswalk = true;
				noripie = walk;
			} else if (input.isKeyDown(input.KEY_V) && onground) {
				iswalk = false;
				noripie = attack;
//				System.out.println("b_l :"+ between_left+"\nb_r :"+between_right);
				if((int)(usay+32)/64 == (int)(y+32)/64){
					if (right == 1 && (between_right > -70 && between_right < 5)){
						USA_P = blowing(USA_P, 0, usay);
					} else if (right == -1 && (between_left < 70 && between_left > -5)) {
						USA_P = blowing(USA_P, 0, usay);
					}
				}

				usax = USA_P.x;
				usay = USA_P.y;

			} else {
				iswalk = false;
				noripie = wait;
			}
			;


			if (y+51 >= detect_ground_top(x+58,y, map3, FLOOR) * 64 ) {// のりぴーの右下と床判定
				onground = true;
				y = detect_ground_top(x+58,y, map3, FLOOR) * 64 - 51;
			}else if (y+51 >= detect_ground_top(x+20,y, map3, FLOOR) * 64 ) { // のりぴーの左下と床判定
				onground = true;
				y = detect_ground_top(x+20,y, map3, FLOOR) * 64 - 51;
			}else if (y+51 >= detect_ground_top(x+58,y, map3, KUMO_ID) * 64 && kumo.getCurrentFrame() == sprite_kumo[1] ) {
				onground = true;
//				y = detect_ground_top(x+58,y, map3, KUMO_ID) * 64 - 51;
			}else if (y+51 >= detect_ground_top(x+20,y, map3, KUMO_ID) * 64 && kumo.getCurrentFrame() == sprite_kumo[1]) {
				onground = true;
//				y = detect_ground_top(x+20,y, map3, KUMO_ID) * 64 - 51;
			}else if (y+51 >= detect_ground_top(x+58,y, map2, CANNON_ID) * 64 ) {// のりぴーの右下と床判定
				onground = true;
				y = detect_ground_top(x+58,y, map2, CANNON_ID) * 64 - 51;
			}else if (y+51 >= detect_ground_top(x+20,y, map2, CANNON_ID) * 64 ) { // のりぴーの左下と床判定
				onground = true;
				y = detect_ground_top(x+20,y, map2, CANNON_ID) * 64 - 51;

			}else {
				onground = false;
				if(ishooking){
					noripie = hooked;
					x = nori_hook.x * 64;
					y = (nori_hook.y + 1) * 64;
				}else{
					noripie = jump;
				}
			}

			if (!ishooking){
				if(detect_collision(x, y, right, map2, HOOK_ID)&&input.isKeyDown(input.KEY_UP) ){
					ishooking = true;
					nori_hook = detect_hook_point(x, y);
					noripie = hooked;
				}
			}else if(input.isKeyDown(input.KEY_SPACE) || onground){
				ishooking = false;
			}


			//System.out.println(detect_ground_top(x+20, map3, KUMO_ID) * 64);
			//System.out.println(x+20);
//			if(detect_collision(x, y, map2, HOOK_ID)){
//				System.out.println("HOOK!");
//			}


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
			g.setColor(Color.orange);
			g.drawString("Please Type \"Enter!\"", 225, 400);
			if(input.isKeyDown(input.KEY_ENTER))
				reset(gc);
		}else if(menu_id==3){

			g.setColor(Color.black);
			g.fillRect(0, 0, 640, 448);
			damage.setLooping(false);
			damage.draw(64*5-32,64*4);
			if(damage.isStopped()){
				gameover.draw();
				g.setColor(Color.orange);
				g.drawString("Please Type \"Enter!\"", 400, 400);
				if(input.isKeyDown(input.KEY_ENTER)){
					if(damage.getFrame() == 3)
						damage.restart();
					reset(gc);
				}
			}

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
				if(map.getTileId(tx+screen_tx, ty+screen_ty, map2)==KUMO_ID ){
					kumo.draw(tx*64,ty*64);
				}
				if(map.getTileId(tx+screen_tx, ty+screen_ty, map2)==HOOK_ID){
					if(nori_hook.equal(new Point(tx+screen_tx,ty+screen_ty)) && ishooking){
						if(right == 1)
							afterhooked.draw(tx*64, ty*64);
						else
							afterhooked.draw(tx*64+80, ty*64, right*64, 64);
					}else
						g.drawImage(hook, tx*64, ty*64);
				}
				if(map.getTileId(tx+screen_tx, ty+screen_ty, map2)==DOKU_ID){
					doku.draw(tx*64,ty*64);
				}
			}
		}
		if(right==1){
			noripie.draw((int)draw_x,(int)draw_y,right*64,64);
		}else if(right == -1){
			noripie.draw((int)draw_x+80,(int)draw_y,right*64,64);
		}
		if(usamuki)
			usax+=2.5f;
		else
			usax-=2.5f;

		if(detect_collision(usax, usay, map2, OBSTACLE_FOR_ENEMY, 10, 50, 10, 50))
			usamuki = !usamuki;

		int draw_usax = (int) (usax % 640);
		int draw_usay = (int) (usay % 448);

		if((screen_mapx*640 < usax && (screen_mapx+1)*640-1 > usax ) && (screen_mapy*448 < usay && (screen_mapy+1)*448-1 > usay))
			g.drawImage(usatan, draw_usax, draw_usay);

		if(shimomuki)
			shimo_y+=3f;
		else
			shimo_y-=3f;

		shimo_normal.setRotation(angle);
		angle+=10;

		if(detect_collision(shimo_x, shimo_y, map2, OBSTACLE_FOR_ENEMY, 10, 50, 10, 50))
			shimomuki = !shimomuki;

		int draw_shimo_x = (int) (shimo_x % 640);
		int draw_shimo_y = (int) (shimo_y % 448);

		if((screen_mapx*640 < shimo_x && (screen_mapx+1)*640-1 > shimo_x ) && (screen_mapy*448 < shimo_y && (screen_mapy+1)*448-1 > shimo_y))
			if(!shimomuki)
				g.drawImage(shimo_super, draw_shimo_x, draw_shimo_y);
			else
				g.drawImage(shimo_normal, draw_shimo_x, draw_shimo_y);

		draw_shell_x = (int) (shell_x % 640);
		draw_shell_y = (int) (cannon_y_list.get(cannon_number) % 448);
		if(cannon_x_list.size()!=0){
			shell_x -= 5.0f;
			if(detect_collision(shell_x, cannon_y_list.get(cannon_number), map2, OBSTACLE_FOR_ENEMY, 10, 50, 10, 50)){
				for (int i = 0;i < cannon_y_list.size(); i++){
					if(cannon_y_list.get(i)/64 == (int)(y+50)/64){
//						System.out.println("a"+i);
						cannon_number = i;
					}
				}
				shell_x = (int) (cannon_x_list.get(cannon_number)-64);
			}
			if((screen_mapx*640 < shell_x && (screen_mapx+1)*640-1 > shell_x ) &&
					(screen_mapy*448 < cannon_y_list.get(cannon_number) && (screen_mapy+1)*448-1 > cannon_y_list.get(cannon_number) ))
				g.drawImage(shell, draw_shell_x, draw_shell_y);
		}

		int draw_doragon_x = (int) (doragon_x % 640);
		int draw_doragon_y = (int) (doragon_y % 448);
		int draw_bless_x = (int)(bless_x % 640);
		int draw_bless_y = (int)(bless_y % 448);



		if((screen_mapx*640 < doragon_x && (screen_mapx+1)*640-1 > doragon_x ) && (screen_mapy*448 < doragon_y && (screen_mapy+1)*448-1 > doragon_y))
				g.drawImage(doragon, draw_doragon_x, draw_doragon_y);
		if(doragon_up==false&&screen_mapx*640 < doragon_x && (screen_mapx+1)*640-1 > doragon_x  && map.getTileId((int)(doragon_x)/64, (int)(doragon_y+128)/64, map2) != WALL2_ID){
			doragon_y +=2.5f;

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

			dra_jump += 2.5f;
			doragon_y = (float) (darara - 128 + Math.sin(Math.toRadians(dra_jump)) * 128);
		}
		//System.out.println(doragon_up);

		//System.out.println(doragon_y);


		if(((int)doragon_y+64)/64 <= y/64 && y/64<=((int)doragon_y+128)/64)
			doragon_flg = true;

		if(doragon_flg){
			if((screen_mapx*640 < bless_x && (screen_mapx+1)*640-1 > bless_x ) && (screen_mapy*448 < bless_y && (screen_mapy+1)*448-1 > bless_y))
				g.drawImage(bless,draw_bless_x-64,draw_bless_y+64);
			bless_x -= 5.0f;
			//bless_y += 0.1;
			if(map.getTileId((int)bless_x/64-1, (int)bless_y/64,map2 )== WALL2_ID){
				doragon_flg=false;
				bless_x = doragon_x;
				bless_y = doragon_y;
			}
		}

		for(int i = 0;i < life; i++)
			g.drawImage(sprite[1],i*32,0,i*32+32,32,0,0,64,64);

//		System.out.println((x+58 - usax-50));
//		g.setColor(Color.black);
//		g.drawString(Float.toString(x+58), draw_x, draw_y);
//		g.drawString(Float.toString(usax+50), usax, usay);
//
//		g.setColor(Color.red);
//		g.drawRect(x, y, 64, 64);
//		g.drawRect(draw_usax, draw_usay, 64, 64);
//		g.drawRect(shimo_x, shimo_y, 64, 64);
//		g.setColor(Color.black);
//		g.drawRect(draw_x+20, draw_y+3, 38, 47);
//		g.drawRect(doragon_x % 640, doragon_y % 448, 5, 5);
//		g.drawRect((bless_x-37)%640, (bless_y+91) % 640, 5, 5);
//		g.drawRect(x, y+47, 64, 1);
//		g.drawRect(x+53, y+27, 5, 5);
//		g.drawRect(usax+5, usay+27, 5, 5);
//		g.drawRect(usax+45, usay+27, 5, 5);
//		g.setColor(Color.pink);
//		g.drawRect((((int)x+32)/64)*64, (((int)y+32)/64)*64, 5, 5);
//		g.setColor(Color.orange);
//		g.drawRect((((int)draw_usax+32)/64)*64, (((int)draw_usay+32)/64)*64, 5, 5);


		}
//			System.out.println("noriko"+(int)x+":"+(int)y);
//			System.out.println("usagi"+(int)usax+":"+(int)usay);
	}

	boolean nori_damage(float enemy_x, float enemy_y){// 障害物たちのあたり判定
		boolean j = false;
		if ((((int) x+32)/64 == ((int) enemy_x+32)/64)
				&& ((int) y+32)/64 == ((int) enemy_y+32)/64){
			j = true;
		}
		return j;
	}

	boolean nori_damage(float enemy_x, float enemy_y, int size){// 障害物たちのあたり判定
		boolean j = false;
		if (
					((int) enemy_x/64 <= ((int) x+32)/64) && ((int) x+32)/64 <= ((int) enemy_x+size/2)/64
				&&	(((int) enemy_y/64 <= ((int) y+32)/64) && ((int) y+32)/64 <= ((int) enemy_y+size/2)/64)
			){
			j = true;
//			System.out.println(j);
		}
		return j;
	}

	boolean nori_damage_bless(float enemy_x, float enemy_y){// 障害物たちのあたり判定
		boolean j = false;
		if ((((int) x+32)/64 == ((int) enemy_x-32)/64)
				&& ((int) y+32)/64 == ((int) enemy_y+96)/64){
			j = true;
		}
		return j;
	}

	boolean detect_collision (float x, float y, int right, int layer, int ID){
		boolean result;
//		int r_x1, r_x2, r_y1, r_y2;
//		if(right < 0){
//			r_x1 = 5; r_x2 = r_x1+38; r_y1 = 3; r_y2 = 50;
//		}else{
//			r_x1 = 20; r_x2 = r_x1+38; r_y1 = 3; r_y2 = 50;
//		}
//
		if(map.getTileId((int)(x+20)/64, (int)(y+3)/64, layer )== ID || 			//		のりぴーの左上と壁判定
				map.getTileId((int)(x+58)/64, (int)(y+3)/64, layer )== ID||		//		のりぴーの右上と壁判定
				map.getTileId((int)(x+20)/64, (int)(y+50)/64, layer )== ID||		//		のりぴーの左下と壁判定
				map.getTileId((int)(x+58)/64, (int)(y+50)/64, layer )== ID)		//		のりぴーの右下と壁判定
			result = true;
		else
			result = false;

		return result;
	}

	boolean detect_collision (float x, float y, int layer, int[] ID, int r_x1, int r_x2, int r_y1, int r_y2){
		boolean result = false;

		for(int i=0; i < ID.length; i++)
			if(
					map.getTileId((int)(x+r_x1)/64, (int)(y+r_y1)/64, layer )== ID[i] || 			//		のりぴーの左上と壁判定
					map.getTileId((int)(x+r_x2)/64, (int)(y+r_y1)/64, layer )== ID[i] ||		//		のりぴーの右上と壁判定
					map.getTileId((int)(x+r_x1)/64, (int)(y+r_y2)/64, layer )== ID[i] ||		//		のりぴーの左下と壁判定
					map.getTileId((int)(x+r_x2)/64, (int)(y+r_y2)/64, layer )== ID[i] ||
					result
				)		//		のりぴーの右下と壁判定
				result = true;
			else
				result = false;

		return result;
	}

	boolean detect_collision (float x, float y, int right, int layer, int[] ID){
		boolean result = false;
//		int r_x1, r_x2, r_y1, r_y2;
//		if(right < 0){
//			r_x1 = 5; r_x2 = r_x1+38; r_y1 = 3; r_y2 = 50;
//		}else{
//			r_x1 = 20; r_x2 = r_x1+38; r_y1 = 3; r_y2 = 50;
//		}
		for(int i=0; i < ID.length; i++)
			if(
					map.getTileId((int)(x+20)/64, (int)(y+3)/64, layer )== ID[i] || 			//		のりぴーの左上と壁判定
					map.getTileId((int)(x+58)/64, (int)(y+3)/64, layer )== ID[i] ||		//		のりぴーの右上と壁判定
					map.getTileId((int)(x+20)/64, (int)(y+50)/64, layer )== ID[i] ||		//		のりぴーの左下と壁判定
					map.getTileId((int)(x+58)/64, (int)(y+50)/64, layer )== ID[i] ||
					result
				)		//		のりぴーの右下と壁判定
				result = true;
			else
				result = false;

		return result;
	}

	boolean detect_collision (float x, int y, int layer, int ID){
		boolean result;
		if(
				map.getTileId((int)(x+20)/64, y, layer )== ID||		//		のりぴーの左下と壁判定
				map.getTileId((int)(x+58)/64, y, layer )== ID			//		のりぴーの右下と壁判定
			)
				result = true;
		else
			result = false;

		return result;
	}


	float detect_ground_top(float x, float y,  int layer, int ID){	//のりぴーの現在地より下の床座標取得
		int min = 10000;
		int i=(int)(y+51)/64;
		for(;i < map.getHeight(); i++)
			if(map.getTileId((int)x/64, i, layer) == ID)
				if(min > i)
					min = i;
		return min;

	}


	Point detect_hook_point (float x, float y){
		int n = 0;
		float min = 10000;
		float diff = 0;
		kouho.clear();
		for (int i = 0; i < hook_list.size(); i++){
			if((int)(x+20)/64 == hook_list.get(i).x || (int)(x+58)/64 == hook_list.get(i).x){
				kouho.add(i);
				n = i;
			}
		}
		for(int j = 0;j < kouho.size(); j++){
			diff = Math.abs((Math.abs(y) - Math.abs(hook_list.get(kouho.get(j)).y*64)));
			System.out.println(diff+" "+Math.abs(hook_list.get(kouho.get(j)).y*64));
			if(min > diff){
				min = diff;
				n = kouho.get(j);
			}
//			System.out.println(kouho.get(j));
		}
//		System.out.println("ナンバー："+n);
		return hook_list.get(n);
	}

	HashMap readgid() throws SlickException {	//マップのID取得

		TMXRead t = new TMXRead();
		ArrayList<XMLElement> gid_xml = t.read(path);
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
		AppGameContainer app = new AppGameContainer(new Main("カミゲー"));
		app.setTargetFrameRate(60);
		app.setDisplayMode(64*10, 64*7, false);
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
		int count = 0;
		issue : for(;;){
			System.out.println("count :"+count++);
			P.x=rnd.nextInt(map.getWidth()*64-640);	//当たったらランダムにふっとばす
			P.y=rnd.nextInt(map.getHeight()*64-448);

			if(goal_point.equal(new Point((int)P.x/640, (int)P.y/448))){
				continue issue;
			}

			for (int i = 0; i < map.getHeight(); i++){
				if(detect_collision(P.x, i, map2, DOKU_ID)){
//					System.out.println("continue!");
//					P.Print();
					continue issue;
				}
			}
			if(
					!detect_collision(P.x, P.y, right, map2, OBSTACLE_FOR_NORI) 	&&		//ランダムで吹っ飛んだ先が壁や障害物にならないように
					!detect_collision(P.x, P.y, right, map1, WARP_ID)	&&
					!detect_collision(P.x, P.y, right, map2, DOKU_ID)	&&
					!detect_collision(P.x, P.y, right, map2, KUMO_ID)
				)
				break;
		}
		return P;
	}

	Point blowing(Point P, int flag, float p){
		int count = 0;
		issue : for(;;){
			System.out.println("count :"+count++);
			if(flag == 1)
				P.x = p;
			else
				P.x=rnd.nextInt(map.getWidth()*64-64);	//当たったらランダムにふっとばす
			if(flag == 0)
				P.y = p;
			else
				P.y=rnd.nextInt(map.getHeight()*64-64)+map.getHeight()*32;
			if(P.x < (screen_mapx+1)*640 && P.x > screen_mapx*640)
				continue issue;

			for (int i = 0; i < map.getHeight(); i++){
				if(detect_collision(P.x, i, map2, DOKU_ID)){
//					System.out.println("continue!");
//					P.Print();
					continue issue;
				}
			}
			if(
					!detect_collision(P.x,P.y,map2,OBSTACLE_FOR_ENEMY, 10, 50, 10, 50) 	&&		//ランダムで吹っ飛んだ先が壁や障害物にならないように
					!detect_collision(P.x,P.y,0,map1,WARP_ID)
				)
				break;
		}
		return P;
	}

	void reset (GameContainer gc){
		life = 3;

		x = 64*2;
		y = 64*4;

		usax = 64*12;usay = 64*5;
		utx=(int)usax/64;//うさたんのタイル位置
		uty=(int)usay/64;

		shimo_x=64*13; shimo_y=64*1;
		stx=(int)shimo_x/64;//しもたんのタイル位置
		sty=(int)shimo_y/64;

		doragon_x=64*18; doragon_y=64*3;
		dtx=(int)doragon_x/64;
		dty=(int)doragon_y/64;

		kumo_count=0;
		kumo_flg=0;

		menu_id = 0;
		screen_mapx = 0;
		screen_mapy = 0;
		screen_tx = 0;
		screen_ty = 0;

		draw_x = 0;
		draw_y = 0;

		right = 1;
		usamuki= true;//trueだと右向き
		shimomuki =true; //trueだと下向き
		rnd = new Random();
		icount = 0;
		iswalk = false;

		shell_x = cannon_x_list.get(cannon_number);
		bless_x = doragon_x;
		bless_y = doragon_y;
		doragon_flg = false;
		doragon_up = false;

		is_shimo_super = false;

		angle = 0;
		jump_flg = false;

		dra_jump = 90;
		darara = 256;
	}


}

class Point {
	float x;
	float y;


	public Point(float _x, float _y){
		x = _x;
		y = _y;
	}

	public Point(Point p){
		x = p.x;
		y = p.y;
	}

	public Point() {
		// TODO 自動生成されたコンストラクター・スタブ

	}

	int point_getTileId(int layer, TiledMap map){
		return map.getTileId((int)this.x, (int)this.y, layer);
	}

	void setPoint(Point _P){
		this.x = _P.x;
		this.y = _P.y;
	}

	void setPoint(float _x, float _y){
		this.x = _x;
		this.y = _y;
	}
	void Print(){
		System.out.println("P.x :"+this.x+" P.y :"+this.y);
	}

	boolean equal(Point p){
		boolean ans = false;

		if((int)p.x == (int)this.x && (int)p.y == (int)this.y)
			ans = true;

		return ans;
	}
}
