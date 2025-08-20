package main.java.map;

import java.util.HashMap;
import java.util.Map;

public class MapLocation {
	private Map<String, Integer> coords = new HashMap<>();;
	private Aril facingDir;
	
	MapLocation(Map<String,Integer> coords, Aril facingDir){
		this.coords=coords;
		this.facingDir=facingDir;
	}

	public Map<String, Integer> getCoords() {
		return coords;
	}

	public void setCoords(Map<String, Integer> coords) {
		this.coords = coords;
	}

	public String getFacingDir() {
		return facingDir.toString();
	}

	public void setFacingDir(Aril facingDir) {
		this.facingDir = facingDir;
	}
}
