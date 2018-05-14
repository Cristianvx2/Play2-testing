package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Post extends Model {

    public String title;
    public Date postedAt;

    @Lob //large text database type @Lob
    public String content;

    @ManyToOne
    public User author;

    @OneToMany(mappedBy = "Post", cascade = CascadeType.ALL)
    public List<Comment> comments;


    public Post(User author, String title, String content)
    {
        this.author = author;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
        this.comments = new ArrayList<Comment>();
    }

    public Post addComment(String author, String content)
    {
        Comment newComment = new Comment(this,author,content).save();
        this.comments.add(newComment);
        this.save();
        return this;
    }
}
