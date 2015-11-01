package org.dynamicloud.manager;

import org.dynamicloud.api.*;
import org.dynamicloud.api.criteria.Conditions;
import org.dynamicloud.api.model.RecordModel;
import org.dynamicloud.bean.BlogRecord;
import org.dynamicloud.bean.PostRecord;
import org.dynamicloud.bean.UserRecord;
import org.dynamicloud.exception.DynamicloudProviderException;

import java.util.List;

/**
 * This is the manager to execute all CRUD operations, queries, etc.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/29/15
 **/
public class BlogManager {
    public static final long USER_MODEL_ID = 980190977L;
    public static final long BLOG_MODEL_ID = 980190978L;
    public static final long POST_MODEL_ID = 980190979L;

    private static final String CSK = "csk#5c6f68d48ff4905a80d396a465580d34fc6e8fa2";
    private static final String ACI = "aci#81969f0abe4d15d4304411e65c6557e64b3b6576";

    /**
     * This method saves a user in Dynamicloud
     *
     * @param user user to save
     */
    public static void saveUser(UserRecord user) {
        DynamicProvider<UserRecord> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        /**
         * You have to build a way to get the specific model according to an object.
         * For example, you could create a bean in Spring applicationContext.xml file and get the model as a singleton object.
         */
        RecordModel model = new RecordModel(USER_MODEL_ID);

        try {
            provider.saveRecord(model, user);
        } catch (DynamicloudProviderException e) {
            log(e.getMessage());
        }
    }

