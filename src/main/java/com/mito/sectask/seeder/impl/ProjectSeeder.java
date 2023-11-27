package com.mito.sectask.seeder.impl;

import org.springframework.stereotype.Component;
import com.mito.sectask.repositories.ProjectRepository;
import com.mito.sectask.seeder.Seeder;
import lombok.RequiredArgsConstructor;

/**
 * Seeder class for table 'projects'
 */
@Component
@RequiredArgsConstructor
public class ProjectSeeder implements Seeder {
    
    private final ProjectRepository projectRepository;
    
    @Override
    public void seed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'seed'");
    }
    
}
