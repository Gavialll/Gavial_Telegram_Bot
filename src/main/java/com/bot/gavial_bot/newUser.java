package com.bot.gavial_bot;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.model.Word;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class newUser extends Thread{
    private Update update;
    private Bot bot;
    private String CHAT_ID;
    private String flag = "";
    private Quiz quiz;
    private final Send send = new Send(bot);
    private List<Word> wordList;
    private Keyboard keyboard;

    @Override
    public void run() {
        if(update.hasMessage() && update.getMessage().getText().equals("/start")) {
            this.CHAT_ID = update.getMessage().getChatId().toString();
            new Keyboard(bot).printButton(Message.hello, StartPoint.STUDY_WORDS, StartPoint.GET_ALL);
            System.out.println(update.getMessage().getChatId());
        }

        if(update.hasCallbackQuery()) {
            CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
            flag = update.getCallbackQuery().getData();
            System.out.println(update.getCallbackQuery().getMessage().getChatId().toString());
        }

        if(quiz != null && quiz.getState().equals(Thread.State.WAITING)) {
            quiz.setUpdate(update);
            System.out.println("fdsd");
        }

        switch(flag) {
            case CallbackData.STUDY_WORDS: {
                quiz = new Quiz();
                quiz.setUpdate(update);
                quiz.setWordList(wordList);
                quiz.setBot(bot);
                quiz.start();
                flag = "";
                break;
            }
            case CallbackData.GET_ALL: {
                StringBuilder str = new StringBuilder();
                for(Word word : wordList) {
                    str.append(word.getEnglish() + " - " + word.getUkraine() + "\n\n");
                }
                send.message(str.toString());
                new Keyboard(bot).printButton(Message.selectActive, StartPoint.START);
                break;
            }
        }
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    public void setUpdate(Update update) {
        this.update = update;
        synchronized(this) {
            notify();
        }
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
