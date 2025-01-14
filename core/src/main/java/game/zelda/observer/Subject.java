package game.zelda.observer;

public interface Subject 
{
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Object event); 
}