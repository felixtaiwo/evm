package com.dbest.sevs.controller;


import com.dbest.sevs.entity.*;
import com.dbest.sevs.security.JWTTokenUtil;
import com.dbest.sevs.service.Logic;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping(value = "api")
@CrossOrigin(origins = "*")
@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private Logic service;
    @Autowired
    private JWTTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Rest Endpoints for user entity
     **/

    @PostMapping(value = "user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User user1 = service.findUserByEmailAddress(user.getEmailAddress());
            User user2 = service.findUserByPhone(user.getPhone());
            if (user1 != null || user2 != null) {
                if (user1 == user2 || (user1 == null && user2.getId() == user.getId()) || (user2 == null && user1.getId() == user.getId()))
                    service.addAndEditUser(user);
                else
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Address and/or phone number is already taken");
            }
            service.addAndEditUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("user/{status}")
    public ResponseEntity<?> getUsers(@PathVariable boolean status,HttpServletRequest httpServletRequest){
        if(!httpServletRequest.getHeader("Authorization").equals(service.handleAdminAuth())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.getUsers(!status));
    }

    @PostMapping(value = "user/{email}/confirmaccount")
    public ResponseEntity<?> confirmAccount(@RequestBody String token, @PathVariable String email) throws GeneralSecurityException, IOException, ClassNotFoundException {
        User user = service.findUserByEmailAddress(email);
        if (user == null) {
            user = service.findUserByPhone(email);
        }
        if (service.confirmToken(user, token)) {
            user.setChangePassword(true);
            user.setToken(null);
            service.addAndEditUser(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "user/confirmaccount/password")
    public ResponseEntity<?> choosePassword(@RequestBody Map<String, String> body) throws GeneralSecurityException, IOException {
        User user = service.findUserByEmailAddress(body.get("email"));
        if (user == null) {
            user = service.findUserByPhone(body.get("email"));
        }
        if (user.isChangePassword()) {
            String password = body.get("newpassword");
            if (password.length() >= 8) {
                user.setPassword(password);
                user.setChangePassword(false);
                service.addAndEditUser(user);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("password character must be greater than 8");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(value = "user/password/change")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        try {
            User user = service.findUserByEmailAddress(body.get("email"));
            if (user == null) {
                user = service.findUserByPhone(body.get("email"));
            }
            if (passwordEncoder.matches(body.get("oldpassword"), user.getPassword())) {
                user.setChangePassword(true);
                service.addAndEditUser(user);
                return choosePassword(body);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "user/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody String email) {
        try {
            User user = service.findUserByEmailAddress(email);
            if (user == null) {
                user = service.findUserByPhone(email);
            }
            user.setPassword(service.resetPassword(user));
            service.addAndEditUser(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "user/{email}/toggleblock")
    public ResponseEntity<?> blockAndUnblockUser(@PathVariable String email,HttpServletRequest httpServletRequest) throws GeneralSecurityException, IOException {
        if(!httpServletRequest.getHeader("Authorization").equals(service.handleAdminAuth())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = service.findUserByEmailAddress(email);
        if (user.isBlocked()) {
            user.setBlocked(false);
            service.addAndEditUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("user unblocked");
        } else {
            user.setBlocked(true);
            service.addAndEditUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("user blocked");
        }
    }

    @PostMapping(value = "auth/user/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        try {
            String username = body.get("username");
            User user = username.split("@").length == 1 ? service.findUserByPhone(username) : service.findUserByEmailAddress(username);
            Audit audit = new Audit();
            audit.setIpAddress(request.getHeader("X-FORWARDED-FOR"));
            if (audit.getIpAddress() == null) audit.setIpAddress(request.getRemoteAddr());
            audit.setUsername(username);
            Map<String, Object> response = new LinkedHashMap<>();
            if (passwordEncoder.matches("", user.getPassword())) {
                response.put("status", "failed");
                response.put("message", "default password in use");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (user.isBlocked()) {
                response.put("status", "failed");
                response.put("message", "Account blocked");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (passwordEncoder.matches(body.get("password"), user.getPassword())) {
                audit.setSuccessfulLogin(true);
                response.put("status", "success");
                String token = jwtTokenUtil.generateToken(user);
                response.put("token", token);
                response.put("Expires", jwtTokenUtil.getExpirationDateFromToken(token).toString());
                response.put("user", user);
                service.saveAudit(audit);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("status", "Failed");
                service.saveAudit(audit);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("auth/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String,String> body){
        String username = body.get("username");
        String password = body.get("password");
        String token =Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8));
        if(token.equals(service.handleAdminAuth())){
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("token", token);
            User user = new User();
            user.setFirstname("SEVS");
            user.setLastname("ADMIN");
            response.put("user",user);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping(value = "user/{email}/token/send")
    public Date resendToken(@PathVariable String email) throws GeneralSecurityException, IOException, ClassNotFoundException {
       return service.resendToken(email);
    }

    @GetMapping(value = "user/{email}/token/regen/send/{type}")
    public Date generateNewToken(@PathVariable String email, @PathVariable String type) throws GeneralSecurityException, IOException, ClassNotFoundException {
       return service.generateUserToken(service.findUserByEmailAddress(email), type);
    }

    /**
     * Controller for Poll
     **/
    @PostMapping(value = "poll")
    public ResponseEntity<?> createPoll(@RequestBody Poll poll, HttpServletRequest httpServletRequest) {
        if(!httpServletRequest.getHeader("Authorization").equals(service.handleAdminAuth())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (poll.getOpeningDate().after(poll.getClosingDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid Dates");
        }
        service.createAndEditPoll(poll);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "poll/{pollId}/add")
    public ResponseEntity<?> addCandidate(@RequestBody Candidate candidate, @PathVariable Long pollId,HttpServletRequest httpServletRequest) {
        if(!httpServletRequest.getHeader("Authorization").equals(service.handleAdminAuth())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            service.addCandidate(candidate, pollId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "poll/vote")
    public ResponseEntity<?> voteCandidate(@RequestBody Map<String, String> request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException, ClassNotFoundException {
        String email = request.get("email");
        String token = request.get("token");
        long pollId = Long.parseLong(request.get("poll"));
        long contestantId = Long.parseLong(request.get("candidate"));
        User user = service.findUserByEmailAddress(email);
        if (service.findPoll(pollId).getUsers().contains(user)) {
            return service.castVote(pollId, contestantId, user.getId(), token);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(value = "user/csv")
    @ResponseBody
    public ResponseEntity<?> getUserCSVOutput(HttpServletRequest httpServletRequest) {
        if(!httpServletRequest.getHeader("Authorization").equals(service.handleAdminAuth())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        File file = service.writeUsersToCSV();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity.status(HttpStatus.OK)
                    .contentLength(file.length())
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(new FileInputStream(file)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "poll/{pollId}/accredit/{userId}")
    public ResponseEntity<?> accreditUser(@PathVariable long pollId, @PathVariable long userId,HttpServletRequest httpServletRequest) {
        if(!httpServletRequest.getHeader("Authorization").equals(service.handleAdminAuth())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            service.accreditUser(pollId, userId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "poll/{pollId}/accredit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StreamingResponseBody> accreditUserByFile(MultipartFile file, @PathVariable long pollId,HttpServletRequest httpServletRequest) throws IOException {
        if(!httpServletRequest.getHeader("Authorization").equals(service.handleAdminAuth())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String co = new String(file.getBytes());
        String[] accUser = co.split("\\r?\\n");
        if (!Objects.equals(file.getContentType(), "text/csv") || accUser.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        StreamingResponseBody responseBody = response -> {
            for (int i = 1; i < accUser.length; i++) {
                try {
                    service.accreditUser(pollId, Long.parseLong(accUser[i].split(",")[1]));
                    response.write(("success," + (double) i / (accUser.length - 1) + "\n").getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                    response.write(("failed," + (double) i / (accUser.length - 1) + "\n").getBytes());
                }
            }
        };
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(responseBody);
    }
    @GetMapping(value = "poll/user/{email}/{type}")
    public List<Poll> userPolls(@PathVariable String email,@PathVariable String type) {
        if (type.equalsIgnoreCase("adminActive")) {
            return service.findAllPolls().stream().filter(p -> !p.isClosed()).collect(Collectors.toList());
        }
        if (type.equalsIgnoreCase("adminClosed")) {
            return service.findAllPolls().stream().filter(Poll::isClosed).collect(Collectors.toList());
        }
        if (type.equalsIgnoreCase("admin")) {
            return service.findAllPolls();
        }
        User user = service.findUserByEmailAddress(email);
        List<Poll> poll = service.findPolls(user.getId());
        List<Poll> myPool = new ArrayList<>();
        if (type.equalsIgnoreCase("active")) {
            for (Poll poll1 : poll) {
                if (!poll1.isClosed() && (poll1.getRecordTrackers() == null || poll1.getRecordTrackers().stream().noneMatch(r -> r.getUser() == user))) {
                    myPool.add(poll1);
                }
                return myPool;
            }

        }
            return poll;
    }


    /**
     * Support
     **/
    @GetMapping(value = "support")
    public void addOrEditSupport(Support support) throws GeneralSecurityException, IOException {
        service.addOrEditSupport(support);
    }
}
