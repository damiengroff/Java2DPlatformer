import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Plateforme extends Obstacles {
	
	private BufferedImage source;
	private TexturePaint texture;
	
	/** 
	 * Determine la taille et la position des plateformes
	 * Charge l'image source correspondant aux plateformes et le 
	 * transforme en texture
	 */
	public Plateforme(int posX, int posY, int tailleX, int tailleY) {
		super (posX, posY, tailleX, tailleY);
		
		//on charge l'image source
		try {
			source = ImageIO.read(new File("./Images/pTexture.jpg"));
		}
		catch(IOException exc) {
			exc.printStackTrace();
		}
		
		//création de la texture à partir de l'image source
		texture = new TexturePaint(source, new Rectangle(0, 0, 
									source.getWidth()/4, 
									source.getHeight()/4));
	}	
	
	/** 
	 * Dessine la plateforme 
	 */
	//on utilise Graphics2D.setPaint(TexturePaint) pour texturer les plateformes
	//https://stackoverflow.com/questions/15327220/fill-rectangle-with-pattern-in-java-swing
	public void dessin(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(texture);
        g2.fill(new Rectangle(posX, posY, tailleX, tailleY));
        g2.setStroke(new BasicStroke(2));   // bordures des plateformes
        g2.setColor(Color.black);
        g2.drawRect(posX, posY, tailleX, tailleY);
	}
	
	/** 
	 * Effet collision dans la boucle du player
	 */
	public void effetCollision(int c) {}
}
	
