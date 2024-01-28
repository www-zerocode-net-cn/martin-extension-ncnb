package com.java2e.martin.extension.ncnb.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.redis.RedisUtil;
import com.java2e.martin.common.vip.license.AbstractServerInfos;
import com.java2e.martin.common.vip.license.LicenseCheckModel;
import com.java2e.martin.common.vip.license.LicenseCommandLineRunner;
import com.java2e.martin.common.vip.license.LinuxServerInfos;
import com.java2e.martin.common.vip.license.WindowsServerInfos;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 零代科技
 * @date 2023/05/08
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/license")
public class LicenseController {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LicenseCommandLineRunner licenseCommandLineRunner;

    /**
     * 获取服务器硬件信息
     *
     * @param osName 操作系统类型，如果为空则自动判断
     * @return com.ccx.models.license.LicenseCheckModel
     * @author 零代科技
     * @date 2023/05/08 13:13
     * @since 1.0.0
     */
    @GetMapping(value = "/getServerInfos")
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        log.info("osName: {}", osName);
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        return abstractServerInfos.getServerInfos();
    }

    @SneakyThrows
    @PostMapping("/upload")
    public R  upload(@RequestParam("file") MultipartFile file) throws IOException, IOException {
        boolean endsWith = file.getOriginalFilename().endsWith(".lic");
        if(!endsWith){
            return R.failed("非法的文件");
        }
//        // 上传该文件/图像至该文件夹下
        String pathname = "/opt/erd" + File.separator + "license.lic";
        File dest = new File(pathname);
        FileUtil.del(dest);
        IoUtil.copy(file.getInputStream(), new FileOutputStream(dest));
        log.info("licence上传成功：{}","/opt/erd");
        licenseCommandLineRunner.install(pathname);
        // 发送licence安装事件
        redisUtil.pub("message-licence-install",pathname);
        return R.ok("证书添加成功");

    }

}