    /**
     * Deletes a user using uid (User ID)
     *
     * @param uid user ID
     */
    public static void deleteUser(Long uid) {
        DynamicProvider provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        RecordModel model = new RecordModel(POST_MODEL_ID);
        Query query = provider.createQuery(model);
        query.add(Conditions.equals("creatrid", uid));

        try {
            provider.delete(query);
        } catch (Exception e) {
            log(e.getMessage());
        }

        deleteBlogs(uid);

        provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        model = new RecordModel(USER_MODEL_ID);
        query = provider.createQuery(model);
        query.add(Conditions.equals("id", uid));

        try {
            provider.delete(query);
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    /**
     * Deletes the blogs where uid is the owner
     *
     * @param uid User ID
     */
    private static void deleteBlogs(Long uid) {
        DynamicProvider<BlogRecord> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        RecordModel model = new RecordModel(BLOG_MODEL_ID);
        model.setBoundClass(BlogRecord.class);

        Query<BlogRecord> query = provider.createQuery(model);
        /**
         * Get only the record id and bind with method setRecordId
         */
        query.setProjection("id as rid");
        /**
         * Selection on creatrid (OwnerID)
         */
        query.add(Conditions.equals("creatrid", uid));

        try {
            List<BlogRecord> records = query.list().getRecords();
            do {
                if (records != null) {
                    for (BlogRecord blog : records) {
                        deleteBlog(blog.getRecordId().longValue());
                    }
                }

                /**
                 * Get the next 15 blogs to delete them.
                 */
                records = query.next().getRecords();
            } while (records != null && !records.isEmpty());
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    /**
     * Delete a blog and its posts
     *
     * @param bid Blog ID
     */
    private static void deleteBlog(long bid) {
        deletePosts(bid);

        DynamicProvider provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        RecordModel model = new RecordModel(BLOG_MODEL_ID);
        Query query = provider.createQuery(model);
        query.add(Conditions.equals("id", bid));

        try {
            provider.delete(query);
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    /**
     * Delete the posts according to bid Blog ID
     *
     * @param bid Blog ID
     */
    private static void deletePosts(long bid) {
        DynamicProvider provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        RecordModel model = new RecordModel(POST_MODEL_ID);
        Query query = provider.createQuery(model);
        query.add(Conditions.equals("blogid", bid));

        try {
            provider.delete(query);
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    /**
     * This method saves a blog in Dynamicloud
     *
     * @param blog blog to save
     */
    public static void saveBlog(BlogRecord blog) {
        DynamicProvider<BlogRecord> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        /**
         * You have to build a way to get the specific model according to an object.
         * For example, you could create a bean in Spring applicationContext.xml file and get the model as a singleton object.
         */
        RecordModel model = new RecordModel(BLOG_MODEL_ID);

        try {
            provider.saveRecord(model, blog);
        } catch (DynamicloudProviderException e) {
            log(e.getMessage());
        }
    }

    /**
     * This method saves a post in Dynamicloud
     *
     * @param post post to save
     */
    public static void savePost(PostRecord post) {
        DynamicProvider<PostRecord> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        /**
         * You have to build a way to get the specific model according to an object.
         * For example, you could create a bean in Spring applicationContext.xml file and get the model as a singleton object.
         */
        RecordModel model = new RecordModel(POST_MODEL_ID);

        try {
            provider.saveRecord(model, post);
        } catch (DynamicloudProviderException e) {
            log(e.getMessage());
        }
    }

    private static void log(String log) {
        System.out.println(log);
    }

    /**
     * Returns a list of users.  The last 15 created users.
     *
     * @return a list of users
     */
    public static List<UserRecord> getUsers() {
        DynamicProvider<UserRecord> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        /**
         * You have to build a way to get the specific model according to an object.
         * For example, you could create a bean in Spring applicationContext.xml file and get the model as a singleton object.
         */
        RecordModel model = new RecordModel(USER_MODEL_ID);
        model.setBoundClass(UserRecord.class);

        try {
            Query<UserRecord> query = provider.createQuery(model);

            RecordResults<UserRecord> list = query.list();

            /**
             * For the next 15 users, we could save this provider in session, in thread local, etc.  Likewise, we could
             * initialize a provider an setCount and setOffset
             */

            return list.getRecords();
        } catch (DynamicloudProviderException e) {
            log(e.getMessage());
        }

        return null;
    }

    /**
     * Returns a list of blogs.  The last 15 created blogs.
     *
     * @param uid User ID
     * @return a list of blogs
     */
    @SuppressWarnings("unchecked")
    public static List<BlogRecord> getBlogs(Long uid) {
        DynamicProvider<BlogRecord> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        /**
         * You have to build a way to get the specific model according to an object.
         * For example, you could create a bean in Spring applicationContext.xml file and get the model as a singleton object.
         */
        RecordModel model = new RecordModel(BLOG_MODEL_ID);
        model.setBoundClass(BlogRecord.class);

        try {
            Query<BlogRecord> query = provider.createQuery(model);
            query.add(Conditions.equals("creatrid", uid));

            RecordResults<BlogRecord> list = query.list();

            /**
             * For the next 15 blogs, we could save this provider in session, in thread local, etc.  Likewise, we could
             * initialize a provider an setCount and setOffset
             */

            return list.getRecords();
        } catch (DynamicloudProviderException e) {
            log(e.getMessage());
        }

        return null;
    }

    /**
     * Returns a list of posts.  The last 15 created posts.
     *
     * @param bid User ID
     * @return a list of blogs
     */
    @SuppressWarnings("unchecked")
    public static List<PostRecord> getPosts(Long bid) {
        DynamicProvider<PostRecord> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

        /**
         * You have to build a way to get the specific model according to an object.
         * For example, you could create a bean in Spring applicationContext.xml file and get the model as a singleton object.
         */
        RecordModel model = new RecordModel(POST_MODEL_ID);
        model.setBoundClass(PostRecord.class);

        try {
            Query<PostRecord> query = provider.createQuery(model);
            query.add(Conditions.equals("blogid", bid));

            RecordResults<PostRecord> list = query.list();

            /**
             * For the next 15 blogs, we could save this provider in session, in thread local, etc.  Likewise, we could
             * initialize a provider an setCount and setOffset
             */

            return list.getRecords();
        } catch (DynamicloudProviderException e) {
            log(e.getMessage());
        }

        return null;
    }
}