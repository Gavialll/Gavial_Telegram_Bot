package com.bot.gavial_bot.component;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Message {
//                                               "I'm a bot🦾 for learning English words📖"
    public static final String selectActive =    "⬇️ ⬇️ ⬇️  Select an action  ⬇️ ⬇️ ⬇️";
    public static final String hello = "Hi✋\uD83D\uDC4B✋ \nMy name Gavial \nI'm a bot\uD83E\uDDBE for learning English words\uD83D\uDCD6";

    public static String printResult(int result) {
        return "You have: " + result + "points \uD83C\uDF7E\uD83C\uDF7E\uD83C\uDF7E";
    }

    public static String printQuestion (String question){
        return "Translate: «" + question.toUpperCase(Locale.ROOT) + "»❔";
    }

    public static String answer(int result, int index, int size) {
        return "Result:      " + result + " ✅\n" + "Question: " + index + "/" + size + "\uD83D\uDCC8" + "\n➖➖➖➖➖➖➖➖➖➖➖";
    }

    public static String answer(int index, int size, String answer) {
        return "Answer is incorrect ❌\n" + "Question: " + index + "/" + size + "\uD83D\uDCC8\n" + "Right answer: " + answer.toUpperCase(Locale.ROOT) + "\n➖➖➖➖➖➖➖➖➖➖➖";
    }


}
