package com.nautilus.controller;

import com.nautilus.exception.ControllerException;
import com.nautilus.model.RequestView;
import com.nautilus.model.StaticGroups;
import com.nautilus.model.entity.Group;
import com.nautilus.model.entity.User;
import com.nautilus.repository.relation_db.GroupRepository;
import com.nautilus.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "Интерфейсы администраторов")
@RequestMapping("/api/user")
@Slf4j
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupRepository groupRepository;

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Создание нового пользователя администратора, слать только логин и пароль, группу не надо!", response = User.class)
    public ResponseEntity<User> saveAdmin(@RequestBody @Validated(RequestView.UserMarker.class) User user, BindingResult bindingResult) throws ControllerException {
        if (bindingResult.hasErrors()) throw buildException(bindingResult);
        Group admins = groupRepository.findByGroupNameCaseInsensitive(StaticGroups.ADMIN_GROUP.getName());
        user.setGroup(admins);
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Создание нового пользователя НЕ администратора, слать только логин и пароль, группу не надо!", response = User.class)
    public ResponseEntity<User> saveUser(@RequestBody @Validated(RequestView.UserMarker.class) User user, BindingResult bindingResult) throws ControllerException {
        if (bindingResult.hasErrors()) throw buildException(bindingResult);
        Group users = groupRepository.findByGroupNameCaseInsensitive(StaticGroups.USER_GROUP.getName());
        user.setGroup(users);
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Вывести список всех юзеров", response = List.class)
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Удаление пользователя", response = Integer.class)
    public ResponseEntity<Integer> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok(id);
    }
}