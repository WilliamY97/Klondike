import java.awt.*;

public class SpadeClass extends SuitClass
{
    public void draw (Graphics g)
    {
	//Draws a spade
	//Declares two arrays for X & Y coordinates of spade
	int iPointsX[] = new int [3];
	int iPointsY[] = new int [3];


	// calculates points on spade & store in the arrays
	iPointsX [0] = getCentreX () - getWidth () / 2;
	iPointsY [0] = getCentreY ();
	iPointsX [1] = getCentreX ();
	iPointsY [1] = getCentreY () - getHeight () / 2;
	iPointsX [2] = getCentreX () + getWidth () / 2;
	iPointsY [2] = getCentreY ();

	// draws the spade using methods available from the Console object (c)
	g.setColor (getColor ());
	g.fillPolygon (iPointsX, iPointsY, 3);


	iPointsX [0] = getCentreX ();
	iPointsY [0] = getCentreY () - (int) (getHeight () / 6);
	iPointsX [1] = getCentreX () + (int) (getWidth () / 6);
	iPointsY [1] = getCentreY () + (int) (getHeight () / 2);
	iPointsX [2] = getCentreX () - (int) (getWidth () / 6);
	iPointsY [2] = getCentreY () + (int) (getHeight () / 2);

	g.fillPolygon (iPointsX, iPointsY, 3);

	g.fillArc (getCentreX () - (int) (getWidth () / 2), getCentreY () - (int) (getHeight () / 4), (int) (getWidth () / 2), (int) (getHeight () / 2), 180, 180);

	g.fillArc (getCentreX (), getCentreY () - (int) (getHeight () / 4), (int) (getWidth () / 2), (int) (getHeight () / 2), 180, 180);
    }
}
