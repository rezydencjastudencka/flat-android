package pl.rpieja.flat.util;

@FunctionalInterface
public interface Callable<Param> {
    void onCall(Param param);
}