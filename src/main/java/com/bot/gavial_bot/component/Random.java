package com.bot.gavial_bot.component;

public class Random {
    public static Integer random(int min, int max){
       return (int)Math.floor(Math.random()*(max-min+1)+min);
    }
}
