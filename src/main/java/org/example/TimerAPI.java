//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

public interface TimerAPI {
    void setTime(int var1);

    int getTime();

    String getFormattedTime(int var1);

    void setRunning(boolean var1);

    boolean isRunning();

    void setCountUp(boolean var1);

    boolean isCountUp();

    void reset();
}
