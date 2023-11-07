package com.mygdx.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;

import javax.swing.*;
import java.sql.Time;

/**
 * ClassName: StartMusic
 * Package: com.mygdx.music
 * Description:
 *
 * @Author NieHao
 * @Create 2023/11/8 2:15
 * @Version 1.0
 */
public class StartMusic{
    private static StartMusic startMusic;
    private StartMusic(){}
    public static StartMusic getInstance(){
        if(startMusic==null) startMusic = new StartMusic();
        return startMusic;
    }
    public void runBoomMusic() {
        Music newMusic = Gdx.audio.newMusic(Gdx.files.internal("video/Fnai.mp3"));
        //newMusic.setLooping(true);
        newMusic.play();
        if(!newMusic.isPlaying()){
            newMusic.dispose();
        }
    }
}
