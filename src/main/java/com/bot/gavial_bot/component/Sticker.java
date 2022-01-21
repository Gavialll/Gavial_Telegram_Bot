package com.bot.gavial_bot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@Component
public class Sticker{
    public static SendSticker get(String name, String CHAT_ID) {
        File file = new File("/Users/andrijdutko/Desktop/Gavial_Bot/src/main/resources/" + name + ".tgs");
        InputFile inputFile = new InputFile();
        inputFile.setMedia(file);
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(inputFile);
        sendSticker.setChatId(CHAT_ID);
        return sendSticker;
    }
}
