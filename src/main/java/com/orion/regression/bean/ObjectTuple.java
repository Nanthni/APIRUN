package com.orion.regression.bean;

import java.util.Objects;

public class ObjectTuple extends Tuple {
	String parent;
	String id;
	String parent_id;
	String name;
	String ext;
	String origin_ds;
	String origin_nm;
	String notes;

	public ObjectTuple(Integer index) {
		super(index.intValue());
	}

	@Override
	public void setUniqueName() {
		if (this.parent != null && this.parent.trim().length() > 0) {
			this.uniqueName = this.parent + "#" + this.name + "[" + this.type_id + "]";
		} else {
			this.uniqueName = this.name + "[" + this.type_id + "]";
		}
		setUnique_id_obj(this.id);
		setDigest_id_obj(this.uniqueName);
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parentName) {
		this.parent = parentName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getOrigin_ds() {
		return origin_ds;
	}

	public void setOrigin_ds(String origin_ds) {
		this.origin_ds = origin_ds;
	}

	public String getOrigin_nm() {
		return origin_nm;
	}

	public void setOrigin_nm(String origin_nm) {
		this.origin_nm = origin_nm;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObjectTuple other = (ObjectTuple) obj;
		if (!Objects.equals(type_id, other.type_id)) {
			error.append("TYPE_ID MISMATCH->" + type_id + "<->" + other.type_id);
			error.append("\n");
			return false;
		}
		if (!Objects.equals(name, other.name)) {
			error.append("NAME MISMATCH->" + name + "<->" + other.name);
			error.append("\n");
			return false;
		}
		if (!Objects.equals(ext, other.ext)) {
			error.append("EXT MISMATCH->" + ext + "<->" + other.ext);
			error.append("\n");
			return false;
		}
		if (!Objects.equals(desc, other.desc)) {
			error.append("DESC MISMATCH->" + desc + "<->" + other.desc);
			error.append("\n");
			return false;
		}/*
		if (!Objects.equals(origin_ds, other.origin_ds)) {
			error.append("ORIGIN_DS MISMATCH->" + origin_ds + "<->" + other.origin_ds);
			error.append("\n");
			return false;
		}
		if (!Objects.equals(origin_nm, other.origin_nm)) {
			error.append("ORIGIN_NM MISMATCH->" + origin_nm + "<->" + other.origin_nm);
			error.append("\n");
			return false;
		}*/
		if (!Objects.equals(txt, other.txt)) {
			error.append("TXT MISMATCH->" + txt + "<->" + other.txt);
			error.append("\n");
			return false;
		}
		if (!Objects.equals(notes, other.notes)) {
			error.append("NOTES MISMATCH->" + notes + "<->" + other.notes);
			error.append("\n");
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + type_id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + ext.hashCode();
		result = 31 * result + desc.hashCode();
		//result = 31 * result + origin_ds.hashCode();
		//result = 31 * result + origin_nm.hashCode();
		result = 31 * result + txt.hashCode();
		return 31 * result + notes.hashCode();
	}
}
