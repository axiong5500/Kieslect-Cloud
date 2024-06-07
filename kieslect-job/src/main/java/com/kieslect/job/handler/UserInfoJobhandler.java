package com.kieslect.job.handler;

import cn.hutool.http.HttpUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;


@Component
public class UserInfoJobhandler {

    @XxlJob("updateAccountStatusExpireJobHandler")
    public ReturnT<String> updateAccountStatusExpireJobHandler() {
        XxlJobHelper.log("XXL-JOB, updateAccountStatusExpireJobHandler.");

        String url = "http://localhost:9999/kieslect-user/user/task/updateAccountStatusExpire";
        String result = HttpUtil.get(url);

        XxlJobHelper.log(String.valueOf(result));
        return ReturnT.SUCCESS;
    }
}
