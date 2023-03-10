package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.constant.AmapConfigConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.responese.TerminalResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TerminalClient {

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String amapSid;

    @Autowired
    private RestTemplate restTemplate;


    public ResponseResult<TerminalResponse> add(String name , String desc){

        // &key=<用户的key>
        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_ADD);
        url.append("?");
        url.append("key="+amapKey);
        url.append("&");
        url.append("sid="+amapSid);
        url.append("&");
        url.append("name="+name);
        url.append("&");
        url.append("desc="+desc);
        System.out.println("创建终端请求："+url.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        System.out.println("创建终端响应："+stringResponseEntity.getBody());
        /**
         * {
         *     "data": {
         *         "name": "车辆2",
         *         "tid": 583145283,
         *         "sid": 797498
         *     },
         *     "errcode": 10000,
         *     "errdetail": null,
         *     "errmsg": "OK"
         * }
         */
        String body = stringResponseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String tid = data.getString("tid");

        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setTid(tid);

        return  ResponseResult.success(terminalResponse);
    }


    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_AROUNDSEARCH);
        url.append("?");
        url.append("key="+amapKey);
        url.append("&");
        url.append("sid="+amapSid);
        url.append("&");
        url.append("center="+center);
        url.append("&");
        url.append("radius="+radius);

        System.out.println("终端搜索请求："+url.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        System.out.println("终端搜索响应："+stringResponseEntity.getBody());

        // 解析终端搜索结果
        String body = stringResponseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");

        List<TerminalResponse> terminalResponseList = new ArrayList<>();

        JSONArray results = data.getJSONArray("results");
        for (int i=0;i<results.size();i++){
            TerminalResponse terminalResponse = new TerminalResponse();

            JSONObject jsonObject = results.getJSONObject(i);
            Long carId = jsonObject.getLong("desc");
            String tid = jsonObject.getString("tid");

            terminalResponse.setCarId(carId);
            terminalResponse.setTid(tid);

            terminalResponseList.add(terminalResponse);
        }



        return ResponseResult.success(terminalResponseList);
    }
}
