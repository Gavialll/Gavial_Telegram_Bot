package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class Keyboard {
    private final Bot bot;
    private Integer keyboardId;

    public Keyboard printMenu() {
        return printButton(Message.hello, Button.STUDY_SENTENCES, Button.STUDY_WORDS, Button.STUDY_IRREGULAR_VERB,  Button.STUDY_SPRINT);
    }

    public Keyboard printButton(String message, Button... arr) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(bot.getCHAT_ID());
        sendMessage.setReplyMarkup(createKeyboard(arr)); //Set keyboard to message

        try {
            keyboardId = bot.execute(sendMessage).getMessageId();
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
        return Keyboard.this;
    }

    public Keyboard printChooseButton(List<String> wordsEnglish, String sentenceUkraine) {
        String message = Message.printQuestion(sentenceUkraine);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(bot.getCHAT_ID());
        sendMessage.setReplyMarkup(this.createChooseButton(wordsEnglish)); //Set keyboard to message

        try {
            this.keyboardId = bot.execute(sendMessage).getMessageId();
        } catch(TelegramApiException e) {
            e.printStackTrace();
        }
        return Keyboard.this;
    }

    public InlineKeyboardMarkup createKeyboard(Button... arr) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Initialization keyboard

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();  //All rows keyboard

        for(Button button : arr) {
            List<InlineKeyboardButton> firstRow = new ArrayList<>(); //First row keyboard

            InlineKeyboardButton btn = new InlineKeyboardButton(); //Initialization new buttons
            btn.setText(button.getName());
            btn.setCallbackData(button.getCallbackData());

            firstRow.add(btn);
            rowList.add(firstRow);

            inlineKeyboardMarkup.setKeyboard(rowList);
        }
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createChooseButton(List<String> words) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Initialization keyboard

        List<List<InlineKeyboardButton>> rowAll = new ArrayList<>(); //All rows keyboard

        for(int i = 0; i <= words.size() - 1; ) {
            List<InlineKeyboardButton> oneRow = new ArrayList<>(); //First row keyboard

            InlineKeyboardButton btn = new InlineKeyboardButton(); //Initialization new buttons
            btn.setText(words.get(i));
            btn.setCallbackData(words.get(i++));
            oneRow.add(btn);

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

    public Keyboard(Bot bot) {
        this.bot = bot;
    }

    public Integer getKeyboardId() {
        return keyboardId;
    }


    //Not used, it is example
//    public Keyboard MenuKeyboard(String message, String... str) {
//        KeyboardRow row1 = new KeyboardRow();
//        row1.add(new KeyboardButton(str[0]));
//        row1.add(new KeyboardButton(str[1]));
//
//        KeyboardRow row2 = new KeyboardRow();
//        row2.add(new KeyboardButton(str[2]));
//        row2.add(new KeyboardButton(str[3]));
//
//        List<KeyboardRow> keyboard = new ArrayList<>();
//        keyboard.add(row1);
//        keyboard.add(row2);
//
//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        replyKeyboardMarkup.setKeyboard(keyboard);
//        replyKeyboardMarkup.setSelective(true);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(false);
//
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText(message);
//        sendMessage.setChatId(bot.getCHAT_ID());
//        //it is set keyboard to message
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        try {
//            bot.execute(sendMessage);
//        } catch(TelegramApiException e) {
//            e.printStackTrace();
//        }
//        return Keyboard.this;
//    }
}
