package onecourse.models;

/**
 * 课程post下的discussion
 */
public class CoursePostDiscussion {
    private int id;
    private int postId;  // 所属的Post
    private String authorName;  // 作者
    private String content;   // 内容

    public CoursePostDiscussion() {
    }

    public CoursePostDiscussion(int postId, String authorName, String content) {
        this.postId = postId;
        this.authorName = authorName;
        this.content = content;
    }

    public CoursePostDiscussion(CoursePostDiscussion cpd, int id) {
        this.postId = cpd.postId;
        this.authorName = cpd.authorName;
        this.content = cpd.content;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPostId() {
        return postId;
    }

    public String getAuthorName() {
        return authorName;
    }
}
