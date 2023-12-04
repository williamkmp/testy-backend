package com.mito.sectask.services.image;

import java.util.Optional;
import com.mito.sectask.entities.Image;

public interface ImageService {
    /**
     * save image to the database
     *
     * @param   imageBinary {@link Byte}[]
     *          image file
     *
     * @return  {@link Optional}<{@link Image}>
     *          conatining image if saved, else
     *          Optional.empty()
     */
    public Optional<Image> saveImage(byte[] imageBinary);

    /**
     * retrieve image from the database
     *
     * @param   id {@link Long}
     *          image record id
     *
     * @return  {@link Optional}<{@link Image}>
     *          conatining image if found
     *          else Optional.empty()
     */
    public Optional<Image> findById(Long id);

    /**
     * saved or update a given user's profile picture
     * to the database. 
     * 
     * @param   userId {@link Long}
     *          id user that the ne image belongs to 
     * 
     * @param   imageBinary {@link Byte array<byte>}
     *          user new profile picture 
     * 
     * @return  {@link Optional}<{@link Image}>
     *          containing the new uploaded image data,
     *          else Optional.empty if failed
     */
    public Optional<Image> saveUserImage(Long userId, byte[] imageBinary);


    /**
     * retrieve user's profile picture from the database,
     * return <code>empty</code> if user doesn't have
     * a profile picture
     * 
     * @param   userId {@link Long}
     *          user's id
     * 
     * @return  {@link Optional}<{@link Image}>
     *          containing user's profile picture, else
     *          Optional.empty()
     */
    public Optional<Image> findUserProfilePicture(Long userId);

    /**
     * delete user's profile picture from the database
     * if thereis any, if theres none do nothing
     * 
     * @param   userId {@link Long}
     *          user's id
     * 
     * @return  {@link Optional}<{@link Image}>
     *          containing user's previous profile picture
     *          before deleteion, else Optional.empty()
     */
    public Optional<Image> deleteUserProfilePicture(Long userId);

    /**
     * save or update a given project's profile picture
     * to the database. 
     * 
     * @param   projectId {@link Long}
     *          id of the project that the image belongs to 
     * 
     * @param   imageBinary {@link Byte array<byte>}
     *          new image file 
     * 
     * @return  {@link Optional}<{@link Image}>
     *          containing the new uploaded image data,
     *          else Optional.empty if failed
     */
    public Optional<Image> saveProjectImage(Long projectId, byte[] imageBinary);


    /**
     * retrieve project's profile picture from the database,
     * return <code>empty</code> if project doesn't have
     * a profile picture
     * 
     * @param   projectId {@link Long}
     *          project's id
     * 
     * @return  {@link Optional}<{@link Image}>
     *          containing project's profile picture, else
     *          Optional.empty()
     */
    public Optional<Image> getProjectPicture(Long projectId);

    /**
     * delete project's profile picture from the database
     * if there is any, if theres none do nothing
     * 
     * @param   projectId {@link Long}
     *          project's id
     * 
     * @return  {@link Optional}<{@link Image}>
     *          containing project's previous profile picture
     *          before deleteion, else Optional.empty()
     */
    public Optional<Image> deleteProjectPicture(Long projectId);

}
