
/**
 * Created by Aakash on 3/18/2017.
 */
package sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class manages all the sound loading and contains
 * different methods for playing required sounds.
 */
public class SoundManager
{

  private final int FOOTSTEPS = 10;
  private final int GROANS = 4;
  private Map<String, AudioClip> sounds;
  private MediaPlayer mp;

  /**
   * Default constructor, which initialize the hashmap
   * where all the required sounds are stored.
   * It calls the initializeSound method, which loads
   * all the requires sounds and stor them in a hashMap
   */
  public SoundManager(){
    sounds = new HashMap<>();
    initializeSound();
  }

  /**
   * This method reads the sounds from the resourses and
   * puts calls the loadSound method.
   */
  private void initializeSound(){
    String name ="";
    URL url = null;

    name = "footstep";
    for (int i = 0; i < FOOTSTEPS; i++){
      url = getClass().getResource(name + i + ".wav");
      loadSoundClip(name + i, url);
    }

    name = "groan";
    for (int i = 0; i < GROANS; i++){
      url = getClass().getResource(name + i + ".wav");
      loadSoundClip(name + i, url);
    }

    name = "death";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);

    name = "heartbeat1";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);

    name = "sword_hit";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);

    name = "sword_swing";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);

    name = "zombiePunch";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);

    name = "exit";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);
  }

  /**
   * This methods loads the sound to the clip from given url and
   * stores in a Hashmap with given ID
   * @param ID
   * @param url
   */
  private void loadSoundClip(String ID, URL url)
  {
    AudioClip sound = new AudioClip(url.toExternalForm());
    sounds.put(ID, sound);
  }

  /**
   * This methods plays the sound of Zombie hitting.
   */
  public void playZombiePunch(){
    double volume = 0.1;
    AudioClip clip = sounds.get("zombiePunch");
    clip.play(volume,0,1,0,1);
  }

  /**
   * This methods plays the heartbeat of the player.
   */
  public void playHeartBeat(){

    double volume = 1;
    AudioClip clip = sounds.get("heartbeat1");
    clip.setRate(0.02);
    clip.play(volume,0,1,0,1);

  }

  /**
   * This method plays the groaning sound of the Zombie.
   * The volume of groan depends how far is zombie from the
   * player. The sound is played with certain balance in the
   * speakers and balance depends on which direction is the
   * zombie located from the player facing direction.
   * @param distance
   * @param balance
   */
  public void playZombieGroan(double distance, double balance)
  {

    Random random = new Random();
    double volume = (2/distance);
    int i = random.nextInt(GROANS);
    AudioClip clip = sounds.get("groan" + i);
    clip.play(volume,balance,1,0,1);

   }

  /**
   * This method plays the player death sound.
   */
  public void playPlayerDeath()
   {
     double volume = 0.3;
     AudioClip clip = sounds.get("death");
     clip.play(volume,0,1,0,1);

   }

  /**
   * This method plays the footstep sound for the player.
   */
  public void playPlayerFootStep(){
    Random random = new Random();
    double volume = 0.1;
    int i = random.nextInt(FOOTSTEPS);
    AudioClip clip = sounds.get("footstep" + i);
    clip.play(volume,0,1,0,1);
  }

  /**
   * This method plays the sound of the sword hitting
   * the zombie.
   */

  public void playSwordHit(){
    double volume = 0.20;
    AudioClip clip = sounds.get("sword_hit");
    clip.play(volume,0,1,0,1);
  }

  /**
   * This method plays the sound of sword swinging in the air.
   */
  public void playSwordSwing(){
    double volume = 0.25;
    AudioClip clip = sounds.get("sword_swing");
    clip.play(volume,0,1,0,1);
  }

  /**
   * This method plays the sound of achievement when player
   * finds the exit and completes the current level.
   */
  public void playExit(){
    double volume = 0.8;
    AudioClip clip = sounds.get("exit");
    clip.play(volume,0,1,0,1);
  }







}
