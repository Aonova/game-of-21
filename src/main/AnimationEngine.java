package main;

import com.twelvemonkeys.imageio.plugins.psd.PSDImageReader; //from twelvemonkeys: open source github project for java file support... pain in the ass to use however

import com.aspose.imaging.Image; //from aspose: evaluation version of commercial code solution... note "evaluation version" (=_=)
import com.aspose.imaging.fileformats.psd.PsdImage;
import com.aspose.imaging.fileformats.psd.layers.Layer;

import com.alternativagame.resource.utils.psd.PSDParser; //from alternivagame: free to use library psd parser... rather dated however and no documentation
import com.alternativagame.resource.utils.psd.layer.PSDLayerPixelData; 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
//==========================================================================================================

import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import main.imagebase.Vector2D;

class AnimationEngine //the greatest product of my efforts, a (mostly) modular WIP animation engine/========================================================
{
    public static ImageIcon createColorRectangle_JLabelBounds(JLabel object, Color color)
    {
        BufferedImage imageA = new BufferedImage(object.getWidth(), object.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int j = 0; j < imageA.getWidth(); j++)
        {
            for (int k = 0; k < imageA.getHeight(); k++)
            {
                imageA.setRGB(j, k, color.getRGB());
            }
        }
        ImageIcon toBeReturned = new ImageIcon(imageA);
        
        return toBeReturned;
    }
    public static ImageIcon[] createSpriteArray_JLabelFadeIn(Icon icon, JLabel object, int frames)
    {
        if (icon == null)
        {
            icon = object.getIcon();
        }
        ImageIcon[] alpha = createSpriteArray_JLabelFadeOut(icon, null, frames);
        ImageIcon[] toBeReturned =  new ImageIcon[alpha.length];
        int j = 0, k = (alpha.length) - 1;
        for(;j<alpha.length;j++)
        {
            toBeReturned[k] = alpha[j];
            k--;
            //System.out.println(k + " " + j);
        }
        return toBeReturned;
    }
    
    public static ImageIcon[] createSpriteArray_JLabelFadeOut(Icon icon, JLabel object, int frames) //creating a sprite array from fading out an icon, from a jlabel or otherwise
    {
        if (icon == null)
        {
            icon = object.getIcon();
        }
        
        BufferedImage referImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = referImage.createGraphics();
        icon.paintIcon(null, graphics, 0, 0);
        
        float highestAlpha = 0.0f;
        
        for (int l = 0; l < referImage.getWidth(); l++)
            {
                for (int m = 0; m < referImage.getHeight(); m++)
                {
                    Color referPixel = new Color(referImage.getRGB(l, m), true); 
                    int a = referPixel.getAlpha();
                    if (a > highestAlpha)
                    {
                        highestAlpha = a; //sampling entire image for the most opaque pixel
                    }                    
                }
            }
        
        int alphaPerFrame = (int)Math.round(highestAlpha/frames);
        
        ImageIcon[] sprites = new ImageIcon[frames];
        
        
        for (int k = 0; k <  frames; k++) //every frame
        {
            BufferedImage frame = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            
            for (int l = 0; l < icon.getIconWidth(); l++) //every x coordinate of pixels
            {
                for (int m = 0; m < icon.getIconHeight(); m++) //every y coordinate of pixels
                {
                    Color referPixel = new Color(referImage.getRGB(l, m), true); 
                    int r = referPixel.getRed();
                    int g = referPixel.getGreen();
                    int b = referPixel.getBlue();
                    int a = referPixel.getAlpha() - (alphaPerFrame*k);
                    if (a < 0)
                    {
                        a = 0;
                    }
                    Color newPixel = new Color(r,g,b,a);
                    
                    frame.setRGB(l, m, newPixel.getRGB()); //set the rgb of every pixel to the respective pixel rgb in the referance image adding alpha based on frame number
                }
            }
                        
            sprites[k] = new ImageIcon(frame);
            
        }
        
        return sprites;
    }
    
