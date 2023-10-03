package server.config;

public interface StopCondition extends java.io.Serializable{
    boolean isReached();
}
