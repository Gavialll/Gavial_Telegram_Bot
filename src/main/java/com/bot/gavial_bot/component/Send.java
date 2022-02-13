package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.stream.FileImageInputStream;
import java.io.File;

public class Send {
    private Bot bot;

    public Integer message(String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(bot.getCHAT_ID());
        sendMessage.setText(message);
        try {
          return bot.execute(sendMessage).getMessageId();
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void Image(String path){
        File file = new File(path);
        InputFile inputFile = new InputFile(file);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(bot.getCHAT_ID());
        sendPhoto.setPhoto(inputFile);

        try {
            bot.execute(sendPhoto);
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Send(Bot bot) {
        this.bot = bot;
    }
}
