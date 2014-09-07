//methods

//variables X,Y,SPEED,IMAGE

//constructor
//getX, getY
//move(randomX,randomY)?
//attack1,2,3...etc
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Boss extends Mob{
    private int cardPhase,numOfSpells,attackPhase;
    private Image HPimage;
    private boolean setup;
    
    public Boss(int n){
	ImageIcon ii = new ImageIcon("BossIcon.png");
	image = ii.getImage();
	ImageIcon HPii = new ImageIcon("HP Bar.jpg");
	HPimage = HPii.getImage();
	x=245; 
	y=100;
	spd=3;
	hp=1000;
	cardPhase=0;
	alive=true;
	attackPeriod=1;
	attackCooldown=1;
	numOfSpells=5;
	cardPhase=n;
	setup=false;
	attackPhase=0;
    }
    public int getNumOfSpells(){
	return numOfSpells;
    }
    public void attack(int n,Character c){
	if (n==0){
	    generalAttack(n+15,2,0);
	}else if(n==1){
	    spell1(c);
	}else if(n==2){
	    spell2(21,1,0);
	}
    }
    public void generalAttack(int number,int spd, double angle){
	int nextAngle = (int)(angle+heading);
	for (int i=0;i<number;i++){
	    bullets.add(new Bullet((double)x,(double)y,nextAngle,spd,"Bullet.png"));
	    nextAngle+=360/number;
	}
	heading+=10;
    }
    public int getCardPhase(){
	return cardPhase;
    }
    public void spell1(Character c){
	Bullet b =(new Bullet(x,y,(int)heading,1.5,"blueBullet.png"));
	bullets.add(b);
	heading+=20;
    }
    //only doing 2 spellcards because lazy af
    public void spell2(int number,double spd, double angle){
	shootDelay=0.15;
        attackPeriod=0.25;
	attackCooldown=0;
	int nextAngle = (int)(angle+heading);
	for (int i=0;i<number;i++){
	    //name each of 10 bullets x.png
	    //source = i+".png";
	    String source = "star";
	    source+=(i%7)+".png";
	    bullets.add(new Bullet((double)x,(double)y,nextAngle,spd,source));
	    nextAngle+=360/number;
	}
	heading+=10;

    }
    public Image getHPBar(){
	return HPimage;
    }
}
 
