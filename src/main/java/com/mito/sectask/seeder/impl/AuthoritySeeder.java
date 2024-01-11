package com.mito.sectask.seeder.impl;

import org.springframework.stereotype.Component;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.seeder.Seeder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthoritySeeder implements Seeder {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PageRepository pageRepository;
    private final UserRepository userRepository;


    @Override
    public void seed() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'seed'");
    }
}
