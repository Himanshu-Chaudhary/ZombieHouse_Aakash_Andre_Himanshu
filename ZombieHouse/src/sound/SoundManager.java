package sound;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Aakash on 3/18/2017.
 */
public class SoundManager
{

  private final int FOOTSTEPS = 10;
  private Map<String, AudioClip> sounds;

  public SoundManager(){
    sounds = new HashMap<>();
    initializeSound();
  }
  private void initializeSound(){
    String name ="";
    URL url = null;

    name = "footstep";
    for (int i = 0; i < FOOTSTEPS; i++){
      url = getClass().getResource(name + i + ".wav");
      loadSoundClip(name + i, url);
    }
    name = "sword_hit";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);

    name = "sword_swing";
    url = getClass().getResource(name + ".wav");
    loadSoundClip(name, url);
  }
  private void loadSoundClip(String ID, URL url){
    AudioClip sound = new AudioClip(url.toExternalForm());
    sounds.put(ID, sound);
  }



  public void playPlayerFootStep(){
    Random random = new Random();
    double volume = 0.4;
    int i = random.nextInt(FOOTSTEPS);
    AudioClip clip = sounds.get("footstep" + i);
    clip.play(volume);
  }

  public void playSwordHit(){
    double volume = 0.4;
    AudioClip clip = sounds.get("sword_hit");
    clip.play(volume);
  }

  public void playSwordSwing(){
    double volume = 0.4;
    AudioClip clip = sounds.get("sword_swing");
    clip.play(volume);
  }





}
