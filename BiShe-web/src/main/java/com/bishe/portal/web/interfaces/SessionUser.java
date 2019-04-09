package com.bishe.portal.web.interfaces;

import java.util.Set;

public interface SessionUser {
    String getUsername();

    Long getTel();

    Set<String> getPermission();
}
