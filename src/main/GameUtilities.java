package main;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JLabel;

public class GameUtilities //Static class containing various useful utilties for the program/=================================================================
{
    public static void createCards(BufferedImage[][] sprites)
    {
        Driver.cards = new Card[4][13];
        String[][] names = new String[4][13];
        
        for (int j = 0; j<names.length; j++)
        {
            String b = "";
            
            if (j == 0)
                b = " of Clubs";
            if (j == 1)
                b = " of Spades";
            if (j == 2)
                b = " of Diamonds";
            if (j == 3)
                b = " of Hearts";
            
            for (int k = 0; k<names[0].length; k++)
            {
                String a = "";
                
                if (k >= 0 && k < 9)
                {
                    a = String.valueOf(k+2);
                }
                if (k == 9)
                    a = "Jack";
                if (k == 10)
                    a = "Queen";
                if (k == 11)
                    a = "King";
                if (k == 12)
                    a = "Ace";
                
                names[j][k] = a + b;
                
            }
        }
        
        for (int j = 0; j<Driver.cards.length; j++)
        {
            for (int k = 0; k<Driver.cards[0].length; k++)
            {
                if (k==12)
                {
                    Driver.cards[j][k] = new Card(sprites[j][k], 0); //special handling for ace
                    Driver.cards[j][k].setAce(true);
                }
                else if (k<9)
                {
                    Driver.cards[j][k] = new Card(sprites[j][k], k+2); //regular case
                }
                else
                {
                    Driver.cards[j][k] = new Card(sprites[j][k], 10); //royalty case
                }
                
                Driver.cards[j][k].name = names[j][k];
                
            }
        }
    }
    
    public static BufferedImage[][] createCardSprites()
    {
        final int width = 67;
        final int height = 95;
        final int rows = 4;
        final int cols = 13;
        
        BufferedImage[][] sprites = new BufferedImage[rows][cols];
        try
        {
            BufferedImage spriteSheet = ImageIO.read(GameUtilities.class.getResource("/Resource Files/finalCards.png")); //card face graphics
                        
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {
                    sprites[i][j] = spriteSheet.getSubimage(j * width,i * height,width,height);
                }
            }
                                 
        }catch (Exception error)
            {
                System.err.print(error);
                System.exit(0);
            }
        
        return sprites;
        
    }
    
    public static boolean allCardsInDeck(Card[][] cards)
    {
        boolean toBeReturned = true;
        for (int j = 0; j<cards.length; j++)
        {
            for (int k = 0; k<cards[0].length; k++)
            {
                if (cards[j][k].isInDeck()==false)
                {
                   toBeReturned = false;
                }
            }
        }
        
        return toBeReturned;   
    }
    
    public static void initializeDeck()
    {
        try{
            Driver.masterDeck = new Deck();
//            Location[] spawnPoints = {  new Location(null,400,0), new Location(null,200,0), new Location(null,0,0), new Location(null,0,150),
//                                    new Location(null,0,300), new Location(null,0,450), new Location(null,0,700), new Location(null,200,700),
//                                    new Location(null,400,700), new Location(null,600,700), new Location(null,900,700), new Location(null,900,450),
//                                    new Location(null,900,300), new Location(null,900,150), new Location(null,900,0), new Location(null,600,0)      };
            Location[] spawnPoints = {    new Location(null,400,0), new Location(null,0,0),
                                            new Location(null,0,300), new Location(null,0,600),
                                            new Location(null,400,600), new Location(null,800,600),
                                            new Location(null,800,300), new Location(null,800,0)     };
            while (allCardsInDeck(Driver.cards)==false)
            {
                int row = randInt(0,Driver.cards.length-1), col = randInt(0,Driver.cards[0].length-1);
                Card card = Driver.cards[row][col];


                if (card.isInDeck()==false)
                {
                    Driver.masterDeck.push(card);
                    animateCardFall(card, spawnPoints);
                    Thread.sleep(20);

                }
            }
            Thread.sleep(1000);
            //AnimationEngine.linearMove(masterDeck.checkTop().cardLabel, masterDeck.checkTop().cardLabel.getX()+20, masterDeck.checkTop().cardLabel.getY()+20, 500);
            //AnimationEngine.linearMove(masterDeck.checkTop().nextCard.cardLabel, masterDeck.checkTop().nextCard.cardLabel.getX()-50, masterDeck.checkTop().nextCard.cardLabel.getY()-50, 500);
            
        }catch (Exception error){}
    }
    
    public static void desposeHands(CountDownLatch localLatch)
    {
        try{
            new SimulProcess((Object)Driver.currentGame.playerHand,"process ClearHand", localLatch).start();
            new SimulProcess((Object)Driver.currentGame.dealerHand,"process ClearHand", localLatch).start();
            
        }catch(Exception e){}
    }
    
    public static void desposeDeck(Deck deck, CountDownLatch localLatch)
    {
        try{
            Location[] disposePoints = {    new Location(null,400,0), new Location(null,0,0),
                                            new Location(null,0,300), new Location(null,0,600),
                                            new Location(null,400,600), new Location(null,800,600),
                                            new Location(null,800,300), new Location(null,800,0)     };
            while(deck.totalCards>0)
            {
                animateCardDispose(deck.pop(), disposePoints);
                Thread.sleep(20);
            }
            
            Driver.masterDeck = null;
            Thread.sleep(500);
            
            localLatch.countDown();
            
        }catch(Exception e){}
        
    }
    
    private static void animateCardDispose(Card card, Location[] disposePoints) //code for that starting deck building animation
    {
        int random = randInt(0,disposePoints.length-1);
        new SimulProcess(card, disposePoints[random], "process DirectedCardDispose").start();
    }
    
    private static void animateCardFall(Card card, Location[] spawnPoints) //code for that starting deck building animation
    {
        int random = randInt(0,spawnPoints.length-1);
        card.cardLabel = new JLabel(Driver.images[9]);
        card.cardLabel.setBounds(spawnPoints[random].xPos-Driver.images[9].getIconWidth()/2,spawnPoints[random].yPos-Driver.images[9].getIconHeight(),Driver.images[9].getIconWidth(),Driver.images[9].getIconHeight());
        card.cardLabel.setIcon((Icon)null);
        Driver.masterPane.add(card.cardLabel, new Integer(3), 0);
        new SimulProcess("CUSTOM animation DECKCARDFALL") 
            {
                public void run()
                {
                    AnimationEngine.linearMove(card.cardLabel, 400-card.cardLabel.getWidth()/2, 300-card.cardLabel.getHeight()/2,450);
                }
            }.start();
        new SimulProcess(card.cardLabel,"animation CardBackFadeIn Synced").start();
    }
    
    public static int randInt(int min, int max) //general purpose method for random int between min and max inclusive
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
}//End GameUtilities Class/=============================================================================================================================