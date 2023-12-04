package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.request.project.ProjectCreateRequest;
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.dto.response.project.CreateProjectResponse;
import com.mito.sectask.entities.Project;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.RequestHttpException;
import com.mito.sectask.services.project.ProjectService;
import com.mito.sectask.values.MESSAGES;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public StandardResponse<CreateProjectResponse> createProject(
        @RequestBody @Valid ProjectCreateRequest request,
        @Caller User caller
    ) {
        Long ownerId = caller.getId();
        Project newProject = new Project()
            .setName(request.getProjectName())
            .setDescription(request.getProjectDescription())
            .setStartDate(request.getProjectStartDate())
            .setEndDate(request.getProjectEndDate());

        Project createdProject = projectService
            .createProject(newProject, ownerId)
            .orElseThrow(() -> new RequestHttpException(MESSAGES.ERROR_INTERNAL_SERVER));

        return new StandardResponse<CreateProjectResponse>()
            .setStatus(HttpStatus.CREATED)
            .setData(
                new CreateProjectResponse()
                    .setProjectId(createdProject.getId().toString())
                    .setProjectName(createdProject.getName())
                    .setProjectDescription(createdProject.getDescription())
                    .setProjectStartDate(createdProject.getStartDate())
                    .setProjectEndDate(createdProject.getEndDate())
            );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public StandardResponse<List<CreateProjectResponse>> getProjects(
        @Caller User caller
    ) {
        List<Project> userProjects = projectService.getUserProjects(
            caller.getId()
        );
        return new StandardResponse<List<CreateProjectResponse>>()
            .setStatus(HttpStatus.NO_CONTENT)
            .setData(
                userProjects
                    .stream()
                    .map(project ->
                        new CreateProjectResponse()
                            .setProjectId(project.getId().toString())
                            .setProjectName(project.getName())
                            .setProjectDescription(project.getDescription())
                            .setProjectStartDate(project.getStartDate())
                            .setProjectEndDate(project.getEndDate())
                    )
                    .toList()
            );
    }
}
