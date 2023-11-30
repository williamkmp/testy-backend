package com.mito.sectask.services.project.impl;

import com.mito.sectask.entities.ProjectEntity;
import com.mito.sectask.entities.RoleEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.entities.UserProjectRoleEntity;
import com.mito.sectask.repositories.ProjectRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserProjectRoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.project.ProjectService;
import com.mito.sectask.values.USER_ROLE;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRoleRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Optional<ProjectEntity> createProject(
        ProjectEntity project,
        Long ownerId
    ) {
        Optional<UserEntity> maybeUser = userRepository.findById(ownerId);
        Optional<RoleEntity> maybeRole = roleRepository.findByName(
            USER_ROLE.FULL_ACCESS
        );
        if (maybeUser.isEmpty() || maybeRole.isEmpty()) {
            return Optional.empty();
        }

        UserEntity owner = maybeUser.get();
        RoleEntity fullAccessRole = maybeRole.get();
        ProjectEntity newProject = projectRepository.save(project);
        UserProjectRoleEntity ownerAuthority = new UserProjectRoleEntity()
            .setProject(newProject)
            .setUser(owner)
            .setRole(fullAccessRole);
        authorityRepository.save(ownerAuthority);

        return Optional.of(newProject);
    }

    @Override
    public List<UserProjectRoleEntity> getInvites(Long userId) {
        // TODO implement this
        return Collections.emptyList();
    }

    @Override
    public List<ProjectEntity> getUserProjects(Long userId) {
        Optional<UserEntity> maybeUser = userRepository.findById(userId);
        if(maybeUser.isEmpty()){
            return Collections.emptyList();
        }


    }
}
