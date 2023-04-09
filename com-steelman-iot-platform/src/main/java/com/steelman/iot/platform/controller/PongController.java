package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;

@RestController
@RequestMapping("/api/ping")
public class PongController extends BaseController {
    @RequestMapping(value = "/ping",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String ping(){
        Type type=new TypeToken<Result>(){}.getType();
        Result success = Result.success();
        return JsonUtils.toJson(success,type);
    }
}
