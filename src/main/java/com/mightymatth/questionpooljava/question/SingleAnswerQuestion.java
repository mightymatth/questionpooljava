package com.mightymatth.questionpooljava.question;

/**
 * Created by matth on 21.02.16..
 */
public class SingleAnswerQuestion extends BaseQuestion {
    String answer;

    public SingleAnswerQuestion(String questionText, String answer) {
        super(questionText);
        this.answer = answer;
    }

    public SingleAnswerQuestion(String questionText, String questionExplanation, String answer) {
        super(questionText, questionExplanation);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
