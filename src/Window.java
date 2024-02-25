import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Window extends JFrame implements KeyListener, MouseListener {

	public Audio[] soundEffects = new Audio[4];
	private int[] keys = { 0, 0 };
	private ArrayList<Obstacles> objectsToDraw;
	private ArrayList<Projectile> projectileToDraw;
	private BufferedImage[] GUI = new BufferedImage[2];
	private BufferedImage background;
	private JPanel p; // panel sur lequel on dessine le niveau

	/**
	 * Construit la fenetre de jeu
	 */
	public Window(String nom, ArrayList<Obstacles> obs, ArrayList<Projectile> prj) {
		super(nom);
		setLocation(0, 0);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);

		try {
			background = ImageIO.read(new File("resources/images/background.jpg"));
			GUI[0] = ImageIO.read(new File("resources/images/Coeur.png"));
			GUI[1] = ImageIO.read(new File("resources/images/fire.png"));
		} catch (IOException exc) {
			exc.printStackTrace();
		}

		objectsToDraw = obs;
		projectileToDraw = prj;

		int n = soundEffects.length;
		URL[] url = new URL[n];
		url[0] = this.getClass().getClassLoader().getResource("resources/audio/music.wav");
		url[1] = this.getClass().getClassLoader().getResource("resources/audio/fireball.wav");
		url[2] = this.getClass().getClassLoader().getResource("resources/audio/jump.wav");
		url[3] = this.getClass().getClassLoader().getResource("resources/audio/step.wav");
		for (int i = 0; i < n; i++) {
			soundEffects[i] = new Audio(url[i]);
		}
		soundEffects[0].jouer();

		/**
		 * On utilise un JPanel et paintComponent plutôt que paint pour
		 * éviter le flickering
		 * "paintComponent is low enough in the call chain to guaranteed
		 * to be double buffered"
		 * https://stackoverflow.com/questions/17965850/stop-flickering-in-swing-when-i-repaint-too-much
		 * 
		 * initialement j'avais fais un double buffering en dessinant sur
		 * une BufferedImage dans le paint puis en rajoutant g.drawImage(...)
		 * problème : g.drawImage entraine un rescale et ralenti donc fortement le
		 * programme
		 * http://javacodespot.blogspot.com/2010/08/java-flickering-problem.html
		 */
		p = new JPanel() {

			public void paintComponent(Graphics g) {

				super.paintComponent(g);

				g.drawImage(background, 0, 0, this); // affichage du fond

				Graphics2D g2d = (Graphics2D) g;

				/*
				 * g2d.setColor(Color.white);
				 * g2d.fillRect(0,0,getSize().width,getSize().height);
				 */

				Jeu.joueur.paint(g2d);

				for (int i = 0; i < objectsToDraw.size(); i++) {
					objectsToDraw.get(i).dessin(g2d);
				}

				for (int i = 0; i < projectileToDraw.size(); i++) {
					projectileToDraw.get(i).dessin(g2d);
				}

				for (int i = 0; i < Jeu.joueur.pointDeVie; i++) {
					g.drawImage(GUI[0], 20 + i * 35, 20, this);
				}

				g.drawImage(GUI[1], 23, 50, this);
				Font f = new Font("Arial", Font.BOLD, 25);
				g.setFont(f);
				g.setColor(Color.white);
				String str = Integer.toString(Jeu.joueur.munitions);
				if (Jeu.joueur.munitions > 0)
					g.drawString(str, 50, 85);
				else
					g.drawString("X", 50, 85);

				displayScore(g);

			}

			private void displayScore(Graphics g) {

				long currentTime = System.currentTimeMillis() - Jeu.startTime;

				int minutes = (int) (currentTime / 60000);
				int seconds = (int) (currentTime / 1000) % 60;

				String time = "Temps : " + minutes + ":" + seconds;

				Font f = new Font("Arial", Font.BOLD, 25);

				g.setFont(f);
				g.setColor(Color.white);
				g.drawString(time, 20, 120);
			}

		};

		p.setVisible(true);
		// se superpose à la jframe
		p.setBounds(0, 0, this.getSize().width, this.getSize().height);

		add(p); // ajout du panel
	}

	// classe imbriquée plutôt qu'anonyme pour le jpanel?

	/*
	 * class DrawingPanel extends JPanel {
	 * 
	 * DrawingPanel() {}
	 * 
	 * public void paintComponents(Graphics g) {
	 * 
	 * super.paintComponent(g);
	 * 
	 * Graphics2D g2d = (Graphics2D)g;
	 * 
	 * g2d.setColor(Color.white);
	 * g2d.fillRect(0,0,getSize().width,getSize().height);
	 * 
	 * Jeu.joueur.paint(g2d);
	 * 
	 * for(int i =0; i<objectsToDraw.size(); i++) {
	 * objectsToDraw.get(i).dessin(g2d);
	 * }
	 * 
	 * for(int i =0; i<projectileToDraw.size(); i++) {
	 * projectileToDraw.get(i).dessin(g2d);
	 * }
	 * }
	 * }
	 */

	/**
	 * La détection des entrée clavier met à jour la vitesse du joueur
	 * On utlise keyPressed et keyReleased pour obtenir des déplacements
	 * fluides, voir la documentation ci-dessous.
	 * 
	 * https://books.google.fr/books?id=dOz-UK8Fl_UC&pg=PA308&lpg=PA308
	 * &dq=java+keypressed+timing&source=bl&ots=ahojXn1GhQ&sig=ACfU3U15
	 * buBkp1KuCg1exrLKVj7yjniSAw&hl=fr&sa=X&ved=2ahUKEwiuivL6_JTpAhWa8
	 * uAKHYuuCUkQ6AEwAnoECAkQAQ#v=onepage&q=java%20keypressed%20timing&f=false
	 */

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {

			case 'd':
				Jeu.joueur.speed = 1;
				keys[0] = 1;
				break;

			case 'q':
				Jeu.joueur.speed = -1;
				keys[1] = 1;
				break;
		}
	}

	/**
	 * On utilise le keyReleased pour stopper le mouvement
	 */
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyChar()) {

			case 'd':
				if (keys[1] == 0) {
					Jeu.joueur.speed = 0;
				}
				keys[0] = 0;
				break;

			case 'q':
				if (keys[0] == 0) {
					Jeu.joueur.speed = 0;
				}
				keys[1] = 0;
				break;
		}
	}

	/**
	 * Touche de saut
	 */
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_SPACE) {
			soundEffects[2].jouer();
			Jeu.joueur.jump = true;
		}
	}

	/**
	 * Mise à jour de l'affichage graphique
	 */
	public void refresh() {
		p.repaint();
	}

	/**
	 * Faire un apparaitre un projectile ne direction de la souris
	 * lorsqu'on clique.
	 */
	public void mousePressed(MouseEvent e) {
		if (Jeu.joueur.munitions > 0) {
			soundEffects[1].jouer();
			Jeu.joueur.munitions--;
			Jeu.listeProj.add(new Projectile(e.getX(), e.getY(),
					Jeu.joueur.getX(), Jeu.joueur.getY()));
		}
	}

	/** 
	 * 
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/** 
	 * 
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/** 
	 * 
	 */
	public void mouseExited(MouseEvent e) {
	}

	/** 
	 * 
	 */
	public void mouseReleased(MouseEvent e) {
	}
}
