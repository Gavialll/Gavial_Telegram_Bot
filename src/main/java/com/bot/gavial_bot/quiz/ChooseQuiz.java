package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.entity.Quiz;
import com.bot.gavial_bot.entity.Sentence;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ChooseQuiz {

    public void start(Bot bot, Update update, SentenceService sentenceService, UserService userService) throws TelegramApiException {
        log.info("Start Choose quiz");

        List<Sentence> sentenceList = sentenceService.getAll();
        Person person = userService.getById(Long.parseLong(bot.getCHAT_ID()));

        if(person.getQuiz().getFlagChoose()) {
            printSentence(bot, sentenceList, person, userService);
            log.info("Print first question");
        }else {
            if(update.hasCallbackQuery()) {
                Quiz quiz = person.getQuiz();
                List<String> buttonsList = Arrays.asList(quiz
                        .getButtonsText()
                        .substring(1, quiz.getButtonsText().length() - 1)
                        .split(", "));
                String callBack = update.getCallbackQuery().getData();

                for(int i = 0; i < buttonsList.size(); i++) {
                    if(buttonsList.get(i).trim().equals(callBack.trim())){
                        if(callBack.contains("✅")){
                            String word = callBack.substring(0, callBack.length()-1);
                            buttonsList.set(i, word);

                            StringBuilder result = new StringBuilder();
                            String[] arr = quiz.getSentenceText().split(" ");
                            for(String element : arr) {
                                if(element.equals(word)) continue;
                                result.append(element).append(" ");
                            }
                            quiz.setSentenceText(result.toString());
                        }else {
                            for(int j = 0; j < buttonsList.size(); j++) {
                                if(buttonsList.get(i).trim().equals(callBack.trim())) {
                                    buttonsList.set(i, callBack + "✅");
                                    break;
                                }
                            }
                            quiz.setSentenceText(quiz.getSentenceText() + callBack + " ");
                        }
                        quiz.setButtonsText(buttonsList.toString());

                        int count = 0;
                        for(String button : buttonsList) {
                            if(button.contains("✅")) {
                                count++;
                            }
                        }
                        if(count == buttonsList.size()){
                            quiz.setScore(quiz.getScore() + 1);
                            Sentence sentence = sentenceList.get(quiz.getIterator());
                            if(quiz.getSentenceText().trim().equals(sentence.getEnglish())){
                                DeleteMessage deleteMessage = new DeleteMessage();
                                deleteMessage.setChatId(bot.getCHAT_ID());
                                deleteMessage.setMessageId(person.getQuiz().getMessageId());
                                bot.execute(deleteMessage);

                                printSentence(bot, sentenceList, person, userService);
                                log.info("Print new question");
                                return;
                            }
                            else {
                                DeleteMessage deleteMessage = new DeleteMessage();
                                deleteMessage.setChatId(bot.getCHAT_ID());
                                deleteMessage.setMessageId(person.getQuiz().getMessageId());
                                bot.execute(deleteMessage);

                                if(quiz.getScore() > quiz.getChooseMaxScore()){
                                    quiz.setChooseMaxScore(quiz.getScore());
                                }

                                new Send(bot).message(Message.printResult(quiz.getScore(),sentence.getEnglish(), quiz.getChooseMaxScore(), quiz.getSentenceText()));
                                new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_CHOOSE, Button.FINISH);
                                userService.save(person);
                                log.info("Finish Choose quiz");
                                return;
                            }
                        }
                    }
                }

                bot.execute(EditMessageText.builder()
                        .chatId(bot.getCHAT_ID())
                        .text(Message.printQuestion(quiz.getIrregularVerbAnswer()) + "\nYou choose: " +quiz.getSentenceText())
                        .replyMarkup(new Keyboard(bot)
                                .createChooseButton(buttonsList))
                        .messageId(person.getQuiz()
                                .getMessageId())
                        .build());

                userService.save(person);
            }
        }
    }

    public void printSentence(Bot bot, List<Sentence> sentenceList, Person person, UserService userService){
        person.getQuiz().setSentenceText("");
        int random = Random.random(0,sentenceList.size());
        Sentence sentence = sentenceList.get(random);
        List<String> words = Arrays.asList(sentence.getEnglish().split(" "));
        Collections.shuffle(words);

        Keyboard keyboard = new Keyboard(bot).printChooseButton(words, sentence.getUkraine());
        person.getQuiz().setButtonsText(words.toString());
        person.getQuiz().setIterator(random);
        person.getQuiz().setIrregularVerbAnswer(sentence.getUkraine());
        person.getQuiz().setMessageId(keyboard.getKeyboardId());
        person.getQuiz().setFlagChoose(false);
        userService.save(person);
    }
}
