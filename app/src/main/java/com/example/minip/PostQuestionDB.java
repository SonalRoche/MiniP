package com.example.minip;

public class PostQuestionDB {

    private String email;
    private String name;
    private String topic;
    private String question;
    private String  flatno;
    private String block;
    private String comment;


    public PostQuestionDB() {
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getFlatno()
    {
        return flatno;
    }

    public void setFlatno(String flatno)
    {
        this.flatno = flatno;
    }

    public String getBlock()
    {
        return block;
    }

    public void setBlock(String block)
    {
        this.block = block;
    }
}
