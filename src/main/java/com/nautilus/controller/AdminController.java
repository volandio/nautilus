package com.nautilus.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "Интерфейсы администраторов")
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

//    @GetMapping()
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ApiOperation(value = "Удаление группы пользователей", response = Integer.class)
//    public ResponseEntity<Integer> get(@PathVariable Integer id) {
//        try {
//            return ResponseEntity.ok();
//        } catch (Exception e) {
//            log.error(e.getLocalizedMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
//        }
//    }
}