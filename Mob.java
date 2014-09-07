import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Mob{
    protected int hp,maxHP,attackPhase;
    protected double x,y,spd,angle,attackCooldown,attackPeriod,lastAttack_start,
	lastAttack_end,lastHeading,shootDelay,lastShot,heading;
    protected Image image;
    protected ImageIcon ii;
    protected boolean alive,attacking;
    protected ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    public Mob(){
        ii = new ImageIcon("Heart.png");
	image = ii.getImage();
	x = 100;
	y = 100;
	spd = 3;
	//angle=x;
	hp=300;
	maxHP=hp;
	attacking=false;
	alive=true;
	attackCooldown=2;
	attackPeriod=0.5;
	shootDelay=0.1;
	lastAttack_end=0;
	lastAttack_start=0;
	lastShot=0;
	heading=0;
    }
    public Mob(double x, double y, double angle, double spd){
	this();
	this.x=x;
	this.y=y;
	this.angle=angle;
	this.spd=spd;
    }
    public Image getPic(){
	return image;
    }
    public double getX(){
	return x;
    }
    public double getY(){
	return y;
    }
    public int getDX(){
	return ii.getIconWidth();
    }
    public int getDY(){
	return ii.getIconHeight();
    }
    public int getHP(){
	return hp;
    }
    public int getMaxHP(){
	return maxHP;
    }
    public void getHit(int dmg){
	hp-=dmg;
    }
    //get spd, get angle.
    public double getSpd(){
	return spd;
    }
    public double getAngle(){
	return angle;
    }
    public double getShootDelay(){
	return shootDelay;
    }
    public void setLastShot(double num){
	lastShot=num;
    }
    public double getLastShot(){
	return lastShot;
    }
    public double getAttackPeriod(){
	return attackPeriod;
    }
    public double getAttackCooldown(){
	return attackCooldown;
    }
    public double getLastAttack_Start(){
	return lastAttack_start;
    }
    public double getLastAttack_End(){
	return lastAttack_end;
    }
    public void setLastAttack_Start(double num){
	lastAttack_start=num;
	//lastAttack refers to timestamp of timer in board.java
    }
    public void setLastAttack_End(double num){
	lastAttack_end=num;
    }
    public boolean isAlive(){
	return alive;
    }
    public void kill(){
	alive=false;
    }
    public boolean isAttacking(){
	return attacking;
    }
    public void clearBullets(){
	bullets= new ArrayList<Bullet>();
    }
    public void attack(int n){
	//n represents the type of attack
	//set LastHeading here after using 1 attack method
	//to create a dependant pattern
	if (n==1){
	    cro(15,2,0);
	}
    }
    public void setAttacking(){
	attacking=true;
    }
    public void endAttack(){
	attacking=false;
    }
    public ArrayList<Bullet> getBullets(){
	return bullets;
    }
    /*used to target
      if (b.getDistTraveled() > 50 && ! b.alreadyTargetted()){
      b.target(c.getX(),c.getY());
      }
    */
    //BASE attack. in rememberance of netlogo.
    public void cro(int number,int spd, double angle){
	int nextAngle = (int)(angle+heading);
	for (int i=0;i<number;i++){
	    bullets.add(new Bullet((double)x,(double)y,nextAngle,spd,"Bullet.png"));
	    nextAngle+=360/number;
	}
	heading+=10;
    }
}
