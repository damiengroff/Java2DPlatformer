import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Piege extends Obstacles {
	
	private BufferedImage[] images = new BufferedImage[4];
	private int direction; /* prend les valeurs : 
							* 0: haut, 1: bas, 
							* 2: gauche, 3: droite
							*/
	
	/** 
	 * Determine la taille et la position des pieges
	 * Charge l'image source correspondant aux pieges
	 */
	public Piege(int posX, int posY, int direction) {
		super (posX, posY, 20, 25);
		this.direction = direction;
		
		try {
			for(int i = 0; i < images.length; i++) {
				String access = "./Images/piege/p"+i+".png";
				images[i] = ImageIO.read(new File(access));
			}
		}
		catch(IOException exc) {
			exc.printStackTrace();
		}
	}	
	
	/** 
	 * Dessine les pieges en fonction de la direction des pics
	 */
	public void dessin(Graphics g) {
		if (direction == 0) {
			g.drawImage(images[0], posX-15, posY-25, this);
		} else if (direction == 1) {
			g.drawImage(images[1], posX-15, posY, this);
		} else if (direction == 2) {
			g.drawImage(images[2], posX-30, posY-15, this);
		} else if (direction == 3) {
			g.drawImage(images[3], posX, posY-15, this);
		}
	}
	
	/** 
	 * Tue le joueur si il touche un piege
     * Supprime ce piege avant d'appeler la methode endGame pour eviter
     * de l'appeler plusieurs fois (car à cause du timer qui est trop 
     * rapide, la collision est détectee plusieurs fois);
	 */
	public void effetCollision(int contact) {
		Jeu.remove(this);
		Jeu.endGame();
	} 
}
