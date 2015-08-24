import java.awt.*;
import java.util.*;

public class DeckClass
{
    //Declare encapsulated data & vector
    private Vector vdeck = new Vector (0, 1);
    private int CentreX = -100;
    private int CentreY = -100;

    //Constructor for deck of 52 cards
    public DeckClass (char typeofdeck)
    {
	if (typeofdeck == 's') // standard deck
	{
	    for (int x = 1 ; x <= 4 ; x++)
	    {
		for (int y = 1 ; y <= 13 ; y++)
		{
		    CardClass card = new CardClass ();

		    card.setCardSize (3);
		    card.setSuitValue (x);
		    card.setFaceValue (y);
		    card.setFaceUp (false);

		    addtheCard (card, 51);
		}
	    }
	}
    }


    //Makes constructor for empty deck
    public DeckClass ()
    {
	//nothing
    }


    //Return size of deck in numbers
    public int getSizeDeck ()
    {
	return vdeck.size ();
    }



    //Removes card at specified location
    public void deleteCard (int position)
    {
	if (getSizeDeck () > 0)
	{
	    vdeck.removeElementAt (position);
	}
    }


    //Method for making random # between two set numbers
    private int RandInt (int iMin, int iMax)
    {
	return iMin + (int) (Math.random () * ((iMax - iMin) + 1));
    }


    //Returns card at specified location
    public CardClass getCard (int position)
    {
	if (getSizeDeck () > 0)
	{
	    return (CardClass) vdeck.elementAt (position);
	}
	else
	{
	    return null;
	}
    }


    //Add selected card to selected position in deck
    public void addtheCard (CardClass cardToAdd, int position)
    {
	if (position == 0 || vdeck.size () == 0) //If deck empty, add card to the top of deck
	{
	    vdeck.insertElementAt (cardToAdd, 0);
	}
	else if (position > vdeck.size ()) //If position greater than # of cards in deck, card is added to end of deck
	{
	    vdeck.insertElementAt (cardToAdd, vdeck.size ());
	}
	else
	{
	    vdeck.insertElementAt (cardToAdd, position); //Add card to selected location
	}
    }


    //Returns deck's CentreX coordinate
    public int getDeckCentreX ()
    {
	return CentreX;
    }


    //Returns deck's centreY coordinate
    public int getDeckCentreY ()
    {
	return CentreY;
    }


    //Draws top card of deck
    public void TopDraw (Graphics g)
    {
	if (getSizeDeck () > 0)
	{
	    CardClass temporaryCard = getCard (0);
	    temporaryCard.setCentre (CentreX, CentreY);
	    temporaryCard.draw (g);
	}
	else
	{
	    g.setColor (Color.black);
	    g.draw3DRect (CentreX - 35, CentreY - 50, 70, 100, true);
	}
    }


    //Sets deck centre coordinates to specific input
    public void setDeckCentre (int xinputs, int yinputs)
    {
	CentreX = xinputs;
	CentreY = yinputs;
    }


    //Determines if a point is in the deck's draw location
    public boolean isPointInside (int Xisin, int Yisin)
    {
	if (Xisin >= CentreX - 35 && Xisin <= CentreX + 35 && Yisin >= CentreY - 50 && Yisin <= CentreY + 50)
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }


    //Shuffles deck by removing random cards and readding them back into the deck at random locations
    public void shuffle ()
    {
	if (getSizeDeck () > 0)
	{
	    int positionRandom;
	    for (int i = 0 ; i <= (getSizeDeck () * getSizeDeck ()) ; i++)
	    {
		positionRandom = RandInt (0, getSizeDeck () - 1);

		CardClass tempCard = getCard (positionRandom);
		deleteCard (positionRandom);
		addtheCard (tempCard, getSizeDeck ());
	    }
	}
    }
}
