package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

    public Send(Bot bot) {
        this.bot = bot;
    }
}
