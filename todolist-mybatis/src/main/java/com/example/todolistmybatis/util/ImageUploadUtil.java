package com.example.todolistmybatis.util;

import com.example.todolistmybatis.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Slf4j
@Component
public class ImageUploadUtil {
    @Value("${imgbb.api.key}") // 从配置文件中读取ImgBB API Key
    private String imgbbApiKey;

    public Result<String> uploadImage(MultipartFile file) {
        try {
            // 1. 验证文件
            if (file.isEmpty()) {
                return Result.fail("文件不能为空");
            }

            // 2. 使用RestTemplate构建请求
            RestTemplate restTemplate = new RestTemplate();

            // 3. 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 4. 构建请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
            body.add("key", imgbbApiKey);
            log.info("key: {}", imgbbApiKey);

            // 5. 发送请求
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.imgbb.com/1/upload",
                    requestEntity,
                    Map.class
            );

            // 6. 处理响应
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("data")) {
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                    String imageUrl = data.get("url").toString();
                    return Result.success("上传成功", imageUrl);
                }
            }
            return Result.fail("ImgBB服务响应异常");

        } catch (IOException e) {
            return Result.fail("文件处理失败: " + e.getMessage());
        } catch (Exception e) {
            return Result.fail("上传失败: " + e.getMessage());
        }
    }
}
