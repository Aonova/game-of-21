package source;

import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SimulProcess implements Runnable //Class to handle multrithreaded processes running simultaniously/===============================================
{

private final Object object;
private final String desc;
private final Location loc;
public CountDownLatch proc; // synchronized processes (denoted by "Synced") will use this to signify that they have finished processing

private Thread thread;
SimulProcess nextProcess;

    public SimulProcess(String desc) //Barebones constructor for processes not requiring any references passed
    {
        this.object = null;
        this.desc = desc;
        this.loc = null;
        this.proc = new CountDownLatch(0);
    }

    public SimulProcess(Object object, String desc) //Basic constructor
    {
        this.object = object;
        this.desc = desc;
        this.loc = null;
        this.proc = new CountDownLatch(0);
    }
    
    public SimulProcess(Object object, Location var, String desc, CountDownLatch proc) //Full constructor
    {
        this.object = object;
        this.desc = desc;
        this.loc = var;
        this.proc = proc;
    }
    
    public SimulProcess(Object object, String desc, CountDownLatch proc) //Synchronized constructor
    {
        this.object = object;
        this.desc = desc;
        this.loc = null;
        this.proc = proc;
    }
    
    public SimulProcess(String desc, CountDownLatch proc) //Basic Synchronized constructor
    {
        this.object = null;
        this.desc = desc;
        this.loc = null;
        this.proc = proc;
    }
    
    public SimulProcess(Object object, Location var, String desc) //Animation-Oriented Constructor
    {
        this.object = object;
        this.desc = desc;
        this.loc = var;
        this.proc = new CountDownLatch(0);
    }
    
    public void run()
    {
        try{
            
            if(desc.equals("process AcePrompt2"))
            {
                try{Thread.sleep(500);}catch(Exception a){}
                Driver.currentGame.resolveAcePrompts(1);
            }

            if(desc.equals("process DRAWCARD"))
            {
                ((Hand)object).addCard(Driver.masterDeck.pop());
                CountDownLatch local = new CountDownLatch(2);
                new SimulProcess((Object)object, "animation CARDTOHAND", local).start();
                Thread.sleep(300);
                new SimulProcess((Object)object, "process FLIPCARD", local).start();
                local.await();
                proc.countDown();
            }
            
            if(desc.equals("process CloseEndGameMessage Synced"))
            {
                    CountDownLatch localLatch = new CountDownLatch(4); //set proc to 4
                    new SimulProcess(Driver.currentGame.messageBG, "animation JLabelFadeFast Synced", localLatch).start();
                    Thread.sleep(250);
                    new SimulProcess(Driver.currentGame.winMessage, "animation JLabelFadeFast Synced", localLatch).start();
                    new SimulProcess(Driver.currentGame.contButton, "animation JLabelFadeFast Synced", localLatch).start();
                    new SimulProcess(Driver.currentGame.reshuffle, "animation JLabelFadeFast Synced", localLatch).start();
                    
                    localLatch.await(); //wait for proc to reach 0
                    
                    Driver.currentGame.messageBG.setIcon(null);
                    Driver.currentGame.winMessage.setIcon(null);
                    Driver.currentGame.contButton.setIcon(null);
                    Driver.currentGame.reshuffle.setIcon(null);

                    Driver.masterPane.remove(Driver.currentGame.messageBG);
                    Driver.masterPane.remove(Driver.currentGame.winMessage);
                    Driver.masterPane.remove(Driver.currentGame.reshuffle);
                    Driver.masterPane.remove(Driver.currentGame.contButton);

                    Driver.masterPane.repaint();
                    Driver.masterPane.revalidate();
                    
                    proc.countDown();
                    
            }
            
            if(desc.equals("process ClearTable"))
            {
                GameUtilities.desposeHands(proc);
                GameUtilities.desposeDeck(Driver.masterDeck, proc);
                
            }
            
            if(desc.equals("process ClearHand"))
            {
                Hand hand = (Hand)object;
                for (int k = 2; k >= 0; k--)
                {
                    if(hand.hand[k] != null)
                    {
                       new SimulProcess(hand.removeCard(), "process HandCardDispose").start();
                    }
                }
                proc.countDown();
            }
            
            if(desc.equals("process HandCardDispose"))
            {
                Card card = (Card)object;
                proc = new CountDownLatch(1);
                new SimulProcess(card.cardLabel, "animation JLabelFade Synced", proc).start();
                AnimationEngine.linearMove(card.cardLabel, 400, 600, 500);
                proc.await();
                Driver.masterPane.remove(card.cardLabel);
            }
            
            if(desc.equals("process DirectedCardDispose"))
            {
                Card card = (Card)object;
                proc = new CountDownLatch(2);
                new SimulProcess(card.cardLabel, loc, "animation DirectedCardMove Synced", proc).start();
                Thread.sleep(100);
                new SimulProcess(card.cardLabel, "animation CardBackFadeOut Synced", proc).start();
                proc.await();
                Driver.masterPane.remove(card.cardLabel);
            }
            if(desc.equals("animation DirectedCardMove Synced"))
            {
                JLabel label = (JLabel)object;
                AnimationEngine.linearMove(label, loc.xPos, loc.yPos, 500);
                proc.countDown();
            }
            
            if(desc.equals("process FLIPCARD"))
            {
               ((Hand)object).getFinalCard().flip();
               proc.countDown();
            }
            
            if(desc.equals("animation CARDTOHAND"))
            {
                //System.out.println("Notice me senpai"); //debug
                AnimationEngine.linearMove((((Hand)object).hand[((Hand)object).finalCardIndx]).cardLabel, ((Hand)object).positions[((Hand)object).finalCardIndx].xPos, ((Hand)object).positions[((Hand)object).finalCardIndx].yPos, 400);
                proc.countDown();
            }
            if(desc.equals("test animation PSDTEST3"))
            {
                while(true)
                {
                    AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayer("/Resource Files/testAnim.psd"), (JLabel)object, 20);
                    
                }
            }
            
            if(desc.equals("test animation PSDTEST2"))
            {
                while(true)
                {
                    AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayer("/Resource Files/testAnim2.psd"), (JLabel)object, 5);
                    
                }
            }
            
            if(desc.equals("test animation CARDIMAGECHANGE"))
            {
                while(true)
                {
                    ((JLabel)object).setIcon(Driver.cards[0][1].getCardImage());
                    for (int j = 0; j<Driver.cards.length; j++)
                    {
                        for(int k = 0; k<Driver.cards[0].length; k++)
                        {
                            ((JLabel)object).setIcon(Driver.cards[j][k].getCardImage());
                            Thread.sleep(200);
                        }
                    }
                }
            }
            
            if(desc.equals("animation FADEANIM_STARTGAME"))
            {
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeOut(null, ((JLabel)object), 20), (JLabel)object, 30);
                ((JLabel)object).setBounds(((JLabel)object).getX(), ((JLabel)object).getY(), 0, 0);
                ((JLabel)object).setIcon(null);
            }
            
            if(desc.equals("animation ContinueShow"))
            {
                Clickable button = (Clickable)object;
                button.setBounds(Location.getLocation(Driver.COORDINATES, "Center").xPos, Location.getLocation(Driver.COORDINATES, "Center").yPos + 20, 0, 0);
                Driver.masterPane.add(button, new Integer(5), 1);
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeIn(Driver.images[8], null, 30), button, 30);
                button.setName("Continue");
            }
            
            if(desc.equals("animation ReshuffleShow"))
            {
                Clickable button = (Clickable)object;
                button.setBounds(Location.getLocation(Driver.COORDINATES, "Center").xPos, Location.getLocation(Driver.COORDINATES, "Center").yPos + 80, 0, 0);
                Driver.masterPane.add(button, new Integer(5), 1);
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeIn(Driver.images[6], null, 30), button, 30);
                button.setName("Reshuffle");
            }
            
            if(desc.equals("animation DealerWinsShow"))
            {
                JLabel winSign = (JLabel)object;
                winSign.setBounds(Location.getLocation(Driver.COORDINATES, "Center").xPos,Location.getLocation(Driver.COORDINATES, "Center").yPos - 80, 0, 0);
                Driver.masterPane.add(winSign, new Integer(5));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeIn(Driver.images[19],null,30), winSign, 60);
                
            }
            
            if(desc.equals("animation JLabelFade Synced"))
            {
                    JLabel label = (JLabel)object;
                    AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeOut(null,label,30), label, 60);
                    label.setIcon(null);
                    proc.countDown();
            }
            
            if(desc.equals("animation JLabelFadeFast Synced"))
            {
                    JLabel label = (JLabel)object;
                    AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeOut(null,label,15), label, 60);
                    label.setIcon(null);
                    proc.countDown();
            }
            
            if(desc.equals("animation JLabelFadeShort"))
            {
                JLabel label = (JLabel)object;
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeOut(null,label,15), label, 60);
                label.setIcon(null);
            }
            
            if(desc.equals("animation JLabelFade"))
            {
                    JLabel label = (JLabel)object;
                    AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeOut(null,label,30), label, 60);
                    label.setIcon(null);
            }
            
            if(desc.equals("animation CardBackFadeOut Synced"))
            {
                JLabel label = (JLabel)object;
                AnimationEngine.animateSprites_V2(Driver.animations[1], label, 60);
                label.setIcon(null);
                proc.countDown();
            }
            
            if(desc.equals("animation JLabelFadeLong"))
            {
                JLabel label = (JLabel)object;
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeOut(null,label,30), label, 30);
                label.setIcon(null);
            }
            
            if(desc.equals("animation DrawShow"))
            {
                JLabel winSign = (JLabel)object;
                winSign.setBounds(Location.getLocation(Driver.COORDINATES, "Center").xPos,Location.getLocation(Driver.COORDINATES, "Center").yPos - 70, 0, 0);
                Driver.masterPane.add(winSign, new Integer(5));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeIn(Driver.images[21],null,30), winSign, 60);
                
            }
            
            if(desc.equals("animation PlayerWinsShow"))
            {
                JLabel winSign = (JLabel)object;
                winSign.setBounds(Location.getLocation(Driver.COORDINATES, "Center").xPos,Location.getLocation(Driver.COORDINATES, "Center").yPos - 80, 0, 0);
                Driver.masterPane.add(winSign, new Integer(5));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeIn(Driver.images[20],null,30), winSign, 60);
                
            }
                        
            if(desc.equals("animation BACKGROUNDANIM"))
            {
                JLabel bgGraphic = new JLabel(new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/B_Anim_bgSprites.png")).getSubimage(0,0,800,600)));
                bgGraphic.setBounds(0,0,800,600);
                Driver.masterPane.add(bgGraphic, new Integer(-4));
                Thread.sleep(800);
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray(800,600,"/Resource Files/B_Anim_bgSprites.png"), bgGraphic, 40);
            }
            
            if(desc.equals("animation FrameShow"))
            {
                ((Clickable)object).setBounds(((Clickable)object).baseCard.cardLabel.getX(), ((Clickable)object).baseCard.cardLabel.getY(), ((Clickable)object).baseCard.cardLabel.getWidth(), ((Clickable)object).baseCard.cardLabel.getHeight());
                Driver.masterPane.add((Clickable)object, new Integer(2));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayerBackwards("/Resource Files/Anim_AceFrame.psd"), (JLabel)object, 40);
            }
            
            if(desc.equals("animation FrameFade"))
            {
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayer("/Resource Files/Anim_AceFrame.psd"), (JLabel)object, 40);
                ((JLabel)object).setIcon(null);
                Driver.masterPane.remove((JLabel)object);
            }
            
            if(desc.equals("animation MessageBGShow"))
            {
                ((JLabel)object).setBounds(0, 150, 800, 300);
                Driver.masterPane.add((JLabel)object, new Integer(4));
                //AnimationEngine.createSpriteArray_PSDbyLayer_V3("/Resource Files/Anim_MessageBG_IN.psd");
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_JLabelFadeIn(Driver.images[22], null, 30), (JLabel)object, 30);
            }
            
            if(desc.equals("animation Ace1Show"))
            {
                ((Clickable)object).setBounds(((Clickable)object).baseCard.cardLabel.getX()-70, ((Clickable)object).baseCard.cardLabel.getY(), ((Clickable)object).baseCard.cardLabel.getWidth(), ((Clickable)object).baseCard.cardLabel.getHeight());
                Driver.masterPane.add((Clickable)object, new Integer(1));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayerBackwards("/Resource Files/Anim_Ace1.psd"), (JLabel)object, 30);
            }
            
            if(desc.equals("animation Ace1Fade"))
            {
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayer("/Resource Files/Anim_Ace1.psd"), (JLabel)object, 30);
                ((Clickable)object).setIcon(null);
                Driver.masterPane.remove((JLabel)object);
            }
            
            if(desc.equals("animation Ace11Show"))
            {
                ((Clickable)object).setBounds(((Clickable)object).baseCard.cardLabel.getX()+75, ((Clickable)object).baseCard.cardLabel.getY(), ((Clickable)object).baseCard.cardLabel.getWidth(), ((Clickable)object).baseCard.cardLabel.getHeight());
                Driver.masterPane.add((Clickable)object, new Integer(1));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayerBackwards("/Resource Files/Anim_Ace11.psd"), (JLabel)object, 30);
            }
            
            if(desc.equals("animation HitShow"))
            {
                ((Clickable)object).setBounds(((Clickable)object).baseCard.cardLabel.getX()+95, ((Clickable)object).baseCard.cardLabel.getY(), ((Clickable)object).baseCard.cardLabel.getWidth(), ((Clickable)object).baseCard.cardLabel.getHeight());
                Driver.masterPane.add((Clickable)object, new Integer(1));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayerBackwards("/Resource Files/Anim_Hit.psd"), (JLabel)object, 30);
            }
            
            if(desc.equals("animation HitFade"))
            {
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayer("/Resource Files/Anim_Hit.psd"), (JLabel)object, 30);
                ((Clickable)object).setIcon(null);
                Driver.masterPane.remove((JLabel)object);
            }
            
            if(desc.equals("animation StandShow"))
            {
                ((Clickable)object).setBounds(((Clickable)object).baseCard.cardLabel.getX()-100, ((Clickable)object).baseCard.cardLabel.getY(), ((Clickable)object).baseCard.cardLabel.getWidth(), ((Clickable)object).baseCard.cardLabel.getHeight());
                Driver.masterPane.add((Clickable)object, new Integer(1));
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayerBackwards("/Resource Files/Anim_Stand.psd"), (JLabel)object, 30);
            }
            
            if(desc.equals("animation StandFade"))
            {
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayer("/Resource Files/Anim_Stand.psd"), (JLabel)object, 30);
                ((Clickable)object).setIcon(null);
                Driver.masterPane.remove((JLabel)object);
            }
            
            if(desc.equals("animation Ace11Fade"))
            {
                AnimationEngine.animateSprites(AnimationEngine.createSpriteArray_PSDbyLayer("/Resource Files/Anim_Ace11.psd"), (JLabel)object, 30);
                ((Clickable)object).setIcon(null);
                Driver.masterPane.remove((JLabel)object);
            }

            if(desc.equals("test animation CARDMOTION"))
            {
                while (true)
                {
                    AnimationEngine.linearMove((JLabel)object, GameUtilities.randInt(0,800), GameUtilities.randInt(0,600), GameUtilities.randInt(200,2000));
                }
            }

            if(desc.equals("test animation STARTMOTION"))
            {
                while (true)
                {
                    AnimationEngine.linearMove((JLabel)object, GameUtilities.randInt(0,800), GameUtilities.randInt(0,600), GameUtilities.randInt(100,1000));
                }
            }
            
            if(desc.equals("animation CardBackFadeIn Synced"))
            {
                AnimationEngine.animateSprites_V2(Driver.animations[0], (JLabel)object, 80);
                proc.countDown();
            }
            
            if(desc.equals("process INITIATEGAME"))
            {
              Driver.currentGame = new GameInstance();
              Driver.currentGame.runGameProcess();
            }
            
            if(desc.equals("process ResetCards"))
            {
                for (int j = 0; j<Driver.cards.length; j++)
                {
                    for (int k = 0; k<Driver.cards[0].length; k++)
                    {
                        Driver.cards[j][k].setFaceDown();
                    }
                }
                proc.countDown();
            }
            
            if(desc.equals("process UpdateDealerPoints"))
            {
                JLabel pointDisplay = (JLabel)object;
                while(((JLabel)object).isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS);
                    if (Driver.currentGame == null)
                    {
                        pointDisplay.setText("Dealer Points: 0");
                    }
                    else if (Driver.currentGame.dealerHand == null)
                    {
                        pointDisplay.setText("Dealer Points: 0");
                    }
                    else
                    {
                        pointDisplay.setText("Dealer Points: " + String.valueOf(Driver.currentGame.dealerHand.totalPoints));
                    }
                }
            }
            
            if(desc.equals("process UpdatePlayerPoints"))
            {
                JLabel pointDisplay = (JLabel)object;
                while(((JLabel)object).isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS);
                    if (Driver.currentGame == null)
                    {
                        pointDisplay.setText("Player Points: 0");
                    }
                    else if (Driver.currentGame.playerHand == null)
                    {
                        pointDisplay.setText("Player Points: 0");
                    }
                    else
                    {
                        pointDisplay.setText("Player Points: " + String.valueOf(Driver.currentGame.playerHand.totalPoints));
                    }
                }
            }
            
            if(desc.equals("process UpdateRunningPrompts"))
            {
                JLabel pointDisplay = (JLabel)object;
                while(((JLabel)object).isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS);
                    if (Driver.currentGame == null)
                    {
                        pointDisplay.setText("Running Prompts: 0");
                    }
                    else
                    {
                        pointDisplay.setText("Running Prompts: " + String.valueOf(Driver.currentGame.runningPrompts));
                    }
                }
            }
            
            if(desc.equals("process UpdateDeckDisplay"))
            {
                JLabel pointDisplay = (JLabel)object;
                while(((JLabel)object).isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS);
                    
                    if (Driver.masterDeck == null)
                    {
                        pointDisplay.setText("<html>====== Deck ======<br>[NO DECK CREATED]</html>");
                        pointDisplay.setSize(pointDisplay.getPreferredSize().width, pointDisplay.getPreferredSize().height);
                    }
                    else
                    {
                        pointDisplay.setText("<html>====== Deck ======<br>" + Driver.masterDeck.toString() + "</html>");
                        pointDisplay.setSize(pointDisplay.getPreferredSize().width, pointDisplay.getPreferredSize().height);    
                    }
                }
            }
            
            if(desc.equals("process UpdateMaxMemory"))
            {
                JLabel pointDisplay = (JLabel)object;
                Runtime runtime = Runtime.getRuntime();
                
                while(pointDisplay.isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS); //refreshing every .5 seconds
                    pointDisplay.setText("Max Memory (MB): " + runtime.maxMemory() / (1048576.0f));  
                }
            }
            
            if(desc.equals("process UpdateTotalMemory"))
            {
                JLabel pointDisplay = (JLabel)object;
                Runtime runtime = Runtime.getRuntime();
                
                while(pointDisplay.isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS); //refreshing every .5 seconds
                    pointDisplay.setText("Total Memory (MB): " + runtime.totalMemory() / (1048576.0f));  
                }
            }
            
            if(desc.equals("process UpdateFreeMemory"))
            {
                JLabel pointDisplay = (JLabel)object;
                Runtime runtime = Runtime.getRuntime();
                
                while(pointDisplay.isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS); //refreshing every .5 seconds
                    pointDisplay.setText("Free Memory (MB): " + runtime.freeMemory() / (1048576.0f));  
                }
            }
            
            if(desc.equals("process UpdateUsedMemory"))
            {
                JLabel pointDisplay = (JLabel)object;
                Runtime runtime = Runtime.getRuntime();
                
                while(pointDisplay.isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS); //refreshing every .5 seconds
                    pointDisplay.setText("Used Memory (MB): " + (runtime.totalMemory() - runtime.freeMemory()) / (1048576.0));  
                }
            }
            
            if(desc.equals("process UpdateThreadCountA"))
            {
                JLabel pointDisplay = (JLabel)object;
                
                while(pointDisplay.isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS); //refreshing every .5 seconds
                    pointDisplay.setText("Thread Count Active: " + Thread.activeCount());  
                }
            }
            
            if(desc.equals("process UpdateThreadCountB"))
            {
                JLabel pointDisplay = (JLabel)object;
                
                while(pointDisplay.isVisible()==true)
                {
                    Thread.sleep(Driver.PREFFERED_UPDATE_TIME_MS); //refreshing every .5 seconds
                    pointDisplay.setText("Thread Count Total: " + ManagementFactory.getThreadMXBean().getThreadCount());  
                }
            }
            
            
        }catch (Exception error)
        {
            System.err.println("Error in Thread #" + this.thread.getId() + ": " + error);
            System.exit(0);
        }
    }
    
    public String getDesc()
    {
        return this.desc;
    }
    
    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this, desc);
            thread.start();
        }
    }
}//End SimulProcess Class/===============================================================================================================================