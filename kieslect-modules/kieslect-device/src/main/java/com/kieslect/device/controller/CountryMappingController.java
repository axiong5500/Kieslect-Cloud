package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.service.ICountryMappingService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/countryMapping")
public class CountryMappingController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CountryMappingController.class);

    @Autowired
    ICountryMappingService countryMappingService;

    @GetMapping("/sys/getList")
    private R<?> sysGetArticleList(){
        return R.ok(countryMappingService.list());
    }
}
