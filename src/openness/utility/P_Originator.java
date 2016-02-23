package openness.utility;

public class P_Originator {
	private String state;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public P_Memento saveStateToMemento() {
		return new P_Memento(state);
	}

	public void getStateFromMemento(P_Memento memento) {
		state = memento.getState();
	}
}
