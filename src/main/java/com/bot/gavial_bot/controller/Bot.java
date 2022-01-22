package com.bot.gavial_bot.controller;

import com.bot.gavial_bot.component.Keyboard;
import com.bot.gavial_bot.component.Message;
import com.bot.gavial_bot.component.Send;
import com.bot.gavial_bot.component.StartPoint;
import com.bot.gavial_bot.repository.UserRepository;
import com.bot.gavial_bot.service.WordService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordService wordService;
    private String CHAT_ID;
    private String flag = "";
    private final Send send = new Send(Bot.this, CHAT_ID);
    private List<ThreadUser> userList = new ArrayList<>();

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        //Старт першого потоку або обновленн старого
        if(update.hasMessage() && update.getMessage().getText().equals("/start")) {
            createNewThread(update);
        }
        if(update.hasCallbackQuery()) {
            CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
            flag = update.getCallbackQuery().getData();
        }
        if(update.hasMessage()) {
            CHAT_ID = update.getMessage().getChatId().toString();
        }

        for(ThreadUser thread : userList) {
            if(thread.getCHAT_ID().equals(CHAT_ID)) {
                log.info("Set Update in Thread with CHAT_ID = " + CHAT_ID);
                thread.setUpdate(update);
            }
        }
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

    private void createNewThread(Update update) {
        for(int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getCHAT_ID().equals(update.getMessage().getChatId().toString()) && userList.get(i).getState().equals(Thread.State.WAITING)) {
                log.info("Reload thread with CHAT_ID = " + update.getMessage().getChatId());
                synchronized (this) {
                    userList.get(i).interrupt();
                }
                userList.remove(i);
            }
        }
        flag = "";
        CHAT_ID = update.getMessage().getChatId().toString();
        log.info("Create new thread with CHAT_ID = " + CHAT_ID);
        new Keyboard(Bot.this).printButton(Message.hello, StartPoint.STUDY_WORDS, StartPoint.GET_ALL);
        ThreadUser user = new ThreadUser();
        user.setCHAT_ID(CHAT_ID);
        user.setWordList(wordService.getAll());
        user.setUpdate(update);
        user.setFlag(flag);
        user.setBot(Bot.this);
        user.start();
        userList.add(user);

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