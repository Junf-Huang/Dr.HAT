package com.junf.drhat.responsitory;

import com.junf.drhat.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRespon extends JpaRepository<User, Integer> {
    //自定义sql语句
    @Query(value = "SELECT * from t_user where uid=?1", nativeQuery = true)
    List<User> mySql(Integer uid);


    boolean existsUserByMoNum(String MoNum);
    boolean existsUserByUid(Integer uid);
}
