package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class Keyboard {
   private Bot bot;

    public Keyboard(Bot bot) {
        this.bot = bot;
    }

    public Keyboard printButton(String message, StartPoint... arr){
        //it is initialization keyboard
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //it is first row keyboard
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        for(int i = 0; i < arr.length; i++) {
            //it is initialization new buttons
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(arr[i].getName());
            btn.setCallbackData(arr[i].getCallbackData());
            firstRow.add(btn);
        }

        //it is all rows keyboard
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(firstRow);

        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(bot.getCHAT_ID());
        //it is set keyboard to message
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            bot.execute(sendMessage);
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
        return Keyboard.this;
    }
}
