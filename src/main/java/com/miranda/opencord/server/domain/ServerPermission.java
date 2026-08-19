package com.miranda.opencord.server.domain;

public enum ServerPermission {
    ADMINISTRATOR(1L << 0),    // 1
    MANAGE_SERVER(1L << 1),    // 2
    MANAGE_ROLES(1L << 2),     // 4
    KICK_MEMBERS(1L << 3),     // 8
    BAN_MEMBERS(1L << 4),      // 16
    CREATE_INVITE(1L << 5),    // 32
    MANAGE_INVITES(1L << 6),   // 64
    MANAGE_CHANNELS(1L << 7);  // 128

    private final long value;

    ServerPermission(long value) {
        this.value = value;
    }

    public long getValue() {
        return this.value;
    }

    public static boolean hasPermission(long userPermissions, ServerPermission permission) {
        if ((userPermissions & ADMINISTRATOR.getValue()) == ADMINISTRATOR.getValue()) {
            return true;
        }
        return (userPermissions & permission.getValue()) == permission.getValue();
    }
}
