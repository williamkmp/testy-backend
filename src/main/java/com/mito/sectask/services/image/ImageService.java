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

}
