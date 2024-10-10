import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Music {
    Clip music;

    Music (String musicFileName) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicFileName));
            this.music = AudioSystem.getClip();
            this.music.open(audioStream);
        } catch (IOException ex) {
            System.out.println("File not found!");
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("Unsupported file!");
        } catch (LineUnavailableException ex) {
            System.out.println("Audio feed already in use!");
        }
    }
    public void start() {
        this.music.start();
    }
    public void loop() {
        this.music.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        this.music.stop();
    }
    public void rewind() {
        this.music.setMicrosecondPosition(0);
    }
}