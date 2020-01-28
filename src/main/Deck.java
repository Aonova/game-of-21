package main;

public class Deck //Linked List Stack of Card objects to represent the deck of cards in each game instance/=====================================================
{
    public int totalCards;
    
    private Card topCard;    // Deck implemented as a Linked List Stack
    
    public Card bottomCard;
    
    public Deck()
    {
        topCard = null;
        totalCards = 0;
    }
    
    public void push(Card card)
    {
        if (topCard == null)
        {
            topCard = card;
            bottomCard = topCard;
            card.nextCard = null;
        }
        else if (topCard != null && totalCards < (13*4))
        {
            card.nextCard = topCard;
            topCard = card;

        }
        else
        {
            System.out.println("Deck is full, you screwed up somewhere");
            System.exit(0);
        }
        card.insertIntoDeck();
        this.topCard.indx = totalCards;
        totalCards++;
    }
    
    public Card pop()
    {
        if (topCard != null)
        {
            Card toBePopped = topCard;
            topCard = topCard.nextCard;
            toBePopped.removeFromDeck();
            totalCards--;
            return toBePopped;
        }
        else
        {
            System.out.println("Deck is empty, you screwed up somewhere");
            return null;
        }
        
        
    }
    
    
    public Card checkTop()
    {
        return topCard;
    }
    
    public Card checkNextCard()
    {
        return topCard.nextCard;
    }
    
    public String toString()
    {
        String string = "[TOP OF DECK]<br>";
        Card currentCard = this.topCard;
        while (currentCard != null)
        {
            string = string + currentCard.toString() + "<br>";
            currentCard = currentCard.nextCard;
            
        }
        string = string + "[END OF DECK]";
        
        return string;
    }
            
}//End Deck Class/======================================================================================================================================