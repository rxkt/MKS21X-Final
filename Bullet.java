//methods

//variables X,Y,SPEED, ANGLE, IMAGE,boolean ALIVE,[targetX,targetY for streamed grazing]

//constructor
//getX, getY
//move()
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Bullet{
    private double x,y,angle,spd;
    private int distTraveled;
    private Image image;
    private ImageIcon ii;
    private boolean alive,targetted,grazed,hit;
    private String source;
    private double targetX,targetY;
    private int ID;
    public Bullet(double x, double y, int angle, double spd, String source){
	this.source=source;
        ii = new ImageIcon(source);
	image = ii.getImage();
	this.x=x;
	this.y=y;
	this.spd=spd;
	this.angle=angle;
	alive=true;
	grazed=false;
	hit=false;
    }
    public Image getPic(){
	return image;
    }
    public Image rotateImage(Image img,double degree){
        BufferedImage bufImg = (BufferedImage)img;
        double angle = Math.toRadians(degree);
	
        return rotate(bufImg,angle);
    }
    public GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
    public BufferedImage rotate(BufferedImage image, double angle) {
	
	double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	int w = image.getWidth(), h = image.getHeight();
	int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
	GraphicsConfiguration gc = getDefaultConfiguration();
	BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
	Graphics2D g = result.createGraphics();
	g.translate((neww-w)/2, (newh-h)/2);
	g.rotate(angle, w/2, h/2);
	g.drawRenderedImage(image, null);
	g.dispose();
        
	return result;
    }
    public ImageIcon getImageIcon(){
	return ii;
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
    public double getAngle(){
	return angle;
    }
    public double getTargetX(){
	return targetX;
    }
    public double getTargetY(){
	return targetY;
    }
    public void setID(int n){
	ID=n;
    }
    public double getID(){
	return ID;
    }
    public boolean alreadyTargetted(){
	return targetted;
    }
    public int getDistTraveled(){
	return distTraveled;
    }
    public boolean isAlive(){
	return alive;
    }
    public boolean alreadyGrazed(){
	return grazed;
    }
    public void setGrazed(){
        grazed=true;
    }
    public boolean alreadyHit(){
	return hit;
    }
    public void setHit(){
	hit=true;
    }
    //returns the discrepancies of top left corner and x,y coords of the center (for picture)
    public int getDX(){
	return ii.getIconWidth()/2;
    }
    public int getDY(){
	return ii.getIconHeight()/2;
    }
    public double getSpd(){
	return spd;
    }
    //MOVING methods.
    public void move(){
        x += spd * Math.cos(Math.toRadians(angle));
	y -= spd * Math.sin(Math.toRadians(angle));
	distTraveled+=spd;
	//dead if projectiles outside borders
	if (y<45 || x<25 || x>500 || y>542){
	    alive=false;
	}
    }
    public boolean hitChar(Character c){
	//check if it is in the 4x4 square
	if (Math.abs(x-c.getX())<getDX()+2 &&
	    Math.abs(y-c.getY())<getDY()+2){
	    //do heavy math calculations
	    if (Math.pow(x-c.getX(),2)+Math.pow(y-c.getY(),2)<
		Math.pow(getDX()+2,2)){
		return true;
	    }
	}
	return false;
    }
    public boolean grazeChar(Character c){
	//check if it is in the 4x4 square
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
    //public void rotate
    public void setAngle(double angle){
	this.angle=angle;
    }
    //rotates counterclockwise by angle
    public void rotate(double angle){
	this.angle+=angle;
    }
    //edits angle. use c.getX and c.getY to aim @ character
    public void target(double x, double y){
	targetX=x;
	targetY=y;
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
