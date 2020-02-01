package com.mychaincode.server.service;

import com.mychaincode.server.repository.DepartmentRepository;
import com.mychaincode.server.repository.OrgsettingRepository;
import com.mychaincode.server.repository.PeersRepository;
import com.mychaincode.server.repository.ServiceSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    OrgsettingRepository orgsettingRepository;
    @Autowired
    PeersRepository peersRepository;
    @Autowired
    ServiceSettingRepository serviceSettingRepository;
}
