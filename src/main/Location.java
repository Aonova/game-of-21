package main;

public class Location //I dont even know why I made this when java.awt.Point is like the exact same thing/======================================================
{
    public String desc;
    public int xPos;
    public int yPos;
    
    public Location(String d, int x, int y)
    {
        desc = d;
        xPos = x;
        yPos = y;
    }
    
    public Location(int x, int y)
    {
        desc = null;
        xPos = x;
        yPos = y;
    }
    
    public static Location getLocation(Location[] array, String desc)
    {
        Location toBeReturned = null;
        for(int k = 0; k<array.length;k++)
        {
            if(array[k].desc.equals(desc))
            {
                toBeReturned = array[k];
            }
        }
        return toBeReturned;
    }
}//End Location Class/==================================================================================================================================