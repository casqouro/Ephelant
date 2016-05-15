package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AnimatedImage extends Image {
    private final AnimatedDrawable drawable;

    public AnimatedImage(AnimatedDrawable drawable) {
        super(drawable);
        this.drawable = drawable;        
    }
    
    @Override
    public void act(float delta)
    {
        drawable.act(delta);
        super.act(delta);
    }    
}