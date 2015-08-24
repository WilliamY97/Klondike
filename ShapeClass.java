import java.awt.*;

//Abstract class ShapeClass
public abstract class ShapeClass
{
    //Declares encapsulated data
    private int iCentreX = 100, iCentreY = 100, iHeight = 50, iWidth = 50;
    private Color iColor = Color.blue;
    private boolean isFilled = true;

    
    //Sets if a shape is filled in or not
    public void setIsFilled (boolean input)
    {
	isFilled = input;
    }

    //Gives back the state of whether the shape is filled in or not
    public boolean getIsFilled ()
    {
	return isFilled;
    }


    //Sets the centre coordinates of the shape to specified inputs (set method)
    public void setCentre (int inputX, int inputY)
    {
	iCentreX = inputX;
	iCentreY = inputY;
    }


    //Gives back value of iCentreX variable (get method)
    public int getCentreX ()
    {
	return iCentreX;
    }


    //Gives back value of iCentreY variable (get method)
    public int getCentreY ()
    {
	return iCentreY;
    }


    //Sets height of shape according to specified value (set method)
    public void setHeight (int input)
    {
	iHeight = input;
    }


    //Gives back value of height variable (get method)
    public int getHeight ()
    {
	return iHeight;
    }


    //Sets width of shape according to specified value (set method)
    public void setWidth (int input)
    {
	iWidth = input;
    }


    //Gives back value of width variable (get method)
    public int getWidth ()
    {
	return iWidth;
    }


    //Sets color of shape according to specified color (set method)
    public void setColor (Color input)
    {
	iColor = input;
    }


    //Gives back value of color variable (get method)
    public Color getColor ()
    {
	return iColor;
    }


    //Abstract draw method
    public abstract void draw (Graphics g);


    //Erases shape
    public void erase (Graphics g)
    {
	Color tempColor = iColor; //creates temp color that is same as shape's current color
	iColor = Color.white; //sets shape's color to white
	draw (g); //draws shape in white
	iColor = tempColor; //sets shape's color as temp color
    }
}
