//package com.whoIsLeader.GooDoggy.user.controller;
//
//import com.whoIsLeader.GooDoggy.user.DTO.SessionRes;
//import com.whoIsLeader.GooDoggy.util.BaseResponse;
//import lombok.extern.slf4j.Slf4j;
//        import org.springframework.web.bind.annotation.GetMapping;
//        import org.springframework.web.bind.annotation.RestController;
//        import javax.servlet.http.HttpServletRequest;
//        import javax.servlet.http.HttpSession;
//        import java.util.Date;
//import java.util.List;
//
//@Slf4j
//@RestController
//public class SessionController {
//    @GetMapping("/session")
//    public BaseResponse<String> sessionInfo(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            return new BaseResponse<>("세션이 존재하지 않습니다.");
//        }
//        //세션 데이터 출력
//        SessionRes sessionRes = new SessionRes.SessionInfo(
//                session.getAttributeNames().toString(),
//                session.getAttribute(session.getAttributeNames().toString()).toString(),
//                session.getId(),
//                session.getMaxInactiveInterval(),
//                new Date(session.getCreationTime()),
//                new Date(session.getLastAccessedTime()),
//                session.isNew()
//        );
//
//        session.getAttributeNames().asIterator()
//                .forEachRemaining(name -> log.info("session name={}, value={}",
//                        name, session.getAttribute(name)));
//        log.info("sessionId={}", session.getId());
//        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
//        log.info("creationTime={}", new Date(session.getCreationTime()));
//        log.info("lastAccessedTime={}", new
//                Date(session.getLastAccessedTime()));
//        log.info("isNew={}", session.isNew());
//        return new BaseResponse<>("세션 정보를 출력합니다.");
//    }
//}