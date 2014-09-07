import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Projectile{
    private double x,y;
    private int spd,angle;
    private Image image;
    private ImageIcon ii;
    //determines if the projectile is on screen
    private boolean alive,targetted;
    private String source;
    public Projectile(double x, double y, int angle,int spd, String source){
	this.source=source;
        ii = new ImageIcon(source);
	image = ii.getImage();
	this.x = x;
	this.y = y;
	this.spd=spd;
	this.angle=angle;
	alive=true;
    }
    public Image getPic(){
        return image;
    }
    public int getDX(){
	return ii.getIconWidth()/2;
    }
    public int getDY(){
	return ii.getIconHeight()/2;
    }
    public String getSource(){
	return source;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public boolean alreadyTargetted(){
	return targetted;
    }
    public boolean isAlive() {
        return alive;
    }
    public boolean hitMob(Mob m){
	//check if it is in the square, change 5by5 to appropriate size
	if (Math.abs(x-m.getX())<getDX()+10 &&
	    Math.abs(y-m.getY())<getDY()+10){
	    //do heavy math calculations
	    if (Math.pow(x-m.getX(),2)+Math.pow(y-m.getY(),2)<
		Math.pow(getDX()+10,2)){
		return true;
	    }
	}
	return false;
    }
    //reference angle as DEGREES. move() will convert it to radians
    //as a more coder-friendly option
    public void setAngle(int angle){
	this.angle = angle;
    }
    public void move(){
        x += spd * Math.cos(Math.toRadians(angle));
	y -= spd * Math.sin(Math.toRadians(angle));
	//dead if projectiles outside borders
	if (y<45 || x<25 || x>500 || y>542){
	    alive=false;
	}
    }
    public void target(double x, double y){
	double dist = Math.sqrt(Math.pow(this.x-x,2)+Math.pow(y-this.y,2));
	angle = (int)Math.toDegrees(Math.asin((1.0*y-this.y)/dist));
	//really flippy trig that took me 2 hours to put together
	if(x>this.x){
	    if(y>this.y){
		angle=angle*(-1);
	    }
	}else{
	    if(this.y>y){
		angle=angle*(-1);
	    }
	    angle+=180;
	}
	if (this.y>y){
	    angle=angle*(-1);
	}
	targetted=true;
    }
}
