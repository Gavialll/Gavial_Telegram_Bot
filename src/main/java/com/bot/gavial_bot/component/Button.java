package com.bot.gavial_bot.component;

public enum Button {
    STUDY_WORDS ("Study words \uD83D\uDCDA", CallbackData.STUDY_WORDS),
    STUDY_SENTENCES ("Study sentences \uD83C\uDF93", CallbackData.STUDY_SENTENCES),
    WRITE_SENTENCES ("Write sentences ✏️", CallbackData.WRITE_SENTENCES),
    CHOOSE_SENTENCES ("Choose sentences \uD83D\uDC46", CallbackData.CHOOSE_SENTENCES),
    STUDY_SPRINT("Sprint \uD83C\uDFC1", CallbackData.STUDY_SPRINT),
    STUDY_IRREGULAR_VERB("Study irregular verb \uD83D\uDCD8", CallbackData.STUDY_IRREGULAR_VERB),
    FINISH("Menu \uD83C\uDFE0", CallbackData.FINISH),
    TRY_AGAIN_IRREGULAR_VERB("Try again 🔄", CallbackData.STUDY_IRREGULAR_VERB),
    TRY_AGAIN_SPRINT("Try again 🔄", CallbackData.STUDY_SPRINT),
    TRY_AGAIN_SENTENCES("Try again 🔄", CallbackData.WRITE_SENTENCES),
    TRY_AGAIN_WORDS("Try again 🔄" , CallbackData.STUDY_WORDS),
    TRY_AGAIN_CHOOSE("Try again 🔄", CallbackData.CHOOSE_SENTENCES);
    private final String name;
    private final String callbackData;

    Button(String name, String callbackData) {
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
