import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Jeu implements ActionListener {
	
	public static ArrayList<Obstacles>listeObstacles = new ArrayList<Obstacles>();
    public static ArrayList<Projectile>listeProj = new ArrayList<Projectile>();
	public static Player joueur;
	public static Window f;
	public static int currentLevel;
	private int drawDelay = 0;
	private static Timer mt; 
	public static long startTime = 0;
	
	/** 
	 * Moteur/colonne vertebrale de la partie fonctionnement du jeu 
     * Cree la fenetre graphique du jeu 
     * Cree le timer du jeu et le fait ecouter
     * Met en place le premier niveau
	 */
	public Jeu() {
		f = new Window("game", listeObstacles, listeProj);
		currentLevel = 1;
		mt = new Timer(1, this);
		setLevel(currentLevel);
		
		startTime = System.currentTimeMillis();
		
	}
	
	/** 
     * Appelee par le timer du jeu
     * Compte le temps entre 2 images
	 * Appelle la boucle du joueur
     * avec la liste d'obstacle du niveau actuel
     * Fait bouger tous les ennemis (basic)
     * Fait bouger tous les projectiles
     * Refresh l'image tous les 16 ms (1000/16)=63 fps
	 */
	public void actionPerformed(ActionEvent e) {
		
			drawDelay++;
			
			joueur.move(listeObstacles);
			
			/*if(joueur.speed!=0 && joueur.isCollisionBas(listeObstacles)) 
				f.soundEffects[3].jouerEnBoucle();
			else
				f.soundEffects[3].arreter();*/
			
			for(int i = 0; i < listeObstacles.size(); i++) {
				if (listeObstacles.get(i) instanceof Basic) {
					((Basic)listeObstacles.get(i)).move(listeObstacles);
				}
			}
			
			for(int i = 0; i < listeProj.size(); i++) {
				listeProj.get(i).move();
				if (listeProj.get(i).collision(listeObstacles)) {
					listeProj.remove(i);
				}
			}
			
			if (drawDelay%16==0) {	// 63 fps
				f.refresh();
				drawDelay=0;
			}
	}
    
    /** 
	 * Methode appelee quand le joueur a gagne
     * Stoppe la musique et le timer
	 * Affiche la fenetre de victoire
	 */
    public static void win() {
        mt.stop();
        f.soundEffects[0].arreter();
		f.setVisible(false);
        new Win().setVisible(true);
    }
	
	/** 
	 * Methode appelee quand le joueur a perdu
     * Stoppe la musique et le timer
	 * Affiche la fenetre de game over
	 */
	public static void endGame() {
        mt.stop();
        f.soundEffects[0].arreter();
        f.soundEffects[3].arreter();
		f.setVisible(false);
		new GameOver().setVisible(true);
	}
	
	/** 
	 * Retire un obstacle de la liste
	 */
	public static void remove(Obstacles obj) {
		listeObstacles.remove(obj);
	}
    
    
    public static int getHighScore() {
		
		String ligne= "";
		int result=0;
		try{
			BufferedReader in = new BufferedReader(new FileReader("highscore.txt"));
			while ((ligne = in.readLine()) != null){
   			  result = Integer.parseInt(ligne);
			}
			in.close();
		}
		catch(IOException exc) {
            exc.printStackTrace();
		}
		return result;
	}
		
	
	public static void setHighScore(int score){
		          
        try{
			PrintWriter out = new PrintWriter(new FileWriter("highscore.txt" ));
            out.println(score);
            out.close();
		}
		catch(IOException exc) {
            exc.printStackTrace();
		}
	}
    
    /**
     * Cache la fenetre du jeu
     * Arrete le timer du jeu
	 * Permet de passer au niveau suivant
	 */
    public static void switchLevel() {
        f.setVisible(false);
        mt.stop();
        currentLevel++;
        setLevel(currentLevel);
	}
	
	/** 
     * Lance la musique
     * Efface la liste d'obstacles au cas ou il y a déjà des elements
     * dedans (passage d'un niveau à l'autre)
     * Lance le timer du jeu
	 * Cree le niveau entré en paramètre en completant la liste
     * d'obstacles 
	 */
	public static void setLevel(int level) {
		listeObstacles.clear();
		f.setVisible(true);
		mt.start();
		
		switch(level) {
				
			case 1:
				joueur = new Player(20, 750);
				
				listeObstacles.add(new Plateforme
                (0, f.getSize().height-50, f.getSize().width, 50));
				listeObstacles.add(new Plateforme
                (0, 0, 5,f.getSize().height));
				listeObstacles.add(new Plateforme
                (f.getSize().width-5, 0, 5, f.getSize().height));
				listeObstacles.add(new Plateforme
                (0, f.getSize().height-400, f.getSize().width-500, 50));
				listeObstacles.add(new Plateforme
                (200, f.getSize().height-700, f.getSize().width-1020, 50));
				listeObstacles.add(new Plateforme
                (f.getSize().width-750, f.getSize().height-750, 750, 50));
				listeObstacles.add(new Plateforme
                (f.getSize().width-500, f.getSize().height-200, 200, 200));
				listeObstacles.add(new Plateforme
                (f.getSize().width-300, f.getSize().height-400, 400, 400));
				listeObstacles.add(new Plateforme
                (0, f.getSize().height-550, 100, 150));
				listeObstacles.add(new Plateforme
                (f.getSize().width-750, f.getSize().height-760, 10, 10));
				
				listeObstacles.add(new Basic
                (f.getSize().width-900, f.getSize().height-470));
				listeObstacles.add(new Basic
                (f.getSize().width-1200, f.getSize().height-470));
				listeObstacles.add(new Basic
                (f.getSize().width/3, f.getSize().height-120));
				listeObstacles.add(new Basic
                (f.getSize().width-725, f.getSize().height-850));
				
				listeObstacles.add(new Piege
                (f.getSize().width/2-50, f.getSize().height-75, 0));
				listeObstacles.add(new Piege
                (f.getSize().width/4, f.getSize().height-75, 0));
				listeObstacles.add(new Piege
                (f.getSize().width-950, f.getSize().height-350, 1));
				listeObstacles.add(new Piege
                (f.getSize().width-350, f.getSize().height-225, 0));
				listeObstacles.add(new Piege
                (f.getSize().width-25, f.getSize().height-450, 2)); 
				listeObstacles.add(new Piege
                (f.getSize().width-25, f.getSize().height-500, 2)); 
				listeObstacles.add(new Piege
                (f.getSize().width-25, f.getSize().height-550, 2)); 
				listeObstacles.add(new Piege
                (f.getSize().width-700, f.getSize().height-425, 0));
				listeObstacles.add(new Piege
                (f.getSize().width-1000, f.getSize().height-425, 0));
				listeObstacles.add(new Piege
                (f.getSize().width-1350, f.getSize().height-425, 0));
				listeObstacles.add(new Piege
                (f.getSize().width-1150, f.getSize().height-650, 1));
				listeObstacles.add(new Piege
                (f.getSize().width-870,	 f.getSize().height-725, 0));	
				listeObstacles.add(new Piege
                (f.getSize().width-350, f.getSize().height-775, 0));
				
				listeObstacles.add(new Sortie
                (f.getSize().width-100, f.getSize().height-847));
			
				break;
			
			case 2:
			
				joueur = new Player(50, 100);
				
				listeObstacles.add(new Plateforme
                (0, (f.getSize().height)-50, (f.getSize().width)-300, 50)); 
				listeObstacles.add(new Plateforme
                (300, 700, (f.getSize().width)-595, 50)); 
				listeObstacles.add(new Plateforme
                ((f.getSize().width)-300, 700, 300, (f.getSize().height)-700)); 
				listeObstacles.add(new Plateforme
                ((f.getSize().width)-200, 100, 50, 500)); 
				listeObstacles.add(new Plateforme
                (0, 350, (f.getSize().width)-1300, 100)); 
				listeObstacles.add(new Plateforme
                ((f.getSize().width)-1305, 400, 510, 50)); 
				listeObstacles.add(new Plateforme
                ((f.getSize().width)-800, 350, 400, 100)); 
				listeObstacles.add(new Plateforme
                ((f.getSize().width)-1450, 150, 600, 50)); 
				listeObstacles.add(new Plateforme
                ((f.getSize().width)-1250, 200, 400, 50)); 
                listeObstacles.add(new Plateforme
                ((f.getSize().width)-750, 80, 150, 20)); 
                listeObstacles.add(new Plateforme
                ((f.getSize().width)-420, 95, 100, 20)); 
                listeObstacles.add(new Plateforme
                ((f.getSize().width)-1550, 240, 100, 20)); 
                listeObstacles.add(new Plateforme
                ((f.getSize().width)-1450, 128, 10, 20));
                listeObstacles.add(new Plateforme
                ((f.getSize().width)-860, 128, 10, 20));
				
				// 5 ennemis
				listeObstacles.add(new Basic
                ((f.getSize().width)-1300, 80));
				listeObstacles.add(new Basic
                ((f.getSize().width)-1250, 330)); 
				listeObstacles.add(new Basic
                ((f.getSize().width)-1400, 630)); 
				listeObstacles.add(new Basic
                (500, (f.getSize().height)-120)); 
				listeObstacles.add(new Basic
                (1200, (f.getSize().height)-120)); 
				
				// 10 pièges + 6 sur la platefome verticale
				listeObstacles.add(new Piege(200, 325, 0)); 
				listeObstacles.add(new Piege
                ((f.getSize().width)-400, 375, 3)); 
				listeObstacles.add(new Piege(900, 450, 1)); 
				listeObstacles.add(new Piege(1100, 675, 0)); 
				listeObstacles.add(new Piege(316, 675, 0)); 
				listeObstacles.add(new Piege(0, 700, 3)); 
				listeObstacles.add(new Piege(316, 750, 1)); 
				listeObstacles.add(new Piege
                ((f.getSize().width)-900, (f.getSize().height)-75, 0));
				listeObstacles.add(new Piege
                (1100, (f.getSize().height)-75, 0)); 
				
				listeObstacles.add(new Piege
                ((f.getSize().width)-220, 150, 2));
				listeObstacles.add(new Piege
                ((f.getSize().width)-220, 300, 2));
				listeObstacles.add(new Piege
                ((f.getSize().width)-220, 450, 2));
				listeObstacles.add(new Piege
                ((f.getSize().width)-150, 150, 3));
				listeObstacles.add(new Piege
                ((f.getSize().width)-150, 300, 3));
				listeObstacles.add(new Piege
                ((f.getSize().width)-150, 450, 3));
				
				listeObstacles.add(new Sortie
                (1500, (f.getSize().height)-147));
				
				break;
			
			case 3:
    
				joueur = new Player
                ((f.getSize().width)-150, (f.getSize().height)-125);
			
				listeObstacles.add(new Plateforme
                (-5, (f.getSize().height)-20, f.getSize().width+10, 20));
				listeObstacles.add(new Plateforme
                (200, (f.getSize().height)-225, (f.getSize().width)-195, 50));
				listeObstacles.add(new Plateforme
                (-5, (f.getSize().height)-420, (f.getSize().width)-200, 50));
				listeObstacles.add(new Plateforme
                (200, (f.getSize().height)-615, (f.getSize().width)-195, 50));
				listeObstacles.add(new Plateforme
                (-5, (f.getSize().height)-810, (f.getSize().width)-200, 50));
				listeObstacles.add(new Plateforme
                (140, (f.getSize().height)-920, 200, 111));
                
                listeObstacles.add(new Plateforme
                (-5, (f.getSize().height)-70, 85,50 ));
                listeObstacles.add(new Plateforme
                ((f.getSize().width)-80, (f.getSize().height)-275, 85,50 ));
                listeObstacles.add(new Plateforme
                (-5, (f.getSize().height)-470, 85,50 ));
                listeObstacles.add(new Plateforme
                ((f.getSize().width)-80, (f.getSize().height)-665, 85,50 ));
				
				listeObstacles.add(new Boss
                (140, (f.getSize().height)-1047));
				
				listeObstacles.add(new Basic
                (400, (f.getSize().height)-84));
				listeObstacles.add(new Basic
                (250, (f.getSize().height)-289));
				listeObstacles.add(new Basic
                (112, (f.getSize().height)-484));
				listeObstacles.add(new Basic
                (250, (f.getSize().height)-679));
				listeObstacles.add(new Basic
                (360, (f.getSize().height)-874));
				
				listeObstacles.add(new Piege
                (115, (f.getSize().height)-370, 1));
				listeObstacles.add(new Piege
                (115, (f.getSize().height)-760, 1));
				listeObstacles.add(new Piege
                ((f.getSize().width)-155, (f.getSize().height)-565, 1));
				
				
				listeObstacles.add(new Sortie
                (25, (f.getSize().height)-906));
				
				break;					
		}

	}
}
