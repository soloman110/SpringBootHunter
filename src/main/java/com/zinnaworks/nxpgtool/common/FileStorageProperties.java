package com.zinnaworks.nxpgtool.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String ifTesturlFn;

    /**
	 * @return the ifTesturlFn
	 */
	public String getIfTesturlFn() {
		return ifTesturlFn;
	}

	/**
	 * @param ifTesturlFn the ifTesturlFn to set
	 */
	public void setIfTesturlFn(String ifTesturlFn) {
		this.ifTesturlFn = ifTesturlFn;
	}

	public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
