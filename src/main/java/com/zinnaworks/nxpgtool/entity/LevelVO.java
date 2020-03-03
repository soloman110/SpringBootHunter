package com.zinnaworks.nxpgtool.entity;

public class LevelVO {

	private int level = 0;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		// 지금 설정된 level이 더 크면 변환 안한다.
		if (this.level < level) 
			this.level = level;
	}
	
}
