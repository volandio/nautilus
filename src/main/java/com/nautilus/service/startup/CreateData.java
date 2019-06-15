package com.nautilus.service.startup;

import com.nautilus.model.StaticGroups;
import com.nautilus.model.entity.Group;
import com.nautilus.model.entity.User;
import com.nautilus.repository.relation_db.GroupRepository;
import com.nautilus.repository.relation_db.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class CreateData implements ApplicationListener<ApplicationStartedEvent> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public CreateData(BCryptPasswordEncoder bCryptPasswordEncoder,
                      UserRepository userRepository,
                      GroupRepository groupRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        if (groupRepository.findByGroupNameCaseInsensitive(StaticGroups.ADMIN_GROUP.getName()) == null) {
            Group adminGroup = new Group();
            adminGroup.setName(StaticGroups.ADMIN_GROUP.getName());
            groupRepository.save(adminGroup);

            Group userGroup = new Group();
            userGroup.setName(StaticGroups.USER_GROUP.getName());
            groupRepository.save(userGroup);

            User adminUser = new User();
            adminUser.setLogin("admin");
            adminUser.setHashPassword(bCryptPasswordEncoder.encode("admin"));
            adminUser.getGroups().add(adminGroup);
            userRepository.save(adminUser);

            User userUser = new User();
            userUser.setLogin("user");
            userUser.setHashPassword(bCryptPasswordEncoder.encode("user"));
            userUser.getGroups().add(userGroup);
            userRepository.save(userUser);
        }
    }
}