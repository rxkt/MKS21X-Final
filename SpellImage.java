import javax.swing.*;
import java.awt.*;
public class SpellImage{
    private Image image;
    private ImageIcon ii;
    private String source;
    private double x,y;
    private double spd;
    private Float alpha;
    private boolean climax;
    public SpellImage(double x, double y, String source){
	this.x=x;
	this.y=y;
	ii = new ImageIcon(source);
	this.source =source;
	image = ii.getImage();
	spd=0.6;
	alpha=0f;
	climax=false;
    }
    public SpellImage(double x, double y, String source, double spd){
	this(x,y,source);
	this.spd=spd;
    }
    //if climax = true, then it has reached the top of its parabolic route
    //if climax==true && alpha=0, then it has comleted its phases
    //remove spellImage out of ArrayList<SpellImage> spellImages in board.java
    public Image getPic(){
	return image;
    }
    public float getAlpha(){
	return alpha;
    }
    public void setAlpha(Float f){
	alpha=f;
    }
    public void addAlpha(Float f){
	alpha+=f;
    }
    public void subtractAlpha(Float f){
	alpha-=f;
    }
    public double getX(){
	return x;
    }
    public double getY(){
	return y;
    }
    public double getSpd(){
	return spd;
    }
    public void move(){
	y+=spd;
    }
    public boolean getClimax(){
	return climax;
    }//addgetclimax u forgot in class lulz waboo

    public void checkClimax(){
	if(alpha > .9){
	    climax = true;
	}
    }
    //in board:
    
}
