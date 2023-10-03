package data;

import java.io.Serializable;
import java.util.HashMap;

public record ID(String id) implements Serializable {
    public static HashMap<String, ID> ids = new HashMap<>();
    public ID {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be blank");
        }
        if (ids.containsKey(id)) {
            throw new IllegalArgumentException("ID already exists");
        }
    }

    @Override
    public String toString() {
        return id;
    }
}
