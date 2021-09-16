package com.claim.server.datacollection.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claim.server.common.po.SUser;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.datacollection.dao.BDataCollectionDao;
import com.claim.server.datacollection.dao.BFileListDao;
import com.claim.server.datacollection.po.BDataCollection;
import com.claim.server.datacollection.po.BFileList;
import com.claim.server.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/collection")
public class CollectionApi {

    @Autowired
    private BDataCollectionDao collectionDao;

    @Autowired
    private BFileListDao fileListDao;

    @Value("${uploadPath}")
    private String uploadPath;

    @RequestMapping(value = "/getCollectionList",method = RequestMethod.POST)
    public IPage getCollectionList(@RequestBody JSONObject params) {
        String reportNo = params.getString("reportNo");
        QueryWrapper<BDataCollection> wrapper =  new QueryWrapper<>();
        wrapper.eq("reportNo",reportNo);
        Page<BDataCollection> page = new Page<>();
        page.setCurrent(params.getLong("page"));
        page.setSize(params.getLong("pageSize"));
        IPage<BDataCollection> result = collectionDao.selectPage(page,wrapper);
        for (BDataCollection collection : result.getRecords()) {
            List<BFileList> fileList = fileListDao.selectByCollectionId(collection.getId());
            collection.setFileList(fileList);
        }
        return result;
    }

    @RequestMapping(value = "/collectionUpload",method = RequestMethod.POST)
    public ApiResponse collectionUpload(@RequestParam MultipartFile[] files, @RequestParam String reportNo, @RequestParam String certifyName, @RequestParam String certifyCode, @RequestParam String collectTime) {
        SUser user = CommonUtil.getLoginUser();
        BDataCollection collection = collectionDao.selectByReportNoAndCertifyCode(reportNo,certifyCode);
        if (collection == null) {
            collection = new BDataCollection();
            collection.setReportNo(reportNo);
            collection.setCertifyCode(certifyCode);
            collection.setCertifyName(certifyName);
            collection.setUploadTime(new Date());
            collection.setUploaderCode(user.getUserCode());
            collection.setUploaderName(user.getUserName());
            collection.setCollectTime(CommonUtil.parseDateTime(collectTime));
            collectionDao.insert(collection);
        } else {
            collection.setCollectTime(CommonUtil.parseDateTime(collectTime));
            collectionDao.updateById(collection);
        }
        if (files != null) {
            String path = uploadPath + File.separator + reportNo + File.separator + certifyCode + File.separator;
            String time = CommonUtil.formatDateTime(new Date());
            for (MultipartFile file : files) {
                try {
                    String filename = file.getOriginalFilename();
                    String prefix = filename.substring(0,filename.lastIndexOf(".")) ;
                    String suffix = filename.substring(filename.lastIndexOf(".")) ;
                    String md5Name = DigestUtils.md5DigestAsHex((prefix+time).getBytes())+suffix;
                    File filePath = new File(path,md5Name);
                    if (!filePath.getParentFile().exists()) {
                        filePath.getParentFile().mkdirs();
                    }
                    file.transferTo(filePath);
                    BFileList fileList = new BFileList();
                    fileList.setCollectionId(collection.getId());
                    fileList.setName(filename);
                    fileList.setUrl(filePath.getAbsolutePath());
                    fileListDao.insert(fileList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        List<BFileList> fileList = fileListDao.selectByCollectionId(collection.getId());
        collection.setFileList(fileList);
        return new ApiResponse(collection);
    }

    @RequestMapping(value = "/fileDownload",method = RequestMethod.POST)
    public void fileDownload(@RequestBody JSONObject params, HttpServletResponse response) {
        String url = params.getString("url");
        String name = params.getString("name");
        File file = new File(url);
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                response.setHeader("content-type","application/octet-stream");
                response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(name,"UTF-8"));
                response.setContentType("application/octet-stream");
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i >= 0) {
                    os.write(buffer,0,i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
