package com.junf.drhat.controller;

import com.junf.drhat.responsitory.UserRespon;
import com.junf.drhat.bean.Result;
import com.junf.drhat.bean.User;
import com.junf.drhat.enums.ResultEnum;
import com.junf.drhat.exception.MyException;
import com.junf.drhat.service.StorageService;
import com.junf.drhat.service.UserService;
import com.junf.drhat.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final StorageService storageService;

    @Autowired
    public UserController(StorageService storageService) {
        this.storageService = storageService;
    }


    @Autowired
    private UserRespon respon;

    @Autowired
    UserService userservice;


    @GetMapping(value = "/user")
    public List<User> getUserList() {
        return respon.findAll();
    }

    @PutMapping(value = "/profilephoto/{id}")
    public boolean handleFileUpload(@PathVariable("id") Integer uid, @RequestParam("file") MultipartFile file) {
        storageService.store(file,uid,"profile-photo.png");
        return true;    //"Change profile photo successfully"
    }


   //统一异常处理
    @ResponseBody
    @Transactional
    @PostMapping(value = "/add")
    public Result<User> addUser(@Valid User user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            //throw new MyException(bindingResult.getFieldError().getDefaultMessage());
            return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
        }

        user.setMoNum(user.getMoNum());
        user.setPassword(user.getPassword());
        user.setUserName("无名氏");
        user.setProfilePhoto("profile-photo.png");

        if(respon.existsUserByMoNum(user.getMoNum())) {
            throw new MyException(ResultEnum.NUMDUPLICATE);
        }
        respon.save(user);
        /*  获取路径
        System.out.println("java.home : "+System.getProperty("java.home"));
        System.out.println("java.class.version : "+System.getProperty("java.class.version"));
        System.out.println("java.class.path : "+System.getProperty("java.class.path"));
        System.out.println("java.library.path : "+System.getProperty("java.library.path"));
        System.out.println("java.io.tmpdir : "+System.getProperty("java.io.tmpdir"));
        System.out.println("java.compiler : "+System.getProperty("java.compiler"));
        System.out.println("java.ext.dirs : "+System.getProperty("java.ext.dirs"));
        System.out.println("user.name : "+System.getProperty("user.name"));
        System.out.println("user.home : "+System.getProperty("user.home"));
        System.out.println("user.dir : "+System.getProperty("user.dir"));
        System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath());
        */

        //return ClassUtils.getDefaultClassLoader().getResource("").getPath();

        //用户文件夹初始化
        storageService.init(user.getUid());
        return ResultUtil.success(user);
    }

    @ResponseBody
    @GetMapping(value = "/get/{id}")
    public Optional<User> getUser(@PathVariable("id") Integer uid) {
        if (respon.existsUserByUid(uid))
            return respon.findById(uid);
        else
            throw new MyException(ResultEnum.NOTFOUND);
    }

    //获取图片URL
    @GetMapping(value = "/get/{id}/default-photo")
    public String getImage(@PathVariable("id") Integer uid, Model model) throws IOException {
        Path pa = Paths.get("upload-dir");

        model.addAttribute("files",
        Files.walk(pa)
                .filter(path -> path.endsWith("profile-photo.png"))   //条件匹配过滤
                /*.map(path -> pa.relativize(path))   */                  //相对路径
                .map(path ->MvcUriComponentsBuilder.fromMethodName(UserController.class,  //工程路径处理
                        "serveFile", uid, path.getFileName().toString()).build().toString())
                .collect(Collectors.toList())
                );

        logger.info("path={}");

        return "upload";
    }


    @GetMapping("/{id}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable("id")Integer uid, @PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename, uid);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping(value = "/postjson")
    public String postJson(@RequestParam String account, @RequestParam String password){
        User user = new User();
        user.setUserName(account);
        user.setPassword(password);
        respon.save(user);
        return "插入完毕";
    }

   @DeleteMapping(value = "/del/{id}")
    public String delJson(@PathVariable("id")Integer uid){
        respon.deleteById(uid);
        return "删除完毕";
   }

   @GetMapping(value = "/search/{id}")
    public Object searchJson(@PathVariable("id")Integer uid) throws Exception{
        return ResultUtil.success(userservice.findOne(uid));

        //return respon.mySql(uid);
   }


}
