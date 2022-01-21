package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Send {
    private Bot bot;
    private String CHAT_ID;

    public void message(String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(CHAT_ID);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Send(Bot bot, String CHAT_ID) {
        this.bot = bot;
        this.CHAT_ID = CHAT_ID;
    }
}
