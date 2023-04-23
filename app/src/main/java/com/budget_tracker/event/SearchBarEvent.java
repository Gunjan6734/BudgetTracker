package com.budget_tracker.event;

public class SearchBarEvent {
    public enum EventType {
        Filter,
        Search_Submit
    }

    EventType eventType;
    String searchText = "";
    String uniqueID = "";

    public SearchBarEvent(EventType eventType, String searchText, String uniqueID) {
        this.eventType = eventType;
        this.searchText = searchText;
        this.uniqueID = uniqueID;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getSearchText() {
        return searchText;
    }

    public String getUniqueID() {
        return uniqueID;
    }
}

