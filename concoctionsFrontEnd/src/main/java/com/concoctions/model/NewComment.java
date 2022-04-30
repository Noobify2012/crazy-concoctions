package com.concoctions.model;

import lombok.Data;

@Data
public class NewComment {
    private long userId;
    private long drinkId;
    private int ranking;
    private String commentBody;
}
