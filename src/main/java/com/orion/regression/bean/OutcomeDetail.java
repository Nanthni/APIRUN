package com.orion.regression.bean;

import com.orion.regression.report.Style;

public class OutcomeDetail {
	String observation;
	Style style;
	Tuple tuple1;
	Tuple tuple2;

	public OutcomeDetail(Tuple tuple1, Tuple tuple2) {
		this.tuple1 = tuple1;
		this.tuple2 = tuple2;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(final String observation) {
		this.observation = observation;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(final Style style) {
		this.style = style;
	}

	public Tuple getTuple1() {
		return tuple1;
	}

	public void setTuple1(Tuple tuple1) {
		this.tuple1 = tuple1;
	}

	public Tuple getTuple2() {
		return tuple2;
	}

	public void setTuple2(Tuple tuple2) {
		this.tuple2 = tuple2;
	}
}