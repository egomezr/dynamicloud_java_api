package org.dynamicloud;

import org.dynamicloud.bean.BlogRecord;
import org.dynamicloud.bean.PostRecord;
import org.dynamicloud.bean.UserRecord;
import org.dynamicloud.manager.BlogManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Main class emulates a Blog system to post comments from users.
 */
public class Main {
    public static final String DYNA_BLOG_TITLE = "----- DynaBlog -----";
    private static UserRecord user;
    private static BlogRecord blog;

    /**
     * Start point of this program.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        showMainMenu();
    }

    /**
     * Show options to create a new user.
     */
    private static void showUserOptions() {
        user = new UserRecord();

        Scanner scanner = new Scanner(System.in);
        log("Enter Username: ", false);
        user.setUsername(scanner.nextLine());

        log("Enter Email: ", false);
        user.setEmail(scanner.nextLine());

        /**
         * Save the user
         */
        log("Creating user...");
        BlogManager.saveUser(user);
        while (user.getRecordId() == null || user.getRecordId().longValue() < 1) {
            log("Error creating user. Try again, press 't' to try again or 'm' to return back to main menu.");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("t")) {
                BlogManager.saveUser(user);
            } else if (response.equalsIgnoreCase("m")) {
                user = null;
                showMainMenu();

                break;
            } else {
                System.exit(0);
            }
        }

        log("User has been created.  UserID -> " + user.getRecordId());
        log("The next operations are attached to this user.");

