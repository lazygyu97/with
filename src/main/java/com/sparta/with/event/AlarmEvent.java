package com.sparta.with.event;

import org.springframework.context.ApplicationEvent;

public class AlarmEvent extends ApplicationEvent {
    public AlarmEvent(Object source) {
        super(source);
    }
}
