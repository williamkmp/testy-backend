package com.mito.sectask.services.project.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.mito.sectask.entities.ProjectEntity;
import com.mito.sectask.entities.QProjectEntity;
import com.mito.sectask.entities.QRoleEntity;
import com.mito.sectask.entities.QUserEntity;
import com.mito.sectask.entities.QUserProjectRoleEntity;
import com.mito.sectask.entities.RoleEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.entities.UserProjectRoleEntity;
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
    @Transactional
    public List<ProjectEntity> getUserProjects(Long userId) {
        final QProjectEntity project = QProjectEntity.projectEntity;
        final QUserEntity user = QUserEntity.userEntity;
        final QUserProjectRoleEntity authority =
            QUserProjectRoleEntity.userProjectRoleEntity;
        final QRoleEntity role = QRoleEntity.roleEntity;

        return query
            .selectFrom(project)
            .join(project.authoritySet, authority)
            .join(authority.user, user)
            .join(authority.role, role)
            .where(user.id.eq(userId))
            .where(authority.isPending.eq(false))
            .where(role.name.eq(USER_ROLE.FULL_ACCESS))
            .orderBy(project.endDate.desc())
            .fetch();
    }

    @Override
    public List<UserEntity> getProjectMembers(
        Long projectId,
        List<USER_ROLE> roles
    ) {
        //TODO: implement this
        return Collections.emptyList();
    }
}
