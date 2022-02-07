package com.bot.gavial_bot.component;

public enum StartPoint {
    STUDY_WORDS ("Study words \uD83D\uDCDA", CallbackData.STUDY_WORDS),
    STUDY_SENTENCES ("Study sentences \uD83D\uDCDA", CallbackData.STUDY_SENTENCES),
    STUDY_SPRINT("Study sprint \uD83D\uDCDA", CallbackData.STUDY_SPRINT),
    FINISH("Finish ⏹", CallbackData.FINISH),
    TRY_AGAIN("Try again \uD83D\uDD04", CallbackData.TRY_AGAIN);

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
