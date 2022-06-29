package com.jumpbraid.engine.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IGameloop {
    public void handlerEvents();
    public void update();
    public void render(SpriteBatch batch);
}