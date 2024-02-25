import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.URL;

public class GameOver extends JFrame implements ActionListener {
    
	private JButton monBoutonReturn;
    private JButton monBoutonQuit;
    private Audio music;
    
    /** 
	 * Construit la fenêtre "Game Over" qui s'affiche quand le joueur
	 * perd la partie
	 */
	public GameOver() {
        setTitle("Game Over");
		setSize(400, 400);
		setLocationRelativeTo(null);
		
		Font police = new Font("Arial", Font.BOLD, 15);
        
		ImageIcon gameOver = new ImageIcon("./Images/gameOver.png");
        Image fond = gameOver.getImage();
        Image newimg = fond.getScaledInstance(400, 400, Image.SCALE_DEFAULT);
        gameOver= new ImageIcon(newimg);
        
        JLabel finJeu= new JLabel(gameOver);
		finJeu.setBounds(70, 0, 250, 200);
		
		monBoutonQuit = new JButton("Quitter le jeu");
        monBoutonQuit.setLayout(null);
        monBoutonQuit.setFont(police);
		monBoutonQuit.setBounds(230, 220, 130, 80);
		monBoutonQuit.setBackground(Color.red);
		monBoutonQuit.setForeground(Color.white);
		
        monBoutonReturn = new JButton("REJOUER !");
        monBoutonReturn.setLayout(null);
        monBoutonReturn.setFont(police);
		monBoutonReturn.setBounds(20, 220, 130, 80);
		monBoutonReturn.setBackground(new Color(0, 200, 0));
		monBoutonReturn.setForeground(Color.white);
		
		JPanel conteneurGameOver = new JPanel();
		conteneurGameOver.setLayout(null);
		conteneurGameOver.setBackground(Color.black);
		conteneurGameOver.add(finJeu);
		conteneurGameOver.add(monBoutonQuit);
		conteneurGameOver.add(monBoutonReturn);
		add(conteneurGameOver);
        
        URL url = this.getClass().getClassLoader().getResource
        ("Musiques/defeat.wav");
		music = new Audio(url);
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        monBoutonReturn.addActionListener(this);
        monBoutonQuit.addActionListener(this);
        music.jouer();
	}
    
    /** 
     * Arrete la musique de la fenetre
	 * Relance le jeu au niveau 1 si on clique sur "monBoutonReturn"
	 * Quitte le jeu si on clique sur "monBoutonQuit"
     * Efface la fenetre
	 */
    public void actionPerformed(ActionEvent e) {
		
        if (e.getSource() == monBoutonReturn) {
			
            music.arreter();
			this.setVisible(false);
            
            if(Jeu.currentLevel == 3) { /* si on est au niveau trois, il
										 * faut arreter le timer 
										 * d'appartion des ennemis 
										 * lorsqu'on recommence
										 * au début
										 */
				for(int i = 0; i < Jeu.listeObstacles.size(); i++) {
					if(Jeu.listeObstacles.get(i) instanceof Boss)
						((Boss)Jeu.listeObstacles.get(i))
                        .enemySpawnTimer.stop();
				}
			}
			
			Jeu.currentLevel = 1;
			Jeu.setLevel(Jeu.currentLevel);
			Jeu.f.soundEffects[0].jouer();
			Jeu.startTime = System.currentTimeMillis();
			
            remove(this);
            
		} else if (e.getSource() == monBoutonQuit) {
			
			music.arreter();
			this.setVisible(false);
            // menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            remove(this);
        }
    }
}
