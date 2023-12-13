package com.mito.sectask.services.image;

import java.util.Optional;
import com.mito.sectask.entities.File;

public interface ImageService {
    /**
     * save image to the database
     *
     * @param   imageBinary {@link Byte}[]
     *          image file
     *
     * @return  {@link Optional}<{@link File}>
     *          conatining image if saved, else
     *          Optional.empty()
     */
    public Optional<File> saveImage(byte[] imageBinary, String extension);

    /**
     * retrieve image from the database
     *
     * @param   id {@link Long}
     *          image record id
     *
     * @return  {@link Optional}<{@link File}>
     *          conatining image if found
     *          else Optional.empty()
     */
    public Optional<File> findById(Long id);


    /**
     * Update or delete user profile image 
     * 
     * @param   userId {@link Long}
     *          user id
     * 
     * @param   imageId {@link Long}
     *          image id, leave blank if want to delete 
     *          user profile picture
     * 
     * @return  {@link Optional}<{@link FIle}>
     *          user profile image data, empty if 
     *          delete 
     */
    public Optional<File> updateUserImage(Long userId, Long imageId);

    /**
     * get a registered image URL for client access
     *    
     * @param   imageId {@link Long}
     *          image id
     * 
     * @return  Image URL path, example 
     *          "http://api.server/image/tqtwrqewqw" 
     */
    public Optional<String> getImageUrl(Long imageId);

}
