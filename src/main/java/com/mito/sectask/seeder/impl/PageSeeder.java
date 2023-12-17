package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.File;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.FileRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.seeder.Seeder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageSeeder implements Seeder {

    private final PageRepository pageRepository;
    private final FileRepository fileRepository;

    @Override
    @Transactional
    public void seed() throws Exception {
        File coverImage1 = fileRepository.findById(1L).orElse(null);
        File coverImage2 = fileRepository.findById(2L).orElse(null);
        File coverImage3 = fileRepository.findById(3L).orElse(null);

        Page testyPageRoot = pageRepository.saveAndFlush(
            new Page()
                .setImagePosition(50f)
                .setImage(coverImage3) // assets/cover3.jpeg
                .setIconKey("emoji-69") // 😎
                .setName("Testy")
        );

        Page binusMayaRootPage = pageRepository.saveAndFlush(
            new Page()
                .setName("Binusmaya Website")
                .setImage(coverImage1) // assets/cover1.jpeg
                .setIconKey("emoji-1180") // 🖥️
                .setImagePosition(50f)
        );

        Page binusMobilePageRoot = pageRepository.saveAndFlush(
            new Page()
                .setImagePosition(50f)
                .setImage(coverImage2) // assets/cove21.jpeg
                .setIconKey("emoji-1170") // 📱
                .setName("Binus Mobile")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(binusMayaRootPage)
                .setIconKey("emoji-1215") // 📃
                .setName("Documentation")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(binusMayaRootPage)
                .setIconKey("emoji-1265") // 📋
                .setName("Notes")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(binusMayaRootPage)
                .setIconKey("emoji-1001") // 🛳️
                .setName("On Boarding")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(binusMobilePageRoot)
                .setIconKey("emoji-1085") // 🌈
                .setName("Mobile UI/UX")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(binusMobilePageRoot)
                .setIconKey("emoji-1072") // ☁️
                .setName("System Design")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(binusMobilePageRoot)
                .setIconKey("emoji-883") // 🌐
                .setName("API Specification")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(testyPageRoot)
                .setIconKey("emoji-875") // 🎨
                .setName("Client Documentation")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(testyPageRoot)
                .setIconKey("emoji-1214") // 📚
                .setName("Project Management")
        );

        pageRepository.saveAndFlush(
            new Page()
                .setParent(testyPageRoot)
                .setIconKey("emoji-1297") // ⚙️
                .setName("Server Documentation")
        );
    }
}
