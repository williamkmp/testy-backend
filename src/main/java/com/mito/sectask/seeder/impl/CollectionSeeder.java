package com.mito.sectask.seeder.impl;

import org.springframework.stereotype.Component;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.seeder.Seeder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CollectionSeeder implements Seeder{

    private final BlockRepository blockRepository;

    @Override
    public void seed() throws Exception {
        // TODO: implement me
    }

}
