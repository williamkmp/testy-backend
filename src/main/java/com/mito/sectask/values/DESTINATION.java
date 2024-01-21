package com.mito.sectask.values;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DESTINATION {

    /**
     * STOMP destination url for /queue/user/{userId}/preview
     *
     * @param userId {@link Long} user id
     * @return {@link String} STOMP destination path
     */
    public static String userPreview(Long userId) {
        return "/queue/user/" + userId.toString() + "/preview";
    }

    /**
     * STOMP destination url for /queue/user/{userId}/notification
     *
     * @param userId {@link Long} user id
     * @return {@link String} STOMP destination path
     */
    public static String userNotification(Long userId) {
        return "/queue/user/" + userId.toString() + "/notification";
    }

    /**
     * STOMP destination url for /topic/page/{pageId}/header
     *
     * @param pageId {@link Long} page id
     * @return {@link String} STOMP destination path
     */
    public static String pageUpdate(Long pageId) {
        return "/topic/page/" + pageId.toString() + "/preview";
    }

    /**
     * STOMP destination url for /topic/page/{pageId}/block
     *
     * @param pageId {@link Long} page id
     * @return {@link String} STOMP destination path
     */
    public static String pageBlock(Long pageId) {
        return "/topic/page/" + pageId.toString() + "/block";
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
}
