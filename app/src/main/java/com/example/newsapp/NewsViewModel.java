package com.example.newsapp;

public class NewsViewModel {
    public String link; //Image Link
    public String title; //News Title
    public String brief;// News Body
    public NewsViewModel(String title, String link,String brief)
    {
        this.link = link;
        this.title = title;
        this.brief=brief;

    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }
    public String getBrief() {
        return brief;
    }

}
