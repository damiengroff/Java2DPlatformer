import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Player extends JFrame {

	public int speed = 0;
	public int pointDeVie = 3;
	public int tempsInvincibilite;
	public boolean invincible;
	public boolean jump;
	public int munitions;
	private int posX;
	private int posY;
	private int tailleX;
	private int tailleY;
	private int fallTimer = 0;
	private int invincibiliteTimer = 0;

	// stocke toutes les images de l'animation
	private BufferedImage[] images = new BufferedImage[23];
	private int currentImage = 0;
	private int animationTimer = 0;

	/**
	 * Determine la taille et la position du joueur
	 * Definit le temps d'invincibilite du joueur
	 * Charge l'image source correspondant au joueur
	 */
	public Player(int x, int y) {
		posX = x;
		posY = y;
		tailleX = 40;
		tailleY = 70;
		munitions = 10;
		tempsInvincibilite = 100;
		invincible = false;
		jump = false;

		try {
			for (int i = 0; i < images.length; i++) {
				String access = "resources/images/player/p" + i + ".png";
				images[i] = ImageIO.read(new File(access));
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Retourne le centre du joueur selon x (depart des projectiles)
	 */
	public int getX() {
		return posX + (int) (tailleX / 2);
	}

	/**
	 * Retourne le centre du joueur selon y (depart des projectiles)
	 */
	public int getY() {
		return posY + (int) (tailleY / 2);
	}

	/**
	 * Dessine le joueur en fonction de s'il est invincible ou non.
	 */
	public void paint(Graphics g) {

		animationTimer++;
		if (animationTimer % 3 == 0) {
			animate();
			animationTimer = 0;
		}

		if (!invincible)
			// g.drawImage(images[animate()], posX, posY, this);
			g.drawImage(images[currentImage], posX, posY, this);
		else
			// g.drawImage(images[animate()+12], posX, posY, this);
			g.drawImage(images[currentImage + 12], posX, posY, this);

	}

	/**
	 * Permet d'animer l'image du joueur en fonction de son deplacement
	 * (s'il est en mouvement ou statique et de quel cote il se deplace)
	 * Animation de déplacement sur 4 images.
	 */
	private int animate() {
		if (speed < 0 && isCollisionBas(Jeu.listeObstacles)) { // à gauche
			if (currentImage < 9 && currentImage >= 5) {
				currentImage++;
				return currentImage;
			} else {
				currentImage = 6;
				return currentImage;
			}
		}

		if (speed > 0 && isCollisionBas(Jeu.listeObstacles)) { // à droite
			if (currentImage < 4 && currentImage >= 0) {
				currentImage++;
				return currentImage;
			} else {
				currentImage = 1;
				return currentImage;
			}
		}

		// saut à droite
		if (speed < 0 && !isCollisionBas(Jeu.listeObstacles)) {
			currentImage = 9;
			return currentImage;
		}

		// saut à gauche
		if (speed > 0 && !isCollisionBas(Jeu.listeObstacles)) {
			currentImage = 4;
			return currentImage;
		}

		// idle alors qu'on allait à gauche
		if (speed == 0 && currentImage > 5 && currentImage < 10) {
			currentImage = 5;
			return 5;
		}
		// idle alors qu'on allait à droite
		if (speed == 0 && currentImage < 5 && currentImage > 0) {
			currentImage = 0;
			return 0;
		}

		return 0;
	}

	/**
	 * Déplacements du joueur. Application de la gravité si on ne touche
	 * aucun sol. La saut est une force plus intense orientée vers le
	 * haut. Déplacements latéraux à la vitesse speed
	 */
	public void move(ArrayList<Obstacles> l) {
		fallTimer++;

		if (isCollisionBas(l) && !jump) {
			fallTimer = 0;
		}

		if (invincible) {
			invincibiliteTimer++;
			if (invincibiliteTimer % tempsInvincibilite == 0) {
				invincibiliteTimer = 0;
				invincible = false;
			}
		}

		// gravite
		if (!isCollisionBas(l) && !jump) {
			// posY += customCast(0.5*10*Math.pow(0.001*fallTimer, 2)+1.0125);
			// posY += 1/2 g * t^2
			// 1 metre == 70 px -> g = 7e-5 px.ms-2)
			// posY += customCast(0.5 * 70 * Math.pow(fallTimer*10e-3,2));
			posY += 7;
		}
		if (jump && fallTimer < 30 && !isCollisionHaut(l)) {
			// posY -= customCast(0.5*40*Math.pow(fallTimer*10e-3, 2));
			posY -= 7;

		} else if (jump && fallTimer > 30) {
			jump = false;
		}

		// à gauche sans contact
		if (!isCollisionGauche(l) && speed < 0 && posX >= -5)
			posX += speed - 5;

		// à droite sans contact
		if (!isCollisionDroite(l) && speed > 0 && posX + tailleX <= Jeu.f.getSize().width + 5)
			posX += speed + 5;

	}

	/**
	 * Détection des collision vers le bas. Il y a 4 cas de collisions.
	 * - "Le joueur dépasse à gauche de la plateforme (ou un autre objet)"
	 * - "Le joueur dépasse à droite"
	 * - "Le joueur dépasse des deux côtés"
	 * - "Le joueur ne dépasse pas."
	 */
	public boolean isCollisionBas(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX &&
					posX + tailleX < l.get(i).posX + l.get(i).tailleX &&
					posY + tailleY >= l.get(i).posY && posY + tailleY <= l.get(i).posY + 10
					&& posY < l.get(i).posY) {

				l.get(i).effetCollision(3);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY + tailleY >= l.get(i).posY && posY < l.get(i).posY
					&& posY + tailleY <= l.get(i).posY + 10) {

				l.get(i).effetCollision(3);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posX > l.get(i).posX && posX + tailleX < l.get(i).posX + l.get(i).tailleX
					&& posY + tailleY >= l.get(i).posY && posY < l.get(i).posY
					&& posY + tailleY <= l.get(i).posY + 10) {

				l.get(i).effetCollision(3);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posX > l.get(i).posX && posX < l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY + tailleY >= l.get(i).posY && posY < l.get(i).posY
					&& posY + tailleY <= l.get(i).posY + 10) {

				l.get(i).effetCollision(3);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Détection des collision vers le haut. Il y a 4 cas de collisions.
	 * - "Le joueur dépasse à gauche de la plateforme (ou un autre objet)"
	 * - "Le joueur dépasse à droite"
	 * - "Le joueur dépasse des deux côtés"
	 * - "Le joueur ne dépasse pas."
	 */
	public boolean isCollisionHaut(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX
					&& posX + tailleX < l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY
					&& posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				l.get(i).effetCollision(2);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posX < l.get(i).posX && posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY
					&& posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				l.get(i).effetCollision(2);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posX > l.get(i).posX && posX + tailleX < l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY
					&& posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				l.get(i).effetCollision(2);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posX > l.get(i).posX && posX < l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX
					&& posY <= l.get(i).posY + l.get(i).tailleY
					&& posY + tailleY > l.get(i).posY + l.get(i).tailleY) {

				l.get(i).effetCollision(2);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			}

		}
		return false;
	}

	/**
	 * Détection des collision vers la gauche. Il y a 4 cas de collisions.
	 * - "Le joueur dépasse en haut de la plateforme (ou un autre objet)"
	 * - "Le joueur dépasse en bas droite"
	 * - "Le joueur dépasse des deux côtés"
	 * - "Le joueur ne dépasse pas."
	 */
	public boolean isCollisionGauche(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			// marges de 5 pour eviter de faire demi tour au millieu d'une plateforme
			// (parfois l'objet peut s'enfoncer de quelques pixels dans la plateforme
			// avant que la collision soit détectée)
			if (posY > l.get(i).posY + 5 && posY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX <= l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				l.get(i).effetCollision(0);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posY < l.get(i).posY + 5 && posY + tailleY > l.get(i).posY + 5
					&& posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX <= l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				l.get(i).effetCollision(0);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posY < l.get(i).posY + 5 && posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX <= l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				l.get(i).effetCollision(0);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posY > l.get(i).posY + 5 && posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX <= l.get(i).posX + l.get(i).tailleX
					&& posX + tailleX > l.get(i).posX + l.get(i).tailleX) {

				l.get(i).effetCollision(0);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			}

		}
		return false;
	}

	/**
	 * Détection des collision vers la droite. Il y a 4 cas de collisions.
	 * - "Le joueur dépasse en haut de la plateforme (ou un autre objet)"
	 * - "Le joueur dépasse en bas droite"
	 * - "Le joueur dépasse des deux côtés"
	 * - "Le joueur ne dépasse pas."
	 */
	public boolean isCollisionDroite(ArrayList<Obstacles> l) {

		for (int i = 0; i < l.size(); i++) {

			if (posY > l.get(i).posY + 5 && posY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX && posX < l.get(i).posX) {

				l.get(i).effetCollision(1);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posY < l.get(i).posY + 5 && posY + tailleY > l.get(i).posY + 5
					&& posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX && posX < l.get(i).posX) {

				l.get(i).effetCollision(1);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posY < l.get(i).posY + 5 && posY + tailleY > (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX && posX < l.get(i).posX) {

				l.get(i).effetCollision(1);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			} else if (posY > l.get(i).posY + 5 && posY + tailleY < (l.get(i).posY + l.get(i).tailleY) - 5
					&& posX + tailleX >= l.get(i).posX && posX < l.get(i).posX) {

				l.get(i).effetCollision(1);
				if (!(l.get(i) instanceof Basic)) {
					return true;
				}

			}

		}
		return false;
	}

	/**
	 * N'arrondi pas systèmatique à l'entier inférieur mais aussi à l'entier
	 * supérieur si 'data'>0.5.
	 * Permet de rendre l'impression de chute par gravité plus fluide.
	 * (sinon il ne tombe dès le début lorsque fallTimer est petit)
	 */
	/*
	 * private int customCast(double data) {
	 * if (data >= 0.5) {
	 * return (int)(data+1);
	 * } else {
	 * return (int)data;
	 * }
	 * }
	 */
}
