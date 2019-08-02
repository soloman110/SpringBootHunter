package com.zinnaworks.nxpgtool.service.impl;

import static com.zinnaworks.nxpgtool.util.CommonUtils.loadData;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zinnaworks.nxpgtool.api.IFParser;
import com.zinnaworks.nxpgtool.common.FileStorageProperties;
import com.zinnaworks.nxpgtool.exception.DataNotValidException;
import com.zinnaworks.nxpgtool.exception.FileStorageException;
import com.zinnaworks.nxpgtool.service.IFService;
import com.zinnaworks.nxpgtool.util.JsonUtil;

@Service
public class IFServiceImpl implements IFService {
	private final Path fileStorageLocation;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Autowired
	public IFServiceImpl(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
	}
	
	@Override
	public Map<String, Object> toTree(String ifName) throws FileNotFoundException, DataNotValidException {
		IFParser ifparser = new IFParser();
		Map<String, Object> jsonIf = ifparser.parse(ifName);
		return jsonIf;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String compare(Map<String, Object> remote, Map<String, Object> apiExcel) {
		for(Map.Entry<String, Object> entry : remote.entrySet()) {
			String fieldName = entry.getKey();
			Object v = entry.getValue();
			if(!apiExcel.containsKey(fieldName)) {
				return entry.getValue().toString() + " 이 존재하지 않음!" + apiExcel.get(fieldName);
			}
			if(v instanceof Map) {
				return compare((Map<String, Object>)v, (Map<String, Object>)apiExcel.get(fieldName));
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> getFieldTree(String ifname) throws FileNotFoundException {
		String apiJson = loadData(fileStorageProperties.getUploadDir() + "/" + ifname+".json");
		Map<String, Object> apiTree = JsonUtil.jsonToObjectHashMap(apiJson,String.class, Object.class);
		return apiTree;
	}
}
