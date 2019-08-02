package com.zinnaworks.nxpgtool.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class Range {
	private int startRowNum;
	private int endRowNum;
	private int startCloumnNum;
	private int endCloumnNum;
}
