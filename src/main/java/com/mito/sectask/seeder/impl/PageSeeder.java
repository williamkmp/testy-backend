package com.mito.sectask.seeder.impl;

import org.springframework.stereotype.Component;
import com.mito.sectask.entities.File;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.FileRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.seeder.Seeder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PageSeeder implements Seeder {

    private final FileRepository fileRepository;
    private final PageRepository pageRepository; 

    @Override
    @Transactional
    public void seed() throws Exception {
        File coverImage1 = fileRepository.findById(1L).orElse(null);
        File coverImage2 = fileRepository.findById(2L).orElse(null);
        File coverImage3 = fileRepository.findById(3L).orElse(null);

        pageRepository.saveAndFlush(
            new Page()
                .setImagePosition(50f)
                .setImage(coverImage3) // assets/cover3.jpeg
                .setIconKey("emoji-69") // 😎
                .setName("Testy")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setName("Binusmaya Website")
                .setImage(coverImage1) // assets/cover1.jpeg
                .setIconKey("emoji-1180") // 🖥️
                .setImagePosition(50f)
        );

        pageRepository.saveAndFlush(
            new Page()
                .setImagePosition(50f)
                .setImage(coverImage2) // assets/cove21.jpeg
                .setIconKey("emoji-1170") // 📱
                .setName("Binus Mobile")
        );
    }
}
