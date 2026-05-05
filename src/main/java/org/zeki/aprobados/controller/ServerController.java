package org.zeki.aprobados.controller;

import org.zeki.aprobados.service.UserService;

public class ServerController {

    private ServerController serverInstance;
    private UserService userService;

    public ServerController() {

    }


    public UserService getUserService() {
        if (userService == null) userService = new UserService();
        return userService;
    }

}
