package main.java;
 
import java.util.List;
import aima.core.agent.Action;
import unibo.basicomm23.utils.CommUtils;
import unibo.planner23.Planner23Util;


public class MainPlannerdemo {
private Planner23Util planner;

	protected void println(String m){
		System.out.println(m);
	}

 	public void doMentalMap() {
 		try {
 		   planner = new Planner23Util();
 		   planner.initAI();
 		   CommUtils.outgreen("Map afer init");
 		   planner.showMap(); 
 		    
 		   //planner.doPathOnMap("[l, w, w, w, w, w, w]");
 		   for( int k=1;k<=2;k++) {
 			   CommUtils.outgreen("BORDO SINITRO / DESTRO: Muovo avanti 4 w");
	 		   for( int i=0; i<4; i++) {
	 			  planner.doMove("w");
	 		   }
	 		  CommUtils.outgreen("TurnLeft l ");
	 		  planner.doMove("l");
	 		  planner.showCurrentRobotState(); 

	 		  CommUtils.outgreen("BORDO INFERIORE / SUPERIORE: Muovo avanti 4 w");
	 		   for( int i=0; i<6; i++) {
	 			  planner.doMove("w");
	 		   }	
		 		planner.showCurrentRobotState(); 

		 		CommUtils.outgreen("TurnLeft l (UP / DOWN)  ");
		 		planner.doMove("l");	 		   
 		   }
 		  planner.showCurrentRobotState(); 
 		  CommUtils.outgreen("move robot to 2,3  ");
 		  moveToPos(2,3);
 		   
 		  planner.saveRoomMap("map0");
 		} catch ( Exception e) {
			//e.printStackTrace()
		}
	}

 	protected void moveToPos(int x, int y) throws Exception {
 		planner.setGoal(x,y);
 		List<Action> plan = planner.doPlan();
 		CommUtils.outblue( "moveToPos plan=" + plan.toString() );
 		
 		String planCompact = planner.doPlanCompact();
 		CommUtils.outblue( "moveToPos compact plan="+planCompact.toString() );
 		
 		planner.doPathOnMap(planCompact);
 		//planner.showMap();
 		CommUtils.outmagenta( "moveToPos final map"  );
 		planner.showCurrentRobotState();
 	}
 
 
	public static void main( String[] args)   {
		
 		MainPlannerdemo appl = new MainPlannerdemo( );
		appl.doMentalMap();

	}

}
