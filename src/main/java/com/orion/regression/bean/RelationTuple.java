package com.orion.regression.bean;

import java.util.Objects;

public class RelationTuple extends Tuple{
	String src_id;
	String tgt_id;
	String value_id;
	String weight;
	String source;
	String target;
	
	public RelationTuple(Integer index) {
		super(index.intValue());
	}
	
	@Override
	public void setUniqueName() {
		this.uniqueName = this.source + "<->" + this.target;
		setUnique_id_rel(this.src_id, this.tgt_id);
		setDigest_id_rel(this.source, this.target);
	}
	
	public String getSrc_id() {
		return src_id;
	}
	public void setSrc_id(String src_id) {
		this.src_id = src_id;
	}
	public String getTgt_id() {
		return tgt_id;
	}
	public void setTgt_id(String tgt_id) {
		this.tgt_id = tgt_id;
	}
	public String getValue_id() {
		return value_id;
	}
	public void setValue_id(String value_id) {
		this.value_id = value_id;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;	
		}
		boolean isEqual = true;
		if (obj == null) {
			isEqual = false;
		}
		if (getClass() != obj.getClass()) {
			isEqual =  false;
		}
		RelationTuple other = (RelationTuple) obj;
		if (!Objects.equals(type_id,other.type_id)) {
			error.append("TYPE_ID MISMATCH : "+type_id+"<->"+other.type_id);
			error.append("\n");
			isEqual =  false;
		}
		/*
		if (!Objects.equals(src_id,other.src_id)) {
			error.append("SRC_ID MISMATCH : "+src_id+"<->"+other.src_id);
			error.append("\n");
			return false;
		}
		if (!Objects.equals(tgt_id,other.tgt_id)) {
			error.append("TGT_ID MISMATCH : "+tgt_id+"<->"+other.tgt_id);
			error.append("\n");
			return false;
		}
		if (!Objects.equals(desc,other.desc)) {
			error.append("DESC MISMATCH : "+desc+"<->"+other.desc);
			error.append("\n");
			return false;
		}		
		if (!Objects.equals(value_id,other.value_id)) {
			error.append("VALUE_ID MISMATCH : "+value_id+"<->"+other.value_id);
			error.append("\n");
			return false;
		}
		*/
		if (!Objects.equals(weight,other.weight)) {
			error.append("WEIGHT MISMATCH : "+weight+"<->"+other.weight);
			error.append("\n");
			isEqual =  false;
		}
		if (!Objects.equals(txt,other.txt)) {
			error.append("TXT MISMATCH : "+txt+"<->"+other.txt);
			error.append("\n");
			isEqual =  false;
		}
		/*
		if (!Objects.equals(source,other.source)) {
			error.append("SOURCE MISMATCH : "+source+"<->"+other.source);
			error.append("\n");
			isEqual =  false;
		}
		if (!Objects.equals(target,other.target)) {
			error.append("TARGET MISMATCH : "+target+"<->"+other.target);
			error.append("\n");
			isEqual =  false;
		}
		*/
		return isEqual;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + type_id.hashCode();
		result = 31 * result + weight.hashCode();
		return result = 31 * result + txt.hashCode();
		//result = 31 * result + source.hashCode();
		//return 31 * result + target.hashCode();
	}

}
