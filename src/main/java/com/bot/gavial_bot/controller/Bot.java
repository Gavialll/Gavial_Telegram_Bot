package com.bot.gavial_bot.controller;

import com.bot.gavial_bot.Quiz;
import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.model.User;
import com.bot.gavial_bot.model.Word;
import com.bot.gavial_bot.newUser;
import com.bot.gavial_bot.repository.UserRepository;
import com.bot.gavial_bot.service.WordService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordService wordService;
    private String CHAT_ID;
    private String flag = "";
    private Quiz quiz;
    private final Send send = new Send(Bot.this);

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            CHAT_ID = update.getMessage().getChatId().toString();
        }
        if(update.hasCallbackQuery()){
            CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        startNewUser(update);
    }

    public void startNewUser(Update update){
        if(update.hasMessage() && update.getMessage().getText().equals("/start")) {
            CHAT_ID = update.getMessage().getChatId().toString();
            new Keyboard(Bot.this).printButton(Message.hello, StartPoint.STUDY_WORDS, StartPoint.GET_ALL);
//            System.out.println(update.getMessage().getChatId());
        }

        if(update.hasCallbackQuery()) {
            CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
            flag = update.getCallbackQuery().getData();
//            System.out.println(update.getCallbackQuery().getMessage().getChatId().toString());
        }

        if(quiz != null && quiz.getState().equals(Thread.State.WAITING)) {
            quiz.setUpdate(update);
        }

        switch(flag) {
            case CallbackData.STUDY_WORDS: {
                quiz = new Quiz();
                quiz.setUpdate(update);
                quiz.setWordList(wordService.getAll());
                quiz.setBot(Bot.this);
                quiz.start();
                flag = "";
                break;
            }
            case CallbackData.GET_ALL: {
                StringBuilder str = new StringBuilder();
                for(Word word : wordService.getAll()) {
                    str.append(word.getEnglish() + " - " + word.getUkraine() + "\n\n");
                }
                send.message(str.toString());
                new Keyboard(Bot.this).printButton(Message.selectActive, StartPoint.STUDY_WORDS);
                break;
            }
        }
    }

    public void setCHAT_ID(String CHAT_ID) {
        this.CHAT_ID = CHAT_ID;
    }

    public String getCHAT_ID() {
        return CHAT_ID;
    }

    @Override
    public String getBotUsername() {
        return "Gavial_Bot";
    }

    @Override
    public String getBotToken() {
        return "5090214861:AAEMtwQNUZm_u8ainDOmm-18Ef9aSnyGOrE";
    }
}

//it is sent audio file
//    String userMessage = update.getMessage().getText().toLowerCase(Locale.ROOT);
//            if(count == 0 || count == 5 || userMessage.equals("red hot")) {
//                    File file = new File("/Users/andrijdutko/Desktop/Gavial_Bot/src/main/resources/Can t Stop   The Red Hot Chilli Peppers.mp3");
//                    sendMessage("Ваш трек: " + file.getName());
//
//                    SendAudio sendAudio = new SendAudio();
//                    InputFile inputFile = new InputFile(file);
//                    sendAudio.setAudio(inputFile);
//                    sendAudio.setChatId(CHAT_ID);
//
//                    execute(sendAudio);
//                    count++;
//                    }