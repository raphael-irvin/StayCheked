package com.example.staycheked.model.object;

public class FAQ {

    //Properties
    String category;
    String question;
    String answer;

    //Constructor
    public FAQ(String question, String answer, String category) {
        this.category = category;
        this.question = question;
        this.answer = answer;
    }

    //Getters
    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
