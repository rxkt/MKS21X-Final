import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame{
    public Window(){
	int width = 800, height = 600;
	//TO DO: make a panel in TITLE MODE
	///////////////////////////////////
	//panel in GAME MODE.
	add(new Board());
	//set default close
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(width,height);
	//centers window
	setLocationRelativeTo(null);
	setTitle("Title");
	setResizable(false);
	setVisible(true);	
    }
    public static void main(String[] args){
	new Window();
    }
}
