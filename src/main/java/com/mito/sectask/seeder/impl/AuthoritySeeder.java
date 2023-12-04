package com.mito.sectask.seeder.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import com.mito.sectask.entities.Authority;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.seeder.Seeder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthoritySeeder implements Seeder {

    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public void seed() throws Exception {
        List<Authority> authorities = getAuthorities();
        authorityRepository.saveAll(authorities);
    }

    private List<Authority> getAuthorities() {
        return Collections.emptyList();
    }
}