package main.imagebase;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.Timer;
import main.Driver;

public class CustomWindow extends JFrame //JFrame extention to apply to game GUI window specifications/==============================================================
{
    public CustomWindow(String name, int defaultWidth, int defaultHeight)
    {
        
        this.setTitle(name); //naming of window
        this.setSize(defaultWidth,defaultHeight); //setting window size
        this.setResizable(false); //remove resizability
        this.setUndecorated(true); //removing title bar
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); //initializing position of window at center screen
        
        addWindowListener(new WindowAdapter() //method listening for close window commands from OS-side
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0); //when heard, close the window and terminate program
            }
        });
          
        
        this.addMouseListener(new MouseAdapter()  //method listening for mouse clicks on frame area for initiating window movement
        {
            public void mousePressed(MouseEvent e) 
            {
                if(!e.isMetaDown())
                    {
                        posX = e.getX();
                        posY = e.getY();
                    }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() //method listening for mouse motion to coordinate window movement
        {
            public void mouseDragged(MouseEvent e) 
            {
                setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
            }
        });
        
    }
    
    
 
    private int posX = 0; //coordinates of last mouse click
    private int posY = 0;
    
} //End Window Class/===================================================================================================================================

