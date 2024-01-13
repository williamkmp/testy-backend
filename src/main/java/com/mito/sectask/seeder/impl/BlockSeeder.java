package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.values.BLOCK_TYPE;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockSeeder implements Seeder {

    private final BlockRepository blockRepository;
    private final PageService pageService;

    @Override
    @Transactional
    public void seed() throws Exception {
        Page testyRootPage = pageService.findById(1L).orElseThrow(() -> new Exception("Page with id [1] is not found"));
        Page bimayRootPage = pageService.findById(2L).orElseThrow(() -> new Exception("Page with id [2] is not found"));
        Page bimobRootPage = pageService.findById(3L).orElseThrow(() -> new Exception("Page with id [3] is not found"));

        saveBlockListToPage(testyRootPage, generateBlockList());
        saveBlockListToPage(bimayRootPage, generateBlockList());
        saveBlockListToPage(bimobRootPage, generateBlockList());
    }

    private List<Block> generateBlockList() {
        List<Block> blocks = new ArrayList<>();

        blocks.add(new Block()
                .setBlockType(BLOCK_TYPE.HEADING_1)
                .setContent("<p>Please Stop Using Autowired Field Injection in the Spring</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.DIVIDER));
        blocks.add(new Block()
                .setBlockType(BLOCK_TYPE.PARAGRAPH)
                .setContent("<p>Are you still using field injection?</p>"));
        blocks.add(
                new Block()
                        .setBlockType(BLOCK_TYPE.PARAGRAPH)
                        .setContent(
                                "<p>As a Java developer, you should be very familiar with the <u>Spring framework</u>. Spring-IOC's <em>Dependency injection</em> feature is the most commonly used feature and the most convenient feature for programmers to develop. The most common usage of <em>Dependency injection</em></p>"));
        blocks.add(
                new Block()
                        .setBlockType(BLOCK_TYPE.PARAGRAPH)
                        .setContent(
                                "<p>I believe many people use the <strong>@Autowired</strong> annotation to implement the Dependency injection function, just like the above code example. But if you declare <em>@Autowiredon</em> the field, the IDE will alarm “Field injection is not recommended” just like this</p>"));
        blocks.add(
                new Block()
                        .setBlockType(BLOCK_TYPE.PARAGRAPH)
                        .setContent(
                                "<p>After reviewing the Spring documentation, I found that after <strong>Spring Framework 4.0</strong>, Spring no longer recommends using field injection and instead recommends constructor injection and setter injection.</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.HEADING_2).setContent("<p>Dependency injection types</p>"));
        blocks.add(new Block()
                .setBlockType(BLOCK_TYPE.PARAGRAPH)
                .setContent("<p>There are three dependency injection ways in spring.</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.BULLET_LIST).setContent("<p>Constructor injection</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.BULLET_LIST).setContent("<p>Setter Injection</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.BULLET_LIST).setContent("<p>Field Injection</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.HEADING_3).setContent("<p>Constructor injection</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.DIVIDER));
        blocks.add(
                new Block()
                        .setBlockType(BLOCK_TYPE.PARAGRAPH)
                        .setContent(
                                "<p>Constructor injection in Spring Boot is a method of injecting dependencies into a class by passing them through the class constructor. In other words, when you create an instance of a class, Spring automatically provides the required dependencies through the constructor.</p>"));
        blocks.add(
                new Block()
                        .setBlockType(BLOCK_TYPE.PARAGRAPH)
                        .setContent(
                                "<p>By using constructor injection, you promote a cleaner and more testable code, as it encourages a clear declaration of dependencies and simplifies the process of writing unit tests with mock dependencies.</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.HEADING_3).setContent("<p>Setter injection</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.DIVIDER));
        blocks.add(
                new Block()
                        .setBlockType(BLOCK_TYPE.PARAGRAPH)
                        .setContent(
                                "<p>Setter injection in Spring Boot is a method of injecting dependencies into a class by providing setter methods for each dependency. In this approach, dependencies are assigned to corresponding fields through setter methods, and Spring, when creating an instance of the class, automatically invokes these setters to inject the required dependencies. Unlike constructor injection, setter injection allows for flexibility in modifying or updating dependencies after the object's creation. However, it may result in less explicit and more mutable code compared to constructor injection, as dependencies can be changed at any point by calling the setter methods. Setter injection is commonly used when dealing with optional dependencies or when needing to update dependencies dynamically during the lifecycle of the object.</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.HEADING_3).setContent("<p>Field injection</p>"));
        blocks.add(new Block().setBlockType(BLOCK_TYPE.DIVIDER));
        blocks.add(
                new Block()
                        .setBlockType(BLOCK_TYPE.PARAGRAPH)
                        .setContent(
                                "<p>Field injection in Spring Boot is a method of injecting dependencies into a class by directly annotating class fields with the <strong>@Autowired</strong> annotation. In this approach, dependencies are directly assigned to annotated fields without the need for explicit constructor or setter methods. While field injection is concise and requires less boilerplate code, it has some drawbacks such as reduced testability, as direct field access makes it challenging to replace dependencies with mocks for unit testing. Field injection is generally discouraged in Spring due to these testing and readability concerns, and constructor or setter injection is often preferred for more maintainable and testable code.</p>"));
        return blocks;
    }

    private void saveBlockListToPage(Page page, List<Block> blocks) {

        // Save blocks to repository
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            block.setId(UUID.randomUUID().toString());
            block.setPage(page);
            Block savedBlock = blockRepository.save(block);
            blocks.set(i, savedBlock);
        }

        // Setting the prev/next relationship
        for (int i = 0; i < blocks.size(); i++) {
            boolean isFirst = (i <= 0);
            boolean isLast = (i >= blocks.size() - 1);

            Block currentBlock = blocks.get(i);

            if (!isFirst) {
                Block prevBlock = blocks.get(i - 1);
                currentBlock.setPrev(prevBlock);
                prevBlock.setNext(currentBlock);
            }

            if (!isLast) {
                Block nextBlock = blocks.get(i + 1);
                currentBlock.setNext(nextBlock);
                nextBlock.setPrev(currentBlock);
            }
        }

        // Resave to repo
        blockRepository.saveAll(blocks);
    }
}
