package web.controllers.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import web.exceptions.NotSignedInException;
import web.model.jpa.entities.Account;
import web.model.jpa.entities.AccountSetting;
import web.model.service.AccountService;
import web.model.service.AccountSettingService;
import web.model.service.sign.SignService;

@RestController
@RequestMapping(value="/api", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AccountRestController {

	@Autowired
	AccountService accountSerivce;
	
	@Autowired
	AccountSettingService accountSettingService;
	
	@Autowired
	SignService signService;

	
	//---AccountSetting
	
	@RequestMapping(value="account/setting/save-pen-name", method=RequestMethod.PUT)
	public AccountSetting savePenName(@RequestBody HashMap<String, Object> jsonMap,
			HttpServletRequest req) throws NotSignedInException {
		Account me = signService.getSign(req.getSession()).getAccount();
		AccountSetting set = accountSettingService.savePenName((String)jsonMap.get("penName"), me);
		return set;
	}
	
	@RequestMapping(value="account/setting/save-introduction", method=RequestMethod.PUT)
	public AccountSetting saveIntroduction(@RequestBody HashMap<String, Object> jsonMap,
			HttpServletRequest req) throws NotSignedInException {
		Account me = signService.getSign(req.getSession()).getAccount();
		AccountSetting set = accountSettingService.saveIntroduction((String)jsonMap.get("introduction"), me);
		return set;
	}
	
	@RequestMapping(value="account/setting/upload-profile-picture", method=RequestMethod.POST)
	public String uploadProfilePicture(
			@RequestParam("file") MultipartFile uploadedFile,
			HttpServletRequest req)
			throws NotSignedInException, IOException {
		Account me = signService.getSign(req.getSession()).getAccount();
		System.out.println(uploadedFile.getName());
		System.out.println(uploadedFile.getOriginalFilename());
		
/*		 byte fileData[] = file.getBytes();
         
         fos = new FileOutputStream(path + "\\" + fileName);
          
         fos.write(fileData);*/
		
		InputStream in = uploadedFile.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(in);
		
		File target = new File("/usr/" + uploadedFile.getOriginalFilename());
		target.createNewFile();
		System.out.println(target.getAbsolutePath().toString());
		
		FileOutputStream fos = new FileOutputStream(target);
		byte[] buf = new byte[200];
		while(bis.read(buf) > 0) {
			fos.write(buf);
		}

		//String filePath = req.getServletContext().getRealPath("/"); 
		/*String filePath = req.getServletContext().getRealPath("/"); 
		try {
			uploadedFile.transferTo(new File(filePath));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		return "";
	}
	
	@RequestMapping(value="account/my-settings", method=RequestMethod.GET)
	public AccountSetting getMySettings(HttpServletRequest req) throws NotSignedInException {
		Account me = signService.getSign(req.getSession()).getAccount();
		return accountSettingService.findAccountSettingByAccountId(me.getId());
	}

}
