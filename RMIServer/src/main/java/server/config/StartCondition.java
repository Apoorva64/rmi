package server.config;

import java.io.Serializable;

public interface StartCondition extends Serializable {
    boolean isReached();
}