package com.zinnaworks.nxpgtool.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class ExcelApiRange {
	private int excelStartRowIndex;
	private int excelLastRowIndex;
	private Range fieldRange;
	private Range necessaryRange;
	private Range typeRange;
	private Range nNullYNRange;
}
