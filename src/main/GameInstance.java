package main;

import java.util.concurrent.CountDownLatch;
import javax.swing.Icon;
import javax.swing.JLabel;

public class GameInstance //Event handler for individual game instances/=======================================================================================
{
    public Hand playerHand;
    public Hand dealerHand;
    public int runningPrompts = 0;
    public boolean gameMoveHit;
    
    public JLabel messageBG = new JLabel((Icon)null);
    public JLabel winMessage = new JLabel((Icon)null);
    public Clickable contButton = new Clickable(null);
    public Clickable reshuffle = new Clickable(null);
    
    public GameInstance()
    {
        GameUtilities.initializeDeck();
        playerHand = new Hand(Location.getLocation(Driver.COORDINATES, "Hand Card 1"), Location.getLocation(Driver.COORDINATES, "Hand Card 2"), Location.getLocation(Driver.COORDINATES, "Hand Card 3"));
        dealerHand = new Hand(Location.getLocation(Driver.COORDINATES, "Dealer Card 1"), Location.getLocation(Driver.COORDINATES, "Dealer Card 2"), Location.getLocation(Driver.COORDINATES, "Dealer Card 3"));
    }
    
    public void runGameProcess()
    {
        normalDrawTurn();
        try{Thread.sleep(400);}catch(Exception e){}
        resolveAcePrompts(0);
        resolveTurnPrompt();
        waitForFinishInput();
        try{Thread.sleep(400);}catch(Exception e){}
        dealerMove(0);
        handleEndGame();
        
    }
    
    public void reinitialize()
    {
        runningPrompts = 0;
        messageBG = new JLabel((Icon)null);
        winMessage = new JLabel((Icon)null);
        contButton = new Clickable(null);
        reshuffle = new Clickable(null);
        
        playerHand = new Hand(Location.getLocation(Driver.COORDINATES, "Hand Card 1"), Location.getLocation(Driver.COORDINATES, "Hand Card 2"), Location.getLocation(Driver.COORDINATES, "Hand Card 3"));
        dealerHand = new Hand(Location.getLocation(Driver.COORDINATES, "Dealer Card 1"), Location.getLocation(Driver.COORDINATES, "Dealer Card 2"), Location.getLocation(Driver.COORDINATES, "Dealer Card 3"));
        
    }
    
    public int checkVictory() //checking victory conditions with current hands: 0 = draw, 1 = dealer victory, 2 = player victory
    {
        int victory = 0;
        
        if (playerHand.totalPoints > dealerHand.totalPoints)
        {    
            if(playerHand.totalPoints <= 21)
            {
                victory = 2;
            }
            
            else if (playerHand.totalPoints > 21)
            {
                if (dealerHand.totalPoints <= 21)
                {
                    victory = 1;
                }
                
                else if (dealerHand.totalPoints > 21)
                {
                    victory = 0;
                }
            }
        }
        
        else if(playerHand.totalPoints < dealerHand.totalPoints)
        {
            if(dealerHand.totalPoints <= 21)
            {
                victory = 1;
            }
            
            else if (dealerHand.totalPoints > 21)
            {
                if (playerHand.totalPoints <= 21)
                {
                    victory = 2;
                }
                
                else if (playerHand.totalPoints > 21)
                {
                    victory = 0;
                }
            }
        }
        else if (playerHand.totalPoints == dealerHand.totalPoints)
        {
            victory = 0;
        }
        
        return victory;
    }
    
    public void dealerMove(int param) //parameters for AI type. 0: normal, slightly stupid, if/else based.// TODO> 1: cheating, knows next card. 2: advanced, takes into account probability keeping track of drawn cards
    {
        CountDownLatch local = new CountDownLatch(0);
        if (param == 0)
        {
            if (playerHand.totalPoints > 21)
            {
                return;
            }

            if (playerHand.totalPoints <= 21)
            {
                if (dealerHand.getAceAmount() == 0)
                {
                    if (dealerHand.totalPoints > playerHand.totalPoints)
                    {
                        return;
                    }

                    else
                    {
                        local = new CountDownLatch(1);
                        new SimulProcess((Object)dealerHand, "process DRAWCARD", local).start();
                    }
                }
                
                if (dealerHand.getAceAmount() == 1)
                {
                    dealerHand.totalPoints += 1;
                    
                    if (dealerHand.totalPoints > playerHand.totalPoints)
                    {
                        return;
                    }

                    else
                    {
                        dealerHand.totalPoints += 10;
                        
                        if (dealerHand.totalPoints > playerHand.totalPoints)
                        {
                            return;
                        }
                        else
                        {
                            local = new CountDownLatch(1);
                            new SimulProcess((Object)dealerHand, "process DRAWCARD", local).start();
                        }
                        
                        //dealerHand.totalPoints -= 10;
                    }
                    
                    //dealerHand.totalPoints -= 1;
                }
                
                //TODO: Implement case if all the cards are aces
            }
        }
        try{local.await();}catch(Exception e){}
    }
    
    public void dealerResolveAces(int param)
    {
        
    }
    
    public void handleEndGame()
    {
        showEndGameMessage(checkVictory());
    }
    
