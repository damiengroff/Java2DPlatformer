import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.URL;

public class Menu extends JFrame implements ActionListener {
	
	private JButton start;
	private JButton regles;
	private boolean appuiBoutonRegles;
	private Audio music;
	
	/** 
	 * Construit la fenêtre "Menu" qui lance le premier niveau
	 */
	public Menu() {
		
		URL url = this.getClass().getClassLoader().getResource
        ("Musiques/menu_music.wav");
		music = new Audio(url);
		
		music.jouer();
		
		setTitle("Menu");
		setSize(800, 540);
		setLocationRelativeTo(null);
		
		Font police1 = new Font("Arial", Font.BOLD, 55);
		Font police2 = new Font("Arial", Font.BOLD, 30);
		Font police3 = new Font("Arial", Font.BOLD, 15);
		
		ImageIcon wallpaper = new ImageIcon("./Images/Wallpaper.png");
        Image fond = wallpaper.getImage();
        Image newimg = fond.getScaledInstance(800, 500, Image.SCALE_DEFAULT);
        wallpaper = new ImageIcon(newimg);
        
		JLabel image = new JLabel(wallpaper);
		image.setBounds(0, 0, 800, 500);
		
		JLabel jeu = new JLabel("SUPER  MARIA  BRASSE");
        jeu.setLayout(null);
        jeu.setFont(police1);
		jeu.setBounds(this.getWidth()/2-340, 20, 680, 100);
		jeu.setForeground(Color.white);
				
		start = new JButton("START");
        start.setLayout(null);
        start.setFont(police2);
		start.setBounds(this.getWidth()/2-80, 150, 140, 90);
		start.setForeground(Color.black);
		
		regles = new JButton("Regles du jeu");
        regles.setLayout(null);
        regles.setFont(police3);
		regles.setBounds(20, 440, 150, 40);
		regles.setForeground(Color.black);
        regles.setBackground(Color.red);
        appuiBoutonRegles = false;
		        
		JPanel conteneurBoutons = new JPanel();
        conteneurBoutons.setLayout(null);
        conteneurBoutons.setBounds(0, 0, 800, 600);
		conteneurBoutons.add(jeu);
		conteneurBoutons.add(start);
		conteneurBoutons.add(displayScore());
		conteneurBoutons.add(regles);
		conteneurBoutons.setOpaque(false);
        		
		JPanel conteneurPrincipal = new JPanel();
		conteneurPrincipal.setLayout(null);
		conteneurPrincipal.setBounds(0, 0, 800, 600);
		conteneurPrincipal.add(conteneurBoutons);
		conteneurPrincipal.add(image);
		add(conteneurPrincipal);
		
		this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        start.addActionListener(this);
        regles.addActionListener(this);
	}
	
	private JLabel displayScore() {
		
		int h_minutes = (int)(Jeu.getHighScore()/60000);
		int h_seconds = (int)(Jeu.getHighScore()/1000)%60;
		
		JLabel s = new JLabel("HIGH SCORE ACTUEL : "+h_minutes+"min"+h_seconds+"s.", SwingConstants.CENTER);
		s.setLayout(null);
		s.setFont(new Font("Arial", Font.BOLD, 30));
		s.setBounds(0, 270, this.getSize().width, 40);
		s.setForeground(Color.white);
		
		return s;
		
	}
	
	/** 
	 * Lance le jeu si on appuie sur "start"
	 * Affiche les commandes si on appuie sur "regles"
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == start) {
			
			music.arreter();
			this.setVisible(false);
			new Jeu();
			
		} else if (e.getSource() == regles) {
			
			if (!appuiBoutonRegles) {
				regles.setSize(740, 40);
				regles.setBackground(new Color(200, 210, 210));
				regles.setText("q : aller vers la gauche  |"+
                               "d : aller vers la droite  |"+
                               "espace : sauter  |"+
                               "cliquer : envoie un projectile");
				appuiBoutonRegles = true;
			} else {
				regles.setSize(150, 40);
				regles.setBackground(Color.red);
				regles.setText("Regles du jeu"); 
				appuiBoutonRegles = false;
			}
		}
	}
}
