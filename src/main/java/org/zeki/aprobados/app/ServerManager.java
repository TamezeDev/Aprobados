package org.zeki.aprobados.app;

import org.zeki.aprobados.service.ModuleService;
import org.zeki.aprobados.service.TestService;
import org.zeki.aprobados.service.UserService;

public class ServerManager {

    private UserService userService;
    private ModuleService moduleService;
    private TestService testService;

    public UserService getUserService() {
        if (userService == null) userService = new UserService();
        return userService;
    }

    public ModuleService moduleService() {
        if (moduleService == null) moduleService = new ModuleService();
        return moduleService;
    }

    public TestService getTestService() {
        if (testService == null) testService = new TestService();
        return testService;
    }
}
