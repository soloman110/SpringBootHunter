package com.zinnaworks.nxpgtool.api;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public abstract class Field implements Serializable{
	
	public enum Type {String, List, Integer, Number, Map, Numeric};
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter
	private int id;
	@Getter @Setter
	private int depth;
	@Getter @Setter
	private String name;
	@Getter @Setter
	private Type type;
	@Getter @Setter
	private boolean nullYN;
	@Getter @Setter
	private boolean necessaryYN;
	@Getter @Setter
	private int parentId;
	@Getter @Setter
	private boolean isLeaf;
	protected Set<Integer> childs = new HashSet<>();
	
	@JsonIgnore
	private Field parent;
	//private Set<Field> childs
	
	public abstract void setChild(int childId);
	public abstract Set<Integer> getChildIds();

	public static Field getRoot() {
		Field root = new CompositeField();
		return root.setName("Root").setId(Integer.MIN_VALUE);
	}
	//bug....수정해야
	public static int buildId(int depth, String fieldName) {
		return depth + fieldName.hashCode();
	}

	@Override
	public String toString() {
		return "Field [id=" + id + ", depth=" + depth + ", name=" + name + ", type=" + type + ", necessaryYN="
				+ necessaryYN + ", nullYN=" + nullYN + "]" + ", parentId=" + parentId + ", isLeaf= " + isLeaf +", childs=" + childs;
	}
}
