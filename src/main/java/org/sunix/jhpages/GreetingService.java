package org.sunix.jhpages;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String greeting(String name) {
        return "HELLO sssss " + name.toUpperCase();
    }

}
