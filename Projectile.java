import java.awt.*;
import java.util.ArrayList;

public class Projectile {
	
	private int tailleX;
	private int tailleY;
	private double posX;
	private double posY;
	private double speedX;
	private double speedY;
	private double speed;
	
	/** 
	 * Determine la taille et la position des projectiles
	 * Definit la vitesse appliquee aux projectiles
	 */
	public Projectile(int cX, int cY, double pX, double pY) { // expliquer ce que c'est cX et cY
		tailleX = 15;
		tailleY = 15;
		
		posX = pX;
		posY = pY;
		
		double distY = cY-pY;
		double distX = cX-pX;
		double dist = Math.sqrt(Math.pow(distX,2) + Math.pow(distY,2));
		
		speed = 10;
		speedX = (speed * distX)/dist;
		speedY = (speed * distY)/dist;	
	}
	
	/** 
	 * Dessine le projectile
	 */
	public void dessin(Graphics g) {
		g.setColor(new Color(255, 140, 0));
		g.fillOval((int)posX, (int)posY, tailleX, tailleY);
	}
	
	/** 
	 * Definit les mouvements des projectiles
	 */
	public void move() {
		posX += speedX;
		posY += speedY;
	}
	
	/** 
	 * Recher les collisions du projectile avec les obstacles
     * "Tue" l'obstacle si c'est un ennemi
	 */
	public boolean collision(ArrayList<Obstacles> l) {
		
		boolean hit = false;
		int objectHit = -1;
		
		for(int i = 0; i < l.size(); i++) {
			
			if (!hit) {
				// collision bas
				if (posX < l.get(i).posX && posX+tailleX > l.get(i).posX
                    && posX+tailleX < l.get(i).posX+l.get(i).tailleX 
                    && posY+tailleY >= l.get(i).posY 
                    && posY<l.get(i).posY) {
					hit = true;
					objectHit = i;
				} else if (posX < l.get(i).posX 
                           && posX+tailleX > l.get(i).posX+l.get(i).tailleX 
                           && posY+tailleY >= l.get(i).posY 
                           && posY < l.get(i).posY) {
					hit = true;
					objectHit = i;
				} else if (posX > l.get(i).posX 
                           && posX+tailleX < l.get(i).posX+l.get(i).tailleX 
                           && posY+tailleY >= l.get(i).posY 
                           && posY < l.get(i).posY) {
					hit = true;
					objectHit = i;
				} else if (posX > l.get(i).posX 
                           && posX < l.get(i).posX+l.get(i).tailleX 
                           && posX+tailleX > l.get(i).posX+l.get(i).tailleX 
                           && posY+tailleY >= l.get(i).posY 
                           && posY < l.get(i).posY) {
					hit = true;
					objectHit = i;
				}
				
				// colision haut
				if (posX < l.get(i).posX 
                    && posX+tailleX > l.get(i).posX 
                    && posX+tailleX < l.get(i).posX+l.get(i).tailleX 
                    && posY <= l.get(i).posY+l.get(i).tailleY 
                    && posY+tailleY > l.get(i).posY+l.get(i).tailleY) {
					hit = true;
					objectHit = i;
				} else if (posX < l.get(i).posX 
                           && posX+tailleX > l.get(i).posX+l.get(i).tailleX 
                           && posY <= l.get(i).posY+l.get(i).tailleY 
                           && posY+tailleY > l.get(i).posY+l.get(i).tailleY) {
					hit = true;
					objectHit = i;
				} else if (posX > l.get(i).posX 
                           && posX+tailleX < l.get(i).posX+l.get(i).tailleX 
                           && posY <= l.get(i).posY+l.get(i).tailleY 
                           && posY+tailleY > l.get(i).posY+l.get(i).tailleY) {
					hit = true;
					objectHit = i;
				} else if (posX > l.get(i).posX 
                           && posX < l.get(i).posX+l.get(i).tailleX 
                           && posX+tailleX > l.get(i).posX+l.get(i).tailleX 
                           && posY <= l.get(i).posY+l.get(i).tailleY 
                           && posY+tailleY > l.get(i).posY+l.get(i).tailleY) {
					hit = true;
					objectHit = i;
				}				
				
				// collision gauche
				if (posY > l.get(i).posY+5 
                    && posY < (l.get(i).posY+l.get(i).tailleY)-5 
                    && posY+tailleY > (l.get(i).posY+l.get(i).tailleY)-5 
                    && posX <= l.get(i).posX+l.get(i).tailleX 
                    && posX+tailleX > l.get(i).posX+l.get(i).tailleX) {
					hit = true;
					objectHit = i;
				} else if (posY < l.get(i).posY+5 
                           && posY+tailleY > l.get(i).posY+5 
                           && posY+tailleY < (l.get(i).posY+l.get(i).tailleY)-5 
                           && posX <= l.get(i).posX+l.get(i).tailleX 
                           && posX+tailleX > l.get(i).posX+l.get(i).tailleX) {
					hit = true;
					objectHit = i;
				} else if (posY < l.get(i).posY+5 
                           && posY+tailleY > (l.get(i).posY+l.get(i).tailleY)-5 
                           && posX <= l.get(i).posX+l.get(i).tailleX 
                           && posX+tailleX > l.get(i).posX+l.get(i).tailleX) {
					hit = true;
					objectHit = i;
				} else if (posY > l.get(i).posY+5 
                           && posY+tailleY < (l.get(i).posY+l.get(i).tailleY)-5 
                           && posX <= l.get(i).posX+l.get(i).tailleX 
                           && posX+tailleX > l.get(i).posX+l.get(i).tailleX) {
					hit = true;
					objectHit = i;
				}				
				
				// collision droit
				if (posY > l.get(i).posY+5 
                    && posY < (l.get(i).posY+l.get(i).tailleY)-5 
                    && posY+tailleY > (l.get(i).posY+l.get(i).tailleY)-5 
                    && posX+tailleX >= l.get(i).posX 
                    && posX < l.get(i).posX) {
					hit = true;
					objectHit = i;
				} else if (posY < l.get(i).posY+5 
                           && posY+tailleY > l.get(i).posY+5 
                           && posY+tailleY < (l.get(i).posY+l.get(i).tailleY)-5 
                           && posX+tailleX >= l.get(i).posX 
                           && posX < l.get(i).posX) {
					hit = true;
					objectHit = i;
				} else if (posY < l.get(i).posY+5 
                           && posY+tailleY > (l.get(i).posY+l.get(i).tailleY)-5 
                           && posX+tailleX >= l.get(i).posX 
                           && posX < l.get(i).posX) {
					hit = true;
					objectHit = i;
				} else if (posY > l.get(i).posY+5 
                           && posY+tailleY < (l.get(i).posY+l.get(i).tailleY)-5 
                           && posX+tailleX >= l.get(i).posX 
                           && posX < l.get(i).posX) {
					hit = true;
					objectHit = i;
				}	
			}	
		}
		
		if (hit) {
			if (l.get(objectHit) instanceof Basic) {
				((Basic)l.get(objectHit)).die();
			}
			return true;
		} else {
			return false;
		}	
	}
}
