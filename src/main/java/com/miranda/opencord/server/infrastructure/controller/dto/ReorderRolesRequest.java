package com.miranda.opencord.server.infrastructure.controller.dto;

import java.util.List;
import java.util.UUID;

public record ReorderRolesRequest(
        List<RolePositionItem> roles
) {
    public record RolePositionItem(
            UUID id,
            Integer position
    ) {}
}
