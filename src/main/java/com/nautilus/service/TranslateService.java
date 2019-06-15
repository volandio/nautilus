package com.nautilus.service;

import com.nautilus.model.entity.Translate;
import com.nautilus.model.entity.User;
import com.nautilus.repository.relation_db.TranslationRepository;
import com.nautilus.repository.relation_db.UserRepository;
import com.nautilus.util.InfoOfCurrentUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TranslateService {

    private final UserRepository userRepository;
    private final TranslationRepository translationRepository;
    private final InfoOfCurrentUser infoOfCurrentUser;

    @Transactional
    public Translate save(Translate translate) {
        User user = userRepository.findByLoginCaseInsensitive(infoOfCurrentUser.getLoginOfAuthUser());
        translate.setUser(user);
        return translationRepository.save(translate);
    }

    @Transactional(readOnly = true)
    public Set<Translate> getAll() {
        User user = userRepository.findByLoginCaseInsensitive(infoOfCurrentUser.getLoginOfAuthUser());
        return user.getTranslates();
    }

    @Transactional
    public void delete(Integer id) {
        translationRepository.deleteById(id);
    }

    @Data
    @NoArgsConstructor
    private static class Response {
        private Integer code;
        private String lang;
        private String[] text;
    }

    public String translate(String word) {
        String key = "trnsl.1.1.20190611T064244Z.3175d61143bf2c8b.5e0e66bee09265f11d0b27ac9bb8181e716a5a76";
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + key + "&text=" + word + "&lang=en-ru";
        ResponseEntity<Response> entity = new RestTemplate().exchange(url, HttpMethod.GET, null, Response.class);
        return entity.getBody().getText()[0];
    }
}
