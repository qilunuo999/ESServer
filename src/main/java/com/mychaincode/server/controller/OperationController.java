package com.mychaincode.server.controller;

import com.mychaincode.server.entity.Users;
import com.mychaincode.server.service.OperationService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operate")
public class OperationController extends BaseController  {
    @Autowired
    OperationService operationService;

    @RequestMapping("/query")
    public String query(@RequestParam("funcname") String funcname, @RequestParam("args") String args){
        Users users = (Users) SecurityUtils.getSubject().getPrincipal();
        return operationService.operateChaincode(users,funcname,args,"query");
    }

    @RequestMapping("/invoke")
    public String invoke(@RequestParam("funcname") String funcname, @RequestParam("args") String args){
        Users users = (Users) SecurityUtils.getSubject().getPrincipal();
        return operationService.operateChaincode(users,funcname,args,"invoke");
    }

}
