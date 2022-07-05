package com.jumpbraid.engine.game;

public interface IGameloop {
    public void handlerEvents();
    public void update();
    public void render();
}