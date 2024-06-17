package com.kieslect.api;


import com.kieslect.common.core.constant.ServiceNameConstants;
import com.kieslect.common.core.domain.R;
import feign.Headers;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务
 *
 * @author kieslect
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE)
public interface RemoteFileService {
    @PostMapping(value ="/file/upload",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<?> uploadFile(@RequestPart("file") @NotNull(message = "文件不能为空") MultipartFile file,
                    @RequestParam("pathType") @NotNull(message = "路径类型不能为空") Integer pathType,
                    @RequestParam(value = "userId", required = false) Long userId);

    @GetMapping("/file/listFilesInFolder")
    @Headers("Content-Type: application/octet-stream")
    ResponseEntity<byte[]> listFilesInFolder(@RequestParam("folderPath") String folderPath);

    @GetMapping("/file/downloadFileByFilePath")
    @Headers("Content-Type: application/octet-stream")
    ResponseEntity<byte[]> downloadFileByFilePath(@RequestParam("filePath") String filePath);


    @PostMapping("/file/healthSport/uploadLocalFileToOSS")
    R<?> uploadLocalFileToOSS(@RequestParam("userId") Long userId,@RequestParam("pathType")  Integer pathType);


    @PostMapping("/file/healthSport/removeOSSFile")
    R<?> removeOSSFile(@RequestParam("userId") Long userId,@RequestParam("pathType")  Integer pathType);

    @PostMapping("/file/remoteUrlToOSS")
    R<?> remoteUrlToOSS(@RequestParam("remoteUrl")  String remoteUrl,
                        @RequestParam("pathType")  Integer pathType);

}
