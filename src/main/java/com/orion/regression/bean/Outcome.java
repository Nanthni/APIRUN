package com.orion.regression.bean;

import java.util.ArrayList;
import java.util.List;

public class Outcome {
	List<Redundancy> stableRedundancies = null;
	List<Redundancy> nextRedundancies = null;
	List<OutcomeDetail> details = null;
	
	public Outcome(){
		this.stableRedundancies = new ArrayList<Redundancy>();
		this.nextRedundancies = new ArrayList<Redundancy>();
		this.details = new ArrayList<OutcomeDetail>();
	}
	
	public void addStableRedundancy(Redundancy redundancy){
		if(stableRedundancies==null) {
			stableRedundancies = new ArrayList<Redundancy>();
		}
		stableRedundancies.add(redundancy);
	}
	
	public void addNextRedundancy(Redundancy redundancy){
		if(nextRedundancies==null) {
			nextRedundancies = new ArrayList<Redundancy>();
		}
		nextRedundancies.add(redundancy);
	}
	
	public void addDetail(OutcomeDetail outcomeDetail){
		if(details==null) {
			details = new ArrayList<OutcomeDetail>();
		}
		details.add(outcomeDetail);
	}
	
	public void addAllDetails(List<OutcomeDetail> details){
		if(details==null) {
			details = new ArrayList<OutcomeDetail>();
		}
		details.addAll(details);
	}

	public List<Redundancy> getStableRedundancies() {
		return stableRedundancies;
	}
	
	public List<Redundancy> getNextRedundancies() {
		return nextRedundancies;
	}
	
	public void setStableRedundancies(List<Redundancy> stableRedundancies) {
		this.stableRedundancies = stableRedundancies;
	}
	
	public void setNextRedundancies(List<Redundancy> nextRedundancies) {
		this.nextRedundancies = nextRedundancies;
	}

	public List<OutcomeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OutcomeDetail> details) {
		this.details = details;
	}
}
