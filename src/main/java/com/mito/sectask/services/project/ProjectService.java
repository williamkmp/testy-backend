package com.mito.sectask.services.project;

import com.mito.sectask.entities.ProjectEntity;
import com.mito.sectask.entities.UserProjectRoleEntity;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    /**
     *  create a project and linked it to an owner
     *
     * @param   project {@link ProjectEntity}
     *          the new project data
     *
     * @param   ownerId {@link Long}
     *          project owner's user id
     *
     * @return  {@link Optional}<{@link ProjectEntity}>
     *          containing the created project, else
     *          Optional.empty()
     */
    public Optional<ProjectEntity> createProject(
        ProjectEntity project,
        Long ownerId
    );

    /**
     * get list of pending ivites for a certain user
     *
     * @param   userId {@link Long}
     *          user id
     *
     * @return  {@link List}<{@link UserProjectRoleEntity}>
     *          where the user, project , and role fields
     *          are not null
     */
    public List<UserProjectRoleEntity> getInvites(Long userId);
}
