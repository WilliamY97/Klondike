import java.awt.*;

public class DiamondClass extends SuitClass
{
    public void draw (Graphics g)
    {
	//Draws a diamond
	//Declares two arrays for X & Y coordinates of diamond
	int iPointsX[] = new int [4];
	int iPointsY[] = new int [4];

	// calculates points on diamond & store in the arrays
	iPointsX [0] = getCentreX () - getWidth () / 2;
	iPointsY [0] = getCentreY ();
	iPointsX [1] = getCentreX ();
	iPointsY [1] = getCentreY () - getHeight () / 2;
	iPointsX [2] = getCentreX () + getWidth () / 2;
	iPointsY [2] = getCentreY ();
	iPointsX [3] = getCentreX ();
	iPointsY [3] = getCentreY () + getHeight () / 2;

	// draws the diamond using methods available from the Console object (c)
	g.setColor (getColor ());
	g.fillPolygon (iPointsX, iPointsY, 4);
    }


}
