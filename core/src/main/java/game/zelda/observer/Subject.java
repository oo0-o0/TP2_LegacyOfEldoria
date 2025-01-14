package game.zelda.observer;

public interface Subject {
	
	void subscribe(Observer observer);
	
	void unsubscribe(Observer observer);
	
	public void notifyObservers(Observer observer);
}

//Extender o observer para player (?) para observar itens e avisar inventory
