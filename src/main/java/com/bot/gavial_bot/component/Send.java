package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Send {
    private Bot bot;

    public void message(String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(bot.getCHAT_ID());
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Send(Bot bot) {
        this.bot = bot;
    }
}
