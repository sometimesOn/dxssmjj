package com.jia.dxssmjj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jia.dxssmjj.service.MsmService;
import com.jia.dxssmjj.utils.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {

        DefaultProfile profile = DefaultProfile.getProfile(ConstantPropertiesUtils.REGION_ID,
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "Vappe");
        request.putQueryParameter("TemplateCode", "SMS_460720623");

        Map<String,Object> params = new HashMap<>();
        params.put("code",code);

        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(params));

        try {
            CommonResponse response = client.getCommonResponse(request);
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
