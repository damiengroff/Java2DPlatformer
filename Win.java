import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.URL;


public class Win extends JFrame implements ActionListener{
    
	private JButton monBoutonReturn;
    private JButton monBoutonQuit;
    private Audio music;
    
    /** 
	 * Construit la fenetre "Win" qui s'affiche quand le joueur gagne
     * (c'est-a-dire quand il finit le niveau 3)
	 */
	public Win() {
		
		setTitle("WIN");
		setSize(400, 520);
		setLocationRelativeTo(null);
		
		Font police = new Font("Arial", Font.BOLD, 15);
        
		ImageIcon win = new ImageIcon("./Images/bravo.png");
        Image fond = win.getImage();
        Image newimg = fond.getScaledInstance(400, 200, Image.SCALE_DEFAULT);
        win = new ImageIcon(newimg);
        
        JLabel finJeu = new JLabel(win);
		finJeu.setBounds(0, 0, 400, 200);
		
        JTextArea credit = new JTextArea("CREDITS:\n"+
                                         "Damien GROFF \n"+ 
                                         "Firmin JACINTHO \n"+
                                         "Bleuenn RICHIER \n"+
                                         "Lucas GEHIN");
		JScrollPane ascenseur = new JScrollPane(credit);
		ascenseur.setBounds(30, 210, 320, 50);
		
		monBoutonQuit = new JButton("Quitter le jeu");
        monBoutonQuit.setLayout(null);
        monBoutonQuit.setFont(police);
		monBoutonQuit.setBounds(230, 275, 130, 80);
		monBoutonQuit.setBackground(Color.red);
		monBoutonQuit.setForeground(Color.white);
		
        monBoutonReturn = new JButton("REJOUER !");
        monBoutonReturn.setLayout(null);
        monBoutonReturn.setFont(police);
		monBoutonReturn.setBounds(20, 275, 130, 80);
		monBoutonReturn.setBackground(new Color(0, 200, 0));
		monBoutonReturn.setForeground(Color.white);
        
		JPanel conteneurWin = new JPanel();
		conteneurWin.setLayout(null);
		conteneurWin.setBackground(Color.black);
		conteneurWin.add(finJeu);
		conteneurWin.add(monBoutonQuit);
		conteneurWin.add(monBoutonReturn);
        conteneurWin.add(ascenseur);
        conteneurWin.add(displayScore(System.currentTimeMillis()-Jeu.startTime)); 
		add(conteneurWin);
        
        
        URL url = this.getClass().getClassLoader().getResource
        ("Musiques/win.wav");
		music = new Audio(url);
        music.jouer();
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        monBoutonReturn.addActionListener(this);
        monBoutonQuit.addActionListener(this);
	}
	
       
    public JLabel displayScore(long finalTime) {
		
		int minutes = (int)(finalTime/60000);
		int seconds = (int)(finalTime/1000)%60;
		
		int h_minutes = (int)(Jeu.getHighScore()/60000);
		int h_seconds = (int)(Jeu.getHighScore()/1000)%60;
		
		if(Jeu.getHighScore()>(int)finalTime) {
			
			Jeu.setHighScore((int)finalTime);
			
			JLabel s = new JLabel("NOUVEAU HIGH SCORE : "+minutes+"min"+seconds+"s.", SwingConstants.CENTER);
			s.setLayout(null);
			s.setFont(new Font("Arial", Font.BOLD, 15));
			s.setBounds(0, 320, this.getSize().width, 150);
			s.setForeground(Color.white);
			
			return s;
			
		}else {
			
			JLabel s = new JLabel("TU AS FAIS : "+minutes+"min"+seconds+"s. HIGHSCORE : "+h_minutes+"min"+h_seconds+"s.", SwingConstants.CENTER);
			s.setLayout(null);
			s.setFont(new Font("Arial", Font.BOLD, 15));
			s.setBounds(0, 320, this.getSize().width, 150);
			s.setForeground(Color.white);
			
			return s;
			
		}
		
	}
	  
    
    /** 
     * Arrete la musique de la fenêtre
	 * Relance le jeu si on clique sur "monBoutonReturn"
	 * Quitte le jeu si on clique sur "monBoutonQuit"
     * Efface la fenêtre
	 */
    public void actionPerformed(ActionEvent e) {
		
        if (e.getSource() == monBoutonReturn) {
			
			music.arreter();
			this.setVisible(false);
            Jeu.currentLevel = 1;
            Jeu.setLevel(Jeu.currentLevel);
            Jeu.startTime = System.currentTimeMillis();
            remove(this);
            
		} else if (e.getSource() == monBoutonQuit) {
			
			music.arreter();
			this.setVisible(false);
            //menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            remove(this);
        }
    }
}

