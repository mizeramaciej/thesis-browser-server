package com.koczy.kurek.mizera.thesisbrowser.model;

import java.util.Date;

public class ThesisFilters {

    private String author;
    private String title;
    private int positionFrom;
    private int positionTo;
    private String institution;
    private String keyWords;
    private int quotationNumber;
    private Date dateFrom;
    private Date dateTo;

    public ThesisFilters() {
    }

    public ThesisFilters(String author, String title, int positionFrom, int positionTo, String institution,
                         String keyWords, int quotationNumber, Date dateFrom, Date dateTo) {
        this.author = author;
        this.title = title;
        this.positionFrom = positionFrom;
        this.positionTo = positionTo;
        this.institution = institution;
        this.keyWords = keyWords;
        this.quotationNumber = quotationNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPositionFrom() {
        return positionFrom;
    }

    public void setPositionFrom(int positionFrom) {
        this.positionFrom = positionFrom;
    }

    public int getPositionTo() {
        return positionTo;
    }

    public void setPositionTo(int positionTo) {
        this.positionTo = positionTo;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public int getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(int quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}