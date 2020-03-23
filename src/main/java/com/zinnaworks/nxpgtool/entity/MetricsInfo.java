package com.zinnaworks.nxpgtool.entity;

import lombok.Data;

@Data
public class MetricsInfo {
	private String men;
	private String menFree;
	private String instanceUptime;
	private String heap;
	private String heapUsed;
	private String heapCommitted;
	private int threads;
	private int threadsPeak;
	private int threadsTotalStarted;
	private int gcGlobalCount;
	private String gcScavengeTime;
	private int gcScavengeCount;
}
