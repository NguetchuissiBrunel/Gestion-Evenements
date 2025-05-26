package cm.polytech.poof2.Observeur;


public interface EventObservable {
    void registerObserver(ParticipantObserver o);
    void removeObserver(ParticipantObserver o);
    void notifyObservers(String message);
}