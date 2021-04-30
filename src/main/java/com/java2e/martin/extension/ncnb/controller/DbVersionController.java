package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.extension.ncnb.service.DbVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 狮少
 * @since 2020-10-29
 */
@Slf4j
@RestController
@RequestMapping()
public class DbVersionController {

    @Autowired
    private DbVersionService dbVersionService;


}

