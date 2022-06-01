package com.tocadosmarin.fourinline.game;

public class GameRunner extends Thread {
    public static boolean isFinished, hasToWait;

    static {
        isFinished = false;
        hasToWait = true;
    }

    private GameRunner() {
    }

    @Override
    public void run() {
        while (!isFinished) {
            synchronized (this) {
                while (hasToWait) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
