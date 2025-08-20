package unibo.planner23.model;

import java.util.HashSet;
import java.util.Set;
import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.StepCostFunction;

public class Functions implements ActionsFunction, ResultFunction, StepCostFunction, GoalTest {
	//public static final double MOVECOST = 1.0;
	//public static final double TURNCOST = 1.0;
	
	public static final double MOVE_FORWARD_COST = 1.0;
	public static final double TURN_COST = 1.0;
	// Un costo di 3.1 rende la retromarcia leggermente più "costosa"
	// della sequenza gira, gira, avanti (costo totale 1.0 + 1.0 + 1.0 = 3.0)
	public static final double MOVE_BACKWARD_COST = 3.1; 

//	@Override
//	public double c(Object arg0, Action arg1, Object arg2) {
//		RobotAction action = (RobotAction) arg1;
//		if (action.getAction() == RobotAction.FORWARD || action.getAction() == RobotAction.BACKWARD)
//			return MOVECOST;
//		else
//			return TURNCOST;
//	}
	
	@Override
	public double c(Object state, Action action, Object statePrimed) {
		RobotAction robotAction = (RobotAction) action;
		
		switch(robotAction.getAction()) {
			case RobotAction.FORWARD:
				return MOVE_FORWARD_COST;
			case RobotAction.BACKWARD:
				return MOVE_BACKWARD_COST; // Applica il costo maggiorato
			case RobotAction.TURNLEFT:
			case RobotAction.TURNRIGHT:
				return TURN_COST;
			default:
				// Lancia un'eccezione o ritorna un costo altissimo in caso di azione non prevista
				throw new IllegalArgumentException("Azione non valida per il calcolo del costo");
		}
	}

	@Override
	public Object result(Object arg0, Action arg1) {
		RobotState state   = (RobotState) arg0;
		RobotAction action = (RobotAction) arg1;
		RobotState result;
		
		switch(action.getAction()) {
			case RobotAction.FORWARD:   result = state.forward(); break;
			case RobotAction.BACKWARD:  result = state.backward(); break;
			case RobotAction.TURNLEFT:  result = state.turnLeft(); break;
			case RobotAction.TURNRIGHT: result = state.turnRight(); break;
			default: throw new IllegalArgumentException("Not a valid RobotAction");
		}
		return result;
	}


	@Override 
	public Set<Action> actions(Object arg0) {
		// Converte l'oggetto generico nello stato specifico del robot.
		RobotState state = (RobotState) arg0;
		
		// Ottiene l'istanza della mappa per verificare la presenza di ostacoli.
		RoomMap map = RoomMap.getRoomMap();
		
		// Crea l'insieme (Set) che conterrà le azioni possibili da questo stato.
		Set<Action> possibleActions = new HashSet<>();

		// 1. Le rotazioni sono sempre possibili in quanto non causano collisioni.
		possibleActions.add(RobotAction.lAction); // Gira a sinistra ('l')
		possibleActions.add(RobotAction.rAction); // Gira a destra ('r')

		// 2. L'avanzamento ('w') è possibile solo se non c'è un ostacolo di fronte.
		if (map.canMove(state.getX(), state.getY(), state.getDirection())) {
			possibleActions.add(RobotAction.wAction);
		}

		// La marcia indietro ('s') è possibile solo se non c'è un ostacolo dietro.
		// Per fare questo, prima calcoliamo qual è la direzione opposta a quella attuale.
		RobotState.Direction backwardDirection = state.getBackwardDirection();
		
		// E poi controlliamo se la cella in quella direzione è libera.
		if (map.canMove(state.getX(), state.getY(), backwardDirection)) {
			possibleActions.add(RobotAction.sAction);
		}

		// Restituisce l'insieme di tutte le azioni valide che l'algoritmo può considerare.
		return possibleActions;
	}

	@Override
	public boolean isGoalState(Object arg0) {
		RobotState state = (RobotState) arg0;
		System.out.println("				Functions check if is dirty and not obstacle: " + (unibo.planner23.model.RobotState) arg0);
		if (RoomMap.getRoomMap().isDirty(state.getX(), state.getY()) &&
				!RoomMap.getRoomMap().isObstacle(state.getX(), state.getY())) {
			System.out.println("				Functions isGoalState true : " + (unibo.planner23.model.RobotState) arg0);
			return true;
		}else {
			System.out.println("				Functions isGoalState false: " + (unibo.planner23.model.RobotState) arg0);
			return false;
		}
	}

}
