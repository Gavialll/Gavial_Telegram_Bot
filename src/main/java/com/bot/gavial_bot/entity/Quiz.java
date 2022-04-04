package com.bot.gavial_bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer score;
    private Boolean quizStatusChoose;
    private Boolean quizStatusSentences;
    private Boolean quizStatusWords;
    private Boolean quizStatusSprint;
    private Boolean quizStatusIrregularVerb;
    private Integer iterator;
    private Long questionId;
    @Column(nullable = false)
    private Integer sprintMaxScore = 0;
    @Column(nullable = false)
    private Integer chooseMaxScore = 0;
    @Column(nullable = false)
    private Integer irregularVerbMaxScore = 0;
    private Integer messageId = 0;
    @Column(nullable = false)
    private String irregularVerbAnswer = "";
    @Column(nullable = false)
    private Boolean flagImgIrregularVerb = true;
    private Boolean flagChoose;
    private String buttonsText = "";
    private String sentenceText = "";


    public Quiz clearFields(){
        this.score = 0;
        this.iterator = 0;
        this.questionId = 0L;
        this.quizStatusSentences = false;
        this.quizStatusWords = false;
        this.quizStatusSprint = false;
        this.quizStatusIrregularVerb = false;
        this.quizStatusChoose = false;
        this.flagChoose = true;
        this.irregularVerbAnswer = "";
        this.buttonsText = "";
        this.sentenceText = "";
        return Quiz.this;
    }

    public Quiz setQuizStatusSentences(Boolean quizStatus) {
        this.quizStatusSentences = quizStatus;
        return Quiz.this;
    }

    public Quiz setQuizStatusWords(Boolean quizStatus) {
        this.quizStatusWords = quizStatus;
        return Quiz.this;
    }

    public Quiz setQuizStatusIrregularVerb(Boolean quizStatusIrregularVerb) {
        this.quizStatusIrregularVerb = quizStatusIrregularVerb;
        return Quiz.this;
    }

    public Quiz() {
    }
}
