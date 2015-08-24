import java.awt.*;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class FoundationClass extends DeckClass
{
    //suit default of foundation is going to be diamond
    private char Suitfoundation = 'd';
    //each char gives a different shape (e.g. h for heart)
    public FoundationClass ()
    {
	//nothing
    }


    public char getFoundationSuit ()
    {
	return Suitfoundation;
    }



    public boolean allowCardPlace (CardClass Cardtest)
    { //checks card being placed is 1 greater than the top card
	if (getSizeDeck () + 1 == Cardtest.getFaceValue ())
	    //checks if card's suit matches the foundation's suit
	    {
		if ((Cardtest.getSuitValue () == 1 && Suitfoundation == 'h') ||
			(Cardtest.getSuitValue () == 2 && Suitfoundation == 'd') ||
			(Cardtest.getSuitValue () == 3 && Suitfoundation == 's') ||
			(Cardtest.getSuitValue () == 4 && Suitfoundation == 'c'))
		{
		    return true;
		}
	    }
	return false;
    }


    //constructor allows set foundation of suit
    public FoundationClass (char typeofF)
    {
	Suitfoundation = typeofF;
    }


    //sets get suit foundation
    public void setFoundationSuit (char inputvalue)
    {
	Suitfoundation = inputvalue;
    }



    //////


    public void TopDraw (Graphics g)
    {
	//draws 1st card in foundation
	if (getSizeDeck () > 0)
	{
	    CardClass temporaryCard = getCard (0);
	    temporaryCard.setCentre (getDeckCentreX (), getDeckCentreY ());
	    temporaryCard.draw (g);
	}
	//if foundation empty - a large suit shape is drawn instead
	else
	{
	    g.setColor (Color.white);
	    g.fill3DRect (getDeckCentreX () - 35, getDeckCentreY () - 50, 70, 100, true);
	    g.setColor (Color.black);
	    g.draw3DRect (getDeckCentreX () - 35, getDeckCentreY () - 50, 70, 100, true);
	    if (Suitfoundation == 'h')
	    {
		HeartClass heart = new HeartClass ();
		heart.setHeight (30);
		heart.setCentre (getDeckCentreX (), getDeckCentreY ());
		heart.setColor (Color.red);
		heart.draw (g);
	    }
	    else if (Suitfoundation == 'd')
	    {
		DiamondClass diamond = new DiamondClass ();
		diamond.setHeight (30);
		diamond.setCentre (getDeckCentreX (), getDeckCentreY ());
		diamond.setColor (Color.red);
		diamond.draw (g);
	    }
	    else if (Suitfoundation == 's')
	    {
		SpadeClass spade = new SpadeClass ();
		spade.setHeight (30);
		spade.setCentre (getDeckCentreX (), getDeckCentreY ());
		spade.setColor (Color.black);
		spade.draw (g);
	    }
	    else if (Suitfoundation == 'c')
	    {
		ClubClass club = new ClubClass ();
		club.setHeight (30);
		club.setCentre (getDeckCentreX (), getDeckCentreY ());
		club.setColor (Color.black);
		club.draw (g);
	    }
	}
    }
}
