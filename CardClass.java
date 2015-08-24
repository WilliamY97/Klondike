import java.awt.*;

public class CardClass extends ShapeClass
{
    //Declares encapsulated data
    private int SuitValue = 1;
    private int FaceValue = 1;
    private Color SuitColor = Color.red;
    private boolean FaceUp = true;

    //Constructor method
    public CardClass ()
    {
	setCentre (320, 250);
	setHeight (100);
	setWidth (70);
    }
    

    //Sets card's suit value according to specified value (set method)
    //Sets color accordingly
    //ie. If user sets suit to a heart, then suit color is automatically set to red
    public void setSuitValue (int input)
    {
	SuitValue = input;
	//If suit is a heart or diamond, set suit's color to red
	if (SuitValue == 1 || SuitValue == 2)
	{
	    SuitColor = Color.red;
	}
	//If suit is a spade or a club, set suit's color to black
	else if (SuitValue == 3 || SuitValue == 4)
	{
	    SuitColor = Color.black;
	}
    }


    //Gives back card's suit value
    public int getSuitValue ()
    {
	return SuitValue;
    }


    //Sets card's face value according to specified value
    public void setFaceValue (int input)
    {
	FaceValue = input;
    }


    //Gives back card's face value
    public int getFaceValue ()
    {
	return FaceValue;
    }


    //Sets card face up or face down
    public void setFaceUp (boolean input)
    {
	FaceUp = input;
    }


    //Gives back card's FaceUp value
    public boolean getFaceUp ()
    {
	return FaceUp;
    }


    //Sets card's size accordingly to one of 4 sizes (inputs)
    public void setCardSize (int input)
    {
	if (input == 1)
	{
	    setHeight (60);
	    setWidth (42);
	}
	else if (input == 2)
	{
	    setHeight (80);
	    setWidth (56);
	}
	else if (input == 3)
	{
	    setHeight (100);
	    setWidth (70);
	}
	else
	{
	    setHeight (120);
	    setWidth (84);
	}
    }


    //Converts card's suit value to string and returns it
    private String SuitValueToString ()
    {
	if (FaceValue == 1)
	{
	    return "A";
	}
	else if (FaceValue == 11)
	{
	    return "J";
	}
	else if (FaceValue == 12)
	{
	    return "Q";
	}
	else if (FaceValue == 13)
	{
	    return "K";
	}
	else if (FaceValue >= 2 && FaceValue <= 10)
	{
	    return Integer.toString (FaceValue);
	}
	else
	{
	    return "";
	}

    }


    //Overrides erase method in ShapeClass
    //Erases card by drawing a white rectangle over card
    public void erase (Graphics g)
    {
	g.setColor (Color.white);

	g.fillRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2, getWidth () + 1, getHeight () + 1);
    }


    //Card's draw method
    public void draw (Graphics g)
    {
	if (FaceUp == true) //If card's FaceUp value is set to true, program will draw the card
	{
	    g.setColor (Color.white);
	    g.fill3DRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2, getWidth () + 1, getHeight () + 1, true);
	    g.setColor (Color.gray);
	    g.draw3DRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2, getWidth (), getHeight (), true);
	    g.setColor (SuitColor);

	    //Determines what suit to draw based off encapsulated data SuitValue
	    if (SuitValue == 1)
	    {
		HeartClass heart = new HeartClass ();
		heart.setHeight ((int) getHeight () / 4);
		heart.setCentre (getCentreX (), getCentreY ());
		heart.setColor (SuitColor);
		heart.draw (g);
	    }
	    else if (SuitValue == 2)
	    {
		DiamondClass diamond = new DiamondClass ();
		diamond.setHeight ((int) getHeight () / 4);
		diamond.setCentre (getCentreX (), getCentreY ());
		diamond.setColor (SuitColor);
		diamond.draw (g);
	    }
	    else if (SuitValue == 3)
	    {
		SpadeClass spade = new SpadeClass ();
		spade.setHeight ((int) getHeight () / 4);
		spade.setCentre (getCentreX (), getCentreY ());
		spade.setColor (SuitColor);
		spade.draw (g);
	    }
	    else if (SuitValue == 4)
	    {
		ClubClass club = new ClubClass ();
		club.setHeight ((int) getHeight () / 4);
		club.setCentre (getCentreX (), getCentreY ());
		club.setColor (SuitColor);
		club.draw (g);
	    }

	    //Draws face value of card in top left corner
	    g.setFont (new Font ("SanSerif", Font.BOLD, (getHeight () / 4)));
	    g.drawString (SuitValueToString (), getCentreX () - (int) (getWidth () * 0.4), getCentreY () - (int) (getHeight () / 4));
	}
	else //If card's FaceUp value is set to false, program will draw a green face down card
	{
	    
	    g.setColor (Color.red);
	    
	    g.fill3DRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2, getWidth (), getHeight (), true);
	    g.setColor (Color.black);
	    g.draw3DRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2, getWidth (), getHeight (), true);
	 
	}
    }
}
