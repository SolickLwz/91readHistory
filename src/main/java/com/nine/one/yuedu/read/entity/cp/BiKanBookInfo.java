package com.nine.one.yuedu.read.entity.cp;

public class BiKanBookInfo {

    private int id;
    private String name;
    private String aliasName;
    private String foreignName;
    private String author;
    private String translator;
    private String briefDescription;
    private String promptDescription;
    private String keywords;
    private String coverImg;
    private int bookType;
    private int bookMediaType;
    private int progress;
    private String chapterCount;
    private long cpUpdateTime;
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public String getPromptDescription() {
        return promptDescription;
    }

    public void setPromptDescription(String promptDescription) {
        this.promptDescription = promptDescription;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getBookType() {
        return bookType;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }

    public int getBookMediaType() {
        return bookMediaType;
    }

    public void setBookMediaType(int bookMediaType) {
        this.bookMediaType = bookMediaType;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(String chapterCount) {
        this.chapterCount = chapterCount;
    }

    public long getCpUpdateTime() {
        return cpUpdateTime;
    }

    public void setCpUpdateTime(long cpUpdateTime) {
        this.cpUpdateTime = cpUpdateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
