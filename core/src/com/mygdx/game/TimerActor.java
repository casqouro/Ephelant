package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TimerActor extends Actor {
    private final Animation anim;
    private float elapsedTime = 0;
    private boolean runTimer = false;
    
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
    
    public void runTimer() {
        runTimer = true;
    }
    
    public void stopTimer() {
        runTimer = false;
    }
    
    public void advance() {
        elapsedTime += 0.5f;
    }
    
    public Boolean isDone() {
        return anim.isAnimationFinished(elapsedTime);
    }
    
    public void setTimerLength(float frameDuration) {
        anim.setFrameDuration(1 / (anim.getKeyFrames().length / frameDuration));
    }
    
    @Override
    public void draw(Batch batch, float alpha){
        if (runTimer) {
            updateTime();
        }
        batch.draw(anim.getKeyFrame(elapsedTime), 150, 445, 100, 100); 
    }     
}