        showMainMenu();
    }

    /**
     * Show the blog menu to create a new Blog.
     */
    private static void showBlogOptions() {
        BlogRecord blog = new BlogRecord();
        blog.setCreatorId(user.getRecordId().longValue());

        Scanner scanner = new Scanner(System.in);
        log("Enter the name of this Blog: ", false);
        blog.setBlogName(scanner.nextLine());

        log("Enter a description: ", false);
        blog.setBlogDescription(scanner.nextLine());

        log("Select a category:");
        log("1 - Technology.");
        log("2 - Medical.");
        log("3 - Science.");
        log("Press any char to General blog.");

        String response = scanner.nextLine();

        switch (response) {
            case "1":
                blog.setBlogCategory("tech");
                break;
            case "2":
                blog.setBlogCategory("med");
                break;
            case "3":
                blog.setBlogCategory("sci");
                break;
            default:
                blog.setBlogCategory("gen");
        }

        blog.setBlogCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        /**
         * Save the blog
         */
        log("Creating blog...");
        BlogManager.saveBlog(blog);
        while (blog.getRecordId() == null || blog.getRecordId().longValue() < 1) {
            log("Error creating blog. Try again, press 't' to try again or 'm' to return back to main menu.");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("t")) {
                BlogManager.saveBlog(blog);
            } else if (response.equalsIgnoreCase("m")) {
                showMainMenu();
                break;
            } else {
                System.exit(0);
            }
        }

        log("Blog has been created.  BlogID -> " + blog.getRecordId());

        showMainMenu();
    }

    /**
     * Show the blog menu to create a new Blog.
     */
    private static void showPostOptions() {
        PostRecord post = new PostRecord();
        post.setCreatorId(user.getRecordId().longValue());

        Scanner scanner = new Scanner(System.in);
        log("Enter the title of this Post: ", false);
        post.setPostTitle(scanner.nextLine());

        log("Enter the content: ", false);
        post.setPostContent(scanner.nextLine());

        post.setPostCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        post.setBlogId(blog.getRecordId().longValue());
        /**
         * Save the post
         */
        log("Posting...");
        BlogManager.savePost(post);
        while (post.getRecordId() == null || post.getRecordId().longValue() < 1) {
            log("Error creating post. Try again, press 't' to try again or 'm' to return back to main menu.");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("t")) {
                BlogManager.savePost(post);
            } else if (response.equalsIgnoreCase("m")) {
                showMainMenu();
                break;
            } else {
                System.exit(0);
            }
        }

        log("Post has been created.  PostID -> " + post.getRecordId());

        showMainMenu();
    }

    /**
     * Shows the main menu
     */
    private static void showMainMenu() {
        log(DYNA_BLOG_TITLE);
        if (user != null) {
            log("Hi " + user.getUsername() + ".. Welcome!");
        }

        log("----- User Options -----");
        if (user == null) {
            log("Enter 'u' to create an user");
        } else {
            log("Enter 'du' to detach the current user.");
        }
        log("Enter 'lu' to list users");

        if (user != null) {
            log("----- Blog Options -----");
            log("Enter 'b' to create a blog");
            log("Enter 'lb' to list the user Blogs");
        }

        if (user != null && blog != null) {
            log("Enter 'dg' to detach the current blog.");
            log("Enter 'lp' to list posts of -> " + blog.getBlogName());
            log("Enter 'p' to post in blog -> " + blog.getBlogName());
        }

        log("Enter 'e' to exit.");

        Scanner scanner = new Scanner(System.in);

        String response = scanner.next();

        switch (response) {
            case "u":
                showUserOptions();
                break;
            case "b":
                showBlogOptions();
                break;
            case "du":
                user = null;
                showMainMenu();
                break;
            case "lu":
                log("Getting users...");
                showUserList(null);
                break;
            case "lb":
                log("Getting blogs...");
                showBlogList(null);
                break;
            case "lp":
                log("Getting post of '" + blog.getBlogName() + "'...");
                showPostList();
                break;
            case "dg":
                blog = null;
                showMainMenu();
                break;
            case "p":
                showPostOptions();
                break;
            case "e":
                log("Bye!");
                System.exit(0);
            default:
                showMainMenu();
        }
    }

    /**
     * Shows a menu with the last 15 created users
     *
     * @param users user list to use.
     */
    private static void showUserList(List<UserRecord> users) {
        if (users == null) {
            users = BlogManager.getUsers();
        }

        log(DYNA_BLOG_TITLE);
        log("Enter the User ID to use for the next operations.");
        log("Enter d-UserID to delete user.");
        log("Enter 'm' to return back to main menu.");

        if (users == null || users.isEmpty()) {
            log("");
            log("No users found.  Press 'u' to create a user or 'm' to return back to main menu.");
        } else {
            log("");
            log("ID");
            for (UserRecord user : users) {
                log(user.getRecordId() + "           " + user.getUsername() + "                                  " + user.getEmail());
            }
        }

        Scanner scanner = new Scanner(System.in);
        String response = scanner.next();

        switch (response) {
            case "u":
                showUserOptions();
                break;
            case "m":
                showMainMenu();
                break;
            default:
                if (response.startsWith("d-")) {
                    try {
                        response = response.substring(2);
                        Long uid = Long.parseLong(response);

                        UserRecord userBy = findUserBy(uid, users);
                        if (userBy == null) {
                            log("User ID " + uid + " doesn't exist.");
                            log("Try again.");

                            showUserList(users);
                        } else {
                            log("Deleting user -> " + userBy.getRecordId().longValue());
                            BlogManager.deleteUser(userBy.getRecordId().longValue());
                            log("User has been deleted.");
                        }
                    } catch (Exception e) {
                        log("Error at moment to delete User.");
                        log("Returning back to main menu");
                    }
                } else {
                    try {
                        Long uid = Long.parseLong(response);
                        UserRecord userBy = findUserBy(uid, users);
                        if (userBy == null) {
                            log("User ID " + uid + " doesn't exist.");
                            log("Try again.");

                            showUserList(users);
                        } else {
                            user = userBy;
                        }
                    } catch (Exception e) {
                        log("Error at moment to get User ID.");
                        log("Returning back to main menu");
                    }

                }

                showMainMenu();
        }
    }

    /**
     * Shows a menu with the last 15 created blogs
     *
     * @param blogs blogs list to use.
     */
    private static void showBlogList(List<BlogRecord> blogs) {
        if (blogs == null) {
            blogs = BlogManager.getBlogs(user.getRecordId().longValue());
        }

        log(DYNA_BLOG_TITLE);
        log("Enter the Blog ID to use for the next operations.");
        log("Enter 'm' to return back to main menu.");

        if (blogs == null || blogs.isEmpty()) {
            log("");
            log("No blogs found.  Press 'b' to create a blog or 'm' to return back to main menu.");
        } else {
            log("");
            log("ID");
            for (BlogRecord blogRecord : blogs) {
                log(blogRecord.getRecordId() + "           " + blogRecord.getBlogName() + "                                  " + blogRecord.getBlogCreateDate());
            }
        }

        Scanner scanner = new Scanner(System.in);
        String response = scanner.next();

        switch (response) {
            case "b":
                showBlogOptions();
                break;
            case "m":
                showMainMenu();
                break;
            default:
                try {
                    Long bid = Long.parseLong(response);
                    BlogRecord blogBy = findBlogBy(bid, blogs);
                    if (blogBy == null) {
                        log("Blog ID " + bid + " doesn't exist.");
                        log("Try again.");

                        showBlogList(blogs);
                    } else {
                        blog = blogBy;
                    }
                } catch (Exception e) {
                    log("Error at moment to get Blog ID.");
                    log("Returning back to main menu");
                }

                showMainMenu();
        }
    }

    /**
     * Shows a menu with the last 15 created post of a blog
     */
    private static void showPostList() {
        List<PostRecord> posts = BlogManager.getPosts(blog.getRecordId().longValue());

        log(DYNA_BLOG_TITLE);
        log("Enter 'm' to return back to main menu.");

        if (posts == null || posts.isEmpty()) {
            log("");
            log("No posts found.  Press 'p' to post or 'm' to return back to main menu.");
        } else {
            log("");
            log("ID");
            for (PostRecord postRecord : posts) {
                log(postRecord.getRecordId() + "           " + postRecord.getPostTitle() + "                                  " + postRecord.getPostContent());
            }
        }

        Scanner scanner = new Scanner(System.in);
        String response = scanner.next();

        switch (response) {
            case "p":
                showPostOptions();
                break;
            case "m":
                showMainMenu();
                break;
            default:
                showMainMenu();
        }
    }

    /**
     * Finds a user in a user record list.
     *
     * @param uid   uid target
     * @param users record list
     * @return the found user id
     */
    private static UserRecord findUserBy(Long uid, List<UserRecord> users) {
        for (UserRecord user : users) {
            if (user.getRecordId().longValue() == uid) {
                return user;
            }
        }

        return null;
    }

    /**
     * Finds a blog in a blog record list.
     *
     * @param bid   bid target
     * @param blogs record list
     * @return the found blog id
     */
    private static BlogRecord findBlogBy(Long bid, List<BlogRecord> blogs) {
        for (BlogRecord blog : blogs) {
            if (blog.getRecordId().longValue() == bid) {
                return blog;
            }
        }

        return null;
    }

    /**
     * Print log in a new line.
     *
     * @param log to print.
     */
    private static void log(String log) {
        log(log, true);
    }

    /**
     * Print log in a new line.
     *
     * @param log     to print.
     * @param newLine indicates if this log will be printed in a new line.
     */
    private static void log(String log, boolean newLine) {
        if (newLine) {
            System.out.println(log);
        } else {
            System.out.print(log);
        }
    }
}