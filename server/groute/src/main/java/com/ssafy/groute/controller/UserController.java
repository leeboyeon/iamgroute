package com.ssafy.groute.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.groute.config.security.JwtTokenProvider;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.service.StorageService;
import com.ssafy.groute.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StorageService storageService;

    @Autowired
    private ObjectMapper mapper;

//    @Value("${spring.servlet.multipart.location}")
    @Value("${spring.http.multipart.location}")
    private String uploadPath;

//    @ApiOperation(value = "프로필사진", notes = "프로필사진")
//    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<byte[]> userImage(@PathVariable("imageName") String imagename) throws IOException {
//        InputStream imageStream = new FileInputStream(uploadPath + "/user/" + imagename);
//        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
//        imageStream.close();
//        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
//    }


    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping(value = "/signup")
    public Boolean registerUser(@RequestBody User user) throws Exception{
        if (userService.findById(user.getId()) != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getPhone() != null && user.getPhone().equals("")) {
            user.setPhone(null);
        }
        if(user.getBirth() != null && user.getBirth().equals("")) {
            user.setBirth(null);
        }
        if(user.getType().equals("none")) {
            user.setImg("default.png");
        }
        userService.registerUser(user);
        return true;
    }

    @ApiOperation(value="로그인", notes="로그인")
    @GetMapping("/login")
//    public ResponseEntity<?> login(User req, HttpServletResponse response) throws Exception{
    public ResponseEntity<?> login(User req) throws Exception{

        User selected = userService.findByUidType(req.getId(), req.getType());

        if(selected == null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }else if(!passwordEncoder.matches(req.getPassword(), selected.getPassword())){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("id",selected.getId());
        resultMap.put("token",jwtTokenProvider.createToken(String.valueOf(selected.getId()), Collections.singletonList("ROLE_USER")));

        selected.setToken(req.getToken());

//        Cookie cookie = new Cookie("loginId", URLEncoder.encode(selected.getId(), "utf-8"));
//        cookie.setMaxAge(1000 * 1000);
//        response.addCookie(cookie);
//        userService.saveUser(selected);

        return new ResponseEntity<Map<String, Object>>(resultMap,HttpStatus.OK);
    }

    @ApiOperation(value="Id 중복 체크", notes="Id 중복 체크")
    @GetMapping("/isUsedId")
    public @ResponseBody Boolean isUsedId(String id) throws Exception {

        if (userService.findById(id) != null) {
            return true;    // 조회되는 User가 있으면 Id 중복
        } else {
            return false;
        }
    }

    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴")
    @DeleteMapping(value = "{userId}")
    public Boolean deleteUser(@PathVariable String userId) throws Exception{
        // jwt token 인증 필요

        if (userService.findById(userId) == null) {
            return false;
        }
        try {
            userService.deleteUser(userId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @ApiOperation(value = "회원수정", notes = "회원수정")
    @PutMapping(value = "/update")
    public Boolean updateUser(@RequestPart(value = "user") String user, @RequestPart(value = "img", required = false) MultipartFile img) throws Exception{
        User inputUser = mapper.readValue(user, User.class);
        String beforeUserImg = inputUser.getImg();

        if (userService.findById(inputUser.getId()) == null) {
            return false;
        }

        if (img != null) {
            String fileName = storageService.store(img, uploadPath + "user");
            inputUser.setImg(fileName);
        } else {
            if(beforeUserImg.equals("") || beforeUserImg.equals("null")){
                inputUser.setImg(null);
            } else {
                inputUser.setImg(beforeUserImg);
            }
        }

        if(inputUser.getPhone() != null && inputUser.getPhone().equals("")) {
            inputUser.setPhone(null);
        }
        if(inputUser.getBirth() != null && inputUser.getBirth().equals("")) {
            inputUser.setBirth(null);
        }

        userService.updateUser(inputUser);
        return true;
    }


    @ApiOperation(value = "유저정보", notes = "유저정보")
    @GetMapping(value = "{userId}")
    public ResponseEntity<?> detailUser(@PathVariable String userId) throws Exception{
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디입니다.");
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "token 수정", notes = "id와 token만 입력")
    @PutMapping(value = "/update/token")
    public Boolean updateUserToken(@RequestBody User user) throws Exception{
        try {
            userService.updateTokenByUserId(user);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @ApiOperation(value = "토큰으로 user 조회", notes = "토큰으로 user 조회")
    @GetMapping(value = "/token/{token}")
    public ResponseEntity<?> selectUserByToken(@PathVariable String token) throws Exception{
        User user = userService.selectUserByToken(token);
        if (user == null) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "email로 id 조회", notes = "email로 id 조회")
    @GetMapping(value = "/id/{email}")
    public ResponseEntity<?> findIdByEmail(@PathVariable String email) throws Exception{
        User user = userService.selectUserIdByEmail(email);
        if (user == null) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "email, id 맞는거 있는지 조회 ", notes = "email, id 맞는거 있는지 조회")
    @GetMapping(value = "/pwd")
    public ResponseEntity<?> isCorrectEmailId(@RequestParam("id") String id, @RequestParam("email") String email) throws Exception{
        try {
            User res = userService.selectUserByIdEmail(id, email);
            if (res != null) {
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @ApiOperation(value = "비밀번호 수정", notes = "id와 password만 입력")
    @PutMapping(value = "/update/password")
    public Boolean updatePassword(@RequestBody User user) throws Exception{
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.updatePassword(user);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @ApiOperation(value="전체 사용자 정보 조회", notes="모든 사용자의 id와 token을 조회한다.")
    @GetMapping("/allUser")
    public ResponseEntity<?> selectAllUser() throws Exception {
        try {
            List<User> res = userService.selectAllUser();
            if(res != null) {
                return new ResponseEntity<List<User>>(res, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
