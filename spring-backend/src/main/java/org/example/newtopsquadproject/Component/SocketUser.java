package org.example.newtopsquadproject.Component;

import java.security.Principal;

public class SocketUser implements Principal {
    //This class implements Principal so can set it as the principal for the websocket session
    public SocketUser(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return null;
    }
}
