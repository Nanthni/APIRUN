package com.orion.regression.bean;

import org.apache.commons.codec.digest.DigestUtils;

public class Tuple {
	int index;
	String digest_id;
	String uniqueName;
	String unique_id;
	String type_id;
	String desc;
	String txt;
	StringBuffer error = new StringBuffer();

	public Tuple(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName() {
		this.uniqueName = uniqueName;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}
	
	public void setUnique_id_obj(String object_id) {
		this.unique_id = object_id;
	}
	
	public void setUnique_id_rel(String src_id, String tgt_id) {
		this.unique_id = src_id+"/"+tgt_id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getError() {
		return error.toString();
	}

	public void setError(String error) {
		this.error = new StringBuffer(error);
	}

	public String getDigest_id() {
		return digest_id;
	}

	public void setDigest_id(String digest_id) {
		this.digest_id = digest_id;
	}

	public void setDigest_id_obj(String uniqueName) {
		String uniqueNameMinusConnector = uniqueName.indexOf("#")!=-1?uniqueName.substring(uniqueName.indexOf("#")):"#";
		String sha256hex = DigestUtils.sha256Hex(uniqueNameMinusConnector);
		this.digest_id = sha256hex;
	}

	public void setDigest_id_rel(String source, String target) {
		String sourceMinusConnector = source.indexOf("#")!=-1?source.substring(source.indexOf("#")):"#";
		String targetMinusConnector = target.indexOf("#")!=-1?target.substring(target.indexOf("#")):"#";
		String sha256hex = DigestUtils.sha256Hex(sourceMinusConnector + "<->" + targetMinusConnector);
		this.digest_id = sha256hex;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
}