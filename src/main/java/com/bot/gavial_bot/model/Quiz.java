package com.bot.gavial_bot.model;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Locale;

@Entity
@Data
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer score;
    private Boolean quizStatusSentences;
    private Boolean quizStatusWords;
    private Integer iterator;
    private Long questionId;

    public Quiz clearFields(){
        this.score = 0;
        this.iterator = 0;
        this.questionId = 0L;
        this.quizStatusSentences = false;
        this.quizStatusWords = false;
        return Quiz.this;
    }

    public void sentences(Bot bot, Update update, SentenceService sentenceService, UserService userService) throws TelegramApiException {
        User user = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        if(update.hasMessage()) {
            int score = userService.getById(Long.parseLong(bot.getCHAT_ID())).getQuiz().getScore();
            int questionsSize = 5;
            if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(sentenceService.getById(user.getQuiz().getQuestionId()).getEnglish())) {
                score++;
                user.getQuiz().setScore(score);
                new Send(bot).message(Message.answer(user.getQuiz().getScore(), user.getQuiz().getIterator(), questionsSize));
                if(user.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.hello, StartPoint.STUDY_SENTENCES, StartPoint.STUDY_WORDS);
                    user.getQuiz().clearFields();
                    userService.save(user);
                    return;
                }
            } else {
                new Send(bot).message(Message.answer(user.getQuiz().getIterator(), questionsSize, sentenceService.getById(user.getQuiz().getQuestionId()).getEnglish()));
                if(user.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.hello, StartPoint.STUDY_SENTENCES, StartPoint.STUDY_WORDS);
                    user.getQuiz().clearFields();
                    userService.save(user);
                    return;
                }
            }
        }

        Integer iterator = user.getQuiz().getIterator();
        List<Sentence> sentenceList = sentenceService.getAll();
        Integer random = Random.random(0, sentenceList.size() -1);
        Sentence sentence = sentenceList.get(random);

        new Send(bot).message(Message.printQuestion(sentence.getUkraine()));
        user.getQuiz().setQuestionId(sentence.getId());
        iterator++;
        user.getQuiz().setIterator(iterator);

        userService.save(user);
    }

    public void words(Bot bot, Update update, WordService wordService, UserService userService) throws TelegramApiException {
        User user = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        if(update.hasMessage()) {
            int score = userService.getById(Long.parseLong(bot.getCHAT_ID())).getQuiz().getScore();
            int questionsSize = 5;
            if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(wordService.getById(user.getQuiz().getQuestionId()).getEnglish())) {
                score++;
                    user.getQuiz().setScore(score);
                    new Send(bot).message(Message.answer(user.getQuiz().getScore(), user.getQuiz().getIterator(), questionsSize));
                    if(user.getQuiz().getIterator() >= questionsSize) {
                        new Sticker(bot).send(score);
                        new Send(bot).message(Message.printResult(score));
                        new Keyboard(bot).printButton(Message.hello, StartPoint.STUDY_SENTENCES, StartPoint.STUDY_WORDS);
                        user.getQuiz().clearFields();
                        userService.save(user);
                        return;
                    }
                } else {
                    new Send(bot).message(Message.answer(user.getQuiz().getIterator(), questionsSize, wordService.getById(user.getQuiz().getQuestionId()).getEnglish()));
                    if(user.getQuiz().getIterator() >= questionsSize) {
                        new Sticker(bot).send(score);
                        new Send(bot).message(Message.printResult(score));
                        new Keyboard(bot).printButton(Message.hello, StartPoint.STUDY_SENTENCES, StartPoint.STUDY_WORDS);
                        user.getQuiz().clearFields();
                        userService.save(user);
                        return;
                    }
                }
            }

            Integer iterator = user.getQuiz().getIterator();
            List<Word> sentenceList = wordService.getAll();
            Integer random = Random.random(0, sentenceList.size() -1);
            Word word = sentenceList.get(random);

            new Send(bot).message(Message.printQuestion(word.getUkraine()));
            user.getQuiz().setQuestionId(word.getId());
            iterator++;
            user.getQuiz().setIterator(iterator);
            userService.save(user);
    }

    public Quiz setQuizStatusSentences(Boolean quizStatus) {
        this.quizStatusSentences = quizStatus;
        return Quiz.this;
    }

    public Quiz setQuizStatusWords(Boolean quizStatus) {
        this.quizStatusWords = quizStatus;
        return Quiz.this;
    }

    public Quiz() {
    }
}
