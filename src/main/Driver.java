package main;

//Imports/========================================================================================================================================
import main.imagebase.CustomWindow;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.imageio.*;
import main.imagebase.CustomImageLabel;
//End Imports/====================================================================================================================================
        

public class Driver //Main Class/=================================================================================================================
{
     public static void main(String[] args) 
     {
        runPreGameProcesses();
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() {
                initializeGUIWindow();
            }
        });
        
        
    }
    
    //==================================[Fields Referenced in all of the Package Classes]=========================================================/////
     
    public static boolean debugMode = false;
    
    public static JFrame masterWindow; //just put it there to not have to do object.getParent()... like 5 times to reference in the code
    public static JLayeredPane masterPane; //the layered "contents" of masterWindow, to be initialized with GUI Window method
        //Layers// -5: Background Image// -4: BG Animated JLabels// 1: Card Frame Designs// 2: Clickable JLabels// 3: Card JLabels// 4: Overlay Message BG// 
    
    public static Deck masterDeck; //the Deck of Cards present in every instance of the game
    public static Card[][] cards; //All the 52 cards, with respective points, image info, etc
    public static GameInstance currentGame; //the reference to the running instance of the game
    
    public static ImageIcon[][] animations; //array loading common animations to decrease cpu load runtime (at the cost of memory overuse)
    public static ImageIcon[] images; //array loading refrences to imageicons to (hopefully) decrease cpu load runtime
    
    //---------------Game properties-----------------------------------------------//
    public static final Location[] COORDINATES = {  new Location("Center", 400, 300),    //orginization of common pixel coordinates
                                                    new Location("Hand Card 3", 617, 253),
                                                    new Location("Hand Card 1", 582, 133),
                                                    new Location("Hand Card 2", 582, 373),
                                                    new Location("Dealer Card 3", 117, 253),
                                                    new Location("Dealer Card 2", 152, 133),
                                                    new Location("Dealer Card 1", 152, 373),
                                                    new Location("Dealer Victory", 216, 62),
                                                    new Location("Player Victory", 580, 62)};
    
    public static final int PREFFERED_UPDATE_TIME_MS = 250; //final field to set prefered update time for repeating threads
    public static final int PREFFERED_ANIM_TIME_MS = 20;
    public static final int PREFFERED_FPS = 30;
    public static double frameTime;
    //---------------------------------------------------------------------------------------------------------------------------------------------
    
     public static void runPreGameProcesses()
    {
        initializeImageResources();
        GameUtilities.createCards(GameUtilities.createCardSprites());
    }
    
    public static void initializeImageResources()
    {
        try
        {
            images = new ImageIcon[30];
            images[0] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/testStartButtonOff.png")));
            images[1] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/testStartButtonOn.png")));
            images[2] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_PlayGameOff.png")));
            images[3] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_PlayGameOn.png")));
            images[4] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_CloseGameOff.png")));
            images[5] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_CloseGameOn.png")));
            images[6] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_ReshuffleOff.png")));
            images[7] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_ReshuffleOn.png")));
            images[8] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_ContinueOff.png")));
            images[9] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/finalCover.png")));
            images[10] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/finalGameIcon.png")));
            images[11] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_Ace1Off.png")));
            images[12] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_Ace1On.png")));
            images[13] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_Ace11Off.png")));
            images[14] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_Ace11On.png")));
            images[15] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_HitOff.png")));
            images[16] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_HitOn.png")));
            images[17] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_StandOff.png")));
            images[18] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_StandOn.png")));
            images[19] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Message_DealerWins.png")));
            images[20] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Message_PlayerWins.png")));
            images[21] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Message_Draw.png")));
            images[22] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Message_BG.png")));
            images[23] = new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/Button_ContinueOn.png")));
            
            animations = new ImageIcon[5][];
            animations[0] = AnimationEngine.createSpriteArray_JLabelFadeIn(Driver.images[9], null, 30); //card cover fading in: 30 frames
            animations[1] = AnimationEngine.createSpriteArray_JLabelFadeOut(Driver.images[9], null, 30); //card cover fading out: 30 frames
            
        }catch (Exception error)
        {
            System.err.println(error);
            System.exit(0);
        }
    }
    
    public static void initializeGUIWindow()
    {
        try{
          
            masterWindow = new CustomWindow("Game of 21", 800, 600);
            masterWindow.setIconImage(images[10].getImage());
            masterPane = masterWindow.getLayeredPane();
            masterWindow.setBackground(new Color(0,0,0,0));
            
            
            CustomImageLabel backgroundImage = new CustomImageLabel();
            backgroundImage.init(new ImageIcon(ImageIO.read(Driver.class.getResource("/Resource Files/finalBG.png"))),400,300);
            masterPane.add(backgroundImage, new Integer(-5), 0);
            
            Clickable closeGameButton = new Clickable("Close Game Button");
            closeGameButton.setIcon(images[4]);
            closeGameButton.setSize(closeGameButton.getIcon().getIconWidth(), closeGameButton.getIcon().getIconHeight());
            closeGameButton.setBounds((COORDINATES[0].xPos-(closeGameButton.getWidth()/2)), COORDINATES[0].yPos + 200, closeGameButton.getWidth(), closeGameButton.getHeight());
            masterPane.add(closeGameButton, new Integer(2), 0);
            
            Clickable playGameButton = new Clickable("Play Game Button");
            playGameButton.setIcon(images[2]);
            playGameButton.setSize(playGameButton.getIcon().getIconWidth(), playGameButton.getIcon().getIconHeight());
            playGameButton.setBounds((COORDINATES[0].xPos-(playGameButton.getWidth()/2)), COORDINATES[0].yPos+180-(playGameButton.getHeight()/2), 165, 45);
            masterPane.add(playGameButton, new Integer(2), 0);
            
            Clickable debug = new Clickable("Debug Show");
            debug.setBounds(25, 20, 50, 20);
            debug.setFont(new Font("Arial", 0, 10));
            debug.setForeground(Color.WHITE);
            debug.setText("Debug");
            masterPane.add(debug, new Integer(5));
            
            
            
            masterWindow.setOpacity(0.0f);
            masterWindow.setVisible(true);
            AnimationEngine.windowFade(masterWindow, 1.0f, 500);
            
//            time1 = System.nanoTime();
//            
//            Timer frameUpdate = new Timer((1000/Driver.PREFFERED_FPS), new ActionListener()
//            {
//                public void actionPerformed(ActionEvent e)
//                {
//                    if (masterWindow.isVisible())
//                    {
//                        RepaintManager.currentManager(masterPane).markCompletelyDirty(masterPane);
//                        RepaintManager.currentManager(masterPane).paintDirtyRegions();
//                    }
//                    long time2 = System.nanoTime();
//                    frameTime = ((time2 - time1)/1000000);
//                    time1 = time2;
//                    long fps = Math.round(1000/frameTime);
//                    System.out.println(fps);
//                    
//                    
//                    
//                }
//            });
//            
//            frameUpdate.setRepeats(true);
//            frameUpdate.start();
            
            /*Clickable startButton = new Clickable("Test Start Button"); //A whole range of test animations and such\/=========================
            startButton.sendReference(startButton, window);
            startButton.setIcon(images[0]);
            startButton.setBounds(coordinates[4].xPos, coordinates[4].yPos,165,35);
            windowContents.add(startButton);
            
            JLabel testPic = new JLabel(cards[0][0].getCardImage());
            windowContents.add(testPic);
            testPic.setBounds(coordinates[1].xPos,coordinates[1].yPos,67,95);*/
            
            /*JLabel testAnimPSD = new JLabel();
            windowContents.add(testAnimPSD);
            testAnimPSD.setBounds(coordinates[1].xPos,coordinates[1].yPos,192,192);
            processes.pushProcess(new SimulProcess(testAnimPSD, window, "test animation PSDTEST"));*/
            
            /*JLabel testAnimPSD2 = new JLabel();
            windowContents.add(testAnimPSD2);
            testAnimPSD2.setBounds(300,200,192,192);
            processes.pushProcess(new SimulProcess(testAnimPSD2, window, "test animation PSDTEST2"));*/

            //processes.pushProcess(new SimulProcess(testPic, "test animation CARDIMAGECHANGE"));
            //processes.pushProcess(new SimulProcess(testPic, "test animation CARDMOTION"));
            //processes.pushProcess(new SimulProcess(startButton, "test animation STARTMOTION")); //================================================
            
        }catch (Exception error)
            {
                System.err.print(error);
                System.exit(0);
            }
    }
    
    static float time1;
}//End Driver Class/=================================================================================================================================
