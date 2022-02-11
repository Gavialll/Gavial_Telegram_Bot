package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.IrregularVerb;
import com.bot.gavial_bot.entity.User;
import com.bot.gavial_bot.service.IrregularVerbService;
import com.bot.gavial_bot.service.UserService;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

public class IrregularVerbQuiz {
    public void start(Bot bot, Update update, UserService userService, IrregularVerbService irregularVerbService) throws TelegramApiException {
        User user = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        List<IrregularVerb> irregularVerbList = irregularVerbService.getAll();
        Integer score = user.getQuiz().getScore();
        Integer iterator = user.getQuiz().getIterator();

        if(user.getQuiz().getIterator() == 0){
            Integer random = Random.random(0, irregularVerbList.size() - 1);
            Integer id = new Send(bot).message("Write all forms irregular verb: " + irregularVerbList.get(random).getUkraine().toUpperCase(Locale.ROOT));
            user.getQuiz().setMessageId(id);
            user.getQuiz().setQuestionId(irregularVerbList.get(random).getId());
            iterator++;
            user.getQuiz().setIterator(iterator);
            userService.save(user);
        }else {
            iterator++;
            user.getQuiz().setIterator(iterator);
            String newResult = user.getQuiz().getIrregularVerbAnswer() + " " + update.getMessage().getText() + " ";
            user.getQuiz().setIrregularVerbAnswer(newResult.trim());
            userService.save(user);

            if(user.getQuiz().getIterator() == 4) {
                if(update.hasMessage()) {
                    IrregularVerb irregularVerb = irregularVerbService.get(user.getQuiz().getQuestionId());

                    if(irregularVerb.toString().equals(user.getQuiz().getIrregularVerbAnswer())) {
                        score++;
                        user.getQuiz().setScore(score);
                        bot.execute(EditMessageText
                                .builder()
                                    .chatId(bot.getCHAT_ID()).text("«" + irregularVerb.toString().toUpperCase(Locale.ROOT) + "» ✅")
                                    .messageId(user.getQuiz().getMessageId())
                                .build());
                        user.getQuiz().setIterator(0);
                        user.getQuiz().setIrregularVerbAnswer("");
                        userService.save(user);
                        start(bot, update, userService, irregularVerbService);
                    } else {
                        if(user.getQuiz().getIrregularVerbMaxScore() < score) user.getQuiz().setIrregularVerbMaxScore(score);

                        String[] irregularVerbs = user.getQuiz().getIrregularVerbAnswer().split(" ");
                        String good = "✅";
                        String bad = "❌";
                        String[] action = {bad, bad, bad};
                        if(irregularVerb.getPresent().equals(irregularVerbs[0])) action[0] = good;
                        if(irregularVerb.getPast().trim().equals(irregularVerbs[1])) action[1] = good;
                        if(irregularVerb.getFuture().trim().equals(irregularVerbs[2])) action[2] = good;

                        new Send(bot).message(Message.printResult(score, irregularVerb.getPresent() + " " + irregularVerb.getPast() + " " + irregularVerb.getFuture(), user.getQuiz().getIrregularVerbMaxScore(),irregularVerbs[0] + action[0] + ", " + irregularVerbs[1] + action[1] + ", " + irregularVerbs[2] + action[2]));
                        new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_IRREGULAR_VERB, Button.FINISH);
                        user.getQuiz().clearFields();
                        userService.save(user);
                    }
                }
            }
        }
    }
}
