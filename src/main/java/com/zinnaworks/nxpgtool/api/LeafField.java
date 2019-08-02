package com.zinnaworks.nxpgtool.api;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LeafField extends Field {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Override
	public void setChild(int childId) {
		throw new UnsupportedOperationException("leaf has not child");
	}

	@JsonIgnore
	@Override
	public Set<Integer> getChildIds() {
		return new HashSet<>();
	}
}
