package com.kieslect.job.handler;

import cn.hutool.http.HttpUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;


@Component
public class StravaJobhandler {

    private static final String domain_url = "http://localhost:9999/kieslect-file/file";

    private static final String task_url_prefix = "/task";

    @XxlJob("parseFileToDBJobHandler")
    public ReturnT<String> parseFileToDBJobHandler() {
        XxlJobHelper.log("XXL-JOB, parseFileToDBJobHandler.");

        String url = domain_url + task_url_prefix + "/parseFileToDB";
        String result = HttpUtil.get(url);

        XxlJobHelper.log(String.valueOf(result));
        return ReturnT.SUCCESS;
    }

    @XxlJob("getDBToTcxJobHandler")
    public ReturnT<String> getDBToTcxJobHandler() {
        XxlJobHelper.log("XXL-JOB, getDBToTcxJobHandler.");

        String url = domain_url + task_url_prefix + "/getDBToTcx";
        String result = HttpUtil.get(url);

        XxlJobHelper.log(String.valueOf(result));
        return ReturnT.SUCCESS;
    }

    @XxlJob("readTcxFileUploadStravaJobHandler")
    public ReturnT<String> readTcxFileUploadStravaJobHandler() {
        XxlJobHelper.log("XXL-JOB, readTcxFileUploadStravaJobHandler.");

        String url = domain_url + task_url_prefix + "/readTcxFileUploadStrava";
        String result = HttpUtil.get(url);

        XxlJobHelper.log(String.valueOf(result));
        return ReturnT.SUCCESS;
    }
}
