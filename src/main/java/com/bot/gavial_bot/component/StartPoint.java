package com.bot.gavial_bot.component;

public enum StartPoint {
    STUDY_WORDS ("Study words \uD83D\uDCDA", CallbackData.STUDY_WORDS),
    STUDY_SENTENCES ("Study sentences \uD83D\uDCDA", CallbackData.STUDY_SENTENCES),
    QUIZ ("Start quiz", CallbackData.QUIZ),
    GET_ALL ("All words", CallbackData.GET_ALL);

    private final String name;
    private final String callbackData;

    StartPoint(String name, String callbackData) {
        this.name = name;
        this.callbackData = callbackData;
    }

    public String getName() {
        return name;
    }

    public String getCallbackData() {
        return callbackData;
    }
}
