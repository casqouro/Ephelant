package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;

public class AnimatedImageTextButton extends ImageTextButton {
    
    private final AnimatedDrawable drawable;
    
    public AnimatedImageTextButton(String text, ImageTextButtonStyle style) {
        super(text, style);
        this.drawable = (AnimatedDrawable) style.down;
        this.setTouchable(Touchable.disabled);
    }

    @Override
    public void act(float delta)
    {
        drawable.act(delta);
        super.act(delta);
    }
}