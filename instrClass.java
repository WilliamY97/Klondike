import java.awt.*;
import java.util.*;

public class instrClass extends Canvas
{
    Font f1 = new Font ("SanSerif", Font.BOLD, 26);
    Font f2 = new Font ("SanSerif", Font.PLAIN, 14);

    // writes down the instructions of the game
    public void paint (Graphics g)
    {
	g.setFont (f1);
	g.setColor (Color.white);
	g.drawString ("Klondike Solitaire Instructions", 10, 40);
	g.setFont (f2);
	g.drawString ("Click 'INSTRUCTIONS' to return to your last screen", 10, 60);
	g.drawString ("1. An ace can be moved to its suit stack.", 10, 120);
	g.drawString ("2. A card at the top of a stack can be moved to its corresponding suit stack if the rank of the card at the top of that", 10, 160);
	g.drawString ("   stack is one less than the card's rank.", 10, 178);
	g.drawString ("3. A card can be moved from the deck, a suit stack, or a building stack to a source stack if the card at the top of the", 10, 200);
	g.drawString ("   destination stack has a different color.", 10, 218);
	g.drawString ("4. Ordered cards at the bottom of a building stack can be moved to another building stack if there is a card in the", 10, 240);
	g.drawString ("   ordered sequence which has a different color and a rank one less than the card.", 10, 258);
    }
}



