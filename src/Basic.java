import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Basic extends Obstacles {

	public boolean dead = false; // statut de l'ennemi
	private int speed = 1;
	private int fallTimer = 0; // duree de chute (gravite)
	private int respawnDelay = 0; // duree avant reaparition
	private int turnDelay = 0;
	private boolean turned = false;

	// stocke toutes les images de l'animation
	private BufferedImage[] images = new BufferedImage[4];
	private int imageDrawn; // retiens l'image qui s'affiche
	private Audio[] soundEffects = new Audio[2];

	/**
	 * Determine la taille et la position de l'ennemi basic
	 * Charge l'image source correspondant a l'ennemi basic
	 */
	public Basic(int posX, int posY) {
		super(posX, posY, 34, 62);
		// initPosX = posX;
		// initPosY = posY;

		// on charge les images
		try {
			for (int i = 0; i < images.length; i++) {
				String access = "resources/images/ennemi/ennemi" + (i + 1) + ".png";
				images[i] = ImageIO.read(new File(access));
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		}

		int n = soundEffects.length;
		URL[] url = new URL[n];
		url[0] = this.getClass().getClassLoader().getResource("resources/audio/hurt.wav");
		url[1] = this.getClass().getClassLoader().getResource("resources/audio/e_die.wav");
		for (int i = 0; i < n; i++) {
			soundEffects[i] = new Audio(url[i]);
		}
	}

	/**
	 * Dessine l'image correspondant a l'ennemi basic
	 */
	public void dessin(Graphics g) {
		if (!dead) {
			g.drawImage(images[animate()], posX, posY, this);
		}
	}

	/**
	 * Animations de l'ennemi (2 images pour le deplacement)
	 * suivant s'il va a gauche, a droite ou s'il est en l'air
	 */
	public int animate() {

		// si on marche vers la droite, image 2 et 3
		if (speed > 0 && isCollisionBas(Jeu.listeObstacles)) {
			if (imageDrawn == 2) {
				imageDrawn = 3;
				return 3;
			} else if (imageDrawn == 3) {
				imageDrawn = 2;
				return 2;
			} else {
				imageDrawn = 2;
				return 2;
			}
		}

		// si on marche vers la gauche, image 0 et 1
		if (speed < 0 && isCollisionBas(Jeu.listeObstacles)) {
			if (imageDrawn == 0) {
				imageDrawn = 1;
				return 1;
			} else if (imageDrawn == 1) {
				imageDrawn = 0;
				return 0;
			} else {
				imageDrawn = 0;
				return 0;
			}
		}

		// si on est en l'air et qu'on va a droite
		if (speed > 0 && !isCollisionBas(Jeu.listeObstacles)) {
			imageDrawn = 2;
			return 2;
		}

		// si on est en l'air et qu'on va a gauche
		if (speed > 0 && !isCollisionBas(Jeu.listeObstacles)) {
			imageDrawn = 0;
			return 0;
		}
		return 0;
	}

	/**
	 * Deplacements de l'ennemi. Ne s'effectuent que s'il est vivant. Il
	 * se deplace à la vitesse 'speed' si il est au sol, sinon il chute.
	 * La valeur de 'speed' s'inverse si il rencontre un objet a gauche
	 * ou a droite.
	 */
	public void move(ArrayList<Obstacles> l) {
		if (!dead) {
			if (isCollisionBas(l)) { // avance si il est à terre
				posX += speed;
				fallTimer = 0;
			} else {
				fallTimer++;
				if (fallTimer > 10) {
					// gravite
					posY += customCast(0.5 * 10 * Math.pow(0.001 * fallTimer, 2) + 1.0125);
				} else {
					/*
					 * Eviter que l'ennemi traverse une plateforme à
					 * l'intersection entre deux cas de collision
					 */
					posX += speed;
				}
			}

			/*
			 * Ajout d'un delay pour eviter qu'il se "retourne à
			 * l'infini" et soit bloqué au contact d'un objet
			 */
			if (!turned) {
				if (isCollisionGauche(l) || posX <= 0) {
					speed = -speed;
					turned = true;
					turnDelay++;
				} else if (isCollisionDroite(l)
						|| (posX + tailleX) >= Jeu.f.getSize().width) {
					speed = -speed;
					turned = true;
					turnDelay++;
				}
			} else {
				turnDelay++;
			}

			if (turnDelay % 100 == 0) {
				turnDelay = 0;
				turned = false;
			}

		} else {
			respawnDelay++;
			if (respawnDelay % 10 == 0) {
				Jeu.remove(this); // respawndelay pour éviter pointeur nul
				/*
				 * dead = false;
				 * posX= initPosX; // plus de respawn dans la version finale du jeu
				 * posY= initPosY;
				 * respawnDelay = 0;
				 */
			}
		}
	}

	/**
	 * Appele lors de la collision avec le joueur. Le parametre 'c'
	 * donne la direction de collision. En fonction de celle-ci,
	 * l'ennemi peut mourir ou faire perdre une vie au joueur.
	 */
	public void effetCollision(int c) {
		if (c == 3) {
			die();
		} else if (!dead) {
			if (!Jeu.joueur.invincible && Jeu.joueur.pointDeVie > 0) {
				soundEffects[0].jouer();
				Jeu.joueur.pointDeVie -= 1;
				Jeu.joueur.invincible = true;
			} else if (Jeu.joueur.pointDeVie == 0) {
				Jeu.endGame();
			}
		}
	}

	/**
	 * Tue l'ennemi (appele par Projectile)
	 */
	public void die() {
		soundEffects[1].jouer();
		dead = true;
	}

	/**
	 * N'arrondi pas systematiquement à l'entier inférieur, mais aussi
	 * a l'entier superieur si 'data'>0.5.
	 * Permet de rendre l'impression de chute par gravite plus fluide.
	 * (sinon il tombe des le debut lorsque fallTimer est petit)
	 */
	private int customCast(double data) {
		if (data >= 0.5) {
			return (int) (data + 1);
		} else {
			return (int) data;
		}
	}

	/**
	 * Detection des collision vers le bas. Il y a 4 cas de collisions.
	 * - "L'ennemi depasse à gauche de la plateforme"
	 * - "L'ennemi depasse à droite"
	 * - "L'ennemi depasse des deux cotes"
	 * - "L'ennemi ne depasse pas."
	 */
	public boolean isCollisionBas(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX
					&& posX + tailleX < l.get(i).posX + l.get(i).tailleX
					&& posY + tailleY >= l.get(i).posY && posY < l.get(i).posY) {
				return true;
			}

			if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY + tailleY >= l.get(i).posY && posY < l.get(i).posY) {
				return true;
			}

			if (posX > l.get(i).posX && posX + tailleX < l.get(i).posX + l.get(i).tailleX
					&& posY + tailleY >= l.get(i).posY && posY < l.get(i).posY) {
				return true;
			}

			if (posX > l.get(i).posX && posX < l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY + tailleY >= l.get(i).posY && posY < l.get(i).posY) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Detection des collision vers le haut. Il y a 4 cas de collisions.
	 * - "L'ennemi depasse à gauche de la plateforme"
	 * - "L'ennemi depasse à droite"
	 * - "L'ennemi depasse des deux cotes"
	 * - "L'ennemi ne depasse pas."
	 */
	public boolean isCollisionHaut(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX
					&& posX + tailleX < l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY &&
					posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				return true;
			}

			if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY
					&& posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				return true;
			}

			if (posX > l.get(i).posX && posX + tailleX < l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY
					&& posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				return true;
			}

			if (posX > l.get(i).posX && posX < l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY
					&& posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				return true;
			}

		}
		return false;
	}

	/**
	 * Detection des collision vers la gauche. Il y a 4 cas de collisions.
	 * - "L'ennemi depasse en haut de la plateforme"
	 * - "L'ennemi depasse en bas"
	 * - "L'ennemi depasse des deux cotes"
	 * - "L'ennemi ne depasse pas."
	 */
	public boolean isCollisionGauche(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			/*
			 * marges de 5 pour eviter de faire demi tour au millieu
			 * d'une plateforme (parfois l'objet peut s'enfoncer de
			 * quelques pixels dans la plateforme avant que la collision
			 * soit détectee)
			 */
			if (posY > l.get(i).posY + 5 && posY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5 &&
					posX <= l.get(i).posX + l.get(i).tailleX &&
					posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				return true;
			}

			if (posY < l.get(i).posY && posY + tailleY > l.get(i).posY + 5
					&& posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX <= l.get(i).posX + l.get(i).tailleX &&
					posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				return true;
			}

			if (posY < l.get(i).posY + 5 && posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX <= l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				return true;
			}

			if (posY > l.get(i).posY + 5 && posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX <= l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				return true;
			}

		}
		return false;
	}

	/**
	 * Detection des collision vers la doite. Il y a 4 cas de collisions.
	 * - "L'ennemi depasse en haut de la plateforme"
	 * - "L'ennemi depasse en bas"
	 * - "L'ennemi depasse des deux cotes"
	 * - "L'ennemi ne depasse pas."
	 */
	public boolean isCollisionDroite(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			if (posY > l.get(i).posY + 5 && posY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX && posX < l.get(i).posX) {

				return true;
			}
			if (posY < l.get(i).posY + 5 && posY + tailleY > l.get(i).posY
					&& posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX && posX < l.get(i).posX) {

				return true;
			}
			if (posY < l.get(i).posY + 5 && posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX &&
					posX < l.get(i).posX) {

				return true;
			}
			if (posY > l.get(i).posY + 5 && posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX &&
					posX < l.get(i).posX) {

				return true;
			}
		}
		return false;
	}
}
