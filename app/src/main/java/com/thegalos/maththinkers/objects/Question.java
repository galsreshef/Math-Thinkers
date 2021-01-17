package com.thegalos.maththinkers.objects;


import java.util.List;

public class Question {

    String question;
    int correctAnswer;

    List<Integer> wrongAnswer;
    List<String> wrongQuestions;


    String correctQuestion;
    String wrongQuestion;

public Question() {}


    public List<String> getWrongQuestions() {
        return wrongQuestions;
    }
    public List<Integer> getWrongAnswer() {
        return wrongAnswer;
    }
    public String getQuestion() {
        return question;
    }
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    public String getCorrectQuestion() {
        return correctQuestion;
    }
    public String getWrongQuestion() {
        return wrongQuestion;
    }

    public void setWrongQuestions(List<String> wrongQuestions) {
        this.wrongQuestions = wrongQuestions;
    }
    public void setWrongAnswer(List<Integer> wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public void setCorrectQuestion(String correctQuestion) {
        this.correctQuestion = correctQuestion;
    }
    public void setWrongQuestion(String wrongQuestion) {
        this.wrongQuestion = wrongQuestion;
    }

}
