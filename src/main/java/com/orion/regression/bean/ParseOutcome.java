package com.orion.regression.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseOutcome {
	List<Redundancy> redundancies = null;
	Map<String, Tuple> parseMap = null;
	
	public ParseOutcome(){
		this.redundancies = new ArrayList<Redundancy>();
		this.parseMap = new HashMap<String, Tuple>();
	}
	
	public void addRedundancy(Redundancy redundancy){
		if(redundancies==null) {
			redundancies = new ArrayList<Redundancy>();
		}
		redundancies.add(redundancy);
	}

	public List<Redundancy> getRedundancies() {
		return redundancies;
	}

	public void setRedundancies(List<Redundancy> redundancies) {
		this.redundancies = redundancies;
	}

	public Map<String, Tuple> getParseMap() {
		return parseMap;
	}

	public void setParseMap(Map<String, Tuple> parseMap) {
		this.parseMap = parseMap;
	}	
}
