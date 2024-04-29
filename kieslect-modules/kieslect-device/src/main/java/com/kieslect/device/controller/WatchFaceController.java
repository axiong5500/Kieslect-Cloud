package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.vo.WatchFaceListVO;
import com.kieslect.device.domain.vo.WatchFaceTypeGroupVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kies on 2017/3/29.
 * 表盘管理
 */
@RestController
@RequestMapping(value ="/watchFace")
public class WatchFaceController {
    // list
    @GetMapping(value = "/list")
    public R<?> list(){
        Map<String,Object> result = new HashMap<>();
        List<WatchFaceTypeGroupVO> watchFaceTypeGroupVOs = new ArrayList<>();
        watchFaceTypeGroupVOs.add(new WatchFaceTypeGroupVO(1,"功能",1));
        watchFaceTypeGroupVOs.add(new WatchFaceTypeGroupVO(2,"抽象",1));
        List<WatchFaceListVO> watchFaceListVOs = new ArrayList<>();
        watchFaceListVOs.add(new WatchFaceListVO(1,"w1","/kieslect-file/file/download/watchface/w1.png",1,"",1,"/kieslect-file/file/download/watchface/w1.zip","0b51fdd742fddee7a1502aa2c5b36610",157023));
        watchFaceListVOs.add(new WatchFaceListVO(2,"w2","/kieslect-file/file/download/watchface/w2.png",1,"",1,"/kieslect-file/file/download/watchface/w2.zip","b8172a21dec88b490c2fe12fd389f6f8",228280));
        watchFaceListVOs.add(new WatchFaceListVO(3,"w3","/kieslect-file/file/download/watchface/w3.png",2,"",1,"/kieslect-file/file/download/watchface/w3.zip","9901c02e6b014ff98f04e5b321a75b84",1480394));
        watchFaceListVOs.add(new WatchFaceListVO(4,"w4","/kieslect-file/file/download/watchface/w4.png",2,"",1,"/kieslect-file/file/download/watchface/w4.zip","200d1f3ab00b0973a02c5e4f7c59739c",6357937));
        result.put("watchFaceTypeGroup",watchFaceTypeGroupVOs);
        result.put("watchFaces",watchFaceListVOs);
        return R.ok(result);
    }
}
