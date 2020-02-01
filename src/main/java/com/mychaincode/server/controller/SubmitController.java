package com.mychaincode.server.controller;

import com.mychaincode.server.entity.Users;
import com.mychaincode.server.service.CertificateService;
import com.mychaincode.server.service.CompanyService;
import com.mychaincode.server.service.SealService;
import org.apache.shiro.SecurityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;

@RestController
@RequestMapping("/submit")
public class SubmitController  extends BaseController {
    @Autowired
    SealService sealService;

    private final
    CompanyService companyService;
    private final
    CertificateService certificateService;
    MultipartFile fileSave;
    String filename;

    private static final Logger log = LoggerFactory.getLogger(SubmitController.class);

    public void saveFile(String path,MultipartFile file) throws IOException {
        OutputStream os = null;
        InputStream inputStream = null;
        String fileName = null;
        try {
            inputStream = file.getInputStream();
            fileName = file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    上传已盖章文件
     */
    @RequestMapping("/uploadVerification")
    public String getPic(HttpServletRequest request, MultipartFile file) throws IOException {
        fileSave = file;
        return "1";
    }

    /*
    验证文件中电子印章的可靠性
     */
    @RequestMapping("/verification")
    public String verification(@RequestParam("id") int id) throws IOException {
        String label = null;
        int result;
        //读出文件的前十行

        result = sealService.findLabel(id,label);
        return String.valueOf(result);
    }


    /*
    上传公司营业执照
     */
    @RequestMapping("/uploadCompanyFile")
    public void uploadCompanyFile(HttpServletRequest request,MultipartFile file) throws IOException {
        String path = "D:\\staticserver\\buslicense";
        saveFile(path,file);
    }

    public SubmitController(CertificateService certificateService, CompanyService companyService) {
        this.certificateService = certificateService;
        this.companyService = companyService;
    }

    /*
    上传公司文件，营业执照为图片
     */
    @RequestMapping("/companyInfo")
    public String saveCompanyInfo(@RequestParam("email") String email, @RequestParam("company") String company,
                                  @RequestParam("creditcode") String creditcode, @RequestParam("verifyinfo") String verifyinfo,
                                  @RequestParam("filename") String filename) throws JSONException {
        //上传文件操作@RequestParam("license") String license
        JSONObject json = new JSONObject();
        json = companyService.submitCompanyInfo(email,company,creditcode,verifyinfo,filename);
        return json.toString();
//        return companyService.submitCompanyInfo(email,company,creditcode,verifyinfo,fileName).toString();
    }

    /*
    上传证书文件
     */
    @RequestMapping("/uploadVerifyFile")
    public void uploadVerifyFile(HttpServletRequest request,MultipartFile file) throws IOException {
        String path = "D:\\staticserver\\verifyfile";
//        filename = file.getOriginalFilename();
        saveFile(path,file);
    }

    /*
    上传证书信息
     */
    @RequestMapping("/certifyInfo")
    public String saveCertifiInfo(@RequestParam("certificate") String certificate, @RequestParam("id") String id,
                                  @RequestParam("filetype") String filetype,@RequestParam("email") String email) throws JSONException {
        JSONObject json = new JSONObject();
        System.out.println(filetype);
        System.out.println(certificate);
        System.out.println(id);
        System.out.println(email);
        json = certificateService.saveApplying(certificate,id,email,filetype);
        return json.toString();
    }

    /*
     读取文件前十行
     */
    public String[] readPdfFile(String name) {
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<>();
        int i = 0;
        try {
            FileReader fr = new FileReader(name);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对ArrayList中存储的字符串进行处理
        int length = arrayList.size();
        String [] array = new String[length];
        for (int j = 0; j < length; j++) {
            String s = arrayList.get(j);
            array[j] = s;
        }
        // 返回数组
        return array;
    }

    /*
    公司信息驳回
     */
    @RequestMapping("/certifyReject")
    public String certifyReject (@RequestParam("id") int id,@RequestParam("state") String state) throws JSONException, CertificateException, FileNotFoundException {
        JSONObject json = new JSONObject();
        json = certificateService.solveApplying(id,state);
        return json.toString();
    }

    /*
    公司信息通过
     */
    @RequestMapping("/confirm")
    public String certify (@RequestParam("id") int id,@RequestParam("state") String state) throws JSONException, CertificateException, FileNotFoundException {
        JSONObject json = new JSONObject();
        System.out.println(id);
        System.out.println(state);
        json = certificateService.solveApplying(id,state);
        return json.toString();
    }

    /*
    * 上传新制印章信息保存
    * @param sealId 印章ID
    * @param sealName 印章名称
    * @param sealInfo 印章的认证信息
    * @param certificateName 证书名
    * @param sealType 印章类型
     */
    @RequestMapping("/uploadSeal")
    public String uploadSeal(@RequestParam("sealId")String sealId, @RequestParam("sealName")String sealName,
                             @RequestParam("certificatename")String certificateName,
                             @RequestParam("sealInfo")String sealInfo, @RequestParam("sealType")String sealType) throws JSONException {
        Users user = (Users) SecurityUtils.getSubject().getPrincipal();
        return sealService.submitSealInfo(user,sealId,sealName,certificateName,sealType,sealInfo).toString();
    }

    /*
    * 上传已盖章文件信息
    *  @param sealId 盖章所使用的印章的ID
    *  @param sealedFileKey 已盖章文件的key，用于数据库查询
    *  @param sealedFileInfo 已盖章文件加密后的文件信息摘要
     */
    @RequestMapping("uploadSealedFile")
    public String uploadSealedFile(@RequestParam("sealId")String sealId, @RequestParam("sealedFileKey")String sealedFileKey,
                                   @RequestParam("sealedFileInfo")String info ){

    }

}
