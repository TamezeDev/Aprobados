package org.zeki.aprobados.controller;

import org.zeki.aprobados.service.ModuleService;
import org.zeki.aprobados.service.UserService;

public class ServerManager {

    private UserService userService;
    private ModuleService moduleService;

    public ServerManager() {
    }

    public UserService getUserService() {
        if (userService == null) userService = new UserService();
        return userService;
    }

    public ModuleService moduleService() {
        if (moduleService == null) moduleService = new ModuleService();
        return moduleService;
    }

}
