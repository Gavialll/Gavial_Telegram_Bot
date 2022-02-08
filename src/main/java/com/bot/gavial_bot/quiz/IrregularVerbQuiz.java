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
        if(update.hasMessage()){
            IrregularVerb irregularVerb = irregularVerbService.get(user.getQuiz().getQuestionId());
            if(update.getMessage().getText().equals(irregularVerb.getPresent() + " " + irregularVerb.getPast() + " " + irregularVerb.getFuture())){
                score++;
                user.getQuiz().setScore(score);
                bot.execute(EditMessageText
                        .builder()
                        .chatId(bot.getCHAT_ID())
                        .text("«" + update.getMessage().getText().toUpperCase(Locale.ROOT) + "» ✅")
                        .messageId(user.getQuiz().getMessId())
                        .build());
                userService.save(user);
            }else {
                if(user.getQuiz().getIrregularVerbMaxScore() < score){
                    user.getQuiz().setIrregularVerbMaxScore(score);
                }
                new Send(bot).message(Message.printResult(score, irregularVerb.getPresent() + " " + irregularVerb.getPast() + " " + irregularVerb.getFuture(), user.getQuiz().getIrregularVerbMaxScore()));
                new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_IRREGULAR_VERB, Button.FINISH);
                user.getQuiz().clearFields();
                userService.save(user);
                return;
            }
        }

        Integer random = Random.random(0, irregularVerbList.size() -1);
        Integer id = new Send(bot).message("Write all forms irregular verb: " + irregularVerbList.get(random).getUkraine().toUpperCase(Locale.ROOT));
        user.getQuiz().setMessId(id);
        user.getQuiz().setQuestionId(irregularVerbList.get(random).getId());
        userService.save(user);
    }
}
