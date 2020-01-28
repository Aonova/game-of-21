package main.imagebase;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Image //Abstract 2D Drawing object which can be used to display and control image on Swing compatible "Image Panes" WIP
{
    private boolean visible; //if set to true, Image will attempt to draw in the next paint() call. Note setting to false does NOT erase the already drawn image.

    private Rectangle2D bounds; //A virtual Rectangle which encloses the image. 
    private Point2D center; //Convenience, the center Point of the bounds
    private BufferedImage image; //The actual image which will be drawn at the bounds location on the ImagePane which draws it if visible
    
    private float opacity;
    private float rotation;
    private float scaleX,scaleY;
    
    private String name; //arbitrary name field to describe image. 
    private Color color; //arbitrary color to set as this images standard for system additions
    
    public Image(Point2D center,BufferedImage image)
    {
        this.center = center;
        this.image = image;
        
        opacity = 1.0f;
        rotation = 0;
        scaleX = 1;
        scaleY = 1;
        color = new Color(randInt(0,255),randInt(0,255),randInt(0,255),250);
        
        calcBounds_Center();
        visible = true;
    }
    
    public Image(float centerX, float centerY, BufferedImage image)
    {
        this.center = new Point2D.Float(centerX,centerY);
        this.image = image;
        
        opacity = 1.0f;
        rotation = 0;
        scaleX = 1;
        scaleY = 1;
        color = new Color(randInt(0,255),randInt(0,255),randInt(0,255),250);
        
        calcBounds_Center();
        visible = true;
    }
    
    public synchronized void setName(String name)
    {
        this.name = name;
    }
    
    public synchronized void setOpacity(float opacity)
    {
        this.opacity = opacity;
    }
    
    public synchronized void setVisible(boolean visible)
    {
        this.visible = visible;
    }
    
    public synchronized void toggleVisible()
    {
        this.visible = !visible;
    }
    
    public synchronized void setRotation(float rotation)
    {
        this.rotation = rotation;
    }
    
    public synchronized void setScale(float scaleX, float scaleY)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    private synchronized void calcBounds_Center()
    {
        float width = image.getWidth();
        float height = image.getHeight();
        
        bounds = new Rectangle2D.Double(center.getX()-(width/2),center.getY()-(height/2),width,height);
    }
    
    private synchronized void calcBounds_Corner()
    {
        Rectangle2D newBounds = new Rectangle2D.Double(bounds.getX(),bounds.getY(),image.getWidth(),image.getHeight());
        center = new Point2D.Double(newBounds.getCenterX(),newBounds.getCenterY());
        bounds = newBounds;
    }
    
    private synchronized Rectangle2D getBounds()
    {
        return bounds;
    }
    
    public synchronized void setImage_CenterLock(BufferedImage image)
    {
        this.image = image;
        calcBounds_Center();
    }
    
    public synchronized void setImage_CornerLock(BufferedImage image)
    {
        this.image = image;
        calcBounds_Corner();
    }
    
    @Override
    public synchronized String toString()
    {
        String n = "";
        if (visible==false)
            n = "in";
        return "Image ["+name+"] centered at ("+center.getX()+","+center.getY()+") >>> "
                + "["+n+"visible, rotation:"+rotation+"°, opacity:"+opacity*100+"%, scale: ("+scaleX+"x,"+scaleY+"x)]";
    }
    
    public synchronized void paint(Graphics2D g2d)
    {
        if (visible==false)
            return;
        g2d.translate(bounds.getX(),bounds.getY());
        g2d.rotate(rotation);
        g2d.scale(scaleX, scaleY);
        g2d.drawImage(image, 0, 0, null);
    }
    
    public synchronized void paintInfo(Graphics2D g2d)
    {
        g2d.translate(bounds.getX(),bounds.getY());
        
        Font font = new Font("Calibri",Font.PLAIN,8);
        String info = this.toString();
        
        g2d.setColor(new Color(color.getRed()/2,color.getGreen()/2,color.getBlue()/2,color.getAlpha()/2));
        g2d.fill(font.createGlyphVector(g2d.getFontRenderContext(), info).getVisualBounds());
        
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(info,0,0);
        g2d.draw(new Ellipse2D.Float(-5,-5,10,10));
   
    }
    
    public static int randInt(int min, int max) //general purpose method for random int between min and max inclusive
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
}

