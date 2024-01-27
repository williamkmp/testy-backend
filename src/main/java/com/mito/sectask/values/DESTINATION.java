package com.mito.sectask.values;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DESTINATION {

    /**
     * STOMP destination url for /topic/user/{userId}/preview
     *
     * @param userId {@link Long} user id
     * @return {@link String} STOMP destination path
     */
    public static String userPreview(Long userId) {
        return "/topic/user/" + userId.toString() + "/preview";
    }

    /**
     * STOMP destination url for /topic/user/{userId}/notification
     *
     * @param userId {@link Long} user id
     * @return {@link String} STOMP destination path
     */
    public static String userNotification(Long userId) {
        return "/topic/user/" + userId.toString() + "/notification";
    }

    /**
     * STOMP destination url for /topic/page/{pageId}/header
     *
     * @param pageId {@link Long} page id
     * @return {@link String} STOMP destination path
     */
    public static String pageUpdate(Long pageId) {
        return "/topic/page/" + pageId.toString() + "/header";
    }

    /**
     * STOMP destination url for /topic/page/{pageId}/block.transaction
     *
     * @param pageId {@link Long} page id
     * @return {@link String} STOMP destination path
     */
    public static String pageBlockTransaction(Long pageId) {
        return "/topic/page/" + pageId.toString() + "/block.transaction";
    }

    /**
     * STOMP destination url for /topic/page/{pageId}/collection
     *
     * @param pageId {@link Long} page id
     * @return {@link String} STOMP destination path
     */
    public static String pageCollection(Long pageId) {
        return "/topic/page/" + pageId.toString() + "/collection";
    }

    /**
     *  STOMP destination url form /topic/page/{pageId}/user/{userId}/url
     *
     * @param pageId {@link Long} page id
     * @param userId {@link Long} user id
     * @return {@link String} STOMP destination path
     */
    public static String pageUserError(Long pageId, Long userId) {
        return (
            "/topic/page/" +
            pageId.toString() +
            "/user/" +
            userId.toString() +
            "/error"
        );
    }
}
