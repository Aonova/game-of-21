package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;

public class Debug 
{
    public static JLabel debugDiagBG = new JLabel();
    public static JLabel debugMessage = new JLabel();
    
   
    public static JLabel playerPoints = new JLabel();
    public static JLabel dealerPoints = new JLabel();
    public static JLabel runningPrompts = new JLabel();
    public static JLabel deckDisplay = new JLabel();
    public static JLabel maxMemory = new JLabel();
    public static JLabel totalMemory = new JLabel();
    public static JLabel freeMemory = new JLabel();
    public static JLabel usedMemory = new JLabel();
    public static JLabel threadCountA = new JLabel();
    public static JLabel threadCountB = new JLabel();
    
    
    
    
    public static void formatDebug()
    {
        Font debugFont = new Font("Arial", 0, 9);
        Color debugColor = Color.WHITE;
        Color bgColor = new Color(0,0,0,100);
        
        debugDiagBG.setBounds(25, 20, 315, 600);
        debugDiagBG.setIcon(AnimationEngine.createColorRectangle_JLabelBounds(debugDiagBG, bgColor));
        Driver.masterPane.add(debugDiagBG,new Integer(1));
        
        playerPoints.setBounds(40, 40, 150, 20);
        playerPoints.setFont(debugFont);
        playerPoints.setForeground(debugColor);
        Driver.masterPane.add(playerPoints,new Integer(2));
        
        dealerPoints.setBounds(40, 55, 150, 20);
        dealerPoints.setFont(debugFont);
        dealerPoints.setForeground(debugColor);
        Driver.masterPane.add(dealerPoints,new Integer(2));
        
        runningPrompts.setBounds(40, 70, 150, 20);
        runningPrompts.setFont(debugFont);
        runningPrompts.setForeground(debugColor);
        Driver.masterPane.add(runningPrompts,new Integer(2));
        
        deckDisplay.setBounds(40, 90, 150, 20);
        deckDisplay.setFont(debugFont);
        deckDisplay.setForeground(debugColor);
        deckDisplay.setMaximumSize(new Dimension(150,500));
        Driver.masterPane.add(deckDisplay,new Integer(2));
    
        maxMemory.setBounds(160, 40, 150, 20);
        maxMemory.setFont(debugFont);
        maxMemory.setForeground(debugColor);
        Driver.masterPane.add(maxMemory,new Integer(2));
    
        totalMemory.setBounds(160, 55, 150, 20);
        totalMemory.setFont(debugFont);
        totalMemory.setForeground(debugColor);
        Driver.masterPane.add(totalMemory,new Integer(2));

        freeMemory.setBounds(160, 70, 150, 20);
        freeMemory.setFont(debugFont);
        freeMemory.setForeground(debugColor);
        Driver.masterPane.add(freeMemory,new Integer(2));
    
        usedMemory.setBounds(160, 85, 150, 20);
        usedMemory.setFont(debugFont);
        usedMemory.setForeground(debugColor);
        Driver.masterPane.add(usedMemory,new Integer(2));
        
        threadCountA.setBounds(160, 100, 150, 20);
        threadCountA.setFont(debugFont);
        threadCountA.setForeground(debugColor);
        Driver.masterPane.add(threadCountA,new Integer(2));
        
        threadCountB.setBounds(160, 115, 150, 20);
        threadCountB.setFont(debugFont);
        threadCountB.setForeground(debugColor);
        Driver.masterPane.add(threadCountB,new Integer(2));
    }

}
