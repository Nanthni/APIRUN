package com.orion.regression.bean;

public class Redundancy {
	Tuple duplicateTuple;
	Tuple originalTuple;
	
	public Redundancy(Tuple duplicateTuple, Tuple originalTuple){
		this.duplicateTuple = duplicateTuple;
		this.originalTuple = originalTuple;
	}
	
	public Tuple getDuplicateTuple() {
		return duplicateTuple;
	}
	public void setDuplicateTuple(Tuple duplicateTuple) {
		this.duplicateTuple = duplicateTuple;
	}
	public Tuple getOriginalTuple() {
		return originalTuple;
	}
	public void setOriginalTuple(Tuple originalTuple) {
		this.originalTuple = originalTuple;
	}
	public String getUniqueKey() {
		return originalTuple.getUniqueName();
	}
}
