package com.mito.sectask.seeder.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.seeder.Seeder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PageSeeder implements Seeder{

    private final PageRepository pageRepository; 

    @Override
    public void seed() throws Exception {
        List<Page> pages = new ArrayList<>();
        
        pages.add(new Page()
            .setName("Binusmaya Website")
        );
        
        pages.add(new Page()
            .setName("Binus Mobile")
        );

        pages.add(new Page()
            .setName("Secur Task")
        );

        pageRepository.saveAll(pages);
    }

}
