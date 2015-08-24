public abstract class SuitClass extends ShapeClass
{
    //Overrides setWidth method in ShapeClass
    //If width is set, height is automatically set as well
    public void setWidth (int input)
    {
	super.setHeight ((int) ((5 * input) / 4));
	super.setWidth (input);
    }


    //Overrides setHeight method in ShapeClass
    //If height is set, width is automatically set as well
    public void setHeight (int input)
    {
	super.setHeight (input);
	super.setWidth ((int) ((4 * input) / 5));
    }
}
