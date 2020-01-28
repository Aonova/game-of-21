package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CountDownLatch;
import javax.swing.JLabel;

public class Clickable extends JLabel //Extension to JLabel to bring basic button-like capabilities/=========================================================
{
    Clickable self = this;
    
    private boolean everClicked = false;
    
    public Object[] relatedObjs = new Object[10]; //to orginize references to related objects to the Clickable, up to potentially 10
    
    public Card baseCard;  //to handle card specific clickables; could include it in "related object".... but whatever
    
    private boolean isClicked = false;
    
    private byte toggle = 0; //to handle toggled buttons
    
    public CountDownLatch proc = new CountDownLatch(0); //proc will be decrimented when click's processing finishes; if synchronity is necessary
    
    public Clickable(String name)
    {
        this.setName(name);
        addMouseListeners();
    }
    
    public Clickable(String name, CountDownLatch proc)
    {
        this.setName(name);
        this.proc = proc;
        addMouseListeners();   
    }
    
    private void addMouseListeners()
    {
        
        this.addMouseListener(new MouseAdapter()  //method listening for mouse events in "Clickable" JLabels
        {
            public void mouseEntered(MouseEvent e) //waiting for mouse entering the area of the Clickable, to change graphic to "on" state
            {
                if (self.getName().equals("Continue"))
                {
                    self.setIcon(Driver.images[23]);
                }
                if (self.getName().equals("Reshuffle"))
                {
                    self.setIcon(Driver.images[7]);
                }
                if (self.getName().equals("Hit"))
                {
                    self.setIcon(Driver.images[16]);
                }
                if (self.getName().equals("Stand"))
                {
                    self.setIcon(Driver.images[18]);
                }
                if (self.getName().equals("Ace 1"))
                {
                    self.setIcon(Driver.images[12]);
                }
                if (self.getName().equals("Ace 11"))
                {
                    self.setIcon(Driver.images[14]);
                }
                if (self.getName().equals("Test Start Button"))
                {
                    self.setIcon(Driver.images[1]);           
                }
                if (self.getName().equals("Play Game Button"))
                {
                    self.setIcon(Driver.images[3]);           
                }
                if (self.getName().equals("Close Game Button"))
                {
                    self.setIcon(Driver.images[5]);           
                }
                
                
            }
            
            public void mouseExited(MouseEvent e) //waiting for mouse exiting the area of the Clickable, to change graphic to "off" state
            {
                if (self.getName().equals("Reshuffle"))
                {
                    self.setIcon(Driver.images[6]);
                }
                if (self.getName().equals("Continue"))
                {
                    self.setIcon(Driver.images[8]);
                }
                if (self.getName().equals("Hit"))
                {
                    self.setIcon(Driver.images[15]);
                }
                if (self.getName().equals("Stand"))
                {
                    self.setIcon(Driver.images[17]);
                }
                if (self.getName().equals("Ace 1"))
                {
                    self.setIcon(Driver.images[11]);
                }
                if (self.getName().equals("Ace 11"))
                {
                    self.setIcon(Driver.images[13]);
                }
                if (self.getName().equals("Test Start Button"))
                {
                    self.setIcon(Driver.images[0]);
                }
                if (self.getName().equals("Play Game Button"))
                {
                    self.setIcon(Driver.images[2]);           
                }
                if (self.getName().equals("Close Game Button"))
                {
                    self.setIcon(Driver.images[4]);           
                }

            }
            
            public void mousePressed(MouseEvent e) //waiting for a mouse press on the area of the Clickable
            {
                self.isClicked = true;
            }
            
            public void mouseReleased(MouseEvent e) //waiting for a mouse release
            {
                if ((self.isClicked == true) && (e.getX() > 0) && (e.getX() < self.getWidth()) && (e.getY() > 0) && (e.getY() < self.getHeight())) //checking if mouse was pressed and released on the area of the Clickable
                {
                    
                    if (self.getName().equals("Continue"))
                    {
                        self.setName("Clicked");
                        Driver.currentGame.reshuffle.setName("Clicked");
                        new SimulProcess("CUSTOM process ContinueClicked", new CountDownLatch(1))
                        {
                            public void run()
                            {
                                try{
                                    CountDownLatch localLatch = new CountDownLatch(1);
                                    new SimulProcess(null, "process CloseEndGameMessage Synced", localLatch).start();                                    
                                    localLatch.await();
                                    localLatch = new CountDownLatch(2);
                                    GameUtilities.desposeHands(localLatch);
                                    localLatch.await();
                                    Driver.currentGame.reinitialize();
                                    Driver.currentGame.runGameProcess();
                                }catch(Exception error){}
                            }
                        }.start();
                        
                        proc.countDown();
                    }
                    
                    if (self.getName().equals("Reshuffle"))
                    {
                        self.setName("Clicked");
                        Driver.currentGame.contButton.setName("Clicked");
                        new SimulProcess("CUSTOM process ReshuffleClicked")
                        {
                            
                            public void run()
                            {
                                try{
                                    CountDownLatch localLatch = new CountDownLatch(1);
                                    new SimulProcess("process CloseEndGameMessage Synced", localLatch).start();
                                    localLatch.await();
                                    localLatch = new CountDownLatch(3);
                                    new SimulProcess("process ClearTable", localLatch).start();
                                    localLatch.await();
                                    localLatch = new CountDownLatch(1);
                                    new SimulProcess("process ResetCards", localLatch).start();
                                    localLatch.await();
                                    new SimulProcess("process INITIATEGAME").start();
                                    

                                }catch(Exception e){System.out.println(e);}
                            }
                        }.start();
                        
                        proc.countDown();
                    }
                    
                    if (self.getName().equals("Stand"))
                    {
                        self.setName("Clicked");
                        ((Clickable)self.relatedObjs[0]).setName("Clicked");
                        new SimulProcess((Object)self,"animation StandFade").start();
                        new SimulProcess((Object)self.relatedObjs[0],"animation HitFade").start();
                        new SimulProcess((Object)self.relatedObjs[1],"animation FrameFade").start();
                        
                        
                        Driver.currentGame.runningPrompts -= 1;
                        Driver.currentGame.gameMoveHit = false;   
                        
                        proc.countDown();
                    }
                    
                    if (self.getName().equals("Hit"))
                    {
                        self.setName("Clicked");
                        ((Clickable)self.relatedObjs[0]).setName("Clicked");
                        new SimulProcess((Object)self,"animation HitFade").start();
                        new SimulProcess((Object)self.relatedObjs[0],"animation StandFade").start();
                        new SimulProcess((Object)self.relatedObjs[1],"animation FrameFade").start();
                        new SimulProcess((Object)Driver.currentGame.playerHand, "process DRAWCARD").start(); 
                        new SimulProcess(null, "process AcePrompt2").start();
                        
                        
                        Driver.currentGame.gameMoveHit = true;
                        
                        proc.countDown();
                    }
                    
                    if (self.getName().equals("Ace 1"))
                    {
                        self.setName("Clicked");
                        ((Clickable)self.relatedObjs[0]).setName("Clicked");
                        new SimulProcess((Object)self,"animation Ace1Fade").start();
                        new SimulProcess((Object)self.relatedObjs[0],"animation Ace11Fade").start();
                        new SimulProcess((Object)self.relatedObjs[1],"animation FrameFade").start();
                        
                        
                        Driver.currentGame.playerHand.totalPoints += 1;
                        Driver.currentGame.runningPrompts -= 1;
                        
                        proc.countDown();
                    }

                    if (self.getName().equals("Ace 11"))
                    {
                        self.setName("Clicked");
                        ((Clickable)self.relatedObjs[0]).setName("Clicked");
                        new SimulProcess((Object)self,"animation Ace11Fade").start();
                        new SimulProcess((Object)self.relatedObjs[0],"animation Ace1Fade").start();
                        new SimulProcess((Object)self.relatedObjs[1],"animation FrameFade").start();
                        
                        
                        Driver.currentGame.playerHand.totalPoints += 11;
                        Driver.currentGame.runningPrompts -= 1;
                        
                        proc.countDown();
                    }

                    if (self.getName().equals("Play Game Button"))
                    {
                        self.setName("Clicked");     
                        new SimulProcess(null,"animation BACKGROUNDANIM").start();
                        new SimulProcess((Object)self,"process INITIATEGAME").start();
                        new SimulProcess((Object)self,"animation JLabelFade").start();
                        
                        
                        proc.countDown();
                    }
                    if (self.getName().equals("Close Game Button"))
                    {
                        self.setName("Clicked");
                        AnimationEngine.windowFade(Driver.masterWindow, 0.0f, 500);
                        System.exit(0);
                    }
                    if (self.getName().equals("Debug Show"))
                    {
                        if (everClicked == false)
                        {
                            Debug.formatDebug();
                            everClicked = true;
                        }
                        if (self.toggle == 0)
                        {
                            Driver.debugMode = true;
                            
                            Debug.debugDiagBG.setVisible(true);
                            
                            Debug.playerPoints.setVisible(true);
                            new SimulProcess(Debug.playerPoints, "process UpdatePlayerPoints").start();

                            Debug.dealerPoints.setVisible(true);
                            new SimulProcess(Debug.dealerPoints, "process UpdateDealerPoints").start();

                            Debug.runningPrompts.setVisible(true);
                            new SimulProcess(Debug.runningPrompts, "process UpdateRunningPrompts").start();
                            
                            Debug.deckDisplay.setVisible(true);
                            new SimulProcess(Debug.deckDisplay, "process UpdateDeckDisplay").start();

                            Debug.maxMemory.setVisible(true);
                            new SimulProcess(Debug.maxMemory, "process UpdateMaxMemory").start();

                            Debug.totalMemory.setVisible(true);
                            new SimulProcess(Debug.totalMemory, "process UpdateTotalMemory").start();

                            Debug.freeMemory.setVisible(true);
                            new SimulProcess(Debug.freeMemory, "process UpdateFreeMemory").start();

                            Debug.usedMemory.setVisible(true);
                            new SimulProcess(Debug.usedMemory, "process UpdateUsedMemory").start();
                            
                            Debug.threadCountA.setVisible(true);
                            new SimulProcess(Debug.threadCountA, "process UpdateThreadCountA").start();
                            
                            Debug.threadCountB.setVisible(true);
                            new SimulProcess(Debug.threadCountB, "process UpdateThreadCountB").start();
                            
                            

                            self.toggle = 1;
                        }
                        
                        else if (self.toggle == 1)
                        {
                            Debug.playerPoints.setVisible(false);
                            Debug.dealerPoints.setVisible(false);
                            Debug.runningPrompts.setVisible(false);
                            Debug.deckDisplay.setVisible(false);
                            Debug.maxMemory.setVisible(false);
                            Debug.totalMemory.setVisible(false);
                            Debug.freeMemory.setVisible(false);
                            Debug.usedMemory.setVisible(false);
                            Debug.threadCountA.setVisible(false);
                            Debug.threadCountB.setVisible(false);
                            Debug.debugDiagBG.setVisible(false);
                            
                            Driver.debugMode = false;
                            

                            self.toggle = 0;
                        }
                    }
                }
                else
                {
                    self.isClicked = false;
                }
            }
        });
    }

}//End Clickable Class/=================================================================================================================================