    public void showEndGameMessage(int param)
    {
        new SimulProcess(messageBG, "animation MessageBGShow").start();
        
        try{Thread.sleep(1000);}catch(Exception e){}
        
        if (param == 0) //draw case
        {
            new SimulProcess(winMessage, "animation DrawShow").start();
        }
        
        if (param == 1) //dealer wins case
        {
            new SimulProcess(winMessage, "animation DealerWinsShow").start();
        }
        
        if (param == 2) //player wins case
        {
            new SimulProcess(winMessage, "animation PlayerWinsShow").start();
        }
        
        try{Thread.sleep(800);}catch(Exception e){}
        
        new SimulProcess(contButton, "animation ContinueShow").start();
        
        try{Thread.sleep(300);}catch(Exception e){}
        
        new SimulProcess(reshuffle, "animation ReshuffleShow").start();
        
    }
    
    public void waitForFinishInput()
    {
        while (true)
        {
            try{Thread.sleep(500);}catch(Exception e){} //checking every .5 secs
            
            if (this.runningPrompts == 0)
            {
                return;
            }
        }
    }
    
    public void normalDrawTurn()
    {
        new SimulProcess((Object)playerHand, "process DRAWCARD").start();
        
        try{Thread.sleep(200);}catch(Exception e){}
        
        new SimulProcess((Object)dealerHand, "process DRAWCARD").start();
        
        try{Thread.sleep(400);}catch(Exception e){}

        new SimulProcess((Object)playerHand, "process DRAWCARD").start();
        
        try{Thread.sleep(200);}catch(Exception e){}
        
        new SimulProcess((Object)dealerHand, "process DRAWCARD").start();
      
        
    }
    
    
    public void resolveAcePrompts(int param)
    {    
        for(int k = 0; k<=playerHand.finalCardIndx; k++)
        { 
            if (param == 1) // case to only check and prompt on 3rd card in hand upon choosing to HIT
            {
                k = 2;
            }

            if (playerHand.hand[k].ace ==  true)
            {
                playerHand.hand[k].frame =  new Clickable("frame");
                playerHand.hand[k].frame.baseCard = playerHand.hand[k];
                try{Thread.sleep(200);}catch(Exception e){}
                new SimulProcess((Object)playerHand.hand[k].frame, "animation FrameShow").start();

                playerHand.hand[k].leftPrompt =  new Clickable("Ace 1");
                playerHand.hand[k].leftPrompt.baseCard = playerHand.hand[k];
                try{Thread.sleep(200);}catch(Exception e){}
                new SimulProcess((Object)playerHand.hand[k].leftPrompt, "animation Ace1Show").start();

                playerHand.hand[k].rightPrompt =  new Clickable("Ace 11");
                playerHand.hand[k].rightPrompt.baseCard = playerHand.hand[k];
                try{Thread.sleep(200);}catch(Exception e){}
                new SimulProcess((Object)playerHand.hand[k].rightPrompt, "animation Ace11Show").start();

                playerHand.hand[k].leftPrompt.relatedObjs[0] = playerHand.hand[k].rightPrompt;
                playerHand.hand[k].leftPrompt.relatedObjs[1] = playerHand.hand[k].frame;

                playerHand.hand[k].rightPrompt.relatedObjs[0] = playerHand.hand[k].leftPrompt;
                playerHand.hand[k].rightPrompt.relatedObjs[1] = playerHand.hand[k].frame;
                
                if (param != 1) // normal case
                {
                    runningPrompts += 1;
                }
            }
            
            if (param == 1) // case to only check and prompt on 3rd card in hand upon choosing to HIT
            {
                if (!playerHand.hand[k].ace ==  true)
                {
                    runningPrompts -= 1;
                }
                
                break;
            }
        }
    }
    
    public void resolveTurnPrompt()
    {
        Driver.masterDeck.bottomCard.frame = new Clickable("frame");
        Driver.masterDeck.bottomCard.frame.baseCard = Driver.masterDeck.bottomCard;
        try{Thread.sleep(200);}catch(Exception e){}
        new SimulProcess((Object)Driver.masterDeck.bottomCard.frame, "animation FrameShow").start();
        
        Driver.masterDeck.bottomCard.leftPrompt = new Clickable("Stand");
        Driver.masterDeck.bottomCard.leftPrompt.baseCard = Driver.masterDeck.bottomCard;
        try{Thread.sleep(200);}catch(Exception e){}
        new SimulProcess((Object)Driver.masterDeck.bottomCard.leftPrompt, "animation StandShow").start();
        
        Driver.masterDeck.bottomCard.rightPrompt = new Clickable("Hit");
        Driver.masterDeck.bottomCard.rightPrompt.baseCard = Driver.masterDeck.bottomCard;
        try{Thread.sleep(200);}catch(Exception e){}
        new SimulProcess((Object)Driver.masterDeck.bottomCard.rightPrompt, "animation HitShow").start();
        
        Driver.masterDeck.bottomCard.leftPrompt.relatedObjs[0] = Driver.masterDeck.bottomCard.rightPrompt;
        Driver.masterDeck.bottomCard.leftPrompt.relatedObjs[1] = Driver.masterDeck.bottomCard.frame;

        Driver.masterDeck.bottomCard.rightPrompt.relatedObjs[0] = Driver.masterDeck.bottomCard.leftPrompt;
        Driver.masterDeck.bottomCard.rightPrompt.relatedObjs[1] = Driver.masterDeck.bottomCard.frame;
        
        runningPrompts += 1;
        
    }
    
    public boolean isBlackJack(Hand hand)
    {
        if(hand.totalPoints==21)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}//End GameInstance Class/==============================================================================================================================