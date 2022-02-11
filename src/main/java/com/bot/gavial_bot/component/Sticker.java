package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
public class Sticker{

    private Bot bot;

    public void send(String name) throws TelegramApiException {
        File file = new File("/src/main/resources/" + name + ".tgs");
        InputFile inputFile = new InputFile();
        inputFile.setMedia(file);
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(inputFile);
        sendSticker.setChatId(bot.getCHAT_ID());
        bot.execute(sendSticker);
    }

    public void send(Integer score) throws TelegramApiException {
        String name = "";
        if(score <= 3) {
            String[] bad = "1 2 8 10 11 15".split(" ");
            name = bad[Random.random(0, bad.length -1)];
        }
        if(score > 3 && score <= 7){
            String[] middle = "17 16 13 12 6".split(" ");
            name = middle[Random.random(0, middle.length -1)];
        }
        if(score > 7 && score <= 10){
            String[] good = "20 18 14 9 7 5 4 3".split(" ");
            name = good[Random.random(0, good.length -1)];
        }
        send(name);
    }

    public Sticker(Bot bot) {
        this.bot = bot;
    }
}
