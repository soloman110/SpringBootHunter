package com.zinnaworks.nxpgtool.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zinnaworks.nxpgtool.api.ExcelParser;
import com.zinnaworks.nxpgtool.common.FileStorageProperties;
import com.zinnaworks.nxpgtool.service.FileStorageService;
import com.zinnaworks.nxpgtool.util.CommonUtils;
import com.zinnaworks.nxpgtool.util.JsonUtil;

@RequestMapping("/nxpgtool")
@Controller
public class ExcelController {
	private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

	@Autowired
	private FileStorageService fileStorageServiceImpl;
	
	 @Autowired
	 FileStorageProperties fileStorageProperties;

	@RequestMapping("/excel")
	public String hello(Model model, @RequestParam(defaultValue = "Ryan") String name) throws FileNotFoundException {

		model.addAttribute("name", name);
		return "excel";
	}
	
	@ResponseBody
	@PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse res) {
        String fileName = fileStorageServiceImpl.storeFile(file);
        String uploadDir = fileStorageProperties.getUploadDir();
        try {
			ExcelParser parser = new ExcelParser(uploadDir + "/" +fileName);
			Map<String, Map<String, Object>> ifInfos = parser.parseAllSheet();
			for(Map.Entry<String, Map<String, Object>> info : ifInfos.entrySet()) {
				String ifName = info.getKey();
				String json = JsonUtil.objectToJson(info.getValue());
				
				CommonUtils.saveJson(uploadDir + "/" +ifName + ".json", json);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return "ok";
    }

	@GetMapping("/resource/download")
	public ResponseEntity<Resource> downloadFile(HttpServletRequest request) throws IOException {
		File f = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "api_field_template.xlsx");
		Resource resource = fileStorageServiceImpl.loadFileAsResource(f.getAbsolutePath());

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
			throw ex;
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}