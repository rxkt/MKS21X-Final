import java.awt.*;
import javax.swing.*;

public class BGPanel{
    private String source;
    
    private double h,w,targetH,targetW;
    private double startH,startW;
    //target denotes the "goal" height and width when expanding.
    public double x,y;
    //coords
    private Image image;
    private ImageIcon ii;
    public BGPanel(double x, double y, String source){
	this.x=x;
	this.y=y;
	ii = new ImageIcon(source);
	image = ii.getImage();
    }	
    public BGPanel(double x, double y,double h,double w,double tH,double tW,String source){
	this.x=x;
	this.y=y;
	this.h=h;
	this.w=w;
	startH=h;
	startW=w;
	targetH=tH;
	targetW=tW;
	this.source=source;
	ii = new ImageIcon(source);
	image = ii.getImage();
    }
    public double getX(){
	return x;
    }
    public double getY(){
	return y;
    }
    public double getHeight(){
	return h;
    }
    public double getTargetHeight(){
	return targetH;
    }
    public void setTargetHeight(double h){
	startH=this.h;
	targetH=h;
    }
    public double getWidth(){
	return w;
    }
    public double getTargetWidth(){
	return targetW;
    }
    public void setTargetWidth(double w){
	startW=this.w;
	targetW=w;
    }
    public Image getPic(){
	return image;
    }
    public boolean adjust(){
	boolean changed=false;
	if (h!= targetH){
	    double dH= (targetH-startH)/200;
	    h+=dH;
	    changed=true;
	}
	if (w!= targetW){
	    double dW= (targetW-startW)/200;
	    w+=dW;
	    changed = true;
	}
	return false;
    }   
}
    
