// Klondike Applet Main
// Programmed by William Yang and Jeffrey Li
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.image.BufferStrategy;


public class KlondikeApplet extends Applet
    implements ActionListener, MouseListener, MouseMotionListener

{ //instance variables
    Graphics g;
    BorderLayout lm = new BorderLayout ();
    Graphics BackBuffer; // declares a canvas to draw on
    Image offscreen; // declares an image to be used for the buffer
    BufferedImage img;

    Panel pNorth = new Panel (); //contains all buttons
    Panel pSouth = new Panel (); //contains all stats excluding mouse stats
    Panel pS1 = new Panel (); //used for spacing
    Panel pS2 = new Panel ();
    Panel pS3 = new Panel ();
    Panel pS4 = new Panel ();
    Panel pS5 = new Panel ();
    Panel pEast = new Panel (); //contains mouse stats

    GridBagConstraints gbc = new GridBagConstraints ();

    Button bNewGame = new Button ("NEW GAME"); //game buttons
    Button bRestart = new Button ("RESTART GAME");
    Button bQuit = new Button ("QUIT GAME");
    Button bInstr = new Button ("INSTRUCTIONS");

    instrClass instr = new instrClass (); //instruction class
    boolean visInstr = false; //boolean to check if the instruction window is visible

    Label lMouse = new Label ("Mouse:");

    TextField tfPass = new TextField (2); //stat textfields
    TextField tfStack = new TextField (2);
    TextField tfScore = new TextField (2);
    TextField tfMouse = new TextField (8);

    static Font f1 = new Font ("Impact", Font.PLAIN, 45);
    static Font f2 = new Font ("Calibri", Font.PLAIN, 12);
    int passValue = 0; //stat values
    int stackValue = 0;
    int scoreValue = 0;
    int mValX = 0; //mouse value 'x'
    int mValY = 0; //mouse value 'y'
    boolean isMenu = true; //boolean to determine if the player is viewing the main menu
    boolean isWin = false; //boolean to determine if the player has won

    String dragType = ""; //used in placement, dropping, and dragging

    CardClass tempCard; //has variety of purposes, ie. dragging, determining a tableau's top card, etc.
    TableauClass tempTableau; //for >1 tableau dragging

    DeckClass stock; //top left, face down, user draws cards from here
    DeckClass waste; //right of stock, where stock is moved to after being drawn, cards here are face up
    DeckClass restartDeck; //used in Restart button action, to store the original deck used to set up the game

    FoundationClass fd; //foundations top right
    FoundationClass fc;
    FoundationClass fh;
    FoundationClass fs;


    TableauClass t[]; //array of tableau objects

    /////////////////////////////////////////////////////
    public void delay (int iDelayTime)  //used in waste to stock animation
    {
	long lFinalTime = System.currentTimeMillis () + iDelayTime;
	do
	{
	}
	while (lFinalTime >= System.currentTimeMillis ());
    }


    /////////////////////////////////////////////////////
    public void init ()
    { //initialization method
	setSize (800, 800);
	setLayout (lm);
	g = getGraphics ();
	isMenu = true;
	
	/////////////////////////////////////////////////////
	tempTableau = new TableauClass (); //instantiates objects that will be used
	tempCard = new CardClass ();
	tempCard.setCentre (900, 900);
	stock = new DeckClass ();
	waste = new DeckClass ();

	offscreen = createImage (800, 800);
	BackBuffer = offscreen.getGraphics ();

	fd = new FoundationClass ('d'); //d = diamond
	fc = new FoundationClass ('c'); //c = club
	fh = new FoundationClass ('h'); //h = heart
	fs = new FoundationClass ('s'); //s = spade

	t = new TableauClass [7]; //7 tableaus
	for (int x = 0 ; x < 7 ; x++)
	{
	    t [x] = new TableauClass (); //keep in mind that arrays start at 0
	}

	/////////////////////////////////////////////////////
	pNorth.setLayout (new GridBagLayout ()); //adds buttons to north of layout with gridbag
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.weightx = 3;
	gbc.weighty = 3;
	gbc.ipadx = 50;
	gbc.ipady = 30;

	pNorth.add (pS1);
	pNorth.add (pS2);
	pNorth.add (pS3);
	pNorth.add (pS4);
	pNorth.add (pS5);

	add ("North", pNorth);

	/////////////////////////////////////////////////////
	pSouth.setLayout (new GridLayout ()); //adds stats to south of layout with flow

	pS1.setLayout (new FlowLayout (FlowLayout.LEFT, 1, 20));
	pS1.add (lMouse);
	pS1.add (tfMouse);

	pSouth.add (bNewGame, gbc);
	pSouth.add (bInstr, gbc);
	pSouth.add (bRestart, gbc);
	pSouth.add (bQuit, gbc);

	add ("South", pSouth);

	/////////////////////////////////////////////////////
	pEast.setLayout (new GridLayout (22, 1, 3, 3)); //adds mouse stats west of layout with grid
	add ("East", pEast);

	/////////////////////////////////////////////////////
	bNewGame.addActionListener (this); //starts the button and mouse listeners
	bRestart.addActionListener (this);
	bQuit.addActionListener (this);
	bInstr.addActionListener (this);
	addMouseListener (this);
	addMouseMotionListener (this);

	instr.setBackground (Color.gray);
	instr.setBounds (new Rectangle (0, 100, 800, 700));
	visInstr = false;
	add ("Center", instr);
	instr.hide ();

    } // init method


    /////////////////////////////////////////////////////
    public void refreshStatsRepaint ()
    {
	if (stock.isPointInside (mValX, mValY) == true) //if mouse is inside the stock,
	{ //set the stat stackValue to the number of cards in the stock
	    stackValue = stock.getSizeDeck ();
	}
	else if (waste.isPointInside (mValX, mValY) == true) //works same as above, for waste
	{
	    stackValue = waste.getSizeDeck ();
	}
	else if (fd.isPointInside (mValX, mValY) == true) //for diamond foundation
	{
	    stackValue = fd.getSizeDeck ();
	}
	else if (fh.isPointInside (mValX, mValY) == true) //heart foundation
	{
	    stackValue = fh.getSizeDeck ();
	}
	else if (fc.isPointInside (mValX, mValY) == true) //club foundation
	{
	    stackValue = fc.getSizeDeck ();
	}
	else if (fs.isPointInside (mValX, mValY) == true) //spade foundation
	{
	    stackValue = fs.getSizeDeck ();
	}
	else if (t [0].isPointInsideNthCard (mValX, mValY) != -1) //tableau 1,
	{ //the isPointInsideNthCard(x,y) returns -1 if the mouse is not inside any card within the tableau
	    stackValue = t [0].getSizeDeck ();
	}
	else if (t [1].isPointInsideNthCard (mValX, mValY) != -1) //tableau 2
	{
	    stackValue = t [1].getSizeDeck ();
	}
	else if (t [2].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [2].getSizeDeck ();
	}
	else if (t [3].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [3].getSizeDeck ();
	}
	else if (t [4].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [4].getSizeDeck ();
	}
	else if (t [5].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [5].getSizeDeck ();
	}
	else if (t [6].isPointInsideNthCard (mValX, mValY) != -1) //tableau 7
	{
	    stackValue = t [6].getSizeDeck ();
	}
	else
	{
	    stackValue = 0;
	}
	tfPass.setText (Integer.toString (passValue)); //sets the stat textfields
	tfStack.setText (Integer.toString (stackValue));
	tfScore.setText (Integer.toString (scoreValue));
	repaint (); //repaints the applet
    }


    /////////////////////////////////////////////////////
    public void mouseClicked (MouseEvent e)
    {
    }


    public void mouseEntered (MouseEvent e)
    {
    }


    public void mouseExited (MouseEvent e)
    {

	mValX = -1;
	mValY = -1;
	refreshStatsRepaint ();
    }


    public void mousePressed (MouseEvent e)
    {

	tfMouse.setText ("Pressed");
	if (stock.isPointInside (mValX, mValY) == true) //move stock to waste
	{
	    if (stock.getSizeDeck () != 0)
	    {
		tempCard = stock.getCard (0);
		stock.deleteCard (0);
		tempCard.erase (g);
		tempCard.setFaceUp (true);
		waste.addtheCard (tempCard, 0);
	    }
	    else
	    {
		while (waste.getSizeDeck () != 0) //move waste to stock
		{
		    tempCard = waste.getCard (0);
		    waste.deleteCard (0);
		    tempCard.erase (g);
		    tempCard.setFaceUp (false);
		    stock.addtheCard (tempCard, 0);
		    refreshStatsRepaint ();
		    delay (60);
		}
		passValue += 1; //increment pass stat by 1
	    }
	}

	if (dragType == "") //not currently dragging
	{
	    if (waste.isPointInside (mValX, mValY) == true && waste.getSizeDeck () != 0)
	    { //if mouse is inside waste, and waste is not empty, set dragType to "waste" and let tempCard = card clicked,
		dragType = "waste";
		tempCard = waste.getCard (0); //temp card will be used in dragging
		waste.deleteCard (0);
	    }
	    else if (t [0].isPointInsideNthCard (mValX, mValY) != -1 && t [0].getSizeDeck () != 0)
	    { //if mouse inside tableau 1, and it's not empty
		if (t [0].isPointInsideNthCard (mValX, mValY) == 1)
		{ //if first card
		    dragType = "1t0"; //drag single card
		    tempCard = t [0].getCard (0); //temp card will be used in dragging
		    t [0].deleteCard (0);
		}
		else
		{
		    tempCard = t [0].getCard (t [0].isPointInsideNthCard (mValX, mValY) - 1); //used below in getFaceUp()
		    if (tempCard.getFaceUp () == true) //cards dragged must be face up
		    {
			dragType = "t0"; //drag multiple card
			for (int i = 0 ; i < t [0].isPointInsideNthCard (mValX, mValY) ; i++) //copy selected cards to tempTableau
			{
			    tempCard = t [0].getCard (i);
			    tempTableau.addtheCard (tempCard, i); //used for dragging
			}
			for (int i = 1 ; i <= tempTableau.getSizeDeck () ; i++) //deletes copied cards
			{
			    t [0].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [1].isPointInsideNthCard (mValX, mValY) != -1 && t [1].getSizeDeck () != 0) //tableau 2, works same as above
	    {
		if (t [1].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t1";
		    tempCard = t [1].getCard (0);
		    t [1].deleteCard (0);
		}
		else
		{
		    tempCard = t [1].getCard (t [1].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t1";
			for (int i = 0 ; i < t [1].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [1].getCard (i);
			    tempTableau.addtheCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getSizeDeck () ; i++)
			{
			    t [1].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [2].isPointInsideNthCard (mValX, mValY) != -1 && t [2].getSizeDeck () != 0) //tableau 3
	    {
		if (t [2].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t2";
		    tempCard = t [2].getCard (0);
		    t [2].deleteCard (0);
		}
		else
		{
		    tempCard = t [2].getCard (t [2].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t2";
			for (int i = 0 ; i < t [2].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [2].getCard (i);
			    tempTableau.addtheCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getSizeDeck () ; i++)
			{
			    t [2].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [3].isPointInsideNthCard (mValX, mValY) != -1 && t [3].getSizeDeck () != 0) //tableau 4
	    {
		if (t [3].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t3";
		    tempCard = t [3].getCard (0);
		    t [3].deleteCard (0);
		}
		else
		{
		    tempCard = t [3].getCard (t [3].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t3";
			for (int i = 0 ; i < t [3].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [3].getCard (i);
			    tempTableau.addtheCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getSizeDeck () ; i++)
			{
			    t [3].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [4].isPointInsideNthCard (mValX, mValY) != -1 && t [4].getSizeDeck () != 0) //tableau 5
	    {
		if (t [4].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t4";
		    tempCard = t [4].getCard (0);
		    t [4].deleteCard (0);
		}
		else
		{
		    tempCard = t [4].getCard (t [4].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t4";
			for (int i = 0 ; i < t [4].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [4].getCard (i);
			    tempTableau.addtheCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getSizeDeck () ; i++)
			{
			    t [4].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [5].isPointInsideNthCard (mValX, mValY) != -1 && t [5].getSizeDeck () != 0) //tableau 6
	    {
		if (t [5].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t5";
		    tempCard = t [5].getCard (0);
		    t [5].deleteCard (0);
		}
		else
		{
		    tempCard = t [5].getCard (t [5].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t5";
			for (int i = 0 ; i < t [5].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [5].getCard (i);
			    tempTableau.addtheCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getSizeDeck () ; i++)
			{
			    t [5].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [6].isPointInsideNthCard (mValX, mValY) != -1 && t [6].getSizeDeck () != 0) //tableau 7
	    {
		if (t [6].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t6";
		    tempCard = t [6].getCard (0);
		    t [6].deleteCard (0);
		}
		else
		{
		    tempCard = t [6].getCard (t [6].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t6";
			for (int i = 0 ; i < t [6].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [6].getCard (i);
			    tempTableau.addtheCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getSizeDeck () ; i++)
			{
			    t [6].deleteCard (0);
			}
		    }
		}
	    }
	}

	if (dragType != "") //prevents graphical bugs
	{
	    g.setColor (Color.white);
	    g.fill3DRect (0, 0, 800, 800, true);
	}

	refreshStatsRepaint ();
    }



    public boolean checkAndPlaceWTFT ()  //waste card or single tableau card to foundation or tableau
    {
	//to foundation
	if (fh.isPointInside (mValX, mValY) && fh.allowCardPlace (tempCard)) //1 heart; if mouse is inside the first card of the tableau and placement is allowed
	{ //functions used work exactly as their names imply, explained in their respective classes
	    fh.addtheCard (tempCard, 0);
	    scoreValue += 1;
	}
	else if (fd.isPointInside (mValX, mValY) && fd.allowCardPlace (tempCard)) //2 diamond
	{
	    fd.addtheCard (tempCard, 0);
	    scoreValue += 1;
	}
	else if (fs.isPointInside (mValX, mValY) && fs.allowCardPlace (tempCard)) //3 spade
	{
	    fs.addtheCard (tempCard, 0);
	    scoreValue += 1;
	}
	else if (fc.isPointInside (mValX, mValY) && fc.allowCardPlace (tempCard)) //4 club
	{
	    fc.addtheCard (tempCard, 0);
	    scoreValue += 1;
	} //to tableau
	else if (t [0].isPointInsideNthCard (mValX, mValY) == 1 && t [0].allowCardPlace (tempCard))
	{ //if mouse is inside the first card of the tableau and placement is allowed
	    t [0].addtheCard (tempCard, 0);
	}
	else if (t [1].isPointInsideNthCard (mValX, mValY) == 1 && t [1].allowCardPlace (tempCard))
	{
	    t [1].addtheCard (tempCard, 0);
	}
	else if (t [2].isPointInsideNthCard (mValX, mValY) == 1 && t [2].allowCardPlace (tempCard))
	{
	    t [2].addtheCard (tempCard, 0);
	}
	else if (t [3].isPointInsideNthCard (mValX, mValY) == 1 && t [3].allowCardPlace (tempCard))
	{
	    t [3].addtheCard (tempCard, 0);
	}
	else if (t [4].isPointInsideNthCard (mValX, mValY) == 1 && t [4].allowCardPlace (tempCard))
	{
	    t [4].addtheCard (tempCard, 0);
	}
	else if (t [5].isPointInsideNthCard (mValX, mValY) == 1 && t [5].allowCardPlace (tempCard))
	{
	    t [5].addtheCard (tempCard, 0);
	}
	else if (t [6].isPointInsideNthCard (mValX, mValY) == 1 && t [6].allowCardPlace (tempCard))
	{
	    t [6].addtheCard (tempCard, 0);
	}
	else
	{
	    return false; //placement not successful
	}
	return true; //placement was successful
    }


    public boolean checkAndPlaceTT ()  //multiple tableau to tableau
    {
	if (t [0].isPointInsideNthCard (mValX, mValY) == 1 && t [0].allowCardPlace (tempCard))
	{ //if mouse is over the first tableau card, and placement is allowed
	    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
	    { //add tempTableau to tableau 1
		tempCard = tempTableau.getCard (i);
		t [0].addtheCard (tempCard, 0);
	    }
	}
	else if (t [1].isPointInsideNthCard (mValX, mValY) == 1 && t [1].allowCardPlace (tempCard))
	{ //works same as above
	    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [1].addtheCard (tempCard, 0);
	    }
	}
	else if (t [2].isPointInsideNthCard (mValX, mValY) == 1 && t [2].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [2].addtheCard (tempCard, 0);
	    }
	}
	else if (t [3].isPointInsideNthCard (mValX, mValY) == 1 && t [3].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [3].addtheCard (tempCard, 0);
	    }
	}
	else if (t [4].isPointInsideNthCard (mValX, mValY) == 1 && t [4].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [4].addtheCard (tempCard, 0);
	    }
	}
	else if (t [5].isPointInsideNthCard (mValX, mValY) == 1 && t [5].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [5].addtheCard (tempCard, 0);
	    }
	}
	else if (t [6].isPointInsideNthCard (mValX, mValY) == 1 && t [6].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [6].addtheCard (tempCard, 0);
	    }
	}
	else
	{
	    return false; //failure
	}
	return true; //success
    }


    public void mouseReleased (MouseEvent e)
    {
	tfMouse.setText ("Released");
	if (dragType != "") //if something is being dragged
	{
	    tempCard.erase (g);
	    tempTableau.EraseTableau (g);
	    if (dragType == "waste" && checkAndPlaceWTFT () == false)
	    { //if placement failed, put the card back where it was before
		waste.addtheCard (tempCard, 0);
	    }
	    else if (dragType == "1t0")
	    { //single tableau dragging
		if (checkAndPlaceWTFT () == false)
		{ //if card placement failed, put it back
		    t [0].addtheCard (tempCard, 0);
		}
		else if (t [0].getSizeDeck ()
			!= 0)                                //if tableau is not empty
		{
		    tempCard = t [0].getCard (0);
		    tempCard.setFaceUp (true); //force card below to be face up
		}
	    }
	    else if (dragType == "1t1")
	    { //same as above
		if (checkAndPlaceWTFT () == false)
		{
		    t [1].addtheCard (tempCard, 0);
		}
		else if (t [1].getSizeDeck () != 0)
		{
		    tempCard = t [1].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t2")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [2].addtheCard (tempCard, 0);
		}
		else if (t [2].getSizeDeck () != 0)
		{
		    tempCard = t [2].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t3")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [3].addtheCard (tempCard, 0);
		}
		else if (t [3].getSizeDeck () != 0)
		{
		    tempCard = t [3].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t4")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [4].addtheCard (tempCard, 0);
		}
		else if (t [4].getSizeDeck () != 0)
		{
		    tempCard = t [4].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t5")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [5].addtheCard (tempCard, 0);
		}
		else if (t [5].getSizeDeck () != 0)
		{
		    tempCard = t [5].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t6")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [6].addtheCard (tempCard, 0);
		}
		else if (t [6].getSizeDeck () != 0)
		{
		    tempCard = t [6].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t0")
	    { //gets the bottom card of the tableau being dragged, used in placement allowance
		tempCard = tempTableau.getCard (tempTableau.getSizeDeck () - 1);
		if (checkAndPlaceTT () == false)
		{ //if placement failed, put the tableau being dragged back
		    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [0].addtheCard (tempCard, 0);
		    }
		}
		else if (t [0].getSizeDeck () != 0) //if tableau is not empty
		{ //force card below to be face up
		    tempCard = t [0].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t1")
	    { //works same as above
		tempCard = tempTableau.getCard (tempTableau.getSizeDeck () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [1].addtheCard (tempCard, 0);
		    }
		}
		else if (t [1].getSizeDeck () != 0)
		{
		    tempCard = t [1].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t2")
	    {
		tempCard = tempTableau.getCard (tempTableau.getSizeDeck () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [2].addtheCard (tempCard, 0);
		    }
		}
		else if (t [2].getSizeDeck () != 0)
		{
		    tempCard = t [2].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t3")
	    {
		tempCard = tempTableau.getCard (tempTableau.getSizeDeck () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [3].addtheCard (tempCard, 0);
		    }
		}
		else if (t [3].getSizeDeck () != 0)
		{
		    tempCard = t [3].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t4")
	    {
		tempCard = tempTableau.getCard (tempTableau.getSizeDeck () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [4].addtheCard (tempCard, 0);
		    }
		}
		else if (t [4].getSizeDeck () != 0)
		{
		    tempCard = t [4].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t5")
	    {
		tempCard = tempTableau.getCard (tempTableau.getSizeDeck () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [5].addtheCard (tempCard, 0);
		    }
		}
		else if (t [5].getSizeDeck () != 0)
		{
		    tempCard = t [5].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t6")
	    {
		tempCard = tempTableau.getCard (tempTableau.getSizeDeck () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getSizeDeck () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [6].addtheCard (tempCard, 0);
		    }
		}
		else if (t [6].getSizeDeck () != 0)
		{
		    tempCard = t [6].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    dragType = ""; //clears dragType
	    tempTableau = new TableauClass (); //clears tempTableau
	}

	refreshStatsRepaint ();
    }


    public void mouseDragged (MouseEvent e)
    {
	tfMouse.setText ("Dragged");
	mValX = e.getX ();
	mValY = e.getY ();

	if (dragType == "waste" || dragType == "1t0" || dragType == "1t1" || dragType == "1t2" ||
		dragType == "1t3" || dragType == "1t4" || dragType == "1t5" || dragType == "1t6")
	{ //drawing for non >1 tableau dragging
	    //tempCard.erase (g);
	    tempCard.setCentre (mValX, mValY);
	    //tempCard.draw (g);
	}
	else if (dragType != "" && dragType != "waste")
	{ //drawing for >1 tableau dragging
	    //tempTableau.EraseTableau (g);
	    tempTableau.setDeckCentre (mValX, mValY);
	    //tempTableau.drawTableau (g);
	}

	refreshStatsRepaint ();
    }


    public void mouseMoved (MouseEvent e)
    {
	tfMouse.setText ("Moved");
	mValX = e.getX ();
	mValY = e.getY ();
	refreshStatsRepaint ();
    }


    /////////////////////////////////////////////////////
    public void actionPerformed (ActionEvent e)
    {
	Object objSource = e.getSource ();

	if (objSource == bNewGame) //if NEW GAME is pressed
	{
	    isMenu = false;
	    g.setColor (Color.white); //clear the screen
	    g.fill3DRect (0, 0, 800, 800, true);

	    passValue = 0; //set all stats to 0
	    scoreValue = 0;

	    tempTableau = new TableauClass ();

	    stock = new DeckClass ('s'); //create a standard deck
	    stock.shuffle ();

	    restartDeck = new DeckClass ();
	    for (int i = 0 ; i <= 51 ; i++) //duplicate stock into restartDeck
	    {
		tempCard = stock.getCard (i);
		restartDeck.addtheCard (tempCard, 51);
	    }

	    waste = new DeckClass ();

	    fd = new FoundationClass ('d');
	    fc = new FoundationClass ('c');
	    fh = new FoundationClass ('h');
	    fs = new FoundationClass ('s');

	    t = new TableauClass [7];
	    for (int x = 0 ; x < 7 ; x++)
	    {
		t [x] = new TableauClass ();
	    }

	    stock.setDeckCentre (100, 150); //set appropriate locations of the decks
	    waste.setDeckCentre (200, 150);

	    fd.setDeckCentre (400, 150);
	    fc.setDeckCentre (500, 150);
	    fh.setDeckCentre (600, 150);
	    fs.setDeckCentre (700, 150);

	    for (int x = 0 ; x < 7 ; x++) //set locations of tableaus
	    {
		t [x].setDeckCentre ((x + 1) * 100, 300);
	    }

	    for (int x = 0 ; x < 7 ; x++) //distribute stock into tableaus
	    {
		for (int y = 0 ; y <= x ; y++)
		{ //first tableau starts with 1 card, seconds tableau starts with 2 cards, etc. goes up to 7th tableau with 7 card
		    tempCard = stock.getCard (0);
		    stock.deleteCard (0);
		    tempCard.erase (g);
		    if (y == x)
		    { //first card in each tableau is face up
			tempCard.setFaceUp (true);
		    }
		    t [x].addtheCard (tempCard, 0);
		}
	    }
	}

	else if (objSource == bInstr)
	{
	    if (!visInstr)
	    {
		visInstr = true;
		instr.show ();
	    }
	    else
	    {
		visInstr = false;
		instr.hide ();
	    }
	}



	else if (objSource == bRestart)
	{ //works same as NEW GAME, but copies the restart deck into stock rather than newing and shuffling a standard stock deck
	    isMenu = false;
	    g.setColor (Color.white);
	    g.fill3DRect (0, 0, 800, 800, true);

	    passValue = 0;
	    scoreValue = 0;

	    tempTableau = new TableauClass ();
	    stock = new DeckClass ();

	    for (int i = 0 ; i <= 51 ; i++)
	    { //copy restartDeck to stock
		tempCard = restartDeck.getCard (i);
		tempCard.setFaceUp (false);
		stock.addtheCard (tempCard, 51);
	    }

	    waste = new DeckClass ();

	    fd = new FoundationClass ('d');
	    fc = new FoundationClass ('c');
	    fh = new FoundationClass ('h');
	    fs = new FoundationClass ('s');

	    t = new TableauClass [7];
	    for (int x = 0 ; x < 7 ; x++)
	    {
		t [x] = new TableauClass ();
	    }

	    stock.setDeckCentre (100, 150);
	    waste.setDeckCentre (200, 150);

	    fd.setDeckCentre (400, 150);
	    fc.setDeckCentre (500, 150);
	    fh.setDeckCentre (600, 150);
	    fs.setDeckCentre (700, 150);

	    for (int x = 0 ; x < 7 ; x++)
	    {
		t [x].setDeckCentre ((x + 1) * 100, 300);
	    }

	    for (int x = 0 ; x < 7 ; x++)
	    {
		for (int y = 0 ; y <= x ; y++)
		{
		    tempCard = stock.getCard (0);
		    stock.deleteCard (0);
		    tempCard.erase (g);
		    if (y == x)
		    {
			tempCard.setFaceUp (true);
		    }
		    t [x].addtheCard (tempCard, 0);
		}
	    }
	}
	else if (objSource == bQuit)
	{ //close the game
	    System.exit (0);
	}
	instr.repaint ();
	refreshStatsRepaint ();
    }



    /////////////////////////////////////////////////////
    public void paint (Graphics g)
    {
	Dimension Screen = getSize ();
	BackBuffer.setColor (Color.white);
	BackBuffer.fillRect (0, 0, Screen.width, Screen.height);
	if (isMenu == true)
	{
	    BackBuffer.setColor (Color.black);
	    BackBuffer.setFont (f1);
	    BackBuffer.drawString ("KLONDIKE SOLITAIRE", 220, 400);
	    BackBuffer.setFont (f2);
	    BackBuffer.drawString ("Klondike is version of Solitaire that is also named the patience game.", 205, 425);
	    CardClass c = new CardClass ();
	    c.setFaceValue (1);
	    c.setSuitValue (1);
	    c.setCentre (300, 300);
	    c.draw (BackBuffer);
	    CardClass c2 = new CardClass ();
	    c2.setFaceValue (2);
	    c2.setSuitValue (2);
	    c2.setCentre (395, 300);
	    c2.draw (BackBuffer);
	    CardClass c3 = new CardClass ();
	    c3.setFaceValue (3);
	    c3.setSuitValue (3);
	    c3.setCentre (490, 300);
	    c3.draw (BackBuffer);
	    CardClass c4 = new CardClass ();
	    c4.setFaceValue (11);
	    c4.setSuitValue (1);
	    c4.setCentre (300, 490);
	    c4.draw (BackBuffer);
	    CardClass c5 = new CardClass ();
	    c2.setFaceValue (12);
	    c2.setSuitValue (2);
	    c2.setCentre (395, 490);
	    c2.draw (BackBuffer);
	    CardClass c6 = new CardClass ();
	    c3.setFaceValue (13);
	    c3.setSuitValue (3);
	    c3.setCentre (490, 490);
	    c3.draw (BackBuffer);
	}

	BackBuffer.setColor (Color.white);
	stock.TopDraw (BackBuffer);
	waste.TopDraw (BackBuffer);
	fd.TopDraw (BackBuffer);
	fc.TopDraw (BackBuffer);
	fh.TopDraw (BackBuffer);
	fs.TopDraw (BackBuffer);
	for (int x = 0 ; x < 7 ; x++)
	{
	    t [x].drawTableau (BackBuffer);
	}
	tempTableau.drawTableau (BackBuffer);
	tempCard.draw (BackBuffer);
	g.drawImage (offscreen, 0, 0, this);

    }


    public void update (Graphics g)
    {
	paint (g);
    }
}


