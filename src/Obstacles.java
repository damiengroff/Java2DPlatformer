import javax.swing.*;
import java.awt.*;

public abstract class Obstacles extends JFrame {

	protected int posX;
	protected int posY;
	protected int tailleX;
	protected int tailleY;

	/**
	 * Classe mere definissant la taille et la position des obstacles
	 */
	public Obstacles(int posX, int posY, int tailleX, int tailleY) {
		this.posX = posX;
		this.posY = posY;
		this.tailleX = tailleX;
		this.tailleY = tailleY;
	}

	/**
	 * Methode abstraite permettant de dessiner les obstacles
	 */
	public abstract void dessin(Graphics g);

	/**
	 * Methode abstraite permettant de definir l'effet des collisions
	 */
	public abstract void effetCollision(int contact);
}
