package com.junf.drhat.service;

import com.junf.drhat.responsitory.UserRespon;
import com.junf.drhat.bean.User;
import com.junf.drhat.enums.ResultEnum;
import com.junf.drhat.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRespon respon;

    public Optional<User> findOne(Integer id) throws Exception{
        Optional<User> user = respon.findById(id);
        if (user == null) {
            throw new MyException(ResultEnum.SEARCH);
        }
        else return user;
    }
}
