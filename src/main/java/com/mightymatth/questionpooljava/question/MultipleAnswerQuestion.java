package com.mightymatth.questionpooljava.question;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by matth on 21.02.16..
 */
public class MultipleAnswerQuestion extends BaseQuestion {
    LinkedList<answerPair> answerList = new LinkedList<>();


    public MultipleAnswerQuestion(String questionText, LinkedList<answerPair> answerList) {
        super(questionText);
        this.answerList = answerList;
    }

    public MultipleAnswerQuestion(String questionText, String questionExplanation, LinkedList<answerPair> answerList) {
        super(questionText, questionExplanation);
        this.answerList = answerList;
    }

    public void addAnswer (String answer, boolean isCorrect) {
        answerList.add(new answerPair(answer, isCorrect));
    }

    public LinkedList<answerPair> shuffleAnswers() {
        LinkedList<answerPair> shuffledAnswerList = new LinkedList<>(answerList);
        Collections.shuffle(shuffledAnswerList);
        return shuffledAnswerList;
    }

    public LinkedList<answerPair> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(LinkedList<answerPair> answerList) {
        this.answerList = answerList;
    }

    public static class answerPair {
        String answer;
        boolean isCorrect;

        public answerPair(String answer, boolean isCorrect) {
            this.answer = answer;
            this.isCorrect = isCorrect;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public boolean getIsCorrect() {
            return isCorrect;
        }

        public void setIsCorrect(boolean isCorrect) {
            this.isCorrect = isCorrect;
        }


    }
}


