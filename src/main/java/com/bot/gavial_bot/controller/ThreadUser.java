package com.bot.gavial_bot.controller;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.model.Word;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
public class ThreadUser extends Thread {
    private String flag;
    private Update update;
    private Bot bot;
    private String CHAT_ID;
    private List<Word> wordList;

    @SneakyThrows
    @Override
    public void run() {
        while(true) {
            if(flag.equals("")) {
                log.info("Thread with CHAT_ID: " + CHAT_ID + ". Status: Wait");
                synchronized (this) {
                    try {
                        this.wait();
                    } catch(InterruptedException e) {
                        return;
                    }
                }
            }
            if(update.hasCallbackQuery()) {
                flag = update.getCallbackQuery().getData();
            }

            switch(flag) {
                case CallbackData.STUDY_WORDS: {
                    log.info(StartPoint.STUDY_WORDS.getCallbackData() + " Status: Run");
                    Send send = new Send(bot, CHAT_ID);
                    int result = 0;
                    int index = 0;
                    int question = 10;
                    Collections.shuffle(wordList);
                    for(Word word : wordList){
                        send.message("Translate word: «" + word.getUkraine().toUpperCase(Locale.ROOT) + "»❔");

                        log.info(StartPoint.STUDY_WORDS.getCallbackData() + " Status: Wait");
                        synchronized(this) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                flag = "";
                                return;
                            }
                        }

                        if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(word.getEnglish())){
                            send.message(Message.answer(++result, ++index, question));
                        }else {
                            send.message(Message.answer(++index, question));
                        }
                        if(question == index) {
                            log.info(StartPoint.STUDY_WORDS.getCallbackData() + " Status: Finish");
                            if(result < 5) bot.execute(Sticker.get(Random.random(1, 19).toString(), bot.getCHAT_ID()));
                            if(result >= 5 && result <= 10) bot.execute(Sticker.get(Random.random(1, 19).toString(), bot.getCHAT_ID()));
                            if(result > 15) bot.execute(Sticker.get("20", bot.getCHAT_ID()));

                            send.message(Message.printResult(result));
                            new Keyboard(bot).printButton(Message.selectActive,StartPoint.STUDY_WORDS, StartPoint.GET_ALL);
                            flag = "";
                            break;
                        }
                    }
                    break;
                }
                case CallbackData.GET_ALL: {
                    log.info(StartPoint.GET_ALL.getCallbackData() + ". Print all words");
                    StringBuilder str = new StringBuilder();
                    for(Word word : wordList) {
                        str.append(word.getEnglish() + " - " + word.getUkraine() + "\n\n");
                    }
                    new Send(bot, CHAT_ID).message(str.toString());
                    new Keyboard(bot).printButton(Message.selectActive, StartPoint.STUDY_WORDS, StartPoint.GET_ALL);
                    flag = "";
                    break;
                }
            }
        }
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
        synchronized (this) {
            notify();
        }
        log.info("Thread with CHAT_ID: " + CHAT_ID + ". Status: Runnable");
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public String toString() {
        return "newUser{" + "CHAT_ID='" + CHAT_ID + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ThreadUser user = (ThreadUser) o;
        return Objects.equals(bot, user.bot) && Objects.equals(CHAT_ID, user.CHAT_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bot, CHAT_ID);
    }
}
