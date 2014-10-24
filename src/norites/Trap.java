package norites;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class Trap {
	
	final int CANNON_ID = 3;

	Image cannon,shell,shimo_normal,shimo_super;
	ArrayList<Integer> cannon_x_list = new ArrayList<>();
	ArrayList<Integer> cannon_y_list = new ArrayList<>();
	int cannon_number = 0;
	boolean shell_flag = true;
	int shell_x = 0;
	
	public void controlling (Input in){
		if(in.isKeyDown(in.KEY_1)){
			cannon_number = 0;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
		if(in.isKeyDown(in.KEY_2)){
			cannon_number = 1;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
		if(in.isKeyDown(in.KEY_3)){
			cannon_number = 2;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
		if(in.isKeyDown(in.KEY_4)){
			cannon_number = 3;
			shell_x = cannon_x_list.get(cannon_number)-64;
		}
	}
	
	public void Trap_render(Graphics g){
		shell_x--;
		
		if(shell_x<=64)
			shell_x = cannon_x_list.get(cannon_number)-64;
								
		if(shell_flag)
			g.drawImage(shell, shell_x, cannon_y_list.get(cannon_number));
				           
	}
}
