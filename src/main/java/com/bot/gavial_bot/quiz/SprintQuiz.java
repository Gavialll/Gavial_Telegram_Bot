package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.*;
import com.bot.gavial_bot.service.IrregularVerbService;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Slf4j
public class SprintQuiz {

    public void start(Bot bot,
                      Update update,
                      UserService userService,
                      WordService wordService,
                      SentenceService sentenceService,
                      IrregularVerbService irregularVerbService) throws TelegramApiException {
        Person person = userService.getById(Long.parseLong(bot.getCHAT_ID()));

        int score = person.getQuiz().getScore();

        switch(person.getQuiz().getIterator()) {
//        switch(4) {
            case 1:{
                System.out.println("Verification Word");
                if(update.hasMessage()) {
                    String rightAnswer = wordService.getById(person.getQuiz().getQuestionId()).getEnglish().toLowerCase(Locale.ROOT).trim();
                    String userAnswer = update.getMessage().getText().toLowerCase(Locale.ROOT).trim();

                    if(rightAnswer.equals(userAnswer)) {
                        System.out.println("answer correct");
                        score++;
                        person.getQuiz().setScore(score);
                        person.getQuiz().clearSprintFields();
                        userService.save(person);
                    }
                    else {
                        if(score > person.getQuiz().getSprintMaxScore()){
                            person.getQuiz().setSprintMaxScore(score);
                        }

                        new Send(bot).message(Message.printResult(person.getQuiz().getScore(),rightAnswer, person.getQuiz().getSprintMaxScore(), userAnswer));
                        person.getQuiz().clearFields();
                        userService.save(person);
                        new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SPRINT, Button.FINISH);
                        return;
                    }
                }
                break;
            }
            case 2:{
                System.out.println("Verification Sentence");
                if(update.hasMessage()) {
                    String rightAnswer = sentenceService.getById(person.getQuiz().getQuestionId()).getEnglish().toLowerCase(Locale.ROOT).trim();
                    String userAnswer = update.getMessage().getText().toLowerCase(Locale.ROOT).trim();

                    if(rightAnswer.equals(userAnswer)) {
                        System.out.println("answer correct");
                        score++;
                        person.getQuiz().setScore(score);
                        person.getQuiz().clearSprintFields();
                        userService.save(person);
                    } else {
                        if(score > person.getQuiz().getSprintMaxScore()){
                            person.getQuiz().setSprintMaxScore(score);
                        }

                        new Send(bot).message(Message.printResult(person.getQuiz().getScore(),rightAnswer, person.getQuiz().getSprintMaxScore(), userAnswer));
                        person.getQuiz().clearFields();
                        userService.save(person);
                        new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SPRINT, Button.FINISH);
                        return;
                    }
                }
                break;
            }
            case 3:{
                System.out.println("Verification Irregular verb");
                if(update.hasMessage()) {
                    Integer iterator = person.getQuiz().getIteratorTwo();
                    iterator++;
                    String irregularVerbAnswer = update.getMessage().getText().toLowerCase(Locale.ROOT).trim() + " ";

                    person.getQuiz().setIrregularVerbAnswer(person.getQuiz().getIrregularVerbAnswer() + irregularVerbAnswer);
                    person.getQuiz().setIteratorTwo(iterator);
                    userService.save(person);
                    if(iterator == 3) {
                        IrregularVerb irregularVerb = irregularVerbService.get(person.getQuiz().getQuestionId());
                        String rightAnswer = (irregularVerb.getPresent() + " " + irregularVerb.getPast() + " " + irregularVerb.getFuture()).trim().toLowerCase(Locale.ROOT);
                        String userAnswer = person.getQuiz().getIrregularVerbAnswer().trim().toLowerCase(Locale.ROOT);

                        if(rightAnswer.equals(userAnswer)){
                            score++;
                            person.getQuiz().setScore(score);
                            person.getQuiz().clearSprintFields();
                            userService.save(person);
                        } else {
                            if(score > person.getQuiz().getSprintMaxScore()){
                                person.getQuiz().setSprintMaxScore(score);
                            }

                            new Send(bot).message(Message.printResult(person.getQuiz().getScore(),rightAnswer, person.getQuiz().getSprintMaxScore(), userAnswer));
                            person.getQuiz().clearFields();
                            userService.save(person);
                            new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SPRINT, Button.FINISH);
                            return;
                        }
                    }else return;
                }
                break;
            }
            case 4:{
                System.out.println("Verification Choose sentence");
                if(person.getQuiz().getFlagChoose() == false) {
                    if(update.hasCallbackQuery()) {
                        if(person.getQuiz().getFlagChoose() == false) {
                            Quiz quiz = person.getQuiz();
                            List<String> buttonsList = Arrays.asList(quiz.getButtonsText().substring(1, quiz.getButtonsText().length() - 1).split(", "));
                            String callBack = update.getCallbackQuery().getData();

                            for(int i = 0; i < buttonsList.size(); i++) {
                                if(buttonsList.get(i).trim().equals(callBack.trim())) {
                                    if(callBack.contains("✅")) {
                                        String word = callBack.substring(0, callBack.length() - 1);
                                        buttonsList.set(i, word);

                                        StringBuilder result = new StringBuilder();
                                        String[] arr = quiz.getSentenceText().split(" ");
                                        for(String element : arr) {
                                            if(element.equals(word)) continue;
                                            result.append(element).append(" ");
                                        }
                                        quiz.setSentenceText(result.toString());
                                    } else {
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
                                    if(count == buttonsList.size()) {
                                        quiz.setScore(quiz.getScore() + 1);
                                        Sentence sentence = sentenceService.getAll().get(quiz.getIteratorTwo());
                                        if(quiz.getSentenceText().trim().equals(sentence.getEnglish())) {
                                            DeleteMessage deleteMessage = new DeleteMessage();
                                            deleteMessage.setChatId(bot.getCHAT_ID());
                                            deleteMessage.setMessageId(person.getQuiz().getMessageId());
                                            bot.execute(deleteMessage);

                                            person.getQuiz().setFlagChoose(true);
                                            userService.save(person);
                                        } else {
                                            DeleteMessage deleteMessage = new DeleteMessage();
                                            deleteMessage.setChatId(bot.getCHAT_ID());
                                            deleteMessage.setMessageId(person.getQuiz().getMessageId());
                                            bot.execute(deleteMessage);

                                            if(score > person.getQuiz().getSprintMaxScore()){
                                                person.getQuiz().setSprintMaxScore(score);
                                            }

                                            new Send(bot).message(Message.printResult(person.getQuiz().getScore(),
                                                    sentence.getEnglish(),
                                                    person.getQuiz().getSprintMaxScore(),
                                                    quiz.getSentenceText()));
                                            new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SPRINT, Button.FINISH);
                                            person.getQuiz().clearFields();
                                            userService.save(person);
                                            log.info("Finish Choose quiz");
                                            return;
                                        }
                                    }
                                }
                            }
                            if(! person.getQuiz().getFlagChoose()) {
                                bot.execute(EditMessageText.builder().chatId(bot.getCHAT_ID()).text(Message.printQuestion(person.getQuiz().getIrregularVerbAnswer()) + "\nYou choose: " + person.getQuiz().getSentenceText()).replyMarkup(new Keyboard(bot).createChooseButton(buttonsList)).messageId(person.getQuiz().getMessageId()).build());
                                userService.save(person);
                                return;
                            }
                        }
                    }
                }
                break;
            }
        }

        System.gc();

        person.getQuiz().setIterator(Random.random(1, 4));
        userService.save(person);

        switch(person.getQuiz().getIterator()){
//        switch(4){
            case 1:{
                System.out.println("Word");
                Word word = wordService.getAll().get(Random.random(0, wordService.getAll().size()));
                new Send(bot).message(Message.printQuestion(word.getUkraine()));

                person.getQuiz().setQuestionId(word.getId());
                userService.save(person);
            break;
            }
            case 2:{
                System.out.println("Sentence");
                Sentence sentence = sentenceService.getAll().get(Random.random(0, sentenceService.getAll().size()-1));
                new Send(bot).message(Message.printQuestion(sentence.getUkraine()));

                person.getQuiz().setQuestionId(sentence.getId());
                userService.save(person);
                break;
            }
            case 3:{
                System.out.println("Irregular verb");
                IrregularVerb irregularVerb = irregularVerbService.getAll().get(Random.random(0, irregularVerbService.getAll().size()-1));
                new Send(bot).message("Write all forms irregular verb: " + Message.upperCaseFirstLetter(irregularVerb.getUkraine()));
                person.getQuiz().setQuestionId(irregularVerb.getId());
                userService.save(person);
                break;
            }
            case 4:{
                System.out.println("Choose sentence");
                printSentence(bot, sentenceService.getAll(), person, userService);
                break;
            }
        }
    }
    public void printSentence(Bot bot, List<Sentence> sentenceList, Person person, UserService userService){
        person.getQuiz().setSentenceText("");
        int random = Random.random(0,sentenceList.size()-1);
        Sentence sentence = sentenceList.get(random);
        List<String> words = Arrays.asList(sentence.getEnglish().split(" "));
        Collections.shuffle(words);

        Keyboard keyboard = new Keyboard(bot).printChooseButton(words, sentence.getUkraine());
        person.getQuiz().setButtonsText(words.toString());
        person.getQuiz().setIteratorTwo(random);
        person.getQuiz().setIrregularVerbAnswer(sentence.getUkraine());
        person.getQuiz().setMessageId(keyboard.getKeyboardId());
        person.getQuiz().setFlagChoose(false);
        userService.save(person);
    }
}
