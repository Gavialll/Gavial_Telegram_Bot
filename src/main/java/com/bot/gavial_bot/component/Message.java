package com.bot.gavial_bot.component;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Message {
    public static final String selectActive = "Select an action️";
    public static final String hello =
            "Hi✋\uD83D\uDC4B✋ \n" +
            "My name Gavial \n" +
            "I'm a bot\uD83E\uDDBE " +
            "for learning English words\uD83D\uDCD6";

    public static String printResult(int result) {
        return "You result: " + result + " \uD83C\uDFC6\uD83C\uDFC6\uD83C\uDFC6";
    }

    public static String printResult(Integer score, String rightAnswer, Integer maxScore, String youAnswer){
        return "You result: " + score + " \uD83C\uDFC5\n" +
                "Max result: " + maxScore + " \uD83C\uDFC6\n" +
                "You answer: "+ upperCaseFirstLetter(youAnswer) + "\n" +
                "Right answer: " + upperCaseFirstLetter(rightAnswer);
    }
    public static String printResult(Integer score, String rightAnswer, Integer maxScore){
        return "You result: " + score + " \uD83C\uDFC5\n" +
                "You answer incorrect ❌\n" +
                "Right answer: «" + upperCaseFirstLetter(rightAnswer) + "»\n" +
                "Max result: " + maxScore+ " \uD83C\uDFC6";
    }

    public static String printQuestion (String question){
        return "Translate: «" + upperCaseFirstLetter(question) + "»";
    }

    public static String answer(int result, int index, int size) {
        return "Result:       " + result + " ✅\n" +
                "Question: " + index + "/" + size + "\uD83D\uDCC8" +
                "\n➖➖➖➖➖➖➖➖➖➖➖";
    }

    public static String answer(int index, int size, String answer) {
        return "Answer is incorrect ❌\n" +
                "Question: " + index + "/" + size + "\uD83D\uDCC8\n" +
                "Right answer: " + upperCaseFirstLetter(answer) +
                "\n➖➖➖➖➖➖➖➖➖➖➖";
    }

    public static String upperCaseFirstLetter(String str){
        String result = "" + str.charAt(0);
        result = result.toUpperCase();
        result += str.substring(1).toLowerCase(Locale.ROOT);
        return result;
    }
}
