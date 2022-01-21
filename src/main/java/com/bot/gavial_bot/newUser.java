package com.bot.gavial_bot;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.model.Word;
import com.bot.gavial_bot.service.WordService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class newUser extends Thread{
    private String flag ;
    private Update update;
    private Bot bot;
    private String CHAT_ID;
    private List<Word> wordList;

    @SneakyThrows
    @Override
    public void run() {

        if(flag.equals("")){
            synchronized(this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        switch(flag) {
            case CallbackData.STUDY_WORDS: {
                Quiz quiz = new Quiz();


//                for(Word word : wordList) {
//                    new Send(bot, CHAT_ID).message(word.getEnglish() + " - " + word.getUkraine());
//                    synchronized(this) {
//                        try {
//                            this.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if(update.getMessage().getText().equals("str")){
//                        new Send(bot, CHAT_ID).message("answer OK");
//                    }
//                    else System.out.println("else");
//                }
                break;
            }
            case CallbackData.GET_ALL: {

                break;
            }
        }
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setCHAT_ID(String CHAT_ID) {
        this.CHAT_ID = CHAT_ID;
    }

    public String getCHAT_ID() {
        return CHAT_ID;
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
