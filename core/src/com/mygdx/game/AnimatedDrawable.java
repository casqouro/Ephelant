package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

class AnimatedDrawable extends BaseDrawable
{
    private final Animation anim;    
    private float stateTime = 0;

    public AnimatedDrawable(Animation anim)
    {
        this.anim = anim;
    }

    public void act(float delta)
    {
        stateTime += delta;
        //System.out.println(delta * 100);
    }
    
    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.draw(anim.getKeyFrame(stateTime), x, y, width, height); 
        //System.out.println(stateTime);
    }
}