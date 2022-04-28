package com.bot.gavial_bot.controller;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.entity.Quiz;
import com.bot.gavial_bot.service.IrregularVerbService;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
@Data
public class Bot extends TelegramLongPollingBot {
    private final SentenceService sentenceService;
    private final IrregularVerbService irregularVerbService;
    private final WordService wordService;
    private final UserService userService;
    private String CHAT_ID;

    public Bot(SentenceService sentenceService, IrregularVerbService irregularVerbService, WordService wordService, UserService userService) {
        this.sentenceService = sentenceService;
        this.irregularVerbService = irregularVerbService;
        this.wordService = wordService;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().getText().equals("/start")) {
            CHAT_ID = update.getMessage().getChatId().toString();
            startBot(update);
        }
        if(update.hasMessage()) {
            CHAT_ID = update.getMessage().getChatId().toString();
        }
        if(update.hasCallbackQuery()) {
            CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
            Person person = userService.getById(Long.parseLong(CHAT_ID));

            switch(update.getCallbackQuery().getData()) {
                case "/studyWords": {
                    log.info("Start -> Word quiz");
                    person.getQuiz().clearFields();
                    person.getQuiz().setQuizStatusWords(true);
                    break;
                }
                case "/irregularVerb": {
                    log.info("Start -> Irregular verb quiz");
                    person.getQuiz().clearFields();
                    person.getQuiz().setQuizStatusIrregularVerb(true);
                    break;
                }
                case "/studySentence": {
                    person.getQuiz().clearFields();
                    execute(EditMessageText.builder()
                            .chatId(getCHAT_ID())
                            .text("Select action")
                            .replyMarkup(new Keyboard(Bot.this)
                                    .createKeyboard(Button.CHOOSE_SENTENCES, Button.WRITE_SENTENCES))
                            .messageId(person.getQuiz()
                                    .getMessageId())
                            .build());
                    break;
                }
                case "/writeSentence": {
                    log.info("Start -> Sentence quiz");
                    person.getQuiz().clearFields();
                    person.getQuiz().setQuizStatusSentences(true);
                    break;
                }
                case "/chooseSentence": {
                    log.info("Start -> Choose quiz");
                    person.getQuiz().clearFields();
                    person.getQuiz().setQuizStatusChoose(true);
                    break;
                }
                case "/studySprint": {
                    log.info("Start -> Sprint quiz");
                    person.getQuiz().clearFields();
                    person.getQuiz().setQuizStatusSprint(true);
                    break;
                }
                case "/finish": {
                    person.getQuiz().clearFields();
                    Keyboard keyboard = new Keyboard(Bot.this).printMenu();
                    person.getQuiz().setMessageId(keyboard.getKeyboardId());
                    break;
                }
            }
            userService.save(person);
        }
        Person person = userService.getById(Long.parseLong(CHAT_ID));

        new SelectActions(Bot.this, update).select(person);
    }

    private void startBot(Update update) throws TelegramApiException {
        new Sticker(Bot.this).send("16");
        Long chatId = update.getMessage().getChatId();
        Person person;
        if(!userService.hasUser(chatId)) {
            person = new Person(chatId, new Quiz().clearFields());
            log.info("Create new user: " + person);

        } else {
            person = userService.getById(Long.parseLong(CHAT_ID));
            person.getQuiz().clearFields();
            log.info("Get user:  " + person);
        }
        Keyboard keyboard = new Keyboard(Bot.this).printMenu();
        person.getQuiz().setMessageId(keyboard.getKeyboardId());
        userService.save(person);
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