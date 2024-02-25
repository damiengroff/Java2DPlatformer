import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Sortie extends Obstacles {

	private BufferedImage image;

	/**
	 * Determine la taille et la position de la sortie
	 * Charge l'image source correspondant a la sortie
	 */
	public Sortie(int posX, int posY) {
		super(posX, posY, 100, 100);

		try {
			image = ImageIO.read(new File("./Images/porte.png"));
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Dessine l'image correspondant a la sortie
	 */
	public void dessin(Graphics g) {
		g.drawImage(image, posX, posY, this);
	}

	/**
	 * Passe au niveau suivant si le joueur est aux niveaux 1 ou 2
	 * Si le joueur est au niveau 3, affiche la fenetre de victoire
	 */
	public void effetCollision(int contact) {
		if (Jeu.currentLevel != 3) {
			Jeu.switchLevel();
		} else {
			Jeu.win();
			Jeu.remove(this);
		}
	}
}
