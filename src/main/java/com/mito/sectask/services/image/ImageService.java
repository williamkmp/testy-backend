package com.mito.sectask.services.image;

import com.mito.sectask.entities.ImageEntity;
import java.util.Optional;

public interface ImageService {
    /**
     * save image to teh database
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
}
