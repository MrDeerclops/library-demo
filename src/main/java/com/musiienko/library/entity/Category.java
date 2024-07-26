package com.musiienko.library.entity;

public enum Category {
    FANTASY("Fantasy"),
    ADVENTURE("Adventure"),
    ROMANCE("Romance"),
    CONTEMPORARY("Contemporary"),
    DYSTOPIAN("Dystopian"),
    MYSTERY("Mystery"),
    HORROR("Horror"),
    THRILLER("Thriller"),
    PARANORMAL("Paranormal"),
    HISTORICAL_FICTION("Historical fiction"),
    SCIENCE_FICTION("Science fiction");

    public final String label;

    Category(String label){
        this.label = label;
    }
}