    public static ImageIcon[] createSpriteArray_PSDbyLayer(String path) //twelvemonkeys psd parser implementation: Fully functional, even if more resource intensive. Thank God for open source, I could work out all the kinks.
    {
        ImageIcon[] sprites = null;
        try
        {           
            ImageInputStream input = ImageIO.createImageInputStream(Driver.class.getResourceAsStream(path));     
            
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (readers.hasNext() != true)
            {
                throw new IllegalArgumentException("No reader for: " + path);
            }
            
            ImageReader reader = readers.next();
            
            reader.setInput(input);
            
            PSDImageReader psdFile = (PSDImageReader)reader;

            //System.out.println(psdFile.getNumImages(true)); //debugging
            
            sprites = new ImageIcon[psdFile.getNumImages(true) - 1];
            
            for(int k = 0; k < sprites.length; k++)
            {
                sprites[k] = new ImageIcon(psdFile.read(k+1));
            }
            
            
            reader.dispose();
            input.close();
                
        }
        catch (Exception e)
        {
            System.err.println(e);
            //System.exit(0);
        }
        
        return sprites;
    }
    
    public static ImageIcon[] createSpriteArray_PSDbyLayer_V2(String path) //aspose's psd parser implementation... perfectly coded but didnt work due to evaluation version limitations (;^;)
    {
        ImageIcon[] sprites = null;
        try
        {
            Image inputFile = Image.load(Driver.class.getResourceAsStream(path));
            PsdImage psdFile = (PsdImage)inputFile;
            Layer[] layers = psdFile.getLayers();
            
            sprites = new ImageIcon[layers.length];
            
            for (int k = 0; k < layers.length; k++)
            {
                BufferedImage currentFrame = new BufferedImage(layers[k].getWidth(), layers[k].getHeight(), BufferedImage.TYPE_INT_ARGB);
                
                for (int l = 0; l < layers[k].getWidth(); l++) 
                {
                    for (int m = 0; m < layers[k].getHeight(); m++)
                    {
                        currentFrame.setRGB(l, m, layers[k].getArgb32Pixel(l, m)); //filling in every single pixel with 32 bit ARGB color
                    }
                }
                sprites[k] = new ImageIcon(currentFrame);
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
            System.exit(0);
        }
        
        return sprites;
        
    }
    public static ImageIcon[] createSpriteArray_PSDbyLayer_V3(String path) //alternitivagames psd parser implementation... works only half the times, the other half gives EoF exception for some reason (;^;)
    {
        ImageIcon[] sprites = null;
        PSDParser psdFile;
        try
        {
            psdFile = new PSDParser((Driver.class.getResourceAsStream(path)));
            java.util.List images = psdFile.getLayerAndMask().getImageLayers();

            sprites = new ImageIcon[images.size()];

            for(int k = 0; k<sprites.length; k++)
            {
                sprites[k] = new ImageIcon(((PSDLayerPixelData)images.get(k)).getImage());
            }
                        
                                 
        }catch (Exception error)
            {
                System.err.print(error);
                //System.exit(0);
            }
        return sprites;
    }
    
    public static ImageIcon[] createSpriteArray_PSDbyLayerBackwards(String path)
    {
        ImageIcon[] alpha = createSpriteArray_PSDbyLayer(path);
        ImageIcon[] toBeReturned =  new ImageIcon[alpha.length];
        int j = 0, k = (alpha.length) - 1;
        for(;j<alpha.length;j++)
        {
            toBeReturned[k] = alpha[j];
            k--;
            //System.out.println(k + " " + j);
        }
        return toBeReturned;
    }
    
    public static ImageIcon[] createSpriteArray(int width, int height, String path) //old version before I found psd plugin
    {
        ImageIcon[] sprites = null;
        try
        {
            BufferedImage spriteSheet = ImageIO.read(AnimationEngine.class.getResource(path));
            sprites = new ImageIcon[spriteSheet.getWidth()/width];
                        
            for (int i = 0; i < sprites.length; i++ )
            {
                sprites[i] = new ImageIcon(spriteSheet.getSubimage(i*width,0,width,height)); 
            }
                                 
        }catch (Exception error)
            {
                System.err.print(error);
                System.exit(0);
            }
        
        return sprites;
    }
    
    //===========================(GUI updating animation methods below)==========================//
    
    public static void linearMove(JLabel ob, int x, int y, int ms) //crude motion animation
    {
        try{
            JLabel object = ob;
            final int msPerFrame = 25; // 40 FPS (1000 / desired FPS)
            int endX = x;
            int endY = y;
            int time = ms;
            int startX = object.getX();
            int startY = object.getY();
            int frameTotal = time / msPerFrame;
            double xPerFrame = (double)(endX - startX)/(double)frameTotal;
            double yPerFrame = (double)(endY - startY)/(double)frameTotal;
            for (int k = 0; k<frameTotal; k++)
            {
                object.setBounds(((int)Math.round(startX+(xPerFrame*k))),((int)Math.round(startY+(yPerFrame*k))), object.getWidth(), object.getHeight());
                Thread.sleep(msPerFrame);
            }
            object.setBounds(endX,endY, object.getWidth(), object.getHeight());

            //System.out.println("End Coordinates : ("+object.getX()+","+object.getY()+")"); //debugging
        }catch  (Exception error)
            {
                System.err.println(error);
                System.exit(0);
            }
    }
    
//    public static void linearMoveVector(CustomImageLabel ob, double x, double y, int ms) //simple motion animation using vectors
//    {
//        try{
//            CustomImageLabel object = ob;
//            final int msPerFrame = 25; // 40 FPS (1000 / desired FPS)
//            int time = ms;
//            Vector2D model = new Vector2D(new Line2D.Double(object.getCenter(), new Point2D.Double(x,y)));
//            int startX = object.getX();
//            int startY = object.getY();
//            int frameTotal = time / msPerFrame;
//            double xPerFrame = (double)(endX - startX)/(double)frameTotal;
//            double yPerFrame = (double)(endY - startY)/(double)frameTotal;
//            for (int k = 0; k<frameTotal; k++)
//            {
//                object.setBounds(((int)Math.round(startX+(xPerFrame*k))),((int)Math.round(startY+(yPerFrame*k))), object.getWidth(), object.getHeight());
//                Thread.sleep(msPerFrame);
//            }
//            object.setBounds(endX,endY, object.getWidth(), object.getHeight());
//
//            //System.out.println("End Coordinates : ("+object.getX()+","+object.getY()+")"); //debugging
//        }catch  (Exception error)
//            {
//                System.err.println(error);
//                System.exit(0);
//            }
//    }
    
    public static void linearMoveCentered(JLabel ob, int x, int y, int ms) //linear motion animation but ending JLabel location centered on coordinates (x, y) given
    {
        try{
            JLabel object = ob;
            final int msPerFrame = 25; // 40 FPS (1000 / desired FPS)
            int endX = x - object.getWidth()/2;
            int endY = y - object.getHeight()/2;
            int time = ms;
            int startX = object.getX();
            int startY = object.getY();
            int frameTotal = time / msPerFrame;
            double xPerFrame = (double)(endX - startX)/(double)frameTotal;
            double yPerFrame = (double)(endY - startY)/(double)frameTotal;
            for (int k = 0; k<frameTotal; k++)
            {
                object.setBounds(((int)Math.round(startX+(xPerFrame*k))),((int)Math.round(startY+(yPerFrame*k))), object.getWidth(), object.getHeight());
                Thread.sleep(msPerFrame);
            }
            object.setBounds(endX, endY, object.getWidth(), object.getHeight());

            //System.out.println("End Coordinates : ("+object.getX()+","+object.getY()+")"); //debugging
        }catch  (Exception error)
            {
                System.err.println(error);
                System.exit(0);
            }
    }
    
    public static void windowFade(JFrame window, float endOpacity, int time) //simple JFrame animated opacity change; time is in milliseconds
    {
        try{
            final float startOpacity = window.getOpacity();
            final int msPerFrame = 25; // ~40 fps
            final int frameTotal = time / msPerFrame;
            float alphaPerFrame = ((endOpacity - startOpacity)/(float)frameTotal);
            for (int frame = 0; frame < frameTotal; frame++)
            {
                if (((startOpacity + alphaPerFrame * frame)<0))
                {
                    window.setOpacity(0.0f);
                }
                else if ((startOpacity + alphaPerFrame * frame)>1)
                {
                    window.setOpacity(1.0f);
                }
                else
                {
                    window.setOpacity(startOpacity + alphaPerFrame * frame);
                }

                Thread.sleep(msPerFrame);

            }
        }catch (Exception error){}
        //System.out.println("Opacity of Window: "+ window.getOpacity()); //debugging
    }
    
    public static void animateSprites_V2(ImageIcon[] spriteArray, JLabel object, int fps) //slightly more optimized performance animation than V1, way less choppy when in conjunction with linear move animation
    {
        try{
            
            final int msPerFrame = (int)Math.round(1000.0/(double)fps);
            final JLabel reference = object;
            
            int newX = (int)Math.round(reference.getX()+(reference.getWidth()/2.0f)-(spriteArray[spriteArray.length-1].getIconWidth()/2.0f));
            int newY = (int)Math.round(reference.getY()+(reference.getHeight()/2.0f)-(spriteArray[spriteArray.length-1].getIconHeight()/2.0f));
            
            object.setBounds(newX, newY, spriteArray[spriteArray.length-1].getIconWidth(), spriteArray[spriteArray.length-1].getIconHeight());
            
            for (int k=0; k<spriteArray.length; k++)
            {
                object.setIcon(spriteArray[k]);
                //int newX = (int)Math.round(reference.getX()+(reference.getWidth()/2.0f)-(object.getIcon().getIconWidth()/2.0f)); //keeping it centered on original position if sprite changes size
                //int newY = (int)Math.round(reference.getY()+(reference.getHeight()/2.0f)-(object.getIcon().getIconHeight()/2.0f)); //rather intensive for something that has to run up to 60 times a second... remove?
                //object.setBounds(newX, newY, object.getIcon().getIconWidth(), object.getIcon().getIconHeight());
                Thread.sleep(msPerFrame);
               // System.out.println(k + " " + Thread.currentThread().getId()); //debugging
            }
            
            newX = (int)Math.round(reference.getX()+(reference.getWidth()/2.0f)-(object.getIcon().getIconWidth()/2.0f));
            newY = (int)Math.round(reference.getY()+(reference.getHeight()/2.0f)-(object.getIcon().getIconHeight()/2.0f));
            
            object.setBounds(newX, newY, object.getIcon().getIconWidth(), object.getIcon().getIconHeight());
            
        }catch (Exception error){}
    }
     
    public static void animateSprites(ImageIcon[] spriteArray, JLabel object, int fps) //simple animation method for JLabels, veeeery memory inefficient, requiring all the sprites of the animation be loaded in a single array
    {
        try{
            
            final int msPerFrame = (int)Math.round(1000.0/(double)fps);
            final JLabel reference = object;
            
            //int newX = (int)Math.round(reference.getX()+(reference.getWidth()/2.0f)-(spriteArray[spriteArray.length-1].getIconWidth()/2.0f));
            //int newY = (int)Math.round(reference.getY()+(reference.getHeight()/2.0f)-(spriteArray[spriteArray.length-1].getIconHeight()/2.0f));
            
            //object.setBounds(newX, newY, spriteArray[spriteArray.length-1].getIconWidth(), spriteArray[spriteArray.length-1].getIconHeight());
            
            for (int k=0; k<spriteArray.length; k++)
            {
                object.setIcon(spriteArray[k]);
                int newX = (int)Math.round(reference.getX()+(reference.getWidth()/2.0f)-(object.getIcon().getIconWidth()/2.0f)); //keeping it centered on original position if sprite changes size
                int newY = (int)Math.round(reference.getY()+(reference.getHeight()/2.0f)-(object.getIcon().getIconHeight()/2.0f)); //rather intensive for something that has to run up to 60 times a second... remove?
                object.setBounds(newX, newY, object.getIcon().getIconWidth(), object.getIcon().getIconHeight());
                Thread.sleep(msPerFrame);
               // System.out.println(k + " " + Thread.currentThread().getId()); //debugging
            }
            
            //newX = (int)Math.round(reference.getX()+(reference.getWidth()/2.0f)-(object.getIcon().getIconWidth()/2.0f));
            //newY = (int)Math.round(reference.getY()+(reference.getHeight()/2.0f)-(object.getIcon().getIconHeight()/2.0f));
            
            //object.setBounds(newX, newY, object.getIcon().getIconWidth(), object.getIcon().getIconHeight());
            
        }catch (Exception error){}
    }
}//End Animation Engine/=================================================================================================================================