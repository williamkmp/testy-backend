package com.mito.sectask.services.project;

import com.mito.sectask.entities.Project;
import com.mito.sectask.entities.User;
import com.mito.sectask.entities.Authority;
import com.mito.sectask.values.USER_ROLE;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    /**
     *  create a project and linked it to an owner
     *
     * @param   project {@link Project}
     *          the new project data
     *
     * @param   ownerId {@link Long}
     *          project owner's user id
     *
     * @return  {@link Optional}<{@link Project}>
     *          containing the created project, else
     *          Optional.empty()
     */
    public Optional<Project> createProject(
        Project project,
        Long ownerId
    );

    /**
     * get list of pending ivites for a certain user
     *
     * @param   userId {@link Long}
     *          user id
     *
     * @return  {@link List}<{@link Authority}>
     *          where the user, project , and role fields
     *          are not null
     */
    public List<Authority> getInvites(Long userId);

    /**
     * get all project list that is affiliated with this user
     *
     * @param   userId {@link Long}
     *          user id
     *
     * @return  {@link List}<{@link Project}>
     *          project affiliated with this user
     */
    public List<Project> getUserProjects(Long userId);

    /**
     * get a certain project list of member filter by
     * user role
     *
     * @param   projectId {@link Long}
     *          project id
     *
     * @param   roles {@link List}<{@link USER_ROLE}>
     *          role filter, allowed user roles
     *
     * @return  {@link List}<{@link User}>
     *          project member, based on filtered role
     */
    public List<User> getProjectMembers(
        Long projectId,
        List<USER_ROLE> roles
    );
}
