import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Loot{
    protected double x,y,spd;
    protected int angle;
    protected Image image;
    protected ImageIcon ii;
    protected boolean alive,absorbed;
    protected String source;
    //if absorbed: target. else MOVE
    public Loot(double x, double y,String source){
	this.x=x;
	this.y=y;
	alive=true;
	absorbed=false;
	spd=1.25;
	this.source = source;
	ii = new ImageIcon(source);
	image = ii.getImage();	
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
    public boolean hitChar(Character c){
	//20x20 hitbox for loot pickup
	if (Math.abs(x-c.getX())<getDX()+20 &&
	    Math.abs(y-c.getY())<getDY()+20){
	    //do heavy math calculations
	    if (Math.pow(x-c.getX(),2)+Math.pow(y-c.getY(),2)<
		Math.pow(getDX()+20,2)){
		return true;
	    }
	}
	return false;
    }
    public double getSpd(){
	return spd;
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
    public void move(){
        y+=spd;
	if (y<45 || x<25 || x>500 || y>542){
	    alive=false;
	}
    }
    public void reverse(){
	spd*=-1;
    }
    public void accelerate(){
	spd+=0.05;
    }
    public void accelerate(double a){
	spd+=a;
    }
    public Boolean isAbsorbed(){
	return absorbed;
    }
    public void setSpd(int n){
	spd=n;
    }
    public void setAbsorbed(){
	absorbed=true;
    }
    public boolean isAlive(){
	return alive;
    }
    //this version of target moves forward
    public void target(double x, double y){
	double dist = Math.sqrt(Math.pow(this.x-x,2)+Math.pow(y-this.y,2));
	angle = (int)Math.toDegrees(Math.asin((y-this.y)/dist));
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
	this.x+=spd*(Math.cos(Math.toRadians(angle)));
	this.y-=spd*(Math.sin(Math.toRadians(angle)));
    }
}
