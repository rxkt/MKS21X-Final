import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Scanner;

//sound + file opening
import java.io.*;
import javax.sound.sampled.*;

public class Board extends JPanel implements ActionListener{
    private Timer timer;
    private double time,lastShot,lastClip,lastAttack,
	last_iFrame,lastBomb,lastCollect,lastContact;
    private int lives,bombs,power,point,grazeCount,scoreCount;
    private int paintPhase,bossStage;
    private Character c;
    private Boss b1;
    private boolean dialogueMode,dialogueFinished,next,gameMode,printDialogue;
    private boolean bombMode,b1summoned,iFrame,hit;
    //when dialogueMode is TRUE, set dialogueFinished to FALSE.
    private String score;
    private ImageIcon bgImageIcon,heartImageIcon,diamondImageIcon;
    private Image background,heart,diamond;
    private ArrayList<Mob> mobs;
    private ArrayList<Loot> loot;
    private ArrayList<SpellImage> SpellImages;
    private ArrayList<BGPanel> panels;
    private Scanner scanner;
    private String source1,source2,name,words;
    private Image image1,image2;
    //for the purposes of non-lag
    //preload into memory?
    private Image projectile,star;
    private Font numFont,textFont;
    public Board(){
	//INITIALIZATION
	setFocusable(true);
	addKeyListener(new TAdapter());
        bgImageIcon = new ImageIcon("background.png");
	background = bgImageIcon.getImage();
        heartImageIcon= new ImageIcon("heart.png");
	heart = heartImageIcon.getImage();
	diamondImageIcon = new ImageIcon("diamond.png");
	diamond = diamondImageIcon.getImage();
	ImageIcon projectile_ii = new ImageIcon("Star.gif");
	projectile = projectile_ii.getImage();
	ImageIcon star_ii = new ImageIcon("Star.png");
	star = star_ii.getImage();
	c = new Character();
	lives=3;
	bombs=3;
	bombMode=false;
	iFrame = false;
	hit=false;
	mobs = new ArrayList<Mob>();
	loot = new ArrayList<Loot>(100);
	SpellImages = new ArrayList<SpellImage>();
	panels = new ArrayList<BGPanel>();
	b1summoned=false;
	power=0;
	lastClip=0;
	scoreCount=0;
	score = new String();
	grazeCount=0;
	//bg music
	try{
	    Clip clip = AudioSystem.getClip();
	    clip.open(AudioSystem.getAudioInputStream(new File("")));
	    clip.loop(Clip.LOOP_CONTINUOUSLY);
	    
	}catch(FileNotFoundException e){
	    System.out.println("You need a BGM Eric. Get one.");
	}catch(Exception e){
	    //no point???
	    e.printStackTrace();
	}
	/*volume control!!!!!!!!!!!!!!!!!! would be nice
	FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	gainControl.setValue(-10.0f);*/
	//FONT
	try{
	    numFont = Font.createFont(Font.TRUETYPE_FONT,new File("TwistedStallions.ttf"));
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(numFont);
	    numFont = numFont.deriveFont(28f);
	    setFont(numFont);
	}catch(IOException|FontFormatException e){
	    e.printStackTrace();
	}

	//test process
	bossStage=0;
	gameMode=false;
	dialogueMode=true;
	dialogueFinished=true;
	printDialogue=false;
	name="";
	words="";
	source1="";
	source2="";
	//to initialize
	
	try{
	    scanner = new Scanner(new File("Dialogue.txt"));
	}catch(FileNotFoundException e){
	    System.out.println("Dialogue file not found.");
	}
	//no more lines: set dialogueFinished
	//when dialogueMode=true && dialogue FINISHED
	//-----gameMode=true, dialogue=false; , dialogueFinished=true
	/*
	gameMode=false;
	dialogueMode=true;
	*/
	timer = new Timer(8,this);
	timer.start();
    }
    public void paint(Graphics g){
	super.paint(g);
	Graphics2D g2d = (Graphics2D)g;

	//CHARACTER ("if char=alive" after death implemented)
	g2d.drawImage(c.getPic(),(int)c.getX()-c.getDX(),(int)c.getY()-c.getDY(),this);
        //PROJECTILES
 	ArrayList<Projectile> projectiles = c.getProjectiles();
	if (projectiles.size() > 0){
	    for (int i=0;i<projectiles.size();i++){
		Projectile p = projectiles.get(i);
		g2d.drawImage(projectile,(int)p.getX()-13,(int)p.getY()-13,this);
	    }
	}
	//LOOT
	if (loot.size() > 0){
	    for (int i=0;i<loot.size();i++){
		Loot l = loot.get(i);
		if (l.getSource().equals("Star.png")){
		    g2d.drawImage(star,(int)(l.getX()-l.getDX()),
			      (int)(l.getY()-l.getDY()),18,18,this);
		}else{
		    g2d.drawImage(l.getPic(),(int)(l.getX()-l.getDX()),
				  (int)(l.getY()-l.getDY()),18,18,this);
		}
	    }
	}
	//BOSS, re-edit to center boss to coordinates after DRAWING THE SPRITE
	if (mobs.size() > 0){
	    for (int i=0;i<mobs.size();i++){
		Mob m = mobs.get(i);
		
		g2d.drawImage(m.getPic(),(int)b1.getX()-16,(int)b1.getY()-36,this);
	        //BULLETS
		ArrayList<Bullet> bullets = m.getBullets();
		if (bullets.size() > 0){
		    for (int j=0;j<bullets.size();j++){
			Bullet b = bullets.get(j);
			if (bossStage!=1){
			    g2d.drawImage(b.getPic(),(int)b.getX()-8,(int)b.getY()-8,this);
			}else{
			    //stage 2 with fancy bullets!!!
			    AffineTransform at = AffineTransform.getTranslateInstance(b.getX(),b.getY());
			    at.scale(1.24,1.24);
			    at.rotate(Math.toRadians(90-b.getAngle()),8,8);
			    g2d.drawImage(b.getPic(),at,this);
			}
		    }
		}
		if ((m instanceof Boss) && b1summoned && b1.isAlive()){
		    g2d.drawImage(b1.getHPBar(),50,50,125*b1.getHP()/b1.getMaxHP()
				  ,5,this);
		}
	    }
	}
	//panels
	if (panels.size() > 0){
	    for (int i=0;i<panels.size();i++){
		if (printDialogue){
		    g2d.drawImage(image1,30,60,this);
		    g2d.drawImage(image2,260,50,this);
				  
		}
		BGPanel p = panels.get(i);
		g2d.drawImage(p.getPic(),(int)p.getX(),(int)p.getY(),
			      (int)p.getWidth(),(int)p.getHeight(),this);
		//dialogue
		if (printDialogue){
		    ////////////////////////////
		    if (name.equalsIgnoreCase("player")){
			g2d.drawString(name,(int)p.getX()+10,(int)p.getY()+20);
		    }else{
			g2d.drawString(name,(int)p.getX()+340,(int)p.getY()+20);
		    }
		    int lines = words.length()/25;
		    for (int j=0;j<lines;j++){
			g2d.drawString(words.substring(j*25,(j+1)*25),(int)p.getX()+20,(int)p.getY()+50+j*20);
		    }
		    g2d.drawString(words.substring((lines)*25),(int)p.getX()+20,(int)p.getY()+50+(lines)*20);
		    
		    
		}
	    }
	}
	//BACKGROUND
	g2d.drawImage(background,0,0,this);
	//INFO
	g2d.drawString("Score", 525,85);
	g2d.drawString(score, 620, 85);
	g2d.drawString("Player",525, 140);
	for(int i=0;i<lives;i++){
	    g2d.drawImage(heart,620+(i*25),120,this);
	}
	g2d.drawString("Spell",525,165);
	for(int i=0;i<bombs;i++){
	    g2d.drawImage(diamond,620+(i*25),145,this);
	}
	g2d.drawString("Power",525,225);
	g2d.drawString(Integer.toString(power),620,225);
	g2d.drawString("Point",525,250);
	g2d.drawString(Integer.toString(point),620,250);
	g2d.drawString("Graze",525,275);
	g2d.drawString(Integer.toString(grazeCount),620,275);
	

	


	/////////////////////
	/////transparency for graphics
	////////////////////
	//SPELLIMAGES
	if (c.isFocused()){
	    g2d.drawImage(c.getHitbox(),(int)c.getX()-35,(int)c.getY()-35,70,70,this);
	}
	if( SpellImages.size() > 0){
	    for (int i=0;i<SpellImages.size();i++){
		SpellImage k = SpellImages.get(i);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
							    k.getAlpha()));
		g2d.drawImage(k.getPic(),(int)k.getX(),(int)k.getY(),this);
		
		
	    }
	    g.dispose();
	}	
	Toolkit.getDefaultToolkit().sync();
	g.dispose();
    }
    //phase determines attack patterns
    public void addBoss(int n){
	//spellNum: 0=general, 1-5=spellcards
	b1 = new Boss(n);
	mobs.add(b1);
	b1summoned=true;
	displaySpell();
    }
    public void addMob(double x, double y,double spd, double angle){
	Mob m = new Mob(x,y,spd,angle);
    }
    public void displaySpell(){
	SpellImages.add(new SpellImage(250,0,"BossImage.png"));
    }
    public void displayBomb(){
	SpellImages.add(new SpellImage(35,150,"PlayerImage.png",-0.6));
    }
    ////////////////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e){
	if (dialogueMode){
	    //change to if hasNext() ==false;
	    if (dialogueFinished){
		dialogueFinished=false;
		panels.add(new BGPanel(50,430,100,0,100,400,"Chatbox.png"));
	    }else{//after dialogeFinshed, set dialogueMode Off FIRST, then dialogueFinished=true
		//open scanner in initialization.
		if (scanner!= null && scanner.hasNextLine() && next){
		    name=scanner.nextLine();
		    words=scanner.nextLine();
		    source1=scanner.nextLine();
		    source2=scanner.nextLine();
		    image1 = (new ImageIcon(source1)).getImage();
		    image2 = (new ImageIcon(source2)).getImage();
		    next=false;
		}else if(scanner != null && ! scanner.hasNext() && next){
		    dialogueMode=false;
		    next=false;
		    name="";
		    words="";
		    image1 = (new ImageIcon("")).getImage();
		    image2 = (new ImageIcon("")).getImage();
		    SpellImages = new ArrayList<SpellImage>();
		}
	    }
	}else{
	    for (int i =0; i<panels.size();i++){
		BGPanel p = panels.get(i);
		if (!dialogueFinished && p.getWidth()>0){
		    p.setTargetWidth(0);
		    dialogueFinished=true;
		}else if(p.getWidth()<=0 || p.getHeight()<=0){
		    panels.remove(p);
		    gameMode=true;
		}
	    }
	}
	for (int i=0;i<panels.size();i++){
	    BGPanel p = panels.get(i);
	    if (!p.adjust() && dialogueMode){
		printDialogue=true;
	    }
	}
	








	//////////////////////////////gameModeStuff
	if (!hit){
	    c.move();
	}
	//CHARACTER MOVE
	if (gameMode){
	    if (!b1summoned && bossStage==0 && time>1){
		addBoss(bossStage);
		//start bossStage
	    }
	    if (c.isShooting() && time-lastShot>0.08){
		c.shoot();
		lastShot=time;
	    }
	    c.syncPower(power);
	    if (c.getY() < 190){
		c.collectLoot(loot);
	    }
	    if (c.isBombing() && bombMode==false && bombs>0 && time-lastBomb>4){
		//1 instance of bombing
		//////////////////////
		bombMode=true;
		c.finishBomb();
		iFrame=true;
		last_iFrame=time;
		lastBomb=time;
		bombs--;
		displayBomb();
		hit=false;
	    }
	}
	if (bombMode){
	    if(time-lastBomb>2){
		bombMode=false;
	    }
	    if(time-lastCollect>0.33){
		for (int i=0;i<mobs.size();i++){
		    ArrayList<Bullet> bullets = mobs.get(i).getBullets();
		    for (int j=0;j<bullets.size();j++){
			Bullet b = bullets.get(j);
			if(Math.abs(b.getX()-c.getX())<100 &&
			   Math.abs(b.getY()-c.getY())<100){
			    loot.add(new Loot(b.getX(),b.getY(),"Star.png"));
			}
		    }
		}
		c.collectLoot(loot);
		lastCollect=time;
	    }
	}
	//PROJECTILES MOVE-DIE(border)
	ArrayList<Projectile> projectiles = c.getProjectiles();
	for (int i=0;i<projectiles.size();i++){
	    Projectile p = projectiles.get(i);
	    if (p.isAlive()){ 
		p.move();
		for(int j=0;j<mobs.size();j++){
		    Mob m = mobs.get(j);
		    if(p.hitMob(m)){
			m.getHit(10);
			projectiles.remove(p);
			scoreCount+=123;
			try{
			    Clip clip = AudioSystem.getClip();
			    clip.open(AudioSystem.getAudioInputStream(new File("se_damage00.wav")));
			    clip.start();
			}catch(FileNotFoundException e1){
			    System.out.println("projectile-mob .wav file not found.");
			}catch(Exception e2){
			    //no point???
			    e2.printStackTrace();
			}
		    }
		}
		//double loop hitMob(m)
		//create arrayList<MobsInMap> in board
		//if collision, set alive=false
		//remove from projectiles list
	    }else{
		projectiles.remove(i);
	    }
	}
	//loot move pattern
	if (loot.size()>0){
	    for (int i =0;i<loot.size();i++){
		Loot l = loot.get(i);
		if(l.isAlive()){
		    if (! l.isAbsorbed()){
			l.move();
		    }else{
			l.target(c.getX(),c.getY());
			l.accelerate();
		    }
		    if (l.getSpd() < 1.25){
			l.accelerate();
		    }
		    if (l.hitChar(c)){
			if (l.getSource().equals("Power.png")){
			    power++;
			    scoreCount+=5000;
			}else if (l.getSource().equals("Point.png")){
			    point++;
			    scoreCount+=3250;
			}else if (l.getSource().equals("Star.png")){
			    scoreCount+=1500;
			}
			loot.remove(l);
			//change to absorbedLoot.wav
			try{
			    Clip clip = AudioSystem.getClip();
			    clip.open(AudioSystem.getAudioInputStream(new File("se_item00.wav")));
			    clip.start();
			}catch(FileNotFoundException e1){
			    //se_plgraze.wav???
			    System.out.println("item collection .wav file not found.");
			}catch(Exception e2){
			    //no point???
			    e2.printStackTrace();
			}
		    }
		}else{
		    loot.remove(i);
		}
	    }
	}
	//SpellImages
	if(SpellImages.size()>0){
	    for(int i=0;i<SpellImages.size();i++){
		SpellImage k = SpellImages.get(i);
		if(k.getAlpha() < 1 && !(k.getClimax())){
		    k.addAlpha(.0075f);
		    k.checkClimax();
		}else if(k.getAlpha() > 0.0075 && k.getClimax()){
		    k.subtractAlpha(.0075f);
		}else if(k.getAlpha() < 0.008 && k.getClimax()){
		    SpellImages.remove(k);
		}
		k.move();
	    }
	}
	//BULLETS MOVE-DIE. Implement boss move.
	//generalize for all mobs for arrayList<Mob>
	if (mobs.size()>0){
	    //boss patterns. make another for mobs
	    for (int k=0;k<mobs.size();k++){
		Mob m = mobs.get(k);
		if ((m instanceof Boss) && b1summoned && b1.isAlive()){
		    if ( time-b1.getLastAttack_End()>b1.getAttackCooldown() &&
			 !b1.isAttacking()){
			b1.setAttacking();
			b1.setLastAttack_Start(time);
		    }
		    if (b1.isAttacking() &&
			time-b1.getLastAttack_Start()>b1.getAttackPeriod()){
			b1.endAttack();
			b1.setLastAttack_End(time);
		    }
		    if (b1.isAttacking()){
			if (time-b1.getLastShot()>b1.getShootDelay()){
			    b1.attack(b1.getCardPhase(),c);
			    b1.setLastShot(time);
			}
		    }
		}
		    
		ArrayList<Bullet> bullets = m.getBullets();
		////
		if (m.getHP()<0){
		    m.kill();
		    mobs.remove(m);
		    if (bossStage < 2){
			bossStage++;
			addBoss(bossStage);
			System.out.println(bossStage);
		    }
		    for (int j=0;j<bullets.size();j++){
			Bullet b = bullets.get(j);
			loot.add(new Loot(b.getX(),b.getY(),"Star.png"));
		    }
		    c.collectLoot(loot);
		    bullets = new ArrayList<Bullet>();
		    for (int i = 0; i<10;i++){
			String path;
			if(Math.random()<0.5){
			    path = "Power.png";
			}else{
			    path = "Point.png";
			}					       
			Loot firstLoot = new Loot(m.getX()+Math.random()*m.getDX()*4-m.getDX()*2,m.getY()+Math.random()*m.getDY()*4-m.getDY()*2,path); 
			firstLoot.reverse();
			loot.add(firstLoot);
		    }
		}
		//give a clean container, no print/interactions after b1 dies
		for (int i=0;i<bullets.size();i++){
		    Bullet b = bullets.get(i);
		    if (b.isAlive()){ 
			b.move();
			if (bossStage==1){
			    if (b.getDistTraveled() >50){
				if (b.getSource().equals("blueBullet.png")){
				    for (int index=0;index<3;index++){
					bullets.add(new Bullet(b.getX(),b.getY(),(int)b.getAngle()-180-(index-1)*15,b.getSpd(),"yellowBullet.png"));
				    }
				    bullets.remove(b);
				}else if(b.getSource().equals("yellowBullet.png")){
				    for (int index=0;index<5;index++){
					bullets.add(new Bullet(b.getX(),b.getY(),(int)b.getAngle()-180-(index-2)*15,b.getSpd(),"redBullet.png"));
				    }
				    bullets.remove(b);
				}
			    }
			}
			if (b.grazeChar(c) && ! b.alreadyGrazed()){
			    grazeCount++;
			    b.setGrazed();
			    //add graze pts @ end of stage
			}
			if (b.hitChar(c) && ! b.alreadyHit() &&
			    iFrame==false && time-lastContact>0.3){
			    try{
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(new File("se_pldead00.wav")));
				clip.start();
			    }catch(FileNotFoundException e1){
				System.out.println("player dying .wav file not found.");
			    }catch(Exception e2){
				//no point???
				e2.printStackTrace();
			    }
			    b.setHit();
			    hit=true;
			    lastContact=time;
			    
			}
		    }else{
			bullets.remove(i);
		    }
		}
	    }
	}
	if (time-last_iFrame>4 && iFrame==true){
	    iFrame=false;
	}
	if (time-lastContact>0.3 && hit){
	    if(lives>0){
		for (int repeats=0;repeats<5;repeats++){
		    Loot freeLoot = new Loot(c.getX()+Math.random()*c.getDX()*4-c.getDX()*2,c.getY()+Math.random()*c.getDY()*4-c.getDY()*2,"Power.png");
				    freeLoot.reverse();
				    loot.add(freeLoot);
		}
		c.losePower();
		c.setX(245);
		c.setY(500);
		lives--;
		for (int i=0;i<mobs.size();i++){
		    Mob m = mobs.get(i);
		    m.clearBullets();
		}
	    }else{
		c.setX(-100);
		c.setY(-100);
		gameMode=false;
		//end all operations under gameMode
	    }
	    iFrame=true;
	    last_iFrame=time;
	    bombs=3;
	    power--;
	    hit=false;
	}
	//INFO.
	score = Integer.toString(scoreCount);
	while(score.length() <9){
	    score="0"+score;
	}
	//paint lags it, paint less
	paintPhase++;
	if (paintPhase==2){
	    repaint();
	    paintPhase=0;
	}
	time+=(1.0/120);
	//TO DO: LIVES. BOMBS. Power/Point?/Graze
    }
    public class TAdapter extends KeyAdapter {
	public void keyReleased(KeyEvent e) {
	    if (gameMode){
		c.keyReleased(e);
	    }
	    int key = e.getKeyCode();
	    if (key == KeyEvent.VK_Z){
		next=true;
	    }
	}
	public void keyPressed(KeyEvent e){
	    if (gameMode){
		c.keyPressed(e);
	    }
	}
    }
}
