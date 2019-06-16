package com.nautilus.controller;

import com.nautilus.model.entity.Translate;
import com.nautilus.service.TranslateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Api(description = "Работа с переводчиком")
@RequestMapping("/api/translate")
@RequiredArgsConstructor
@Slf4j
public class TranslateController extends AbstractController{

    private final TranslateService translateService;

    @GetMapping("/get/{word}")
    @ApiOperation(value = "Получение перевода слова с английского языка", response = String.class)
    public ResponseEntity<String> translate(@PathVariable String word) {
        try {
            return ResponseEntity.ok(translateService.translate(word));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @GetMapping()
    @ApiOperation(value = "Получение списка всех слов и их переводов, которые сохранил текущий юзер", response = List.class)
    public ResponseEntity<Set<Translate>> getAll() {
        return ResponseEntity.ok(translateService.getAll());
    }

    @PostMapping()
    @ApiOperation(value = "Сохранение слова и его перевода для текущего юзера", response = Translate.class)
    public ResponseEntity save(@RequestBody @Validated Translate translate, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный запрос");

        try {
            return ResponseEntity.ok(translateService.save(translate));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удаление слова с переводом из списка у текущего юзера")
    public ResponseEntity delete(@PathVariable Integer id) {
        translateService.delete(id);
        return ResponseEntity.ok(id);
    }
}
