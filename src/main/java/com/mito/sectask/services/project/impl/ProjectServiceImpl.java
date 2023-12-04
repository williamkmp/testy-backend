package com.mito.sectask.services.project.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Project;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.ProjectRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserProjectRoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.project.ProjectService;
import com.mito.sectask.values.USER_ROLE;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRoleRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JPAQueryFactory query;

    @Override
    @Transactional
    public Optional<Project> createProject(
        Project project,
        Long ownerId
    ) {
        Optional<User> maybeUser = userRepository.findById(ownerId);
        Optional<Role> maybeRole = roleRepository.findByName(
            USER_ROLE.FULL_ACCESS
        );
        if (maybeUser.isEmpty() || maybeRole.isEmpty()) {
            return Optional.empty();
        }

        User owner = maybeUser.get();
        Role fullAccessRole = maybeRole.get();
        Project newProject = projectRepository.save(project);
        Authority ownerAuthority = new Authority()
            .setProject(newProject)
            .setUser(owner)
            .setRole(fullAccessRole);
        authorityRepository.save(ownerAuthority);

        return Optional.of(newProject);
    }

    @Override
    public List<Authority> getInvites(Long userId) {
        // TODO implement this
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public List<Project> getUserProjects(Long userId) {
       //TODO: implement this
       return null;
    }

    @Override
    public List<User> getProjectMembers(
        Long projectId,
        List<USER_ROLE> roles
    ) {
        //TODO: implement this
        return Collections.emptyList();
    }
}
