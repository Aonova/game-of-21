package main;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card //Class orginizing data and functions related to individual cards/===========================================================================
{
    public int indx;
    public String name;
    public JLabel cardLabel;
    public Card nextCard = null;
    public boolean ace = false;
    private boolean isFaceUp = false;
    private boolean inDeck = false;
    private ImageIcon cardImage = null;
    private int cardValue;
    
    public Clickable frame;
    public Clickable leftPrompt;
    public Clickable rightPrompt;
    
    public Card(BufferedImage image, int value)
    {
        cardImage = new ImageIcon(image);
        cardValue = value;
    }
    
    public void setAce(boolean param)
    {
        ace = param;
    }
    
    public void flip()
    {
        isFaceUp = !isFaceUp;
        this.graphicUpdate();
    }
    
    public void setFaceDown()
    {
        isFaceUp = false;
        this.graphicUpdate();
    }
    
    public void graphicUpdate()
    {
        if (isFaceUp == false)
        {
            cardLabel.setIcon(Driver.images[9]);
        }
        else
        {
            cardLabel.setIcon(cardImage);
        }
    }
    
    public void insertIntoDeck()
    {
        inDeck = true;
    }
    
    public void removeFromDeck()
    {
        inDeck = false;
        isFaceUp = false;
        indx = -1;
        nextCard = null;
    }
    
    public boolean isInDeck()
    {
        return inDeck;
    }
    
    public ImageIcon getCardImage()
    {
        return cardImage;
    }
    
    public int getCardValue()
    {
        return cardValue;
    }
    
    public void setCardImage(BufferedImage image)
    {
        cardImage = new ImageIcon(image);
    }
    
    public void setCardValue(int value)
    {
        cardValue = value;
    }
    
    public String toString()
    {
        String string;
        
        if (this.isInDeck() ==  false)
        {
            string = name;
        }
        
        else
        {
            string = ("#" + indx + " [" + name + "]");
        }
        
        return string;
    }
}//End Card Class/=======================================================================================================================================