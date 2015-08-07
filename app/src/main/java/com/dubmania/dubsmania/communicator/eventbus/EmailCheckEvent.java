package com.dubmania.dubsmania.communicator.eventbus;

/**
 * Created by hardik.parekh on 8/2/2015.
 */
public class EmailCheckEvent {
    private String email;

    public EmailCheckEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
