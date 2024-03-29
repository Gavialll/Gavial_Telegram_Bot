package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.IrregularVerb;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.service.IrregularVerbService;
import com.bot.gavial_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

@Slf4j
public class IrregularVerbQuiz {
    public void start(Bot bot, Update update, UserService userService, IrregularVerbService irregularVerbService) throws TelegramApiException {
        Person person = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        List<IrregularVerb> irregularVerbList = irregularVerbService.getAll();
        Integer score = person.getQuiz().getScore();
        Integer iterator = person.getQuiz().getIterator();

        if(person.getQuiz().getIterator() == 0){
            iterator++;
            if(person.getQuiz().getFlagImgIrregularVerb()) {
                new Send(bot).Image("src/main/resources/Image/ExampleIrregularVerb.jpg");
                person.getQuiz().setFlagImgIrregularVerb(false);
            }
            Integer random = Random.random(0, irregularVerbList.size() - 1);
            Integer id = new Send(bot).message("Write all forms irregular verb: " + Message.upperCaseFirstLetter(irregularVerbList.get(random).getUkraine()));
            person.getQuiz().setMessageId(id);
            person.getQuiz().setQuestionId(irregularVerbList.get(random).getId());
            person.getQuiz().setIterator(iterator);
            userService.save(person);
            log.info("Print -> first Irregular verb: " + iterator);
        }else {
            iterator++;
            person.getQuiz().setIterator(iterator);
            String newResult = person.getQuiz().getIrregularVerbAnswer() + " " + update.getMessage().getText().trim() + " ";
            person.getQuiz().setIrregularVerbAnswer(newResult.trim());
            userService.save(person);

            if(person.getQuiz().getIterator() == 4) {
                if(update.hasMessage()) {
                    IrregularVerb irregularVerb = irregularVerbService.get(person.getQuiz().getQuestionId());
                    String userAnswer = irregularVerb.toString().toLowerCase(Locale.ROOT);
                    String rightAnswer = person.getQuiz().getIrregularVerbAnswer().toLowerCase(Locale.ROOT);

                    if(userAnswer.equals(rightAnswer)) {
                        score++;

                        bot.execute(EditMessageText
                                .builder()
                                    .chatId(bot.getCHAT_ID()).text("«" + Message.upperCaseFirstLetter(userAnswer) + "» ✅")
                                    .messageId(person.getQuiz().getMessageId())
                                .build());

                        person.getQuiz().setScore(score);
                        person.getQuiz().setIterator(0);
                        person.getQuiz().setIrregularVerbAnswer("");

                        userService.save(person);

                        start(bot, update, userService, irregularVerbService);
                        log.info("Print -> next Irregular verb: " + iterator);
                    } else {
                        if(person.getQuiz().getIrregularVerbMaxScore() < score) person.getQuiz().setIrregularVerbMaxScore(score);

                        String[] irregularVerbs = person.getQuiz().getIrregularVerbAnswer().toUpperCase(Locale.ROOT).split(" ");
                        String good = "✅";
                        String bad = "❌";
                        String[] action = {bad, bad, bad};

                        if(irregularVerb.getPresent().toUpperCase(Locale.ROOT).trim().equals(irregularVerbs[0])) action[0] = good;
                        if(irregularVerb.getPast().toUpperCase(Locale.ROOT).trim().equals(irregularVerbs[1])) action[1] = good;
                        if(irregularVerb.getFuture().toUpperCase(Locale.ROOT).trim().equals(irregularVerbs[2])) action[2] = good;

                        String question = irregularVerb.getPresent() + ", " + irregularVerb.getPast() + ", " + irregularVerb.getFuture();
                        String answer = irregularVerbs[0] + action[0] + ", " + irregularVerbs[1] + action[1] + ", " + irregularVerbs[2] + action[2];

                        new Send(bot).message(Message.printResult(score, question, person.getQuiz().getIrregularVerbMaxScore(),answer));
                        new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_IRREGULAR_VERB, Button.FINISH);
                        person.getQuiz().clearFields();
                        userService.save(person);
                        log.info("Finish -> Irregular verb");
                    }
                }
            }
        }
    }
}
