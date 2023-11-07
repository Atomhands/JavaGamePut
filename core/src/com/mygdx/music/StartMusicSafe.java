package com.mygdx.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * ClassName: StartMusicSafe
 * Package: com.mygdx.music
 * Description:
 *
 * @Author NieHao
 * @Create 2023/11/8 2:34
 * @Version 1.0
 */
public class StartMusicSafe {
    private volatile static StartMusicSafe startMusicSafe;
    private StartMusicSafe(){}
    public static StartMusicSafe getInstance(){
        synchronized (StartMusicSafe.class){
            if (startMusicSafe==null){
                startMusicSafe = new StartMusicSafe();
            }
        }
        return startMusicSafe;
    }
    public void runShotMusic(){
        Music newMusic = Gdx.audio.newMusic(Gdx.files.internal("video/Fshot.mp3"));
        newMusic.play();
        if(!newMusic.isPlaying()){
            newMusic.dispose();
        }
    }
    public void runBoomMusic() {
        Music newMusic = Gdx.audio.newMusic(Gdx.files.internal("video/nai.mp3"));
        //newMusic.setLooping(true);
        newMusic.play();
        if(!newMusic.isPlaying()){
            newMusic.dispose();
        }
    }
}
