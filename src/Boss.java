import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Boss extends Obstacles implements ActionListener {

	public Timer enemySpawnTimer;
	private int initPosX;
	private int initPosY;
	private boolean dead = false;
	private BufferedImage image;

	/**
	 * Determine la taille et la position du boss
	 * Charge l'image source correspondant au boss
	 * Lance un timer qui va créer un ennemi tous les 5 sec
	 */
	public Boss(int posX, int posY) {
		super(posX, posY, 150, 150);
		initPosX = posX;
		initPosY = posY;

		try {
			image = ImageIO.read(new File("resources/images/BossFinal.png"));
		} catch (IOException exc) {
			exc.printStackTrace();
		}

		enemySpawnTimer = new Timer(5000, this);
		enemySpawnTimer.start();
	}

	/**
	 * Dessine l'image du boss si il n'est pas mort
	 * Si il est mort, animation d'un rectangle qui change
	 * de couleur aléatoirement.
	 */
	public void dessin(Graphics g) {
		if (!dead) {
			g.drawImage(image, posX, posY, this);
		} else {
			g.setColor(new Color((int) (Math.random() * 255),
					(int) (Math.random() * 255),
					(int) (Math.random() * 255)));
			g.fillRect(posX, posY, tailleX, tailleY);
		}
	}

	/**
	 * Tue le boss si on le touche sur la tete
	 * Sinon, le boss retire une vie au joueur
	 * sauf si le joueur n'a plus qu'une vie, il meurt et
	 * la partie se termine.
	 */
	public void effetCollision(int c) {
		if (c == 3) {
			dead = true;
		} else if (!dead) {
			if (!Jeu.joueur.invincible && Jeu.joueur.pointDeVie > 0) {
				Jeu.joueur.pointDeVie -= 1;
				Jeu.joueur.invincible = true;
			} else if (!Jeu.joueur.invincible
					&& Jeu.joueur.pointDeVie == 0) {
				Jeu.endGame();
			}
		}
	}

	/**
	 * Appelee toute les 5s par le timer du boss
	 * Cree un ennemi juste devant le boss si celui-ci n'est pas mort
	 * Sinon, retire le boss pour eviter une erreur de pointeur
	 * pour le timer
	 */
	public void actionPerformed(ActionEvent e) {
		if (!dead) {
			Jeu.listeObstacles.add(new Basic(initPosX + 151, initPosY));
		} else {
			Jeu.remove(this); // je le mets ici car on a besoin d'un delay
		}
	}
}
