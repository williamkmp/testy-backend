package com.mito.sectask.services.image;

import com.mito.sectask.entities.File;
import java.util.Optional;

public interface ImageService {
    /**
     * save image to the database
     *
     * @param imageBinary {@link Byte}[] image file
     * @return {@link Optional}<{@link File}> conatining image if saved, else Optional.empty()
     */
    public Optional<File> saveImage(byte[] imageBinary, String extension);

    /**
     * retrieve image from the database
     *
     * @param id {@link Long} image record id
     * @return {@link Optional}<{@link File}> conatining image if found else Optional.empty()
     */
    public Optional<File> findById(Long id);

    /**
     * Update or delete user profile image
     *
     * @param userId {@link Long} user id
     * @param imageId {@link Long} image id, leave <code>null</code> to delete user profile picture
     * @return {@link Optional}<{@link FIle}> user profile image data, empty if delete
     */
    public Optional<File> updateUserImage(Long userId, Long imageId);

    /**
     * Update or delete page cover image background
     *
     * @param userId {@link Long} user id
     * @param imageId {@link Long} image id, leave <code>null</code> to delete page cover image
     * @return {@link Optional}<{@link FIle}> page cover image data, empty if delete
     */
    public Optional<File> updatePageCoverImage(Long userId, Long imageId);

    /**
     * get a registered image URL for client access
     *
     * @param imageId {@link Long} image id, can be null
     * @return Image URL path, example "http://api.server/image/tqtwrqewqw"
     */
    public Optional<String> getImageUrl(Long imageId);
}
