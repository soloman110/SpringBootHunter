package com.zinnaworks.nxpgtool.api;

import java.util.Set;

public class CompositeField extends Field {

	private static final long serialVersionUID = 1L;

	@Override
	public void setChild(int childId) {
		this.childs.add(childId);
	}

	@Override
	public Set<Integer> getChildIds() {
		return this.childs;
	}

}
