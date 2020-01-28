package source;

public class Hand
{
    public Card[] hand = new Card[3];
    public int finalCardIndx = -1;
    public Location[] positions = new Location[3];
    public int totalPoints; 
    
    public Hand(Location first, Location second, Location third)
    {
        totalPoints = 0;
        positions[0] = first;
        positions[1] = second;
        positions[2] = third;
    }
    
    public void addCard(Card toBeAdded)
    {
        if (finalCardIndx < 1)
        {
            finalCardIndx++;
            hand[finalCardIndx] = toBeAdded;
            calculatePoints(0);
        }
        else if (finalCardIndx == 1)
        {
            finalCardIndx++;
            hand[finalCardIndx] = toBeAdded;
            calculatePoints(1);
        }
    }
    
    public Card removeCard()
    {
        Card toBeReturned = null;
        if (finalCardIndx >= 0)
        {
            toBeReturned = this.getFinalCard();
            this.hand[finalCardIndx--] = null;
        }
        return toBeReturned;
    }
    
    public Card getFinalCard()
    {
        return hand[finalCardIndx];
    }
    
    public void calculatePoints(int param)
    {
        if (param == 1)
        {
            totalPoints += hand[2].getCardValue();
        }
        else
        {
            totalPoints = 0;
        }
        for (int k = 0; k<=finalCardIndx && param != 1; k++)
        {
            totalPoints += hand[k].getCardValue();
        }
    }
    
    public int getAceAmount()
    {
        int toBeReturned = 0;
                
        for (int k = 0; k<=finalCardIndx; k++)
        {
            if (hand[k].ace==true)
            {
                toBeReturned++;
            }
        }
        
        return toBeReturned;
    }
    
}