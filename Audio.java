import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
 
public class Audio extends Thread {
	
    private AudioClip sound;
    private boolean isRunning = false;
    
    /**
     *Créé une musique à partir d'un chemin url 
     */
    public Audio(URL url){
        sound = Applet.newAudioClip(url);
    }
    
    /**
     *Lance la musique 
     */ 
    public void jouer() {
        sound.play();
    }
     
    /**
     *Lance la musique en boucle
     */
    public void jouerEnBoucle() {
		if(!isRunning)sound.loop();
		isRunning=true;
    }
    
    /**
     *Arrête la musique 
     */
    public void arreter() {
		isRunning=false;
        sound.stop();
    }
}
