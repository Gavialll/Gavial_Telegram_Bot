package com.bot.gavial_bot;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.model.Word;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Quiz extends Thread{
    private Bot bot;
    private List<Word> wordList;
    private Update update;

    @SneakyThrows
    @Override
    public void run() {
        Send send = new Send(bot);
        int result = 0;
        int index = 0;
        int question = 10;
        Collections.shuffle(wordList);
        for(Word word : wordList){
            send.message("Translate word: «" + word.getUkraine().toUpperCase(Locale.ROOT) + "»❔");

                synchronized(this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(word.getEnglish())){
                    send.message(Message.answer(++result, ++index, question));
                }else {
                    send.message(Message.answer(++index, question));
                }
            if(question == index) {
                if(result < 3) bot.execute(Sticker.get("22", bot.getCHAT_ID()));
                if(result >= 3 && result <= 8) bot.execute(Sticker.get("17", bot.getCHAT_ID()));
                if(result > 8) bot.execute(Sticker.get("20", bot.getCHAT_ID()));
                
                send.message(Message.printResult(result));
                new Keyboard(bot).printButton(Message.selectActive,StartPoint.STUDY_WORDS, StartPoint.GET_ALL);
                interrupt();
                return;
            }
        }
    }

    public void setUpdate(Update update) {
        this.update = update;
        synchronized(this) {
            notify();
        }
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
