package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Sentence;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


public class Keyboard {
   private Bot bot;
   private Integer keyboardId;

    public Keyboard(Bot bot) {
        this.bot = bot;
    }

    public Keyboard printButton(String message, Button... arr){
        //it is initialization keyboard
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        //it is all rows keyboard
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for(int i = 0; i < arr.length; i++) {
            //it is first row keyboard
            List<InlineKeyboardButton> firstRow = new ArrayList<>();
            //it is initialization new buttons
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(arr[i].getName());
            btn.setCallbackData(arr[i].getCallbackData());

            firstRow.add(btn);

            rowList.add(firstRow);

            inlineKeyboardMarkup.setKeyboard(rowList);
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(bot.getCHAT_ID());
        //it is set keyboard to message
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
           keyboardId = bot.execute(sendMessage).getMessageId();
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
        return Keyboard.this;
    }

    public InlineKeyboardMarkup editKeyboard(Button... arr){
        //it is initialization keyboard
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        //it is all rows keyboard
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for(int i = 0; i < arr.length; i++) {
            //it is first row keyboard
            List<InlineKeyboardButton> firstRow = new ArrayList<>();
            //it is initialization new buttons
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(arr[i].getName());
            btn.setCallbackData(arr[i].getCallbackData());

            firstRow.add(btn);

            rowList.add(firstRow);

            inlineKeyboardMarkup.setKeyboard(rowList);
        }
        return inlineKeyboardMarkup;
    }

    public Keyboard printMenu(){
        return printButton(Message.hello, Button.STUDY_SENTENCES, Button.STUDY_WORDS, Button.STUDY_SPRINT, Button.STUDY_IRREGULAR_VERB);
    }

    public Keyboard MenuKeyboard(String message, String... str) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(str[0]));
        row1.add(new KeyboardButton(str[1]));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(str[2]));
        row2.add(new KeyboardButton(str[3]));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(bot.getCHAT_ID());
        //it is set keyboard to message
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            bot.execute(sendMessage);
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
        return Keyboard.this;
    }

    public Integer getKeyboardId() {
        return keyboardId;
    }

    public Keyboard printChooseButton(List<String> words, String sentenceUkraine){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        //it is all rows keyboard
        List<List<InlineKeyboardButton>> rowAll = new ArrayList<>();

        for(int i = 0; i <= words.size()-1;) {
            //it is first row keyboard
            List<InlineKeyboardButton> oneRow = new ArrayList<>();

            if(i != words.size()) {
                //it is initialization new buttons
                InlineKeyboardButton btn = new InlineKeyboardButton();
                btn.setText(words.get(i));
                btn.setCallbackData(words.get(i++));
                oneRow.add(btn);
            }
            if(i != words.size()) {
                InlineKeyboardButton btn1 = new InlineKeyboardButton();
                btn1.setText(words.get(i));
                btn1.setCallbackData(words.get(i++));
                oneRow.add(btn1);
            }

            rowAll.add(oneRow);

            inlineKeyboardMarkup.setKeyboard(rowAll);
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(Message.printQuestion(sentenceUkraine));
        sendMessage.setChatId(bot.getCHAT_ID());
        //it is set keyboard to message
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            keyboardId = bot.execute(sendMessage).getMessageId();
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
        return Keyboard.this;
    }

    public InlineKeyboardMarkup editChooseButton(List<String> words){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        //it is all rows keyboard
        List<List<InlineKeyboardButton>> rowAll = new ArrayList<>();

        for(int i = 0; i <= words.size()-1;) {
            //it is first row keyboard
            List<InlineKeyboardButton> oneRow = new ArrayList<>();

            if(i != words.size()) {
                //it is initialization new buttons
                InlineKeyboardButton btn = new InlineKeyboardButton();
                btn.setText(words.get(i));
                btn.setCallbackData(words.get(i++));
                oneRow.add(btn);
            }
            if(i != words.size()) {
                InlineKeyboardButton btn1 = new InlineKeyboardButton();
                btn1.setText(words.get(i));
                btn1.setCallbackData(words.get(i++));
                oneRow.add(btn1);
            }

            rowAll.add(oneRow);

            inlineKeyboardMarkup.setKeyboard(rowAll);
        }
        return inlineKeyboardMarkup;
    }
}
