package com.mightymatth.questionpooljava.question;

/**
 * Created by matth on 21.02.16..
 */
public class BaseQuestion {
    String questionText;
    String questionExplanation;


    public BaseQuestion(String questionText) {
        this.questionText = questionText;
        questionExplanation = "";
    }

    public BaseQuestion(String questionText, String questionExplanation) {
        this.questionText = questionText;
        this.questionExplanation = questionExplanation;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionExplanation() {
        return questionExplanation;
    }

    public void setQuestionExplanation(String questionExplanation) {
        this.questionExplanation = questionExplanation;
    }

    public void appendQuestionExplanation (String explToAppend) {
        questionExplanation = (questionExplanation + "\n" + explToAppend).trim();
    }
}
