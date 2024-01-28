package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.extension.ncnb.service.SwaggerCodegenService;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/11
 * @describtion SwaggerCodegenController
 * @since 1.0
 */
@RestController
@RequestMapping("/codegen")
public class SwaggerCodegenController {

    @Autowired
    private SwaggerCodegenService swaggerCodegenService;

    @GetMapping
    public String test() {
        return "ok";
    }

    @PostMapping("/spring/spring-boot")
    public R springBoot(@RequestBody Map map) {
        return swaggerCodegenService.springBoot(map);
    }

}
