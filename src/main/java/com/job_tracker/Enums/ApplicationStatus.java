package com.job_tracker.Enums;

public enum ApplicationStatus {
    DRAFT,
    APPLIED,
    INTERVIEW,
    OFFER,
    REJECTED,
    DELETED;

    public boolean canTransitionTo(
            ApplicationStatus applicationStatus
    )
    {
        return switch (this) {
            case DRAFT ->
                applicationStatus == APPLIED || applicationStatus == DELETED || applicationStatus == REJECTED;
            case APPLIED ->
                applicationStatus == INTERVIEW || applicationStatus == DELETED || applicationStatus == REJECTED;
            case INTERVIEW ->
                applicationStatus == OFFER || applicationStatus == REJECTED || applicationStatus == DELETED;
            case OFFER, REJECTED ->
                applicationStatus == DELETED;
            case DELETED ->
                false;
            default -> false;
        };
    }
}
