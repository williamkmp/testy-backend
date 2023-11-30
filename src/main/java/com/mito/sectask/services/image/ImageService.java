package com.mito.sectask.services.image;

import java.util.Optional;
import com.mito.sectask.entities.ImageEntity;

public interface ImageService {
    /**
     * save image to the database
     *
     * @param   imageBinary {@link Byte}[]
     *          image file
     *
     * @return  {@link Optional}<{@link ImageEntity}>
     *          conatining image if saved, else
     *          Optional.empty()
     */
    public Optional<ImageEntity> saveImage(byte[] imageBinary);

    /**
     * retrieve image from the database
     *
     * @param   id {@link Long}
     *          image record id
     *
     * @return  {@link Optional}<{@link ImageEntity}>
     *          conatining image if found
     *          else Optional.empty()
     */
    public Optional<ImageEntity> findById(Long id);

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
     * @return  {@link Optional}<{@link ImageEntity}>
     *          containing the new uploaded image data,
     *          else Optional.empty if failed
     */
    public Optional<ImageEntity> saveUserImage(Long userId, byte[] imageBinary);


    /**
     * retrieve user's profile picture from the database,
     * return <code>empty</code> if user doesn't have
     * a profile picture
     * 
     * @param   userId {@link Long}
     *          user's id
     * 
     * @return  {@link Optional}<{@link ImageEntity}>
     *          containing user's profile picture, else
     *          Optional.empty()
     */
    public Optional<ImageEntity> findUserProfilePicture(Long userId);

    /**
     * delete user's profile picture from the database
     * if thereis any, if theres none do nothing
     * 
     * @param   userId {@link Long}
     *          user's id
     * 
     * @return  {@link Optional}<{@link ImageEntity}>
     *          containing user's previous profile picture
     *          before deleteion, else Optional.empty()
     */
    public Optional<ImageEntity> deleteUserProfilePicture(Long userId);

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
     * @return  {@link Optional}<{@link ImageEntity}>
     *          containing the new uploaded image data,
     *          else Optional.empty if failed
     */
    public Optional<ImageEntity> saveProjectImage(Long projectId, byte[] imageBinary);


    /**
     * retrieve project's profile picture from the database,
     * return <code>empty</code> if project doesn't have
     * a profile picture
     * 
     * @param   projectId {@link Long}
     *          project's id
     * 
     * @return  {@link Optional}<{@link ImageEntity}>
     *          containing project's profile picture, else
     *          Optional.empty()
     */
    public Optional<ImageEntity> getProjectPicture(Long projectId);

    /**
     * delete project's profile picture from the database
     * if there is any, if theres none do nothing
     * 
     * @param   projectId {@link Long}
     *          project's id
     * 
     * @return  {@link Optional}<{@link ImageEntity}>
     *          containing project's previous profile picture
     *          before deleteion, else Optional.empty()
     */
    public Optional<ImageEntity> deleteProjectPicture(Long projectId);

}
