package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TimerActor extends Actor {
    private final Animation anim;
    private float elapsedTime = 0;
    public boolean runTimer = false;
    
    public TimerActor(Animation anim) {
        this.anim = anim;
    }            
    
    private void updateTime() {
        elapsedTime += Gdx.graphics.getDeltaTime();
    }
    
    public void reset() {
        elapsedTime = 0;
        runTimer = false;
    }
    
    public void start() {
        runTimer = true;
    }
    
    public void advance() {
        elapsedTime += 0.5f;
    }
    
    @Override
    public void draw(Batch batch, float alpha){
        if (runTimer) {
            updateTime();
        }
        batch.draw(anim.getKeyFrame(elapsedTime), 150, 445, 100, 100); 
    }     
}
