import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
public class Character{
    //image directory of character
    private String imageSrc;
    //get Board to character.setPower() after obtaining loot?
    //then use power to change attack pattern
    private double x,y,spd;
    private int power,shootPhase;
    private Image image,hitboxImage;
    private ImageIcon ii,hitbox_ii;
    private boolean left,right,up,down,shooting,bombing,focus;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    public Character(){
        ii = new ImageIcon("Character.gif");
        image = ii.getImage();
	hitbox_ii = new ImageIcon("Hitbox.png");
	hitboxImage = hitbox_ii.getImage();
	//change coordinates of spawn
        x = 245;
        y = 500;
	shootPhase=0;
	left=false;
	right=false;
	up=false;
	down=false;
	shooting=false;
	spd=3.5;
    }
    //public drainStarsFromMonster?()
    public void move(){
        if (left && x>40){
	    x-=spd;
	}
	if (right && x<480){
	    x+=spd;
	}
	if (up && y>50){
	    y-=spd;
	}
	if (down && y<510){
	    y+=spd;
	}
    }
    public ArrayList<Projectile> getProjectiles(){
	return projectiles;
    }
    public void shoot(){
	shootPhase++;
	//if shootphase=16 then set to 0
	if (shooting){
	    //base attack
	    projectiles.add(new Projectile(x-10,y-20,90,14,"star.gif"));
	    projectiles.add(new Projectile(x+10,y-20,90,14,"star.gif"));
	
	    if (power>=16 && shootPhase==4){
		projectiles.add(new Projectile(x-20,y,90,6,"star.gif"));
		projectiles.add(new Projectile(x+20,y,90,6,"star.gif"));
	    }
	    if (shootPhase>8){
		shootPhase=0;
	    }
	}
	//final resetting shootPhase
    }
    
    //create a method focus() which adds a "slow down + focus" animation on top.
    //create a method leftRight() which changes the animation of the character to move left/right
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double x){
	this.x=x;
    }
    public void setY(double y){
	this.y=y;
    }
    public void addPower(){
	power++;
    }
    //losing power after dying
    public void losePower(){
	power-=10;
    }
    public void syncPower(int n){
	power = n;
    }
    public int getPower(){
	return power;
    }
    public int getDX(){
	return ii.getIconWidth()/2;
    }
    public int getDY(){
	return ii.getIconHeight()/2;
    }
    public Image getPic(){
        return image;
    }
    public Image getHitbox(){
	return hitboxImage;
    }
    public boolean isShooting(){
	return shooting;
    }
    public boolean isBombing(){
	return bombing;
    }
    public void finishBomb(){
	bombing=false;
    }
    public boolean isFocused(){
	return focus;
    }
    //change spd=5 @ collectLoot, target forever
    public void collectLoot(ArrayList<Loot> loot){
	for (int i =0;i<loot.size();i++){
	    Loot l = loot.get(i);
	    if (! l.isAbsorbed()){
		l.setSpd(5);
		l.setAbsorbed();
	    } 
	}
    }
    public synchronized void keyPressed(KeyEvent e){	
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT){
            left=true;
        }
	if (key == KeyEvent.VK_RIGHT){
            right=true;
	}
	if (key == KeyEvent.VK_UP){
            up=true;
        }
	if (key == KeyEvent.VK_DOWN){
            down=true;
	}
	if (key == KeyEvent.VK_Z){
	    shooting=true;
	}
	if (key == KeyEvent.VK_SHIFT){
	    focus=true;
	    spd=1.66;
	}
	if (key == KeyEvent.VK_X){
	    bombing=true;
	}
    }
    public synchronized void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT){
            left=false;
        }
        if (key == KeyEvent.VK_RIGHT){
            right=false;
        }
        if (key == KeyEvent.VK_UP){
            up=false;
        }
        if (key == KeyEvent.VK_DOWN){
            down=false;
        }
	if (key == KeyEvent.VK_Z){
	    shooting=false;
	}
	if (key == KeyEvent.VK_SHIFT){
	    focus=false;
	    spd=3.5;
	}
	if (key == KeyEvent.VK_X){
	    bombing=false;
	}
    }
}
