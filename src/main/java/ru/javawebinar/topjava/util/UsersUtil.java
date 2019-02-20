package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> USER_LIST = Arrays.asList(
            new User(null, "First User Name", "hoba@go.com", "PaSsWoRd", Role.ROLE_ADMIN),
            new User(null, "Second User Name", "joy@bonn.gov", "pAsSwOrD", Role.ROLE_USER),
            new User(null, "Third User Name", "felicia@atlantic.com", "PASSWORD", Role.ROLE_USER),
            new User(null, "Different User Name", "yo@yo.su", "password", Role.ROLE_USER)
    );
}
