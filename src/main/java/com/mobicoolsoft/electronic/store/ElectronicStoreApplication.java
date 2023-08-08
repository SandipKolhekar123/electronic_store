package com.mobicoolsoft.electronic.store;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.entity.Role;
import com.mobicoolsoft.electronic.store.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {


        SpringApplication.run(ElectronicStoreApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Role role1 = new Role();
            role1.setRoleId(AppConstants.ROLE_ADMIN);
            role1.setRoleName("ADMIN");

            Role role2 = new Role();
            role2.setRoleId(AppConstants.ROLE_STAFF);
            role2.setRoleName("STAFF");

            Role role3 = new Role();
            role3.setRoleId(AppConstants.ROLE_USER);
            role3.setRoleName("USER");

            List<Role> roles = List.of(role1, role2, role3);
            List<Role> result = this.roleRepository.saveAll(roles);
            result.forEach(role -> {
                System.out.println(role.getRoleName());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
